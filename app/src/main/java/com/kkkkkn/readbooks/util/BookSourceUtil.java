package com.kkkkkn.readbooks.util;

public class BookSourceUtil {
    private static BookSourceUtil bookSourceUtil;
    private BookSourceImp imp;
    private BookSourceUtil() {
    }

    public static BookSourceUtil getBookSourceUtil(){
        if(bookSourceUtil==null){
            synchronized (BookSourceUtil.class){
                if(bookSourceUtil==null){
                    bookSourceUtil=new BookSourceUtil();
                }
            }
        }
        return bookSourceUtil;
    }

    public void setBookSource(BookSourceImp bookSourceImp){
        this.imp=bookSourceImp;
    }

    public void getChapterList(String str){
        imp.getChapterList(str);
    }

    public void getChapterContent(String str){
        imp.getChapterContent(str);
    }

    public void searchBook(String str){
        imp.searchBook(str);
    }

    public void setListener(BookSourceListener bookSourceListener){
        imp.setListener(bookSourceListener);
    }
}
