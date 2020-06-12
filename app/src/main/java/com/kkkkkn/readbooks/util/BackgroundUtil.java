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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class BackgroundUtil implements BackgroundUtilImp, Callback, Interceptor {
    private final static String TAG="BackgroundUtil";
    private static BackgroundUtil backgroundUtil=null;
    private Context mContext=null;
    private BackgroundUtilListener listener=null;
    private OkHttpClient mOkHttpClient=null;
    private final static String loginURL="123.56.6.157:30480/User/Login";
    private final static String searchBooksURL="123.56.6.157:30480/Account/Login2";
    private final static String addFavoriteBookURL="123.56.6.157:30480/Account/Login3";
    private final static String getBookInfoURL="123.56.6.157:30480/Account/Login4";
    private final static String getChapterContentURL="123.56.6.157:30480/Account/Login5";

    private BackgroundUtil(Context context,BackgroundUtilListener listener) {
        this.mContext=context.getApplicationContext();
        this.listener=listener;
        this.mOkHttpClient=new OkHttpClient.Builder().addInterceptor(this).build();
    }

    public static BackgroundUtil getInstance(Context context,BackgroundUtilListener listener){
        if(backgroundUtil==null){
            synchronized (BackgroundUtil.class){
                if(backgroundUtil==null){
                    backgroundUtil=new BackgroundUtil(context,listener);
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
        /*Request request=new Request.Builder()
                .url("asdsad")
                .post()
                .build();*/
       // mOkHttpClient.newCall().enqueue(this);
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

    /**
     * @param account 登录用户名
     * @param password 登录密码-md5加密后的字符串
     */
    @Override
    public void accountLogin(String account, String password) {
        MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
        JSONObject jsonObject=new JSONObject();

        try {
            jsonObject.put("accountName",account);
            jsonObject.put("accountPassword",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody=RequestBody.create(jsonObject.toString(),mediaType);
        Request request = new Request.Builder()
                .url(loginURL)
                .post(requestBody)
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
        String reqUrl=response.request().url().toString();
        switch(reqUrl){
            case loginURL:
                //判断请求是否成功

                //保存返回的accountid和token字符串

                break;
            case searchBooksURL:

                break;
            default:
                break;
        }
        listener.success(123123123);
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
