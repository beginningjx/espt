package com.espt.jx.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.espt.jx.App
import com.espt.jx.R
import com.espt.jx.dao.User
import com.espt.jx.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessageActivity : AppCompatActivity() {
    private val mHeadImg: ImageView by lazy { findViewById(R.id.headImg) }
    private val mTitle: TextView by lazy { findViewById(R.id.title) }
    private val mTitleEdit: EditText by lazy { findViewById(R.id.title_edit) }
    private val mQq: TextView by lazy { findViewById(R.id.qq) }
    private val mQqPassword: TextView by lazy { findViewById(R.id.qq_password) }
    private val mQqPasswordEdit: EditText by lazy { findViewById(R.id.qq_password_edit) }
    private val mId: TextView by lazy { findViewById(R.id.id) }
    private val mCollect: TextView by lazy { findViewById(R.id.collect) }
    private val mHistory: TextView by lazy { findViewById(R.id.history) }
    private val mPublish: TextView by lazy { findViewById(R.id.publish) }
    private val mModify: Button by lazy { findViewById(R.id.modify) }
    private val mCancellation: TextView by lazy { findViewById(R.id.cancellation) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        init()
        initView()
    }


    private fun init() {
        StatusBarUtils.defaultStatusBar(this)

    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        CoroutineScope(Dispatchers.IO).launch {
            val id = DataStoreUtils.getData("id", 0)
            val user = App.db.userDao().getIdUser(id)
            val collect = App.db.collectDao().getAllId(id)
            val history = App.db.historyDao().getAllId(id)
            val data = App.db.dataDao().getUserId(id)

            withContext(Dispatchers.Main) {
                Glide.with(mHeadImg).load(user.qq_picture).into(mHeadImg)
                mTitle.text = user.qq_name
                mTitleEdit.setText(user.qq_name)
                mQq.text = user.qq
                mId.text = "您是第 ${user.id} 位注册的用户"
                mQqPasswordEdit.setText(user.password)
                mCollect.text = "${collect.size}"
                mHistory.text = "${history.size}"
                mPublish.text = "${data.size}"
            }
            mModify.setOnClickListener {
                if (mTitle.isVisible) {
                    // 修改
                    setVisible("保存个人信息", View.GONE, View.VISIBLE)
                } else {
                    // 保存
                    if (mTitleEdit.text.toString() == "") {
                        Toast.makeText(applicationContext, "昵称不能为空！", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    if (mQqPasswordEdit.text.toString() == "") {
                        Toast.makeText(applicationContext, "密码不能为空！", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    setVisible("修改个人信息", View.VISIBLE, View.GONE)
                    CoroutineScope(Dispatchers.IO).launch {
                        App.db.userDao().update(
                            User(
                                user.id,
                                mTitleEdit.text.toString(),
                                mQqPasswordEdit.text.toString(),
                                user.qq,
                                user.qq_picture
                            )
                        )
                        DataStoreUtils.setData("qq_name", user.qq_name)
                    }
                    mTitle.text = mTitleEdit.text.toString()
                    Toast.makeText(applicationContext, "修改成功！", Toast.LENGTH_SHORT).show()
                }
            }

            mCancellation.setOnClickListener {
                DialogUtil.textDialog(
                    context = this@MessageActivity,
                    contents = "注销之后无法恢复，确定注销吗？",
                    cDetermine = {
                        CoroutineScope(Dispatchers.IO).launch {
                            App.db.userDao().delete(user.id!!)
                        }
                        DataStoreUtils.clearData()
                        FlowBus.send("MyFragment", 1)
                        Toast.makeText(applicationContext, "注销成功！", Toast.LENGTH_SHORT).show()
                        finish()
                    })
            }
        }
    }

    private fun setVisible(string: String, int1: Int, int2: Int) {
        mModify.text = string
        mTitle.visibility = int1
        mQqPassword.visibility = int1
        mTitleEdit.visibility = int2
        mQqPasswordEdit.visibility = int2
    }
}