package com.kkkkkn.readbooks.util.eventBus;

import com.kkkkkn.readbooks.model.BaseModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MessageEvent {
    public final EventMessage message;
    public final Object value;

    public MessageEvent(EventMessage message,Object what){
        this.message=message;
        this.value=what;
    }
}
