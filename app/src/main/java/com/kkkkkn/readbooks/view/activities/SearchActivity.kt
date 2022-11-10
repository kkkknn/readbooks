package com.kkkkkn.readbooks.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kkkkkn.readbooks.databinding.ActivitySearchBinding
import com.kkkkkn.readbooks.model.adapter.SearchBookResultAdapter
import com.kkkkkn.readbooks.model.entity.BookInfo
import com.kkkkkn.readbooks.presenter.PresenterSearch
import com.kkkkkn.readbooks.view.view.SearchActivityView
import com.mancj.materialsearchbar.MaterialSearchBar
import es.dmoral.toasty.Toasty

class SearchActivity : BaseActivity<ActivitySearchBinding>(), SearchActivityView {
    private val arrayList: ArrayList<BookInfo> = ArrayList()
    private var presenterSearch: PresenterSearch? = null
    private var searchStr: String? = null
    private var isEnd = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        presenterSearch = PresenterSearch(applicationContext, this)
        presenterSearch!!.init()
    }


    private fun initView() {
        //listview相关初始化
        val searchBookResultAdapter = SearchBookResultAdapter(arrayList, this)

        mViewBinding.searchListView.layoutManager=LinearLayoutManager(this)
        mViewBinding.searchListView.adapter=(searchBookResultAdapter)
        searchBookResultAdapter.setItemOnClickListener(object :SearchBookResultAdapter.ItemOnClickListener {
            override fun onItemClick(position: Int) {
                if (position < arrayList.size) {
                    toBrowsingActivity(arrayList[position])
                }
            }

        })

        mViewBinding.toolbar.setNavigationOnClickListener {
            //返回上级页面
            finish()
        }

        mViewBinding.searchView.setOnSearchActionListener(object :
            MaterialSearchBar.OnSearchActionListener {
            override fun onSearchStateChanged(enabled: Boolean) {
                if(!enabled){
                    //清空当前列表
                    mViewBinding.searchListView.adapter?.notifyItemRangeRemoved(0,arrayList.size)
                    arrayList.clear()
                }
            }

            override fun onSearchConfirmed(text: CharSequence?) {
                //请求字符串不为空，开始进行网络请求
                if (text != null) {
                    if (text.isNotEmpty()&& arrayList.isNotEmpty()) {
                        //清空当前列表
                        mViewBinding.searchListView.adapter?.notifyItemRangeRemoved(0,arrayList.size)
                        arrayList.clear()
                        /*//防止抬起落下都触发此事件
                                searchView.setIconified(true);*/
                    }
                    //请求搜索
                    searchStr = text.toString()
                    presenterSearch!!.searchBook(arrayList.size, searchStr)
                }
            }

            override fun onButtonClicked(buttonCode: Int) {
                Log.i("TAG", "onButtonClicked: 点击了按钮")
            }
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

    override fun syncBookList(arrayList: ArrayList<BookInfo>) {
        runOnUiThread {
            /*if (list.size < presenterSearch!!.pageSize) {
                isEnd = true
            }*/
            if (mViewBinding.searchListView.visibility == View.INVISIBLE || mViewBinding.searchListView.visibility == View.GONE) {
                mViewBinding.searchListView.visibility = View.VISIBLE
            }
            //增量更新
            for(i in 0 until arrayList.size){
                this.arrayList.add(arrayList[i])
                mViewBinding.searchListView.adapter?.notifyItemInserted(arrayList.size)
            }
            //回到顶部
            mViewBinding.searchListView.scrollToPosition(0)
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