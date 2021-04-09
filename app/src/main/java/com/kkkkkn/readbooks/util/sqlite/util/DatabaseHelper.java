package com.kkkkkn.readbooks.util.sqlite.util;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {
    //图书表
    public static final String BookTableName="book";
    public static final String BookTable_field_bookId="bookId";
    public static final String BookTable_field_bookName="bookName";
    public static final String BookTable_field_bookUrl="bookUrl";
    public static final String BookTable_field_bookImgUrl="bookImgUrl";
    public static final String BookTable_field_bookAuthorName="bookAuthorName";
    public static final String BookTable_field_bookIsEnjoy="bookIsEnjoy";
    public static final String BookTable_field_bookAbout="bookAbout";
    public static final String BookTable_field_bookNewChapterName="bookNewChapterName";
    public static final String BookTable_field_bookFromType="bookFromType";
    public static final String BookTable_field_bookChapterPagesUrl="bookChapterPagesUrl";

    //阅读进度表
    public static final String ReadTableName="read_progress";
    public static final String ReadTable_field_id="progressId";
    public static final String ReadTable_field_bookId="bookId";
    public static final String ReadTable_field_chapterPageCount="chapterPageCount";
    public static final String ReadTable_field_chapterCount="chapterCount";
    public static final String ReadTable_field_chapterCharCount="chapterCharCount";

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    //数据库第1次创建时 则会调用，即 第1次调用 getWritableDatabase（） / getReadableDatabase（）时调用
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建表
        String  book_table_sql = "CREATE TABLE " + BookTableName + "(" +
                BookTable_field_bookId + " integer primary key autoincrement , " +
                BookTable_field_bookName + " varchar(60) ," +
                BookTable_field_bookUrl + " varchar(60) ," +
                BookTable_field_bookImgUrl + " varchar(60) ," +
                BookTable_field_bookAuthorName + " varchar(60) ," +
                BookTable_field_bookIsEnjoy + " boolean ," +
                BookTable_field_bookAbout + " varchar(60) ," +
                BookTable_field_bookNewChapterName + " varchar(60) ," +
                BookTable_field_bookFromType + " integer ," +
                BookTable_field_bookChapterPagesUrl + " varchar(60) " +
                ");";

        String chapter_table_sql = "CREATE TABLE " + ReadTableName + "(" +
                ReadTable_field_id + " integer primary key autoincrement , " +
                ReadTable_field_bookId + " integer, " +
                ReadTable_field_chapterPageCount + " integer , " +
                ReadTable_field_chapterCount + " integer , " +
                ReadTable_field_chapterCharCount + " integer ," +
                "foreign key ("+ReadTable_field_bookId+") references "+BookTableName+" ("+BookTable_field_bookId+")" +
                ");";

        try {
            sqLiteDatabase.execSQL(book_table_sql);
            sqLiteDatabase.execSQL(chapter_table_sql);
        } catch (SQLException e) {
            Log.e(TAG, "onCreate " + BookTableName + "Error" + e.toString());
            Log.e(TAG, "onCreate " + ReadTableName + "Error" + e.toString());
        }
    }


    // 4. 数据库升级时自动调用
    // 在继承SQLiteOpenHelper类的子类中复写
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly()) { //开启外键
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
