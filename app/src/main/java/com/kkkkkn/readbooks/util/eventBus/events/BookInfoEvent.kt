package com.kkkkkn.readbooks.util.eventBus.events

import com.kkkkkn.readbooks.util.eventBus.EventMessage

class BookInfoEvent {
    var message: EventMessage
    var token: String
    var accountId: Int
    var bookId: Int
    var pageSize = 0
    var pageCount = 0

    constructor(
        message: EventMessage,
        token: String,
        accountId: Int,
        bookId: Int,
        pageSize: Int,
        pageCount: Int
    ) {
        this.message = message
        this.token = token
        this.accountId = accountId
        this.bookId = bookId
        this.pageSize = pageSize
        this.pageCount = pageCount
    }

    constructor(message: EventMessage, token: String, accountId: Int, bookId: Int) {
        this.message = message
        this.token = token
        this.accountId = accountId
        this.bookId = bookId
    }
}