package com.espt.jx.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.espt.jx.App
import com.espt.jx.R
import com.espt.jx.adapter.StringAdapter
import com.espt.jx.dao.HS
import com.espt.jx.ui.activity.SearchActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@SuppressLint("NotifyDataSetChanged")
class SearchHFragment : Fragment() {

    private lateinit var mView: View
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var stringAdapter: StringAdapter
    private val mList by lazy { ArrayList<HS>() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView = inflater.inflate(R.layout.fragment_search_h, container, false)
        init()
        return mView
    }


    private fun init() {
        mRecyclerView = mView.findViewById(R.id.recyclerView)

        CoroutineScope(Dispatchers.IO).launch {
            mList.addAll(App.db.hsDao().getAll())
        }

        stringAdapter = StringAdapter(mList)
        mRecyclerView.adapter = stringAdapter

        stringAdapter.onItemClick = { _, position ->
            (requireContext() as SearchActivity).setSearchDFragment(mList[position].history)

            CoroutineScope(Dispatchers.IO).launch {
                App.db.hsDao().deleteHistoryData(mList[position].history)
                App.db.hsDao().insert(HS(null, mList[position].history))
            }
        }

        stringAdapter.onItemLongClick = { _, position ->
            CoroutineScope(Dispatchers.IO).launch {
                App.db.hsDao().deleteHistoryData(mList[position].history)

                withContext(Dispatchers.Main){
                    mList.remove(mList[position])
                    stringAdapter.notifyDataSetChanged()
                }
            }
            false
        }
    }
}