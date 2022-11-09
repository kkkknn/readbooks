package com.kkkkkn.readbooks.view.view

import android.os.Bundle
import com.kkkkkn.readbooks.model.entity.ChapterInfo

interface BookInfoActivityView : BaseView {
    fun syncChapterList(linkedList: ArrayList<ChapterInfo>)
    fun toLoginActivity()
    fun toBrowsingActivity(bundle: Bundle)
}