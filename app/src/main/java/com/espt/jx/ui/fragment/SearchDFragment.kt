package com.espt.jx.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.espt.jx.App
import com.espt.jx.R
import com.espt.jx.adapter.HomeAdapter
import com.espt.jx.dao.Data
import com.espt.jx.ui.activity.DetailsActivity
import com.espt.jx.utils.LoginUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("NotifyDataSetChanged")
class SearchDFragment : Fragment() {

    private lateinit var mView: View
    private lateinit var mRecyclerView: RecyclerView
    private var homeAdapter: HomeAdapter? = null
    private val mList by lazy { ArrayList<Data>() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView = inflater.inflate(R.layout.fragment_search_d, container, false)
        init()
        return mView
    }


    private fun init() {
        mRecyclerView = mView.findViewById(R.id.recyclerView)

        homeAdapter = HomeAdapter(mList)
        mRecyclerView.adapter = homeAdapter

        homeAdapter?.setOnItemClickListener { adapter, _, position ->
            val intent = Intent(requireContext(), DetailsActivity::class.java)
            intent.putExtra("id", adapter.getItem(position)?.data_id)
            LoginUtils.isLogin(requireContext(), intent)
        }
    }


    /**
     * 初始化数据
     * @param [string] 字符串
     */
    fun initData(string: String) {
        CoroutineScope(Dispatchers.IO).launch {
            mList.clear()
            mList.addAll(App.db.dataDao().getIntroductionLike(string))

            withContext(Dispatchers.Main) {
                homeAdapter?.notifyDataSetChanged()
            }
        }
    }
}