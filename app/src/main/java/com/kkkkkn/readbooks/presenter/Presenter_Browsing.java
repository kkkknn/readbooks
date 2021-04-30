package com.kkkkkn.readbooks.presenter;

public class Presenter_Browsing {
    private  volatile static Presenter_Browsing presenter_browsing=null;

    private Presenter_Browsing() {
    }

    public static Presenter_Browsing getInstance(){
        if(presenter_browsing==null){
            synchronized (Presenter_Browsing.class){
                if(presenter_browsing==null){
                    presenter_browsing=new Presenter_Browsing();
                }
            }
        }
        return presenter_browsing;
    }

    //获取章节列表 ，页码数
    public void getChapterList(String bookUrl){

    }

    //获取章节内容
    public void getChapterContent(String chapterUrl){

    }

    //获取保存的章节进度
    public void getBookProgress(int bookUrl){

    }


}
