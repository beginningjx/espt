package com.espt.jx.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.espt.jx.R
import com.espt.jx.utils.LiveDataUtils
import com.espt.jx.ui.activity.*
import com.espt.jx.utils.DataStoreUtils
import com.espt.jx.utils.LoginUtils


class MyFragment : Fragment() {

    private var view: View? = null
    private var mQqPicture: ImageView? = null
    private var mQqName: TextView? = null
    private var mQq: TextView? = null
    private var mLogin: View? = null
    private var mCollect: LinearLayout? = null
    private var mHistory: LinearLayout? = null
    private var mPublish: LinearLayout? = null
    private var mMessage: LinearLayout? = null
    private var mFeedback: LinearLayout? = null
    private var mSetting: LinearLayout? = null
    private var mExitLogin: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.fragment_my, container, false)

        init()
        initView()

        return view
    }

    private fun init() {
        mQqPicture = view?.findViewById(R.id.qq_picture)
        mQqName = view?.findViewById(R.id.qq_name)
        mQq = view?.findViewById(R.id.qq)
        mLogin = view?.findViewById(R.id.login)
        mCollect = view?.findViewById(R.id.collect)
        mHistory = view?.findViewById(R.id.history)
        mPublish = view?.findViewById(R.id.publish)
        mMessage = view?.findViewById(R.id.message)
        mFeedback = view?.findViewById(R.id.feedback)
        mSetting = view?.findViewById(R.id.setting)
        mExitLogin = view?.findViewById(R.id.exitLogin)
    }

    @SuppressLint("CommitPrefEdits")
    private fun initView() {

        initLogin()

        // 接收通知
        LiveDataUtils.mutableLiveData.observe(requireActivity()) {
            initLogin()
        }

        mLogin?.setOnClickListener {
            if (DataStoreUtils.getData("id", 0) == 0) {
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
            }
        }

        mCollect?.setOnClickListener {
            LoginUtils.isLogin(requireActivity(), CollectActivity::class.java)
        }
        mHistory?.setOnClickListener {
            LoginUtils.isLogin(requireActivity(), HistoryActivity::class.java)
        }
        mPublish?.setOnClickListener {
            LoginUtils.isLogin(requireActivity(), PublishActivity::class.java)
        }
        mMessage?.setOnClickListener {
            LoginUtils.isLogin(requireActivity(), MessageActivity::class.java)
        }
        mFeedback?.setOnClickListener {
            LoginUtils.isLogin(requireActivity(), FeedbackActivity::class.java)
        }
        mSetting?.setOnClickListener {
            startActivity(Intent(requireActivity(), SettingActivity::class.java))
        }

        mExitLogin?.setOnClickListener {
            // 退出登录
            DataStoreUtils.clearData()

            // 初始化登录
            initLogin(
                mQqPicture!!,
                ContextCompat.getDrawable(requireContext(), R.mipmap.ic_launcher)!!,
                "点击登录",
                "",
                View.GONE
            )
        }
    }

    /**
     * 初始化登录
     * @param [imageView] 图像视图
     * @param [drawable] 图片
     * @param [qqName] qq名字
     * @param [qq] qq
     * @param [int] 隐藏
     */
    private fun <T> initLogin(
        imageView: ImageView,
        drawable: T,
        qqName: String,
        qq: String,
        int: Int
    ) {
        // 初始化登录
        Glide.with(imageView)
            .load(drawable)
            .into(imageView)
        mQqName?.text = qqName
        mQq?.text = qq
        mExitLogin?.visibility = int
    }


    private fun initLogin() {
        if (DataStoreUtils.getData("id", 0) != 0) {
            // 初始化登录
            initLogin(
                mQqPicture!!,
                DataStoreUtils.getData("qq_picture", ""),
                DataStoreUtils.getData("qq_name", ""),
                DataStoreUtils.getData("qq", ""),
                View.VISIBLE
            )
        }
    }
}