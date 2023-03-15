package com.espt.jx.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.espt.jx.R
import com.espt.jx.utils.StatusBarUtils

class FeedbackActivity : AppCompatActivity() {

    private val mClose: ImageView by lazy { findViewById(R.id.close) }
    private val mEditText: EditText by lazy { findViewById(R.id.editText) }
    private val mSubmit: Button by lazy { findViewById(R.id.submit) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        StatusBarUtils.defaultStatusBar(this)

        mClose.setOnClickListener { finish() }

        mSubmit.setOnClickListener {
            if (mEditText.text.toString() == "") {
                Toast.makeText(applicationContext, "建议不能为空哦！", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val emailIntent = Intent(Intent.ACTION_SEND)/*不带附件发送邮件*/
            emailIntent.type = "plain/text"/*邮件标题*/
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "意见反馈")
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("1017656986@qq.com"))
            emailIntent.putExtra(Intent.EXTRA_TEXT, mEditText.text.toString()) //发送的内容
            startActivity(Intent.createChooser(emailIntent, "分享"))
        }

    }
}