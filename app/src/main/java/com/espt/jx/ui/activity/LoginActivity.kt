package com.espt.jx.ui.activity

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.espt.jx.App
import com.espt.jx.R
import com.espt.jx.dao.User
import com.espt.jx.utils.FlowBus
import com.espt.jx.utils.LoginUtils
import com.espt.jx.utils.StatusBarUtils
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.random.Random

@SuppressLint("SetTextI18n")
class LoginActivity : AppCompatActivity() {

    private val mHeadImg: ImageView by lazy { findViewById(R.id.headImg) }
    private val mTitle: TextView by lazy { findViewById(R.id.title) }
    private val mQq: EditText by lazy { findViewById(R.id.qq) }
    private val mPassword: EditText by lazy { findViewById(R.id.password) }
    private val mCodeView: LinearLayout by lazy { findViewById(R.id.codeView) }
    private val mCode1: EditText by lazy { findViewById(R.id.code1) }
    private val mCode2: TextView by lazy { findViewById(R.id.code2) }
    private val mLogin: Button by lazy { findViewById(R.id.login) }
    private val mRegister1: TextView by lazy { findViewById(R.id.register1) }
    private val mRegister: TextView by lazy { findViewById(R.id.register) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
        initView()
    }

    private fun init() {
        StatusBarUtils.defaultStatusBar(this)
        // 删除线
        mCode2.paintFlags = mCode2.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        mRegister.setOnClickListener {
            if (mRegister.text == "登录") {
                login()
            } else {
                register()
            }
        }

        mQq.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && mQq.text.toString() != "") {
                CoroutineScope(Dispatchers.IO).launch {
                    val body = OkHttpClient().newCall(
                        Request.Builder().url("https://api.btstu.cn/qqxt/api.php?qq=${mQq.text}")
                            .get().build()
                    ).execute().body?.string()

                    val fromJson = Gson().fromJson(body, JsonObject::class.java)

                    withContext(Dispatchers.Main) {
                        println(fromJson.get("imgurl").asString)
                        Glide.with(mHeadImg).load(fromJson.get("imgurl").asString).into(mHeadImg)
                    }
                }
            }
        }

        mLogin.setOnClickListener {
            if (mQq.text.toString() == "") {
                Toast.makeText(applicationContext, "QQ不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (mPassword.text.toString() == "") {
                Toast.makeText(applicationContext, "密码不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (mRegister.text == "登录") {
                // 注册
                if (mCode1.text.toString() == "") {
                    Toast.makeText(applicationContext, "验证码不能为空", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (mCode1.text.toString() != mCode2.text.toString()) {
                    Toast.makeText(applicationContext, "验证码错误", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                CoroutineScope(Dispatchers.IO).launch {
                    if (App.db.userDao().isQQ(mQq.text.toString())) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(applicationContext, "QQ号已存在", Toast.LENGTH_SHORT).show()
                        }
                        return@launch
                    }

                    val body = OkHttpClient().newCall(
                        Request.Builder().url("https://api.btstu.cn/qqxt/api.php?qq=${mQq.text}")
                            .get().build()
                    ).execute().body?.string()

                    val fromJson = Gson().fromJson(body, JsonObject::class.java)

                    App.db.userDao().insert(
                        User(
                            null,
                            fromJson.get("name").asString,
                            mPassword.text.toString(),
                            mQq.text.toString(),
                            fromJson.get("imgurl").asString
                        )
                    )

                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "注册成功！", Toast.LENGTH_SHORT).show()
                    }
                    login()
                }
            } else {
                // 登录
                CoroutineScope(Dispatchers.IO).launch {
                    if (App.db.userDao().login(mQq.text.toString(), mPassword.text.toString())) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(applicationContext, "登录成功", Toast.LENGTH_SHORT).show()
                        }

                        val user = App.db.userDao().getQQUser(mQq.text.toString())
                        LoginUtils.login(
                            user.id!!,
                            user.qq,
                            user.qq_name,
                            user.qq_picture
                        )

                        // 发送通知
                        FlowBus.send("MyFragment", 0)

                        finish()
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(applicationContext, "用户名或密码错误！", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

    /**
     * 登录
     */
    private fun login() {
        CoroutineScope(Dispatchers.Main).launch {
            mTitle.text = "Login"
            mLogin.text = "Login"
            mCodeView.visibility = View.GONE
            mRegister1.text = "没有账号？"
            mRegister.text = "注册"
        }
    }


    /**
     * 注册
     */
    private fun register() {
        CoroutineScope(Dispatchers.Main).launch {
            mTitle.text = "Register"
            mLogin.text = "Register"
            mCodeView.visibility = View.VISIBLE
            mCode2.text = Random.nextInt(1000, 9999).toString()
            mRegister1.text = "已有账号？"
            mRegister.text = "登录"
        }
    }
}