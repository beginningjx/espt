package com.espt.jx.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.espt.jx.ui.activity.LoginActivity

object LoginUtils {

    /**
     * 登录
     * @param [id] 用户id
     * @param [qq] qq号码
     * @param [qq_name] qq名字
     * @param [qq_picture] qq照片
     */
    fun login(id: Int, qq: String, qq_name: String, qq_picture: String) {
        DataStoreUtils.setData("id", id)
        DataStoreUtils.setData("qq", qq)
        DataStoreUtils.setData("qq_name", qq_name)
        DataStoreUtils.setData("qq_picture", qq_picture)
    }

    /**
     * 是否登录
     * @param [mContext] 上下文
     * @param [loginActivity] 登录Activity
     */
    fun isLogin(mContext: Context, loginActivity: Class<*>) {
        if (DataStoreUtils.getData("id", 0) != 0) {
            mContext.startActivity(Intent(mContext, loginActivity))
        } else {
            Toast.makeText(mContext, "请登录后在操作", Toast.LENGTH_SHORT).show()
            mContext.startActivity(Intent(mContext, LoginActivity::class.java))
        }
    }

    /**
     * 是否登录
     * @param [mContext] 上下文
     * @param [intent] Intent
     */
    fun isLogin(mContext: Context, intent: Intent) {
        if (DataStoreUtils.getData("id", 0) != 0) {
            mContext.startActivity(intent)
        } else {
            Toast.makeText(mContext, "请登录后在操作", Toast.LENGTH_SHORT).show()
            mContext.startActivity(Intent(mContext, LoginActivity::class.java))
        }
    }
}