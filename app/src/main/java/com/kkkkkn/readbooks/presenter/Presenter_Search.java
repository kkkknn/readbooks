package com.kkkkkn.readbooks.presenter;

public class Presenter_Search {
    private  volatile static Presenter_Search presenter_search=null;

    private Presenter_Search() {
    }

    public static Presenter_Search getInstance(){
        if(presenter_search==null){
            synchronized (Presenter_Browsing.class){
                if(presenter_search==null){
                    presenter_search=new Presenter_Search();
                }
            }
        }
        return presenter_search;
    }

    //根据关键字/作者搜索图书，添加到list中并展示  eventbus 发送
    public void searchBook(String str){
        if (str==null){
            return;
        }

    }


}
