package com.kkkkkn.readbooks.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.kkkkkn.readbooks.databinding.ActivityBookInfoBinding
import com.kkkkkn.readbooks.model.adapter.BookChaptersAdapter
import com.kkkkkn.readbooks.model.entity.BookInfo
import com.kkkkkn.readbooks.model.entity.ChapterInfo
import com.kkkkkn.readbooks.presenter.PresenterInfo
import com.kkkkkn.readbooks.util.ImageUtil
import com.kkkkkn.readbooks.view.view.BookInfoActivityView
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import es.dmoral.toasty.Toasty

class BookInfoActivity : BaseActivity<ActivityBookInfoBinding>(), BookInfoActivityView {
    private var bookInfo: BookInfo? = null
    private val chapterList: ArrayList<ChapterInfo> = ArrayList()
    private var chaptersAdapter: BookChaptersAdapter? = null
    private var presenterInfo: PresenterInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        presenterInfo = PresenterInfo(applicationContext, this)
        presenterInfo!!.init()

        //查找图书信息是否存在
        bookInfo = intent.getSerializableExtra("bookInfo") as BookInfo?
        //获取图书信息
        if (bookInfo == null || bookInfo!!.isEmpty) {
            finish()
            return
        }
        mViewBinding.bookInfoAuthorName.text = bookInfo!!.authorName
        mViewBinding.bookInfoBookName.text = bookInfo!!.bookName
        mViewBinding.bookInfoBookAbout.text = bookInfo!!.bookAbout
        bookInfo!!.bookImgUrl?.let {
            ImageUtil.loadImage(
                it,
                applicationContext, mViewBinding.bookInfoBookImg
            )
        }
        //发送获取图书章节列表
        presenterInfo!!.getBookChapters(chapterList.size, bookInfo!!.bookId)
    }

    //初始化绑定控件
    private fun initView() {
        //关闭下拉刷新
        mViewBinding.infoChapterRefresh.setEnableRefresh(false)
        mViewBinding.infoChapterRefresh.setOnLoadMoreListener(object :OnLoadMoreListener{

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                //发送获取图书章节列表
                presenterInfo!!.getBookChapters(chapterList.size, bookInfo!!.bookId)
            }

        })
        
        //绑定控件
        chaptersAdapter = BookChaptersAdapter(chapterList, this)
        mViewBinding.bookInfoChaptersListView.layoutManager= LinearLayoutManager(this)
        mViewBinding.bookInfoChaptersListView.adapter = chaptersAdapter
        //章节点击跳转
        chaptersAdapter!!.setItemOnClickListener(object :BookChaptersAdapter.ItemOnClickListener{
            override fun onItemClick(position: Int) {
                val bundle = Bundle()
                bundle.putSerializable("chapterInfo", chapterList[position])
                bundle.putSerializable("bookInfo", bookInfo)
                Log.i(tag, "initView: 开始跳转 chapterInfo "+chapterList[position].chapterPath+" bookInfo "+bookInfo.toString())
                toBrowsingActivity(bundle)
            }

        })

        //跳转到浏览界面，从第一章开始阅读
        mViewBinding.btnStartRead.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("bookInfo", bookInfo)
            bundle.putSerializable("chapterInfo", chapterList[0])
            toBrowsingActivity(bundle)
        }

        //添加到主页书架
        mViewBinding.btnAddEnjoy.setOnClickListener { //todo 弹窗或者动画效果 自定义加载框
            presenterInfo!!.addBookShelf(bookInfo!!.bookId)
        }


    }

    override fun syncChapterList(linkedList: ArrayList<ChapterInfo>) {
        runOnUiThread {
            if(mViewBinding.infoChapterRefresh.isLoading){
                //判断已无更多数据加载
                if (linkedList.size < presenterInfo!!.pageSize) {
                    mViewBinding.infoChapterRefresh.finishLoadMoreWithNoMoreData()
                }else{
                    mViewBinding.infoChapterRefresh.finishLoadMore(true)
                }
            }
            val start=chapterList.size
            chapterList.addAll(linkedList)
            mViewBinding.bookInfoChaptersListView.adapter?.notifyItemRangeChanged(start,chapterList.size)
        }
    }

    override fun toLoginActivity() {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
    }

    override fun toBrowsingActivity(bundle: Bundle) {
        val intent = Intent(applicationContext, BookBrowsingActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
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

    override fun onDestroy() {
        super.onDestroy()
        chapterList.clear()
        if (presenterInfo != null) {
            presenterInfo!!.release()
        }
    }

    override fun getViewBinding(): ActivityBookInfoBinding {
        return ActivityBookInfoBinding.inflate(layoutInflater)
    }

    override fun editStatusBar(){
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .navigationBarDarkIcon(true)
            .init()
    }
}
