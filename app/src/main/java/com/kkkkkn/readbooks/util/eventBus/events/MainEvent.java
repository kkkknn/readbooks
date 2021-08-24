package com.kkkkkn.readbooks.util.eventBus.events;

import com.kkkkkn.readbooks.util.eventBus.EventMessage;

public class MainEvent {
    public String token;
    public int accountId;
    public EventMessage message;

    public MainEvent(EventMessage message, int accountId,String token) {
        this.token = token;
        this.accountId = accountId;
        this.message = message;
    }
}
