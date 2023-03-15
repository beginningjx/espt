package com.espt.jx.utils

import android.content.Context
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.espt.jx.R


object DialogUtil {

    private lateinit var dialog: AlertDialog

    /**
     * 文本对话框
     * @param [context] 上下文
     * @param [title] 标题
     * @param [contents] 内容
     * @param [cancel] 取消
     * @param [determine] 确定
     * @param [cCancel] 取消
     * @param [cDetermine] 确定
     */
    fun textDialog(
        context: Context,
        title: String? = "提示！",
        contents: String,
        cancel: String? = "取消",
        determine: String? = "确定",
        cCancel: () -> Any? = { dialog.cancel() },
        cDetermine: () -> Any? = { dialog.cancel() }
    ) {
        dialog = AlertDialog.Builder(context, R.style.DialogUtil).create()

        val view = FrameLayout.inflate(context, R.layout.dialog_text, null)
        val mTitle: TextView = view.findViewById(R.id.title)
        val mContent: TextView = view.findViewById(R.id.content)
        val mCancel: TextView = view.findViewById(R.id.cancel)
        val mDetermine: TextView = view.findViewById(R.id.determine)
        mTitle.text = title
        mContent.text = contents
        mCancel.text = cancel
        mDetermine.text = determine
        mCancel.setOnClickListener {
            cCancel()
            dialog.cancel()
        }
        mDetermine.setOnClickListener {
            cDetermine()
            dialog.cancel()
        }
        dialog.apply {
            setView(view)
        }.show()
    }
}