package com.kkkkkn.readbooks.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.AdapterView.OnItemClickListener
import android.widget.HeaderViewListAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kkkkkn.readbooks.R
import com.kkkkkn.readbooks.databinding.ActivitySearchBinding
import com.kkkkkn.readbooks.model.adapter.SearchBookResultAdapter
import com.kkkkkn.readbooks.model.entity.BookInfo
import com.kkkkkn.readbooks.presenter.Presenter_Search
import com.kkkkkn.readbooks.view.customView.CustomSearchView.SearchViewListener
import com.kkkkkn.readbooks.view.customView.SoftKeyBoardListener
import com.kkkkkn.readbooks.view.customView.SoftKeyBoardListener.OnSoftKeyBoardChangeListener
import com.kkkkkn.readbooks.view.view.SearchActivityView
import es.dmoral.toasty.Toasty

class SearchActivity : BaseActivity<ActivitySearchBinding>(), SearchActivityView {
    private val arrayList: ArrayList<BookInfo> = ArrayList()
    private var presenterSearch: Presenter_Search? = null
    private var searchStr: String? = null
    private var isEnd = false
    private var softKeyBoardListener: SoftKeyBoardListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        setSoftKeyBoardListener()
        presenterSearch = Presenter_Search(applicationContext, this)
        presenterSearch!!.init()
    }

    private fun setSoftKeyBoardListener() {
        softKeyBoardListener = SoftKeyBoardListener(this@SearchActivity)
        //软键盘状态监听
        softKeyBoardListener!!.setListener(object : OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {
                //软键盘已经显示，做逻辑
                mViewBinding.searchView.onKeyBoardState(true)
            }

            override fun keyBoardHide(height: Int) {
                //软键盘已经隐藏,做逻辑
                mViewBinding.searchView.onKeyBoardState(false)
            }
        })
    }

    private fun initView() {
        //listview相关初始化
        val searchBookResultAdapter = SearchBookResultAdapter(arrayList, this)

        mViewBinding.searchListView.layoutManager=LinearLayoutManager(this)
        mViewBinding.searchListView.adapter=(searchBookResultAdapter)
        searchBookResultAdapter.setItemOnClickListener(SearchBookResultAdapter.ItemOnClickListener {
            if (it < arrayList.size) {
                toBrowsingActivity(arrayList[it])
            }
        })

        mViewBinding.searchView.requestEdit()
        mViewBinding.searchView.setSearchViewListener(object : SearchViewListener {
            override fun onRefreshAutoComplete(text: String) {}
            override fun onSearch(text: String) {
                //请求字符串不为空，开始进行网络请求
                if (text.isNotEmpty()) {
                    isEnd = false
                    //清空当前列表
                    arrayList.clear()
                    mViewBinding.searchListView.adapter?.notifyDataSetChanged()
                    //请求搜索
                    searchStr = text
                    presenterSearch!!.searchBook(arrayList.size, searchStr)
                    /*//防止抬起落下都触发此事件
                    searchView.setIconified(true);*/
                }
            }

            override fun onScancode() {}
            override fun onEditViewClick() {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewBinding.searchView.visibility = View.VISIBLE
        arrayList.clear()
        presenterSearch!!.release()
    }

    private fun toBrowsingActivity(bookInfo: BookInfo) {
        val intent = Intent(applicationContext, BookInfoActivity::class.java)
        intent.putExtra("bookInfo", bookInfo)
        startActivity(intent)
    }

    override fun syncBookList(list: ArrayList<BookInfo>) {
        runOnUiThread {
            if (list.size < presenterSearch!!.pageSize) {
                isEnd = true
            }
            if (mViewBinding.searchListView.visibility == View.INVISIBLE || mViewBinding.searchListView.visibility == View.GONE) {
                mViewBinding.searchListView.visibility = View.VISIBLE
            }
            mViewBinding.loadingView.visibility = View.GONE
            arrayList.addAll(list)
            mViewBinding.searchListView.adapter?.notifyDataSetChanged()
        }
    }

    override fun toLoginActivity() {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
    }

    override fun showMsgDialog(type: Int, msg: String) {
        runOnUiThread {
            if (type > 0) {
                Toasty.success(applicationContext, msg, Toast.LENGTH_SHORT, true).show()
            } else {
                Toasty.error(applicationContext, msg, Toast.LENGTH_SHORT, true).show()
            }
        }
    }


    override fun getViewBinding(): ActivitySearchBinding {
        return ActivitySearchBinding.inflate(layoutInflater)
    }
}