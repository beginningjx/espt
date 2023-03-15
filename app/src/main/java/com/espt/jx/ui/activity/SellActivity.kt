package com.espt.jx.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.espt.jx.App
import com.espt.jx.R
import com.espt.jx.adapter.SellAdapter
import com.espt.jx.dao.Data
import com.espt.jx.utils.DataStoreUtils
import com.espt.jx.utils.DataUtil
import com.espt.jx.utils.GlideEngine
import com.espt.jx.utils.StatusBarUtils
import com.google.android.material.tabs.TabLayout
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import top.zibin.luban.Luban
import top.zibin.luban.OnNewCompressListener
import java.io.File


@SuppressLint("NotifyDataSetChanged")
class SellActivity : AppCompatActivity() {

    private val mClose: TextView by lazy { findViewById(R.id.close) }
    private val mPublish: CardView by lazy { findViewById(R.id.publish) }
    private val mEditText: EditText by lazy { findViewById(R.id.editText) }
    private val mRecyclerView: RecyclerView by lazy { findViewById(R.id.recyclerView) }
    private val mCategoryText: TextView by lazy { findViewById(R.id.categoryText) }
    private val mTabLayout: TabLayout by lazy { findViewById(R.id.tabLayout) }
    private val mEditMoney: EditText by lazy { findViewById(R.id.editMoney) }
    private var mList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell)

        init()
        initView()

    }

    private fun init() {
        StatusBarUtils.defaultStatusBar(this)
    }

    private fun initView() {
        val sellAdapter = SellAdapter(mList)
        mRecyclerView.adapter = sellAdapter

        sellAdapter.onItemClick = { _, _ ->
            PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.createGlideEngine()).setMaxSelectNum(6)
                .setCompressEngine(CompressFileEngine { context, source, call ->
                    Luban.with(context).load(source).ignoreBy(100)
                        .setCompressListener(object : OnNewCompressListener {
                            override fun onStart() {}
                            override fun onSuccess(source: String?, compressFile: File) {
                                call?.onCallback(source, compressFile.absolutePath)
                            }

                            override fun onError(source: String?, e: Throwable?) {
                                call?.onCallback(source, null)
                            }
                        }).launch()
                })
                .forResult(object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(result: ArrayList<LocalMedia?>) {
                        for (item in result) {
                            mList.add(0, item?.compressPath!!)
                        }
                        sellAdapter.notifyDataSetChanged()
                    }

                    override fun onCancel() {}
                })
        }

        sellAdapter.onItemLongClick = { _, position ->
            mList.removeAt(position)
            sellAdapter.notifyDataSetChanged()
            false
        }
        // 关闭
        mClose.setOnClickListener { finish() }
        // 发布
        mPublish.setOnClickListener {

            if (mEditText.text.toString() == "") {
                Toast.makeText(applicationContext, "简介不能为空哦！", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (mList.size == 1) {
                Toast.makeText(applicationContext, "图片不能为空哦！", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (mCategoryText.text.toString() == "去补充") {
                Toast.makeText(applicationContext, "分类不能为空哦！", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (mEditMoney.text.toString() == "") {
                Toast.makeText(applicationContext, "价格是必填项哦！", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val stringBuffer = StringBuffer()
            for (i in 0 until mList.size - 1) {
                stringBuffer.append(mList[i]).append("&&&")
            }

            CoroutineScope(Dispatchers.IO).launch {
                App.db.dataDao().insert(
                    Data(
                        null,
                        mEditText.text.toString(),
                        stringBuffer.toString(),
                        mCategoryText.text.toString(),
                        mEditMoney.text.toString(),
                        DataStoreUtils.getData("id", 0),
                        DataStoreUtils.getData("qq_name", ""),
                        DataStoreUtils.getData("qq_picture", "")
                    )
                )
            }
            setResult(0)
            Toast.makeText(applicationContext, "发布成功！", Toast.LENGTH_SHORT).show()
            finish()
        }

        for (item in DataUtil.arrayOf) {
            mTabLayout.addTab(mTabLayout.newTab().setText(item), false)
        }

        mTabLayout.addOnTabSelectedListener(DataUtil.onTabSelectedListener {
            mCategoryText.text = it?.text
        })
    }
}