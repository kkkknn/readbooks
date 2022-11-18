package com.kkkkkn.readbooks.presenter

import android.content.Context
import android.util.Log
import com.kkkkkn.readbooks.model.ModelSearch
import com.kkkkkn.readbooks.model.entity.AccountInfo
import com.kkkkkn.readbooks.model.entity.BookInfo
import com.kkkkkn.readbooks.util.StringUtil.isEmpty
import com.kkkkkn.readbooks.view.view.SearchActivityView
import kotlinx.coroutines.*
import org.json.JSONObject

class PresenterSearch(context: Context?, private val searchActivityView: SearchActivityView) :
    BasePresenter(context!!, ModelSearch()){
    private val modelSearch: ModelSearch = baseModel as ModelSearch

    //根据关键字/作者搜索图书，添加到list中并展示  eventbus 发送
    fun searchBook(size: Int, str: String?) {
        if (isEmpty(str)) {
            Log.e("TAG", "searchBook:  搜索关键词为空")
            return
        }
        val accountInfo: AccountInfo = super.accountInfo
        if (!accountInfo.isHasToken) {
            searchActivityView.toLoginActivity()
            return
        }
        //协程
        val job = Job()
        CoroutineScope(job).launch(Dispatchers.Main) {
            var jsonObject: JSONObject
            val result = withContext(Dispatchers.IO) {
                jsonObject = modelSearch.searchBook(
                    str!!,
                    size / PageSize + 1,
                    PageSize,
                    accountInfo.accountId,
                    accountInfo.accountToken!!,
                )
            }
            val code = jsonObject.getBoolean("status")
            if (code) {
                searchActivityView.syncBookList(jsonObject.get("data") as ArrayList<BookInfo>)
            } else {
                searchActivityView.showMsgDialog(-1,jsonObject.getString("data"))
            }

        }

    }

    companion object {
        const val PageSize = 20
    }
}