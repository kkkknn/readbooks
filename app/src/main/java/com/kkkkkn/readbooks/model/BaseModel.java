package com.kkkkkn.readbooks.model;


import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseModel {
    private static final String cachePath="cache";
    private boolean is_register=false;
    private CallBack callBack;

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallback(CallBack callback){
        this.callBack=callback;
    }

    public interface CallBack{
        void onSuccess(int type,Object object);
        void onError(int type,Object object);
    }

    public void register(){
        if(!is_register){
            EventBus.getDefault().register(this);
            is_register=true;
        }
    }

    public void unregister(){
        if(is_register){
            EventBus.getDefault().unregister(this);
            is_register=false;
        }
    }
    public String getCachePath(Context context){
        return context.getFilesDir().getAbsolutePath()+cachePath;
    }


}
