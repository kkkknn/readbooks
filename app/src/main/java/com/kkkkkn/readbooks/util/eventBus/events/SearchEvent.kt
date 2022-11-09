package com.kkkkkn.readbooks.util.eventBus.events

import com.kkkkkn.readbooks.util.eventBus.EventMessage

class SearchEvent(
    var message: EventMessage,
    var accountId: Int,
    var token: String,
    var keyWord: String,
    var pageCount: Int,
    var pageSize: Int
)