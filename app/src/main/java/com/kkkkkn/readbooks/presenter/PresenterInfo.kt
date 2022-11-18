package com.kkkkkn.readbooks.presenter

import android.content.Context
import com.kkkkkn.readbooks.model.ModelBookInfo
import com.kkkkkn.readbooks.model.entity.ChapterInfo
import com.kkkkkn.readbooks.view.view.BookInfoActivityView
import kotlinx.coroutines.*
import org.json.JSONObject

class PresenterInfo(context: Context?, private val bookInfoActivityView: BookInfoActivityView) :
    BasePresenter(context!!, ModelBookInfo()) {
    val pageSize = 20
    private val modelBookInfo: ModelBookInfo = baseModel as ModelBookInfo


    //添加到用户收藏列表
    fun addBookShelf(book_id: Int) {
        val accountInfo = super.accountInfo
        if (!accountInfo.isHasToken) {
            bookInfoActivityView.toLoginActivity()
            return
        }

        val job= Job()
        CoroutineScope(job).launch (Dispatchers.Main){
            var jsonObject:JSONObject?=null
            val result= withContext(Dispatchers.IO){
                jsonObject = modelBookInfo.addEnjoyBook(
                    accountInfo.accountId, book_id,
                    accountInfo.accountToken!!
                )
            }
            if(jsonObject!=null){
                val code= jsonObject!!.getBoolean("status")
                if(code){
                    bookInfoActivityView.showMsgDialog(1, jsonObject!!.getString("data"))
                }else{
                    bookInfoActivityView.showMsgDialog(-1, jsonObject!!.getString("data"))
                }
            }
        }

    }

    //读取图书信息，返回相关对象，然后进行展示 eventbus 返回
    fun getBookChapters(size: Int, bookId: Int) {
        //根据bookid读取图书章节列表
        if (bookId == 0 || bookId == -1) {
            bookInfoActivityView.showMsgDialog(-1,"图书信息错误")
            return
        }
        //获取token缓存
        val accountInfo = super.accountInfo
        if (!accountInfo.isHasToken) {
            bookInfoActivityView.showMsgDialog(-1,"用户信息错误")
            bookInfoActivityView.toLoginActivity()
            return
        }

        val job= Job()
        CoroutineScope(job).launch (Dispatchers.Main){
            var jsonObject:JSONObject?=null
            val result= withContext(Dispatchers.IO){
                jsonObject = modelBookInfo.getChapterList(
                    accountInfo.accountId, bookId,
                    accountInfo.accountToken!!,
                    pageSize,
                    size / pageSize + 1
                )
            }
            if(jsonObject!=null){
                val code= jsonObject!!.getBoolean("status")
                if(code){
                    val list:ArrayList<ChapterInfo> = jsonObject!!.get("data") as ArrayList<ChapterInfo>
                    bookInfoActivityView.syncChapterList(list)
                }else{
                    bookInfoActivityView.showMsgDialog(-1, jsonObject!!.getString("data"))
                }
            }
        }

    }

}