package com.kkkkkn.readbooks.model;

public class BaseModel {
    public interface CallBack{
        void onSuccess(int type,Object object);
        void onError(int type,Object object);
    }
}
