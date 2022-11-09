package com.kkkkkn.readbooks.util.eventBus.events

import com.kkkkkn.readbooks.model.entity.BookInfo
import com.kkkkkn.readbooks.util.eventBus.EventMessage

class MainEvent {
    var token: String? = null
    var accountId = 0
    var message: EventMessage
    var url: String? = null
    var path: String? = null
    var name: String? = null
    var bookShelf: ArrayList<BookInfo>? = null

    constructor(message: EventMessage, bookInfos: ArrayList<BookInfo>?) {
        bookShelf = bookInfos
        this.message = message
    }

    constructor(message: EventMessage, accountId: Int, token: String?) {
        this.token = token
        this.accountId = accountId
        this.message = message
    }

    constructor(
        message: EventMessage,
        name: String?,
        path: String?,
        url: String?,
        accountId: Int,
        token: String?
    ) {
        this.token = token
        this.accountId = accountId
        this.message = message
        this.url = url
        this.path = path
        this.name = name
    }
}