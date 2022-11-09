package com.kkkkkn.readbooks.model.entity

import java.io.Serializable

class BookShelfItem : Serializable {
    var bookid = 0
    var bookName: String? = null
    var bookImgUrl: String? = null
    var isIsUpdate = false

}