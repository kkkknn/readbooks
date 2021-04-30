package com.kkkkkn.readbooks.presenter;

import android.content.Context;

import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.model.sqlite.SqlBookUtil;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class Presenter_Main {
    private volatile static Presenter_Main presenter_main=null;

    private Presenter_Main() {
    }

    public static Presenter_Main getInstance(){
        if(presenter_main==null){
            synchronized (Presenter_Browsing.class){
                if(presenter_main==null){
                    presenter_main=new Presenter_Main();
                }
            }
        }
        return presenter_main;
    }

    //获取书架信息，并显示到页面上 通过eventbus发送
    public void getBookShelfList(Context context){
        SqlBookUtil sqlBookUtil=SqlBookUtil.getInstance(context).initDataBase();
        ArrayList<BookInfo> list=sqlBookUtil.getEnjoyBook();
        EventBus.getDefault().post(new MessageEvent(EventMessage.SYNC_BOOKSHELF,list));
    }

    //获取APP更新信息，通过eventbus发送
    public void checkUpdate(){

    }

    //更新APK
    public void downloadAPK(){

    }


}
