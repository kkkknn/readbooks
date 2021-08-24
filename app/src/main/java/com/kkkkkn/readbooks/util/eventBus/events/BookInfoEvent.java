package com.kkkkkn.readbooks.util.eventBus.events;

import com.kkkkkn.readbooks.util.eventBus.EventMessage;

public class BookInfoEvent {
    public EventMessage message;
    public String token;
    public int accountId;
    public int bookId;
    public int pageSize=0;
    public int pageCount=0;

    public BookInfoEvent(EventMessage message,String token, int accountId, int bookId, int pageSize, int pageCount) {
        this.message=message;
        this.token = token;
        this.accountId = accountId;
        this.bookId = bookId;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
    }

    public BookInfoEvent(EventMessage message,String token, int accountId, int bookId) {
        this.message=message;
        this.token = token;
        this.accountId = accountId;
        this.bookId = bookId;
    }
}
