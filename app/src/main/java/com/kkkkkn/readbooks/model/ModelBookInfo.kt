package com.kkkkkn.readbooks.model

import com.kkkkkn.readbooks.model.entity.ChapterInfo
import com.kkkkkn.readbooks.util.network.retrofit.RetrofitUtil
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class ModelBookInfo : BaseModel() {

    fun addEnjoyBook(account_id: Int, book_id: Int, token: String):JSONObject {
        val response =RetrofitUtil.instance.service.addFavoriteBook(
            account_id.toString(),
            token,
            book_id.toString(),
            account_id.toString()
        ).execute()

        val retJsonObject:JSONObject= JSONObject()

        if(response.isSuccessful){
            val retJson=JSONObject(response.body()!!.string())
            when(retJson.getString("code")){
                "success" -> {
                    retJsonObject.put("status",true)
                    retJsonObject.put("data",retJson.getString("data"))
                }
                "error" -> {
                    retJsonObject.put("status",false)
                    retJsonObject.put("data",retJson.getString("data"))
                }
                else -> {
                    retJsonObject.put("status",false)
                    retJsonObject.put("data","解析错误")
                }
            }
        }else{
            retJsonObject.put("status",false)
            retJsonObject.put("data","网络请求失败")
        }
        return retJsonObject
    }

    fun getChapterList(
        account_id: Int,
        book_id: Int,
        token: String,
        page_size: Int,
        page_count: Int
    ) :JSONObject{
        val response =RetrofitUtil.instance.service.getChapterList(
            account_id.toString(),
            token,
            book_id.toString(),
            page_count.toString(),
            page_size.toString()
        ).execute()

        val retJsonObject:JSONObject= JSONObject()
        if(response.isSuccessful){
            val retJson=JSONObject(response.body()!!.string())
            when(retJson.getString("code")){
                "success" -> {
                    val jsonArray = JSONArray(retJson.getString("data"))
                    val arrayList = ArrayList<ChapterInfo>()
                    for (i in 0 until jsonArray.length()) {
                        val chapterInfo = ChapterInfo(jsonArray[i])
                        arrayList.add(chapterInfo)
                    }
                    retJsonObject.put("status",true)
                    retJsonObject.put("data",arrayList)
                }
                "error" -> {
                    retJsonObject.put("status",false)
                    retJsonObject.put("data",retJson.getString("data"))
                }
                else -> {
                    retJsonObject.put("status",false)
                    retJsonObject.put("data","解析错误")
                }
            }
        }else{
            retJsonObject.put("status",false)
            retJsonObject.put("data","网络请求失败")
        }

        return retJsonObject
    }
}