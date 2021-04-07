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
    public static final String BookTableName="book";
    public static final String BookTable_field_bookId="book_id";
    public static final String BookTable_field_bookName="book_name";
    public static final String BookTable_field_bookUrl="book_url";
    public static final String BookTable_field_bookImgUrl="book_img_url";
    public static final String BookTable_field_bookAuthorName="book_author_name";
    public static final String BookTable_field_bookIsEnjoy="book_isEnjoy";
    public static final String BookTable_field_bookAbout="book_about";
    public static final String BookTable_field_bookNewChapterName="book_newChapterName";
    public static final String BookTable_field_bookFromType="book_fromType";
    public static final String BookTable_field_bookChapterPagesUrl="book_chapterPagesUrl";

    public static final String ChapterTableName="chapter";
    public static final String ChapterTable_field_chapterId="chapter_id";
    public static final String ChapterTable_field_chapterName="chapter_name";
    public static final String ChapterTable_field_chapterUrl="chapter_url";
    public static final String ChapterTable_field_chapterContent="chapter_content";
    public static final String ChapterTable_field_chapterBookId="chapter_book_id";
    public static final String ChapterTable_field_chapterIsDownload="chapter_isDownload";

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
