package com.kkkkkn.readbooks.util.eventBus.events;

import com.kkkkkn.readbooks.util.eventBus.EventMessage;

public class RegisterEvent {
    public String name;
    public String password;
    public EventMessage message;

    public RegisterEvent(EventMessage message,String name, String password) {
        this.message=message;
        this.name = name;
        this.password = password;
    }
}
