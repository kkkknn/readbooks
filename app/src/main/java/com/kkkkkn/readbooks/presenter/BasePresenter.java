package com.kkkkkn.readbooks.presenter;

import android.content.Context;

public class BasePresenter {
    private Context context;

    public BasePresenter(Context context) {
        this.context = context;
    }

    public Context getContext(){
        return context;
    }
}
