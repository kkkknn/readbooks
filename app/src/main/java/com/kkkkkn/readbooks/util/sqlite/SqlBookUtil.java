package com.kkkkkn.readbooks.util.sqlite;


import android.content.ContentValues;
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

import java.util.ArrayList;
import java.util.List;


public class SqlBookUtil {
    private static final String TAG="SqlBookUtil";
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

    public SqlBookUtil initDataBase(){
        if(context!=null&&databaseHelper==null){
            // 调用getReadableDatabase()或getWritableDatabase()才算真正创建或打开数据库
            this.databaseHelper=new DatabaseHelper(context,DATABASE_NAME,factory,DATABASE_VER);
        }
        return mSqlBookUtil;
    }


    public ArrayList<BookInfo> getEnjoyBook() {
        if(this.databaseHelper==null){
            return null;
        }
        SQLiteDatabase db=databaseHelper.getWritableDatabase();
        ArrayList<BookInfo> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "select * from "+DatabaseHelper.BookTableName+
                        " where "+DatabaseHelper.BookTable_field_bookIsEnjoy +" = 1"
                ,null);
        while(cursor.moveToNext()){
            BookInfo info=new BookInfo();
            info.setBookId(cursor.getInt(0));
            info.setAuthorName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.BookTable_field_bookAuthorName)));
            info.setBookName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.BookTable_field_bookName)));
            info.setBookUrl(cursor.getString(cursor.getColumnIndex(DatabaseHelper.BookTable_field_bookUrl)));
            info.setBookFromType(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.BookTable_field_bookFromType)));
            info.setBookAbout(cursor.getString(cursor.getColumnIndex(DatabaseHelper.BookTable_field_bookAbout)));
            info.setNewChapterName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.BookTable_field_bookNewChapterName)));
            info.setBookImgUrl(cursor.getString(cursor.getColumnIndex(DatabaseHelper.BookTable_field_bookImgUrl)));
            info.setEnjoy(cursor.getString(cursor.getColumnIndex(DatabaseHelper.BookTable_field_bookIsEnjoy)).equals("1"));
            info.setChapterPagesUrlStr(cursor.getString(cursor.getColumnIndex(DatabaseHelper.BookTable_field_bookChapterPagesUrl)));
            list.add(info);
        }
        db.close();
        return list;
    }

    public boolean addEnjoyBook(BookInfo book) {
        if(this.databaseHelper==null||book==null||book.isEmpty()){
            return false;
        }
        SQLiteDatabase db=databaseHelper.getWritableDatabase();
        //首先查询图书是否存在，根据图书来源，图书名字，作者名字判断
        String where=DatabaseHelper.BookTable_field_bookName+
                "=? and "+
                DatabaseHelper.BookTable_field_bookAuthorName+
                "=? and "+
                DatabaseHelper.BookTable_field_bookFromType+
                "=?";
        ContentValues values=new ContentValues();
        values.put(DatabaseHelper.BookTable_field_bookIsEnjoy,book.getEnjoy()?1:0);
        int ret=db.update(DatabaseHelper.BookTableName,values,where,new String[]{book.getBookName(),book.getAuthorName(),Integer.toString(book.getBookFromType())});
        if(ret==0){
            //无此图书进行添加
            ContentValues bookInfo=new ContentValues();
            bookInfo.put(DatabaseHelper.BookTable_field_bookName,book.getBookName());
            bookInfo.put(DatabaseHelper.BookTable_field_bookAuthorName,book.getAuthorName());
            bookInfo.put(DatabaseHelper.BookTable_field_bookImgUrl,book.getBookImgUrl());
            bookInfo.put(DatabaseHelper.BookTable_field_bookUrl,book.getBookUrl());
            bookInfo.put(DatabaseHelper.BookTable_field_bookIsEnjoy,book.getEnjoy()?1:0);
            bookInfo.put(DatabaseHelper.BookTable_field_bookAbout,book.getBookAbout());
            bookInfo.put(DatabaseHelper.BookTable_field_bookNewChapterName,book.getNewChapterName());
            bookInfo.put(DatabaseHelper.BookTable_field_bookFromType,book.getBookFromType());
            bookInfo.put(DatabaseHelper.BookTable_field_bookChapterPagesUrl,book.getChapterPagesUrlStr());
            //此处返回的ret2值是插入成功后的主键ID
            long ret2=db.insert(DatabaseHelper.BookTableName,null,bookInfo);
            Log.i(TAG, "addEnjoyBook: "+ret2);
        }
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
