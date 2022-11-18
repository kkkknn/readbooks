package com.kkkkkn.readbooks.model

import com.kkkkkn.readbooks.model.entity.BookInfo
import com.kkkkkn.readbooks.util.network.retrofit.RetrofitUtil
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class ModelSearch : BaseModel() {

    fun searchBook(
        key_word: String,
        page_count: Int,
        page_size: Int,
        account_id: Int,
        token: String
    ):JSONObject {
        val jsonObject=JSONObject()
        val response=RetrofitUtil.instance.service.searchBook(
            account_id.toString(),
            token,
            key_word,
            page_count.toString(),
            page_size.toString()
        ).execute()

        if(response.isSuccessful){
            val json=JSONObject(response.body()!!.string())
            when(json.getString("code")){
                "success"->{
                    //解析json数组
                    val jsonArray = JSONArray(json.getString("data"))
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
                    jsonObject.put("status",true)
                    jsonObject.put("data",arrayList)
                }
                "error"->{
                    jsonObject.put("status",false)
                    jsonObject.put("data",json.getString("data"))
                }
                else->{
                    jsonObject.put("status",false)
                    jsonObject.put("data","网络请求错误")
                }
            }
        }else{
            jsonObject.put("status",false)
            jsonObject.put("data","网络请求错误")
        }
        return jsonObject
    }
}