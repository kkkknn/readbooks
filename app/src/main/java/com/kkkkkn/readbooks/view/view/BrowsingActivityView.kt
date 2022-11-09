package com.kkkkkn.readbooks.view.view

import com.kkkkkn.readbooks.model.entity.ChapterInfo
import org.json.JSONArray

interface BrowsingActivityView :BaseView {
    fun syncChapterList(list: ArrayList<ChapterInfo>)
    fun toLoginActivity()
    fun syncReadView(jsonArray: JSONArray)
    fun setLoading(type: Boolean)
}