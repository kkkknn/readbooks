package com.kkkkkn.readbooks.model.network;

import androidx.annotation.NonNull;

import com.kkkkkn.readbooks.model.BaseModel;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {
    //服务器IP
    private final static String IP="http://81.70.239.217";
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

    public String login(String name, String password, final BaseModel.CallBack callBack){
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("accountName", name);
        formBody.add("accountPassword", password);
        Request request = new Request.Builder()
                .url(IP+"/account/login")
                .post(formBody.build())//传递请求体
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                callBack.onSuccess(1,response.body().string());
                System.out.println(response.body().string());
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callBack.onError(-1,"");
            }
        });


        return null;
    }


    public String register(String name,String password){
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("accountName", name);
        formBody.add("accountPassword", password);
        Request request = new Request.Builder()
                .url(IP+"/account/register")
                .post(formBody.build())//传递请求体
                .build();
        Call call=okHttpClient.newCall(request);
        Response response= null;
        try {
            response = call.execute();
            if(response.isSuccessful()){
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
