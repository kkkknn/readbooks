package com.kkkkkn.readbooks.presenter;

public class Presenter_Info {
    private  volatile static Presenter_Info presenter_info=null;

    private Presenter_Info() {
    }

    public static Presenter_Info getInstance(){
        if(presenter_info==null){
            synchronized (Presenter_Browsing.class){
                if(presenter_info==null){
                    presenter_info=new Presenter_Info();
                }
            }
        }
        return presenter_info;
    }

    //读取图书信息，返回相关对象，然后进行展示 eventbus 返回
    public void getBookInfo(String bookUrl){

    }
}
