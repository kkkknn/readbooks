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
                .callTimeout(5, TimeUnit.SECONDS)
                //读取超时
                .readTimeout(5, TimeUnit.SECONDS)
                //写入超时
                .writeTimeout(5, TimeUnit.SECONDS)
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

    public void post(Request request,Callback callback){
        okHttpClient.newCall(request).enqueue(callback);
    }

}
