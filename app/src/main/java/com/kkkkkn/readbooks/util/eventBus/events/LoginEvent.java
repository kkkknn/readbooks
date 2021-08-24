package com.kkkkkn.readbooks.util.eventBus.events;

import com.kkkkkn.readbooks.util.eventBus.EventMessage;

public class LoginEvent {
    public String name;
    public String password;
    public EventMessage message;

    public LoginEvent(EventMessage message,String name, String password) {
        this.message=message;
        this.name = name;
        this.password = password;
    }
}
