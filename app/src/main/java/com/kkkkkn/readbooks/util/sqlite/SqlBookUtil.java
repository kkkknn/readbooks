package com.kkkkkn.readbooks.util.sqlite;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kkkkkn.readbooks.entity.BookInfo;
import com.kkkkkn.readbooks.util.sqlite.util.SQLiteUtil;


public class SqlBookUtil {
    private static volatile SqlBookUtil mSqlBookUtil;
    private SQLiteDatabase sqLiteDatabase;
    private SqlBookUtil(){

    }

    public static synchronized SqlBookUtil getInstance(){
        if(mSqlBookUtil==null){
            synchronized (SqlBookUtil.class){
                if(mSqlBookUtil==null){
                    mSqlBookUtil=new SqlBookUtil();
                }
            }
        }
        return mSqlBookUtil;
    }

    public void setSqLiteUtil(Context context){
        /*this.sqLiteUtil=new SQLiteUtil(context,"APPBook_SQL",1,);
        // 调用getReadableDatabase()或getWritableDatabase()才算真正创建或打开数据库
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();*/
    }


    public boolean UpdateBook(BookInfo bookInfo) {
        return false;
    }

    public boolean addEnjoyBook(BookInfo book, int fromType) {
        return false;
    }

    public boolean setReadProgress(int chapterCount, int charCount, int bookId) {
        return false;
    }

    public boolean delEnjoyBook(int bookId) {
        return false;
    }


}
