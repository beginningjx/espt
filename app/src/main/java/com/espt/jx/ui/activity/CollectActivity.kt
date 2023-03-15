package com.espt.jx.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.espt.jx.App
import com.espt.jx.R
import com.espt.jx.adapter.HomeAdapter
import com.espt.jx.dao.Data
import com.espt.jx.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("NotifyDataSetChanged")
class CollectActivity : AppCompatActivity() {

    private val mRecyclerView: RecyclerView by lazy { findViewById(R.id.recyclerView) }
    private val mList by lazy { ArrayList<Data>() }
    private lateinit var homeAdapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collect)

        init()
        initView()
    }

    private fun init() {
        StatusBarUtils.defaultStatusBar(this)
        homeAdapter = HomeAdapter(mList)
        mRecyclerView.adapter = homeAdapter
    }

    private fun initView() {

        initData()

        val registerForActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                initData()
            }

        homeAdapter.setOnItemClickListener { adapter, _, position ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("id", adapter.getItem(position)?.data_id)
            intent.putExtra("position", position)
            registerForActivityResult.launch(intent)
        }
        homeAdapter.setOnItemLongClickListener { adapter, _, position ->
            CoroutineScope(Dispatchers.IO).launch {
                App.db.collectDao()
                    .delete(
                        DataStoreUtils.getData("id", 0),
                        mList[position].data_id!!
                    )
                withContext(Dispatchers.Main) {
                    adapter.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
            }
            false
        }
        findViewById<ImageView>(R.id.close).setOnClickListener { finish() }

        findViewById<ImageView>(R.id.clear).setOnClickListener {
            DialogUtil.textDialog(this, "警告！", "清空之后无法恢复，确定全部清空吗？", cDetermine = {
                CoroutineScope(Dispatchers.IO).launch {
                    App.db.collectDao().deleteAll(DataStoreUtils.getData("id", 0))
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "清空成功！", Toast.LENGTH_SHORT).show()
                        initData()
                    }
                }
            })
        }
    }


    private fun initData() {
        CoroutineScope(Dispatchers.IO).launch {
            mList.clear()
            mList.addAll(
                App.db.dataDao()
                    .getDataIds(App.db.collectDao().getAllId(DataStoreUtils.getData("id", 0)))
            )
            withContext(Dispatchers.Main) {
                homeAdapter.notifyDataSetChanged()
            }
        }
    }
}