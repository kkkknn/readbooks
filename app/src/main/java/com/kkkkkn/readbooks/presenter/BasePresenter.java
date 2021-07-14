package com.kkkkkn.readbooks.presenter;

import android.content.Context;

import com.kkkkkn.readbooks.model.BaseModel;

public class BasePresenter {
    private Context context;
    private BaseModel baseModel;

    public BasePresenter(Context context,BaseModel baseModel) {
        this.context = context;
        this.baseModel=baseModel;
    }

    public Context getContext(){
        return context;
    }

    public void init(){
        baseModel.register();
    }

    public void release(){
        baseModel.unregister();
    }

    public BaseModel getBaseModel(){
        return baseModel;
    }
}
