package com.kkkkkn.readbooks.util.eventBus.events;

import com.kkkkkn.readbooks.util.eventBus.EventMessage;

public class BrowsingEvent {
    public EventMessage message;
    public String token;
    public int accountId;
    public int bookId;
    public int pageSize;
    public int pageCount;
    public String path;

    public BrowsingEvent(EventMessage message, String token, int accountId, int bookId, int pageSize, int pageCount) {
        this.message = message;
        this.token = token;
        this.accountId = accountId;
        this.bookId = bookId;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
    }

    public BrowsingEvent(EventMessage message, String token, int accountId,String path) {
        this.message = message;
        this.token = token;
        this.accountId = accountId;
        this.path=path;
    }
}
