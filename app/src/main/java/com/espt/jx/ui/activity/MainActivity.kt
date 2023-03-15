package com.espt.jx.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.espt.jx.R
import com.espt.jx.ui.fragment.HomeFragment
import com.espt.jx.ui.fragment.MyFragment
import com.espt.jx.utils.FlowBus
import com.espt.jx.utils.LoginUtils
import com.espt.jx.utils.StatusBarUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    // 设置页面
    private val fragments = arrayOf(HomeFragment(), MyFragment())
    private val mClose: ImageView by lazy { findViewById(R.id.close) }
    private val mViewPager2: ViewPager2 by lazy { findViewById(R.id.viewPager2) }
    private val mHome: ImageView by lazy { findViewById(R.id.home) }
    private val mMy: ImageView by lazy { findViewById(R.id.my) }
    private val mFloatingActionButton: FloatingActionButton by lazy {
        findViewById(
            R.id.floatingActionButton
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        initView()
    }


    /**
     * 初始化
     */
    private fun init() {
        StatusBarUtils.defaultStatusBar(this)

        mViewPager2.offscreenPageLimit = fragments.size
        mViewPager2.adapter = fragmentStateAdapter(fragments)
        mViewPager2.registerOnPageChangeCallback(onPageChangeCallback())
        mViewPager2.getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER
    }

    /**
     * 初始化视图
     */
    private fun initView() {
        mClose.setOnClickListener {
            finish()
        }
        mHome.setOnClickListener {
            mViewPager2.currentItem = 0
        }
        mMy.setOnClickListener {
            mViewPager2.currentItem = 1
        }

        val registerForActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == 0) {
                    FlowBus.send("HomeFragment")
                }
            }

        mFloatingActionButton.setOnClickListener {
            LoginUtils.isLogin(this, SellActivity::class.java, registerForActivityResult)
        }
    }

    /**
     * 碎片状态适配器
     * @param [fragments] 片段
     */
    private fun fragmentStateAdapter(fragments: Array<Fragment>) =
        object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }

            override fun getItemCount(): Int {
                return fragments.size
            }
        }

    /**
     * 改变回调页
     */
    private fun onPageChangeCallback() = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (position == 0) {
                mHome.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext, R.color.background
                    )
                )
                mMy.setColorFilter(ContextCompat.getColor(applicationContext, R.color.black))
            } else {
                mHome.setColorFilter(ContextCompat.getColor(applicationContext, R.color.black))
                mMy.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext, R.color.background
                    )
                )
            }
        }
    }
}