package com.kkkkkn.readbooks.model;

import androidx.annotation.NonNull;

import com.kkkkkn.readbooks.ServerConfig;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.model.network.HttpUtil;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;

import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class Model_Main extends BaseModel {


    @Subscribe
    @Override
    public void syncProgress(MessageEvent event) {
        switch (event.message){
            case SYNC_BOOKSHELF:
                //获取当前登录信息
                AccountInfo info=(AccountInfo)event.value;
                if(info==null||info.getAccount_id()<=0||info.getAccount_token().isEmpty()){
                    getCallBack().onError(-2,"获取登录信息异常");
                    return;
                }
                //获取书架
                getBookShelf(info.getAccount_id(),info.getAccount_token());
                break;
        }
    }

    private void getBookShelf(int id,String token){
        //okhttp post请求网络
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("accountId", Integer.toString(id));
        formBody.add("token",token);
        Request request = new Request.Builder()
                .url(ServerConfig.IP+ServerConfig.getFavoriteBook)
                .post(formBody.build())//传递请求体
                .build();
        HttpUtil.getInstance().post(request, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getCallBack().onError(-1,"访问出错");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //解析返回值
                
            }
        });
    }

}
