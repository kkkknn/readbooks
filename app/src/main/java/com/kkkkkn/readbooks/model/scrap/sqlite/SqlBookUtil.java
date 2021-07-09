package com.kkkkkn.readbooks.model.scrap.sqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.model.scrap.sqlite.util.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


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
        values.put(DatabaseHelper.BookTable_field_bookIsEnjoy,book.isEnjoy()?1:0);
        int ret=db.update(DatabaseHelper.BookTableName,values,where,new String[]{book.getBookName(),book.getAuthorName(),Integer.toString(book.getBookFromType())});
        if(ret==0){
            //无此图书进行添加
            ContentValues bookInfo=new ContentValues();
            bookInfo.put(DatabaseHelper.BookTable_field_bookName,book.getBookName());
            bookInfo.put(DatabaseHelper.BookTable_field_bookAuthorName,book.getAuthorName());
            bookInfo.put(DatabaseHelper.BookTable_field_bookImgUrl,book.getBookImgUrl());
            bookInfo.put(DatabaseHelper.BookTable_field_bookUrl,book.getBookUrl());
            bookInfo.put(DatabaseHelper.BookTable_field_bookIsEnjoy,book.isEnjoy()?1:0);
            bookInfo.put(DatabaseHelper.BookTable_field_bookAbout,book.getBookAbout());
            bookInfo.put(DatabaseHelper.BookTable_field_bookNewChapterName,book.getNewChapterName());
            bookInfo.put(DatabaseHelper.BookTable_field_bookFromType,book.getBookFromType());
            bookInfo.put(DatabaseHelper.BookTable_field_bookChapterPagesUrl,book.getChapterPagesUrlStr());
            //此处返回的ret2值是插入成功后的主键ID
            long ret2=db.insert(DatabaseHelper.BookTableName,null,bookInfo);
            book.setBookId((int)ret2);
            Log.i(TAG, "addEnjoyBook: "+ret2);
            //添加章节记录
            boolean flag=setReadProgress(0,0,0,(int)ret2);
            if(flag){
                Log.i(TAG, "addEnjoyBook: 添加成功");
            }else{
                Log.i(TAG, "addEnjoyBook: 添加失败");
            }
        }
        db.close();
        return true;
    }

    //设置图书浏览进度
    public boolean setReadProgress(int chapterPageCount,int chapterCount, int charCount, int bookId) {
        if(this.databaseHelper==null||bookId==0){
            Log.i(TAG, "getReadProgress: 为空，写入失败 bookId "+bookId);
            return false;
        }
        //查询当前书籍有无浏览记录
        SQLiteDatabase db=databaseHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DatabaseHelper.ReadTable_field_chapterCharCount,charCount);
        values.put(DatabaseHelper.ReadTable_field_chapterCount,chapterCount);
        values.put(DatabaseHelper.ReadTable_field_chapterPageCount,chapterPageCount);
        String where=DatabaseHelper.ReadTable_field_bookId+
                " = ?";
        int ret=db.update(DatabaseHelper.ReadTableName,values,where,new String[]{Integer.toString(bookId)});
        if(ret==0){
            //开始设置浏览目录到数据库
            ContentValues progress=new ContentValues();
            progress.put(DatabaseHelper.ReadTable_field_bookId,bookId);
            progress.put(DatabaseHelper.ReadTable_field_chapterPageCount,chapterPageCount);
            progress.put(DatabaseHelper.ReadTable_field_chapterCount,chapterCount);
            progress.put(DatabaseHelper.ReadTable_field_chapterCharCount,charCount);
            long id=db.insert(DatabaseHelper.ReadTableName,null,progress);
            Log.i(TAG, "setReadProgress: 添加成功，主键为： "+id);

        }
        Log.i(TAG, "setReadProgress: ret "+ret);

        return true;
    }

    //读取图书浏览进度
    public String getReadProgress(int bookId){
        if(this.databaseHelper==null||bookId==0){
            Log.i(TAG, "getReadProgress: 为空，获取失败");
            return null;
        }
        SQLiteDatabase db=databaseHelper.getWritableDatabase();
        String sql="select * from "+
                DatabaseHelper.ReadTableName+
                " where "+
                DatabaseHelper.ReadTable_field_bookId+
                " =?";
        Cursor cursor=db.rawQuery(sql,new String[]{Integer.toString(bookId)});
        if(cursor.moveToNext()){
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("chapterLineCount",cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ReadTable_field_chapterCharCount)));
                jsonObject.put("chapterCount",cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ReadTable_field_chapterCount)));
                jsonObject.put("chapterPageCount",cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ReadTable_field_chapterPageCount)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }
        return null;
    }


    public boolean delEnjoyBook(int bookId) {
        if(this.databaseHelper==null){
            return false;
        }

        return false;
    }


}
