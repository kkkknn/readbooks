package com.kkkkkn.readbooks.view.view

import com.kkkkkn.readbooks.model.entity.BookInfo

interface SearchActivityView :BaseView{
    fun syncBookList(arrayList: ArrayList<BookInfo>)
    fun toLoginActivity()
}