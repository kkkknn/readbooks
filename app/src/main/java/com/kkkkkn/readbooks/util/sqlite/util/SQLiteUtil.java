package com.kkkkkn.readbooks.util.sqlite.util;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.content.ContentValues.TAG;

public class SQLiteUtil extends SQLiteOpenHelper {
    private final String BookTableName="book";
    private final String BookTable_field_bookId="book_id";
    private final String BookTable_field_bookName="book_name";
    private final String BookTable_field_bookUrl="book_url";
    private final String BookTable_field_bookImgUrl="book_img_url";
    private final String BookTable_field_bookAuthorName="book_author_name";
    private final String BookTable_field_bookIsEnjoy="book_isEnjoy";

    private final String ChapterTableName="chapter";
    private final String ChapterTable_field_chapterId="chapter_id";
    private final String ChapterTable_field_chapterName="chapter_name";
    private final String ChapterTable_field_chapterUrl="chapter_url";
    private final String ChapterTable_field_chapterContent="chapter_content";
    private final String ChapterTable_field_chapterBookId="chapter_book_id";
    private final String ChapterTable_field_chapterIsDownload="chapter_isDownload";

    public SQLiteUtil(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
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
                BookTable_field_bookIsEnjoy + " boolean " +
                ");";

        String chapter_table_sql = "CREATE TABLE " + ChapterTableName + "(" +
                ChapterTable_field_chapterId + " integer primary key autoincrement , " +
                ChapterTable_field_chapterName + " varchar(60) , " +
                ChapterTable_field_chapterUrl + " varchar(60) , " +
                ChapterTable_field_chapterContent + " text , " +
                ChapterTable_field_chapterBookId + " integer , " +
                ChapterTable_field_chapterIsDownload + " boolean " +
                ");";

        try {
            sqLiteDatabase.execSQL(book_table_sql);
            sqLiteDatabase.execSQL(chapter_table_sql);
        } catch (SQLException e) {
            Log.e(TAG, "onCreate " + BookTableName + "Error" + e.toString());
            Log.e(TAG, "onCreate " + ChapterTableName + "Error" + e.toString());

        }
    }


    // 4. 数据库升级时自动调用
    // 在继承SQLiteOpenHelper类的子类中复写
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
