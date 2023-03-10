package com.espt.jx.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.espt.jx.App
import com.espt.jx.R
import com.espt.jx.adapter.HomeAdapter
import com.espt.jx.dao.Data
import com.espt.jx.utils.DataStoreUtils
import com.espt.jx.utils.FlowUtil
import com.espt.jx.utils.LiveDataUtils
import com.espt.jx.utils.StatusBarUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CollectActivity : AppCompatActivity() {

    private val mRecyclerView: RecyclerView by lazy { findViewById(R.id.recyclerView) }
    private val mList: ArrayList<Data> by lazy { ArrayList() }
    private lateinit var observer: Observer<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collect)

        init()
        initView()
    }

    private fun init() {
        StatusBarUtils.defaultStatusBar(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView() {

        val homeAdapter = HomeAdapter(mList)
        mRecyclerView.adapter = homeAdapter

        CoroutineScope(Dispatchers.IO).launch {
            mList.addAll(
                App.db.dataDao()
                    .getDataIds(App.db.collectDao().getAllId(DataStoreUtils.getData("id", 0)))
            )
            withContext(Dispatchers.Main) {
                homeAdapter.notifyDataSetChanged()
            }
        }

        homeAdapter.onItemClick = { _, position ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("id", mList[position].data_id)
            intent.putExtra("position", position)
            startActivity(intent)
        }
        homeAdapter.onItemLongClick = { _, position ->
            CoroutineScope(Dispatchers.IO).launch {
                App.db.collectDao()
                    .delete(DataStoreUtils.getData("id", 0), mList[position].data_id!!)
                withContext(Dispatchers.Main) {
                    mList.removeAt(position)
                    homeAdapter.notifyDataSetChanged()
                }
            }
            false
        }
        findViewById<ImageView>(R.id.close).setOnClickListener { finish() }


        LiveDataUtils.collectActivity.observe(this){
            if (mList.isEmpty()) {
                return@observe
            }
            mList.removeAt(it)
            homeAdapter.notifyDataSetChanged()
        }


        // 接收删除收藏的通知
        CoroutineScope(Dispatchers.Main).launch {
            FlowUtil.uiState.collect {
                if (mList.isEmpty()) {
                    return@collect
                }
                mList.removeAt(it)
                homeAdapter.notifyDataSetChanged()
            }
        }
    }
}