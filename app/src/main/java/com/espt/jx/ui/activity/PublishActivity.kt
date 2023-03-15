package com.espt.jx.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.espt.jx.App
import com.espt.jx.R
import com.espt.jx.adapter.HomeAdapter
import com.espt.jx.dao.Data
import com.espt.jx.utils.DataStoreUtils
import com.espt.jx.utils.DialogUtil
import com.espt.jx.utils.FlowBus
import com.espt.jx.utils.StatusBarUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PublishActivity : AppCompatActivity() {
    private val mList by lazy { ArrayList<Data>() }
    private val mRecyclerView: RecyclerView by lazy { findViewById(R.id.recyclerView) }
    private lateinit var homeAdapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish)

        init()
        initView()
    }

    private fun init() {
        StatusBarUtils.defaultStatusBar(this)
        homeAdapter = HomeAdapter(mList)
        mRecyclerView.adapter = homeAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView() {
        initData()

        homeAdapter.setOnItemClickListener { _, _, position ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("id", homeAdapter.getItem(position)?.data_id)
            startActivity(intent)
        }
        homeAdapter.setOnItemLongClickListener { adapter, _, position ->
            CoroutineScope(Dispatchers.IO).launch {
                App.db.dataDao().delete(adapter.getItem(position)?.data_id!!)
                withContext(Dispatchers.Main) {
                    adapter.removeAt(position)
                    adapter.notifyDataSetChanged()
                    FlowBus.send("HomeFragment")
                }
            }
            false
        }
        findViewById<ImageView>(R.id.close).setOnClickListener {
            finish()
        }

        findViewById<ImageView>(R.id.clear).setOnClickListener {
            DialogUtil.textDialog(this, "警告！", "清空之后无法恢复，确定全部清空吗？", cDetermine = {
                CoroutineScope(Dispatchers.IO).launch {
                    App.db.dataDao().deleteAll(DataStoreUtils.getData("id", 0))
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "清空成功！", Toast.LENGTH_SHORT).show()
                        FlowBus.send("HomeFragment")
                        initData()
                    }
                }
            })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initData() {
        CoroutineScope(Dispatchers.IO).launch {
            mList.clear()
            mList.addAll(App.db.dataDao().getUserId(DataStoreUtils.getData("id", 0)))
            withContext(Dispatchers.Main) {
                homeAdapter.notifyDataSetChanged()
            }
        }
    }
}