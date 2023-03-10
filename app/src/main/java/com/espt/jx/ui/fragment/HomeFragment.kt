package com.espt.jx.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.espt.jx.App
import com.espt.jx.R
import com.espt.jx.ui.activity.DetailsActivity
import com.espt.jx.adapter.HomeAdapter
import com.espt.jx.dao.Data
import com.espt.jx.utils.LoginUtils
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {

    private var mView: View? = null
    private var mRecyclerView: RecyclerView? = null
    private val mList: ArrayList<Data> by lazy { ArrayList() }
    private var mSearch: MaterialCardView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_home, container, false)
        init()
        initView()
        return mView
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun init() {
        mRecyclerView = mView?.findViewById(R.id.recyclerView)
        mSearch = mView?.findViewById(R.id.search)

        val homeAdapter = HomeAdapter(mList)
        mRecyclerView?.adapter = homeAdapter

        homeAdapter.onItemClick = { _, position ->
            val intent = Intent(requireContext(), DetailsActivity::class.java)
            intent.putExtra("id", mList[position].data_id)
            LoginUtils.isLogin(requireContext(), intent)
        }

        homeAdapter.onItemLongClick = { _, _ ->
            false
        }

        CoroutineScope(Dispatchers.IO).launch {
            mList.addAll(App.db.dataDao().getAll())

            withContext(Dispatchers.Main) {
                homeAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun initView() {

    }


}