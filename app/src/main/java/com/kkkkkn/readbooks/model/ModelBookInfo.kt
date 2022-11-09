package com.kkkkkn.readbooks.model

import com.kkkkkn.readbooks.model.entity.ChapterInfo
import com.kkkkkn.readbooks.util.ServerConfig
import com.kkkkkn.readbooks.util.eventBus.EventMessage
import com.kkkkkn.readbooks.util.eventBus.events.BookInfoEvent
import com.kkkkkn.readbooks.util.network.HttpUtil.Companion.instance
import okhttp3.*
import org.greenrobot.eventbus.Subscribe
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

class ModelBookInfo : BaseModel() {
    @Subscribe
    fun syncProgress(event: BookInfoEvent) {
        when (event.message) {
            EventMessage.GET_BOOK_CHAPTER_LIST -> getChapterList(
                event.accountId,
                event.bookId,
                event.token,
                event.pageSize,
                event.pageCount
            )
            EventMessage.ADD_BOOK -> addEnjoyBook(event.accountId, event.bookId, event.token)
            else -> {}
        }
    }

    private fun addEnjoyBook(account_id: Int, book_id: Int, token: String) {
        val formBody = FormBody.Builder()
        formBody.add("book_id", book_id.toString())
        formBody.add("account_id", account_id.toString())
        val request: Request = Request.Builder()
            .url(ServerConfig.IP + ServerConfig.addFavoriteBook)
            .addHeader("accountId", account_id.toString())
            .addHeader("token", token)
            .post(formBody.build()) //传递请求体
            .build()
        instance?.post(request, object : Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val retStr = response.body!!.string()
                try {
                    val retJson = JSONObject(retStr)
                    val retCode = retJson.getString("code")
                    val retData = retJson.getString("data")
                    when (retCode) {
                        "success" -> {
                            callBack!!.onSuccess(1002, "收藏成功")
                        }
                        "error" -> {
                            callBack!!.onError(-1002, retData)
                        }
                        else -> {
                            callBack!!.onError(-1, "请求失败")
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callBack!!.onError(-1, "请求失败")
            }
        })
    }

    private fun getChapterList(
        account_id: Int,
        book_id: Int,
        token: String,
        page_size: Int,
        page_count: Int
    ) {
        val formBody = FormBody.Builder()
        formBody.add("bookId", book_id.toString())
        formBody.add("pageCount", page_count.toString())
        formBody.add("pageSize", page_size.toString())
        val request: Request = Request.Builder()
            .url(ServerConfig.IP + ServerConfig.getChapterList)
            .addHeader("accountId", account_id.toString())
            .addHeader("token", token)
            .post(formBody.build()) //传递请求体
            .build()
        instance?.post(request, object : Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val retStr = response.body?.string()
                try {
                    if(retStr==null){
                        throw NullPointerException()
                    }
                    val retJson = JSONObject(retStr)
                    val retCode = retJson.getString("code")
                    val retData = retJson.getString("data")
                    when (retCode) {
                        "success" -> {
                            //解析返回值
                            val jsonArray = JSONArray(retData)
                            val arrayList = ArrayList<ChapterInfo>()
                            for (i in 0 until jsonArray.length()) {
                                val chapterInfo = ChapterInfo(jsonArray[i])
                                arrayList.add(chapterInfo)
                            }
                            callBack!!.onSuccess(1001, arrayList)
                        }
                        "error" -> {
                            callBack!!.onError(-1001, retData)
                        }
                        else -> {
                            callBack!!.onError(-1, "请求失败")
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callBack!!.onError(-1, "请求失败")
            }
        })
    }
}