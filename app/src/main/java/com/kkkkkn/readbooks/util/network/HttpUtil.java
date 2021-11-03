package com.kkkkkn.readbooks.util.network;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    private static volatile HttpUtil httpUtil;
    private OkHttpClient okHttpClient;
    private HttpUtil() {
        okHttpClient=new OkHttpClient().newBuilder()
                //访问超时
                .callTimeout(2, TimeUnit.MINUTES)
                //读取超时
                .readTimeout(15, TimeUnit.SECONDS)
                //写入超时
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

    }

    public static HttpUtil getInstance(){
        if(httpUtil==null){
            synchronized (HttpUtil.class){
                if(httpUtil==null){
                    httpUtil=new HttpUtil();
                }
            }
        }
        return httpUtil;
    }

    public void post(final Request request,final Callback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                okHttpClient.newCall(request).enqueue(callback);
            }
        }).start();
    }

    public void get(final Request request,final Callback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                okHttpClient.newCall(request).enqueue(callback);
            }
        }).start();
    }

}
