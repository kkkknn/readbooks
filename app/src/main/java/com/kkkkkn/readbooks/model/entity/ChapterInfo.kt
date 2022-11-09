package com.kkkkkn.readbooks.model.entity

import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable

class ChapterInfo(`object`: Any) : Serializable,
    Comparable<ChapterInfo?> {
    var chapterId = 0
    var chapterNum = 0
    var chapterName: String? = null
    var chapterPath: String? = null

    init {
        //jsonobject 强制赋值
        val jsonObject = `object` as JSONObject
        try {
            chapterId = jsonObject.getInt("chapter_id")
            //拆分章节字符串，取出名字和章节数
            val str = jsonObject.getString("chapter_name")
            val arr = str.split("_").toTypedArray()
            chapterNum = arr[0].toInt()
            chapterName = arr[1]
            chapterPath = jsonObject.getString("chapter_url")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun compareTo(other: ChapterInfo?): Int {
        return chapterNum - other!!.chapterNum
    }


}