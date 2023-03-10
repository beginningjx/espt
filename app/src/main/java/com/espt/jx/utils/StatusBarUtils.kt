package com.espt.jx.utils

import android.app.Activity
import com.espt.jx.R
import com.gyf.immersionbar.ImmersionBar

object StatusBarUtils {

    /**
     * 默认状态栏导航栏 -- 白底黑字
     * @param [mActivity] 活动
     */
    fun defaultStatusBar(mActivity: Activity) {
        ImmersionBar.with(mActivity)
            .barColor(R.color.white) //同时自定义状态栏和导航栏颜色
            .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
            .navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
            .fitsSystemWindows(true)
            .init()
    }
}