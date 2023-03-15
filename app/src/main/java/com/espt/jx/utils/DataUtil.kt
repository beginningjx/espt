package com.espt.jx.utils

import com.google.android.material.tabs.TabLayout

object DataUtil {
    val arrayOf = arrayOf(
        "电脑数码",
        "日用百货",
        "汽车消费",
        "游戏",
        "食品生鲜",
        "金融服务",
        "家用电器",
        "配饰腕表",
        "旅游出行",
        "运动户外",
        "图书音像",
        "文化娱乐",
        "服饰鞋包",
        "玩模乐器",
        "房产置业",
        "个护化妆",
        "办公设备",
        "健康服务",
        "母婴用品",
        "家居家装",
        "艺术收藏"
    )

    fun onTabSelectedListener(callable: (TabLayout.Tab?) -> Unit) =
        object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                callable(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        }

}