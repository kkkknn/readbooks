package com.kkkkkn.readbooks.util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//网络请求工具类
class NetworkUtil implements Callback{
    private static NetworkUtil networkUtil;
    private NetworkUtilListener listener;
    private static OkHttpClient okHttpClient;

    private NetworkUtil() {
        okHttpClient=new OkHttpClient();
    }

    public static NetworkUtil getInstance(){
        if(networkUtil==null){
            synchronized (NetworkUtil.class){
                if(networkUtil==null){
                    networkUtil=new NetworkUtil();
                }
            }
        }
        return networkUtil;
    }

    //2.设置服务器返回监听
    public void setListener(NetworkUtilListener listener){
        this.listener=listener;
    }

    //1.请求服务器 get请求
    public Response requestGet(Request request) throws IOException {
        return okHttpClient.newCall(request).execute();
    }

    public void requestPost(Request request){
        okHttpClient.newCall(request).enqueue(this);
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        listener.Error();
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        listener.Success(response);
    }
}
