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
import com.espt.jx.ui.activity.SearchActivity
import com.espt.jx.utils.DataUtil
import com.espt.jx.utils.FlowBus
import com.espt.jx.utils.LoginUtils
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {

    private lateinit var mView: View
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mSearch: MaterialCardView
    private lateinit var mTabLayout: TabLayout
    private lateinit var homeAdapter: HomeAdapter
    private val mList by lazy { ArrayList<Data>() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView = inflater.inflate(R.layout.fragment_home, container, false)
        init()
        return mView
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun init() {
        mRecyclerView = mView.findViewById(R.id.recyclerView)
        mSearch = mView.findViewById(R.id.search)
        mTabLayout = mView.findViewById(R.id.tabLayout)

        homeAdapter = HomeAdapter(mList)
        mRecyclerView.adapter = homeAdapter

        homeAdapter.setOnItemClickListener { adapter, _, position ->
            val intent = Intent(requireContext(), DetailsActivity::class.java)
            intent.putExtra("id", adapter.getItem(position)?.data_id)
            LoginUtils.isLogin(requireContext(), intent)
        }

        mSearch.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }

        var str = "全部"

        mTabLayout.addOnTabSelectedListener(DataUtil.onTabSelectedListener {
            str = it?.text.toString()
            getData(str)
        })

        mTabLayout.addTab(mTabLayout.newTab().setText("全部"), true)
        for (item in DataUtil.arrayOf) {
            mTabLayout.addTab(mTabLayout.newTab().setText(item), false)
        }

        FlowBus.receive("HomeFragment") {
            getData(str)
        }
    }

    /**
     * 获取数据
     * @param [string] String
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun getData(string: String) {
        CoroutineScope(Dispatchers.IO).launch {
            mList.clear()
            if (string == "全部") {
                mList.addAll(App.db.dataDao().getAll())
            } else {
                mList.addAll(App.db.dataDao().getDataCategory(string))
            }
            withContext(Dispatchers.Main) {
                homeAdapter.notifyDataSetChanged()
            }
        }
    }

}