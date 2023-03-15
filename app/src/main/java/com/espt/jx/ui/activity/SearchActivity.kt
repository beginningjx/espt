package com.espt.jx.ui.activity


import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.espt.jx.App
import com.espt.jx.R
import com.espt.jx.dao.HS
import com.espt.jx.ui.fragment.SearchDFragment
import com.espt.jx.ui.fragment.SearchHFragment
import com.espt.jx.utils.DataStoreUtils
import com.espt.jx.utils.StatusBarUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private val mEditText: EditText by lazy { findViewById(R.id.editText) }
    private val mSearch: TextView by lazy { findViewById(R.id.search) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        init()
        initView()
    }

    private fun init() {
        StatusBarUtils.defaultStatusBar(this)
    }

    private fun initView() {
        replaceFrame(SearchHFragment())

        mEditText.setOnEditorActionListener { _: TextView?, keyCode: Int, _: KeyEvent? ->
            if (keyCode == KeyEvent.ACTION_DOWN) {
                search()
            }
            false
        }

        mSearch.setOnClickListener { search() }

        findViewById<ImageView>(R.id.close).setOnClickListener { finish() }
    }

    private fun search() {
        if (mEditText.text.toString() == "") {
            Toast.makeText(applicationContext, "搜索内容不能为空！", Toast.LENGTH_SHORT).show()
            return
        }

        setSearchDFragment()

        if (DataStoreUtils.getData("id", 0) != 0) {
            CoroutineScope(Dispatchers.IO).launch {
                App.db.hsDao().deleteHistoryData(mEditText.text.toString())
                App.db.hsDao()
                    .insert(HS(null, DataStoreUtils.getData("id", 0), mEditText.text.toString()))
            }
        }

        // 隐藏软键盘
        val im = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        im.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

    /**
     * 设置数据页面
     * @param [string] 字符串
     */
    fun setSearchDFragment(string: String? = mEditText.text.toString()) {
        val searchDFragment = SearchDFragment()
        replaceFrame(searchDFragment)
        searchDFragment.initData(string!!)
    }

    /**
     * 取代框架
     * @param [fragment] 片段
     */
    private fun replaceFrame(fragment: Fragment) {
        val bt = supportFragmentManager.beginTransaction()
        bt.replace(R.id.frameLayout, fragment)
        bt.apply { commit() }
    }
}