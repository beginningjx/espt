package com.espt.jx.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.espt.jx.App
import com.espt.jx.R
import com.espt.jx.adapter.HomeAdapter
import com.espt.jx.dao.Data
import com.espt.jx.utils.DataStoreUtils
import com.espt.jx.utils.StatusBarUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PublishActivity : AppCompatActivity() {

    private val mRecyclerView: RecyclerView by lazy { findViewById(R.id.recyclerView) }
    private val mList: ArrayList<Data> by lazy { ArrayList() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish)

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
                    .getDataIds(App.db.publishDao().getAllId(DataStoreUtils.getData("id", 0)))
            )
            withContext(Dispatchers.Main) {
                homeAdapter.notifyDataSetChanged()
            }
        }

        homeAdapter.onItemClick = { _, position ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("id", mList[position].data_id)
            startActivity(intent)
        }
        homeAdapter.onItemLongClick = { _, position ->
            CoroutineScope(Dispatchers.IO).launch {
                App.db.publishDao()
                    .delete(DataStoreUtils.getData("id", 0), mList[position].data_id!!)
                withContext(Dispatchers.Main) {
                    mList.removeAt(position)
                    homeAdapter.notifyDataSetChanged()
                }
            }
            false
        }
        findViewById<ImageView>(R.id.close).setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        App.db.close()
        super.onDestroy()
    }
}