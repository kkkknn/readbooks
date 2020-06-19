package com.kkkkkn.readbooks.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.kkkkkn.readbooks.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class BackgroundUtil implements BackgroundUtilImp, Callback, Interceptor {
    private final static String TAG="BackgroundUtil";
    public final static int CONNECT_TIMEOUT = 60;
    public final static int READ_TIMEOUT = 100;
    public final static int WRITE_TIMEOUT = 60;
    private static BackgroundUtil backgroundUtil=null;
    private Context mContext=null;
    private BackgroundUtilListener listener=null;
    private OkHttpClient mOkHttpClient=null;
    private final static String loginURL="http://123.56.6.157:30480/Account/Login";
    private final static String searchBooksURL="http://123.56.6.157:30480/Book/SearchBook";
    private final static String addFavoriteBookURL="123.56.6.157:30480/Account/Login3";
    private final static String getBookInfoURL="123.56.6.157:30480/Account/Login4";
    private final static String getChapterContentURL="123.56.6.157:30480/Account/Login5";

    private BackgroundUtil(Context context) {
        this.mContext=context.getApplicationContext();
        this.mOkHttpClient=new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(this)
                .build();
    }

    public static BackgroundUtil getInstance(Context context){
        if(backgroundUtil==null){
            synchronized (BackgroundUtil.class){
                if(backgroundUtil==null){
                    backgroundUtil=new BackgroundUtil(context);
                }
            }
        }
        return backgroundUtil;
    }

    public BackgroundUtil setListener(BackgroundUtilListener listener){
        backgroundUtil.listener=listener;
        return backgroundUtil;
    }

    @Override
    public void searchBooks(String keywordStr, int accountId,String tokenStr) {
        FormBody body = new FormBody.Builder()
                .add("str",keywordStr)
                .add("mode",String.valueOf(2))
                .add("page",String.valueOf(0)).build();
        Request request = new Request.Builder()
                .addHeader("token",tokenStr)
                .addHeader("accountId",String.valueOf(accountId))
                .url(searchBooksURL)
                .post(body)
                .build();
        mOkHttpClient.newCall(request).enqueue(this);
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
    public boolean setAccountId(int id) {
        if(mContext==null||id==0){
            return false;
        }
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(mContext.getString(R.string.PRE_NAME), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(mContext.getString(R.string.KEY_ID),id);
        Log.i(TAG, "setAccountId: 写入缓存"+id);
        return editor.commit();
    }

    @Override
    public boolean setTokenStr(String str) {
        if(mContext==null||str.isEmpty()){
            return false;
        }
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(mContext.getString(R.string.PRE_NAME), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(mContext.getString(R.string.KEY_TOKEN),str);
        Log.i(TAG, "setAccountId: 写入缓存"+str);
        return editor.commit();
    }

    /**
     * @param account 登录用户名
     * @param password 登录密码-md5加密后的字符串
     */
    @Override
    public void accountLogin(String account, String password) {
        FormBody body = new FormBody.Builder()
                .add("accountName",account)
                .add("accountPassword",password).build();
        Request request = new Request.Builder()
                .url(loginURL)
                .post(body)
                .build();
        mOkHttpClient.newCall(request).enqueue(this);

    }

    /**
     * @param account 注册用户名
     * @param password 注册密码-md5加密后的字符串
     */
    @Override
    public void accountRegister(String account, String password) {


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
        if(response.body()==null){
            listener.error(333);
            return;
        }
        String reqUrl=response.request().url().toString();
        String resStr= response.body().string();
        Log.i(TAG, "onResponse:  "+resStr);
        listener.success(resStr);
    }

    /**
     * 输出每次okhttp请求的访问耗时
     *
     * @param chain
     * @return
     * @throws IOException
     */
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();

        long startTime = System.nanoTime();
        Log.d(TAG, String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        Response response =  chain.proceed(request);

        long endTime = System.nanoTime();
        Log.d(TAG, String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (endTime - startTime) / 1e6d, response.headers()));

        return response;
    }
}
