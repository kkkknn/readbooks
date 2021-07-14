package com.kkkkkn.readbooks.model.network;

import androidx.annotation.NonNull;

import com.kkkkkn.readbooks.model.BaseModel;
import com.kkkkkn.readbooks.util.StringUtil;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;

import org.greenrobot.eventbus.EventBus;

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



   /* public void login(String name, String password){
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
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                EventBus.getDefault().post(new MessageEvent(EventMessage.NET_ERROR,"访问出错"));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                EventBus.getDefault().post(new MessageEvent(EventMessage.NET_OK,response.body().string()));
            }
        });
    }


    public void register(String name,String password){
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("accountName", name);
        formBody.add("accountPassword", password);
        Request request = new Request.Builder()
                .url(IP+"/account/register")
                .post(formBody.build())//传递请求体
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                EventBus.getDefault().post(new MessageEvent(EventMessage.NET_ERROR,"注册失败，请联系开发人员"));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                EventBus.getDefault().post(new MessageEvent(EventMessage.NET_OK,response.body().string()));
            }
        });

    }*/
}
