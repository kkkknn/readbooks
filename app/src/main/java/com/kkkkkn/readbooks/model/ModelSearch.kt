package com.kkkkkn.readbooks.model

import com.kkkkkn.readbooks.model.entity.BookInfo
import com.kkkkkn.readbooks.util.ServerConfig
import com.kkkkkn.readbooks.util.StringUtil.isEmpty
import com.kkkkkn.readbooks.util.eventBus.EventMessage
import com.kkkkkn.readbooks.util.eventBus.events.SearchEvent
import com.kkkkkn.readbooks.util.network.HttpUtil.Companion.instance
import okhttp3.*
import org.greenrobot.eventbus.Subscribe
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

class ModelSearch : BaseModel() {
    @Subscribe
    fun syncProgress(event: SearchEvent) {
        when (event.message) {
            EventMessage.SEARCH_BOOK ->                 //搜索图书
                searchBook(
                    event.keyWord,
                    event.pageCount,
                    event.pageSize,
                    event.accountId,
                    event.token
                )
            else -> {}
        }
    }

    private fun searchBook(
        key_word: String,
        page_count: Int,
        page_size: Int,
        account_id: Int,
        token: String
    ) {
        val formBody = FormBody.Builder()
        formBody.add("str", key_word)
        formBody.add("pageCount", page_count.toString())
        formBody.add("pageSize", page_size.toString())
        val request: Request = Request.Builder()
            .url(ServerConfig.IP + ServerConfig.searchBook)
            .post(formBody.build()) //传递请求体
            .addHeader("accountId", account_id.toString())
            .addHeader("token", token)
            .build()
        instance?.post(request, object : Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                //解析返回值
                try {
                    val jsonObject = JSONObject(response.body!!.string())
                    val code = jsonObject.getString("code")
                    val data = jsonObject.getString("data")
                    if (isEmpty(code)) {
                        callBack!!.onError(-1, "网络请求错误")
                        return
                    }
                    if (code == "success") {
                        //解析json数组
                        val jsonArray = JSONArray(data)
                        val arrayList = ArrayList<BookInfo>()
                        for (j in 0 until jsonArray.length()) {
                            val bookInfo = BookInfo()
                            val jsonObject1 = jsonArray[j] as JSONObject
                            bookInfo.setAuthorName(jsonObject1.getString("author_name"))
                            bookInfo.setBookAbout(jsonObject1.getString("book_about"))
                            bookInfo.setChapterSum(jsonObject1.getInt("book_chapter_sum"))
                            bookInfo.setBookId(jsonObject1.getInt("book_id"))
                            bookInfo.setBookImgUrl(jsonObject1.getString("book_img_url"))
                            bookInfo.setBookName(jsonObject1.getString("book_name"))
                            bookInfo.setBookUrl(jsonObject1.getString("book_url"))
                            bookInfo.setSourceName(jsonObject1.getString("source_name"))
                            arrayList.add(bookInfo)
                        }
                        callBack!!.onSuccess(1, arrayList)
                    } else if (code == "error") {
                        callBack!!.onError(-2, data)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callBack!!.onError(-1, "网络请求错误")
            }
        })
    }
}