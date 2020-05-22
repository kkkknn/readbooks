package com.kkkkkn.readbooks.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.kkkkkn.readbooks.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Method;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BackgroundUtil implements BackgroundUtilImp, Callback {
    private static BackgroundUtil backgroundUtil=null;
    private Context mContext=null;
    private BackgroundUtilListener listener=null;
    private static OkHttpClient mOkHttpClient=null;

    private BackgroundUtil(Context context,BackgroundUtilListener listener) {
        this.mContext=context.getApplicationContext();
        this.listener=listener;
    }

    public static BackgroundUtil getInstance(Context context,BackgroundUtilListener listener){
        if(backgroundUtil==null){
            synchronized (BackgroundUtil.class){
                if(backgroundUtil==null){
                    backgroundUtil=new BackgroundUtil(context,listener);
                    mOkHttpClient=new OkHttpClient();
                }
            }
        }
        return backgroundUtil;
    }

    @Override
    public void searchBooks(String keywordStr, int accountId) {
        if(keywordStr==null||keywordStr.isEmpty()||accountId==0||mOkHttpClient==null){
            listener.error(-1);
        }
        Request request=new Request.Builder()
                .url("asdsad")
                .post()
                .build();
        mOkHttpClient.newCall().enqueue(this);
    }

    @Override
    public void addFavoriteBook(String bookStr, int accountId, String tokenStr) {

    }

    @Override
    public void getBookInfo(String bookStr, int accountId, String tokenStr) {

    }

    @Override
    public void getChapterContent(String chapterStr, int accountId, String tokenStr) {

    }

    @Override
    public String getTokenStr() {
        if(mContext==null){
            return null;
        }
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(mContext.getString(R.string.PRE_NAME), Context.MODE_PRIVATE);
        return sharedPreferences.getString(mContext.getString(R.string.KEY_TOKEN),"");
    }

    @Override
    public int getAccountId() {
        if(mContext==null){
            return 0;
        }
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(mContext.getString(R.string.PRE_NAME), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(mContext.getString(R.string.KEY_ID),0);
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        listener.error(123123123);
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        listener.success(123123123);
    }

}
