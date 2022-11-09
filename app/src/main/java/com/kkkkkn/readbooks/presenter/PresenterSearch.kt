package com.kkkkkn.readbooks.presenter

import android.content.Context
import android.util.Log
import com.kkkkkn.readbooks.model.BaseModel
import com.kkkkkn.readbooks.model.ModelSearch
import com.kkkkkn.readbooks.model.entity.AccountInfo
import com.kkkkkn.readbooks.model.entity.BookInfo
import com.kkkkkn.readbooks.util.StringUtil.isEmpty
import com.kkkkkn.readbooks.util.eventBus.EventMessage
import com.kkkkkn.readbooks.util.eventBus.events.SearchEvent
import com.kkkkkn.readbooks.view.view.SearchActivityView
import org.greenrobot.eventbus.EventBus

class PresenterSearch(context: Context?, private val searchActivityView: SearchActivityView) :
    BasePresenter(context!!, ModelSearch()), BaseModel.CallBack {
    private val modelSearch: ModelSearch = baseModel as ModelSearch

    init {
        modelSearch.setCallback(this)
    }

    //根据关键字/作者搜索图书，添加到list中并展示  eventbus 发送
    fun searchBook(size: Int, str: String?) {
        if (isEmpty(str)) {
            Log.e("TAG", "searchBook:  搜索关键词为空")
            return
        }
        val accountInfo: AccountInfo = super.accountInfo
        if (!accountInfo.isHasToken) {
            onError(-2, "获取用户信息失败")
            return
        }
        EventBus.getDefault().post(
            SearchEvent(
                EventMessage.SEARCH_BOOK,
                accountInfo.accountId,
                accountInfo.accountToken!!,
                str!!,
                size / PageSize + 1,
                PageSize
            )
        )
    }

    override fun onSuccess(type: Int, `object`: Any) {
        when (type) {
            1 -> searchActivityView.syncBookList(`object` as ArrayList<BookInfo>)
            else -> {}
        }
    }

    override fun onError(type: Int, `object`: Any) {
        when (type) {
            -1 -> Log.i("TAG", "onError: $`object`")
            -2 -> {
                val str = `object` as String
                Log.i("TAG", "onError: $`object`")
                if (str == "令牌验证失败，请重新尝试") {
                    searchActivityView.toLoginActivity()
                }
            }
            else -> {}
        }
    }

    companion object {
        const val PageSize = 20
    }
}