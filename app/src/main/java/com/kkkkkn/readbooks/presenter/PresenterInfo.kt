package com.kkkkkn.readbooks.presenter

import android.content.Context
import com.kkkkkn.readbooks.model.BaseModel
import com.kkkkkn.readbooks.model.ModelBookInfo
import com.kkkkkn.readbooks.model.entity.ChapterInfo
import com.kkkkkn.readbooks.util.eventBus.EventMessage
import com.kkkkkn.readbooks.util.eventBus.events.BookInfoEvent
import com.kkkkkn.readbooks.view.view.BookInfoActivityView
import org.greenrobot.eventbus.EventBus

class PresenterInfo(context: Context?, private val bookInfoActivityView: BookInfoActivityView) :
    BasePresenter(context!!, ModelBookInfo()), BaseModel.CallBack {

    val pageSize = 20

    private val modelBookInfo: ModelBookInfo = baseModel as ModelBookInfo

    init {
        modelBookInfo.setCallback(this)
    }

    //添加到用户收藏列表
    fun addBookShelf(book_id: Int) {
        val accountInfo = super.accountInfo
        if (!accountInfo.isHasToken) {
            onError(-2, "用户信息错误")
            return
        }
        EventBus.getDefault().post(
            BookInfoEvent(
                EventMessage.ADD_BOOK,
                accountInfo.accountToken!!,
                accountInfo.accountId,
                book_id
            )
        )
    }

    //读取图书信息，返回相关对象，然后进行展示 eventbus 返回
    fun getBookChapters(size: Int, bookid: Int) {
        //根据bookid读取图书章节列表
        if (bookid == 0 || bookid == -1) {
            onError(-1, "图书信息错误")
            return
        }
        //获取token缓存
        val accountInfo = super.accountInfo
        if (!accountInfo.isHasToken) {
            onError(-2, "用户信息错误")
            return
        }
        EventBus.getDefault().post(
            BookInfoEvent(
                EventMessage.GET_BOOK_CHAPTER_LIST,
                accountInfo.accountToken!!,
                accountInfo.accountId,
                bookid,
                pageSize,
                size / pageSize + 1
            )
        )
    }

    override fun onSuccess(type: Int, `object`: Any) {
        when (type) {
            1001 -> bookInfoActivityView.syncChapterList(`object` as ArrayList<ChapterInfo>)
            1002 -> bookInfoActivityView.showMsgDialog(type, (`object` as String))
            else -> {}
        }
    }

    override fun onError(type: Int, `object`: Any) {
        when (type) {
            -1001 -> bookInfoActivityView.showMsgDialog(type, (`object` as String))
            -1002 -> bookInfoActivityView.showMsgDialog(type, (`object` as String))
            -2 -> bookInfoActivityView.toLoginActivity()
            else -> {}
        }
    }

}