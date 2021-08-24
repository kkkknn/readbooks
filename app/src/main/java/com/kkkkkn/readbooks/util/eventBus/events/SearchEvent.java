package com.kkkkkn.readbooks.util.eventBus.events;

import com.kkkkkn.readbooks.util.eventBus.EventMessage;

public class SearchEvent {
    public int accountId;
    public EventMessage message;
    public String token;
    public String keyWord;
    public int pageCount;
    public int pageSize;

    public SearchEvent(EventMessage message,int accountId,  String token, String keyWord, int pageCount, int pageSize) {
        this.accountId = accountId;
        this.message = message;
        this.token = token;
        this.keyWord = keyWord;
        this.pageCount = pageCount;
        this.pageSize = pageSize;
    }
}
