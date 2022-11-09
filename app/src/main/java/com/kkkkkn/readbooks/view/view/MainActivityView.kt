package com.kkkkkn.readbooks.view.view

import com.kkkkkn.readbooks.model.entity.BookInfo

interface MainActivityView :BaseView {
    fun syncBookShelf(list: ArrayList<BookInfo>)
    fun syncBookShelfError(str: String?)
    fun showUpdateDialog(msg: String, url: String, version: String)
    fun toSearchActivity()
    fun toLoginActivity()
    fun updateProgress(progress: Int)
    fun installAPK(filePath: String)
}