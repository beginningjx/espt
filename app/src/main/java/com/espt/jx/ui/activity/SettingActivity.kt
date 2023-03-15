package com.espt.jx.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.espt.jx.R
import com.espt.jx.utils.FlowBus
import com.espt.jx.utils.StatusBarUtils

class SettingActivity : AppCompatActivity() {

    private val mClose: ImageView by lazy { findViewById(R.id.close) }
    private val mS1: LinearLayout by lazy { findViewById(R.id.s1) }
    private val mS2: LinearLayout by lazy { findViewById(R.id.s2) }
    private val mS3: LinearLayout by lazy { findViewById(R.id.s3) }
    private val mS4: LinearLayout by lazy { findViewById(R.id.s4) }
    private val mS5: LinearLayout by lazy { findViewById(R.id.s5) }
    private val mS6: LinearLayout by lazy { findViewById(R.id.s6) }
    private val mExitLogin: Button by lazy { findViewById(R.id.exitLogin) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        StatusBarUtils.defaultStatusBar(this)

        mClose.setOnClickListener { finish() }
        mS1.setOnClickListener {
            startActivity(Intent(this, MessageActivity::class.java))
        }
        mS2.setOnClickListener {
            startActivity(Intent(this, FeedbackActivity::class.java))
        }
        mS3.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                " 软件名称：${getString(R.string.app_name)}\n官方网站：https://www.xxx.com".trimIndent()
            )
            shareIntent.type = "text/plain"
            //设置分享列表的标题，并且每次都显示分享列表
            startActivity(Intent.createChooser(shareIntent, "想分享给谁呢？"))
        }
        mS4.setOnClickListener {
            val copy = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            copy.setPrimaryClip(ClipData.newPlainText("QQ群号码", "******"))
            Toast.makeText(this, "已复制QQ群号码", Toast.LENGTH_SHORT).show()
        }
        mS5.setOnClickListener {
            val copy = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            copy.setPrimaryClip(ClipData.newPlainText("公众号名称", "******"))
            Toast.makeText(this, "已复制微信公众号", Toast.LENGTH_SHORT).show()
        }
        mS6.setOnClickListener {
            Toast.makeText(this, "已清除缓存", Toast.LENGTH_SHORT).show()
        }
        mExitLogin.setOnClickListener {
            FlowBus.send("MyFragment", 1)
            finish()
        }
    }
}