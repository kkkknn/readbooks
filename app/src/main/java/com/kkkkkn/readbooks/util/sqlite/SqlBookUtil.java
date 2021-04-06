package com.kkkkkn.readbooks.util.sqlite;


import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.os.Build;
import android.util.Log;

import com.kkkkkn.readbooks.entity.BookInfo;
import com.kkkkkn.readbooks.util.sqlite.util.DatabaseHelper;
import com.orhanobut.logger.Logger;


public class SqlBookUtil {
    private static volatile SqlBookUtil mSqlBookUtil;
    private DatabaseHelper databaseHelper;
    private Context context;
    private static int DATABASE_VER=1;
    private static String DATABASE_NAME="APPBook_SQL";
    // 执行 query 方法时，使用该对象，用于生成 cursor
    private SQLiteDatabase.CursorFactory factory = new SQLiteDatabase.CursorFactory() {
        @Override
        public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
            return new SQLiteCursor(masterQuery, editTable, query);
        }
    };

    private SqlBookUtil(){
    }

    public static synchronized SqlBookUtil getInstance(Context context){
        if(mSqlBookUtil==null){
            synchronized (SqlBookUtil.class){
                if(mSqlBookUtil==null){
                    mSqlBookUtil=new SqlBookUtil();
                }
            }
        }
        mSqlBookUtil.context=context;
        return mSqlBookUtil;
    }

    public void initDataBase(){
        if(context!=null&&databaseHelper==null){
            // 调用getReadableDatabase()或getWritableDatabase()才算真正创建或打开数据库
            this.databaseHelper=new DatabaseHelper(context,DATABASE_NAME,factory,DATABASE_VER);
        }
    }


    public boolean UpdateBook(BookInfo bookInfo) {
        if(this.databaseHelper==null){
            return false;
        }

        return false;
    }

    public boolean addEnjoyBook(BookInfo book, int fromType) {
        if(this.databaseHelper==null||book==null||book.isEmpty()){
            return false;
        }
        boolean isExist=false;
        SQLiteDatabase db=databaseHelper.getWritableDatabase();
        //首先查询图书是否存在，根据图书来源，图书名字，作者名字判断
        if(book!=null&&!book.getAuthorName().isEmpty()&&!book.getBookName().isEmpty()){
            String searchBook="select * from "+
                    DatabaseHelper.BookTableName+
                    " where "+
                    DatabaseHelper.BookTable_field_bookName+
                    "=? and "+
                    DatabaseHelper.BookTable_field_bookAuthorName+
                    "=?";
            String[] values=new String[2];
            values[0] = book.getBookName();
            values[1] = book.getAuthorName();
            Cursor cursor=db.rawQuery(searchBook,values);
            //有此图书就进行属性修改，
            if(cursor!=null&&cursor.getCount()>0){
                while (cursor.moveToNext()){
                    String bookId=cursor.getColumnName(0);
                    Logger.d(bookId);
                }
                return true;
            }
        }

        //无此图书进行添加
        String addBookString="insert into "+
                DatabaseHelper.BookTableName+
                "(" +
                DatabaseHelper.BookTable_field_bookName+
                ","+
                DatabaseHelper.BookTable_field_bookAuthorName+
                ","+
                DatabaseHelper.BookTable_field_bookImgUrl+
                ","+
                DatabaseHelper.BookTable_field_bookUrl+
                ","+
                DatabaseHelper.BookTable_field_bookIsEnjoy+
                ") values(?,?,?,?,?)";
        String[] values=new String[5];
        values[0]=book.getBookName();
        values[1]=book.getAuthorName();
        values[2]=book.getBookImgUrl();
        values[3]=book.getBookUrl();
        values[4]=book.getIsEnjoy();
        /*values[5]=book.getBookAbout();
        values[6]=book.getNewChapterName();*/

        db.execSQL(addBookString,values);
        db.close();
        return true;
    }

    public boolean setReadProgress(int chapterCount, int charCount, int bookId) {
        if(this.databaseHelper==null){
            return false;
        }

        return false;
    }

    public boolean delEnjoyBook(int bookId) {
        if(this.databaseHelper==null){
            return false;
        }

        return false;
    }


}
