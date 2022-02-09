package com.kkkkkn.readbooks.util.eventBus.events;

import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;

import java.util.ArrayList;

public class MainEvent {
    public String token;
    public int accountId;
    public EventMessage message;
    public String url;
    public String path;
    public String name;
    public ArrayList<BookInfo> bookShelf;

    public MainEvent(EventMessage message, ArrayList<BookInfo> bookInfos) {
        this.bookShelf = bookInfos;
        this.message = message;
    }

    public MainEvent(EventMessage message, int accountId,String token) {
        this.token = token;
        this.accountId = accountId;
        this.message = message;
    }

    public MainEvent(EventMessage message,String name,String path,String url, int accountId,String token) {
        this.token = token;
        this.accountId = accountId;
        this.message = message;
        this.url=url;
        this.path=path;
        this.name=name;
    }
}
