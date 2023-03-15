package com.espt.jx.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.espt.jx.App
import com.espt.jx.R
import com.espt.jx.adapter.DetailsAdapter
import com.espt.jx.dao.Collect
import com.espt.jx.dao.Data
import com.espt.jx.dao.History
import com.espt.jx.dao.User
import com.espt.jx.utils.DataStoreUtils
import com.espt.jx.utils.StatusBarUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsActivity : AppCompatActivity() {

    private val mCategory: TextView by lazy { findViewById(R.id.category) }
    private val mQqPicture: ImageView by lazy { findViewById(R.id.qq_picture) }
    private val mQqName: TextView by lazy { findViewById(R.id.qq_name) }
    private val mQq: TextView by lazy { findViewById(R.id.qq) }
    private val mMoney: TextView by lazy { findViewById(R.id.money) }
    private val mIntroduction: TextView by lazy { findViewById(R.id.introduction) }
    private val mRecyclerView: RecyclerView by lazy { findViewById(R.id.recyclerView) }
    private val mCollect: ImageView by lazy { findViewById(R.id.collect) }
    private val mContact: Button by lazy { findViewById(R.id.contact) }

    private val userId by lazy { DataStoreUtils.getData("id", 0) }
    private val dataId by lazy { intent.getIntExtra("id", 0) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        init()
        initView()
    }

    private fun init() {
        StatusBarUtils.defaultStatusBar(this)
    }

    private fun initView() {
        mRecyclerView.isNestedScrollingEnabled = false//禁止滑动

        CoroutineScope(Dispatchers.IO).launch {
            // 根据数据ID查找对应数据
            val data: Data = App.db.dataDao().getDataId(dataId)
            val user: User = App.db.userDao().getIdUser(data.user_id)

            // 判断是否收藏
            if (App.db.collectDao().isCollect(userId, dataId)) {
                isCollect(R.drawable.collect_, R.color.red)
            } else {
                isCollect(R.drawable.collect, R.color.black)
            }

            // 删除浏览历史
            App.db.historyDao().delete(userId, dataId)
            // 新增浏览历史
            App.db.historyDao().insert(History(null, userId, dataId))

            withContext(Dispatchers.Main) {
                Glide.with(mQqPicture).load(user.qq_picture).into(mQqPicture)
                mQqName.text = user.qq_name
                mQq.text = user.qq
                mMoney.text = data.money
                mIntroduction.text = data.introduction
                mCategory.text = data.category

                val detailsAdapter = DetailsAdapter(data.picture.split("&&&"))
                mRecyclerView.adapter = detailsAdapter
            }
        }

        mContact.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=365309496&version=1")
                    )
                )
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "是没有安装QQ吗?", Toast.LENGTH_SHORT).show()
            }
        }

        mCollect.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                if (App.db.collectDao().isCollect(userId, dataId)) {
                    // 有,删除收藏
                    App.db.collectDao().delete(userId, dataId)
                    isCollect(R.drawable.collect, R.color.black)

                    val intExtra = intent.getIntExtra("position", -1)
                    if (intExtra != -1) {
                        //赋值
                        setResult(intExtra)
                    }
                } else {
                    // 没有,添加收藏
                    App.db.collectDao().insert(Collect(null, userId, dataId))
                    isCollect(R.drawable.collect_, R.color.red)
                }
            }
        }

        findViewById<ImageView>(R.id.close).setOnClickListener {
            finish()
        }
    }

    private fun isCollect(drawable: Int, color: Int) {
        mCollect.setImageDrawable(ContextCompat.getDrawable(applicationContext, drawable))
        mCollect.setColorFilter(ContextCompat.getColor(applicationContext, color))
    }
}