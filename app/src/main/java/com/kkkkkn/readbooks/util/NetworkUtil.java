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
public class NetworkUtil implements Callback{
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

    //设置监听
    public void setListener(NetworkUtilListener listener){
        this.listener=listener;
    }

    //1.请求服务器
    public void requestGet(String string){
        okHttpClient.newCall( new Request.Builder().get().url(string).build()).enqueue(this);
    }

    public void requestPost(RequestBody requestBody){

    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        listener.Error();
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        listener.Success(response);
    }

    //2.设置服务器返回监听


    //3.网络关闭


    //4.网络状态恢复



}
