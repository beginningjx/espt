package com.espt.jx.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.espt.jx.App
import com.espt.jx.R
import com.espt.jx.adapter.StringAdapter
import com.espt.jx.dao.HS
import com.espt.jx.ui.activity.SearchActivity
import com.espt.jx.utils.DataStoreUtils
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mView = inflater.inflate(R.layout.fragment_search_h, container, false)
        init()
        return mView
    }


    private fun init() {
        mRecyclerView = mView.findViewById(R.id.recyclerView)

        CoroutineScope(Dispatchers.IO).launch {
            if (DataStoreUtils.getData("id", 0) != 0) {
                mList.addAll(App.db.hsDao().getAll(DataStoreUtils.getData("id", 0)))
            }
        }

        stringAdapter = StringAdapter(mList)
        mRecyclerView.adapter = stringAdapter

        stringAdapter.onItemClick = { _, position ->
            (requireContext() as SearchActivity).setSearchDFragment(mList[position].history)
            if (DataStoreUtils.getData("id", 0) != 0) {
                CoroutineScope(Dispatchers.IO).launch {
                    App.db.hsDao().deleteHistoryData(mList[position].history)
                    App.db.hsDao()
                        .insert(HS(null, DataStoreUtils.getData("id", 0), mList[position].history))
                }
            }
        }

        stringAdapter.onItemLongClick = { _, position ->
            CoroutineScope(Dispatchers.IO).launch {
                App.db.hsDao().deleteHistoryData(mList[position].history)

                withContext(Dispatchers.Main) {
                    mList.remove(mList[position])
                    stringAdapter.notifyDataSetChanged()
                }
            }
            false
        }

        mView.findViewById<ImageView>(R.id.clear).setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                App.db.hsDao().deleteAllData(DataStoreUtils.getData("id", 0))
                withContext(Dispatchers.Main) {
                    mList.clear()
                    stringAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}