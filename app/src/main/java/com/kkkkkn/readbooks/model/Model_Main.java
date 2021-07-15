package com.kkkkkn.readbooks.model;

import com.kkkkkn.readbooks.util.eventBus.MessageEvent;

import org.greenrobot.eventbus.Subscribe;

public class Model_Main extends BaseModel {


    @Subscribe
    @Override
    public void syncProgress(MessageEvent event) {

    }
}
