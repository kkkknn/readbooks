package com.kkkkkn.readbooks.model;

import androidx.annotation.NonNull;

import com.kkkkkn.readbooks.ServerConfig;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.model.network.HttpUtil;
import com.kkkkkn.readbooks.util.StringUtil;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

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
                String ret_str=response.body().string();
                if(!StringUtil.isEmpty(ret_str)){
                    try {
                        JSONObject jsonObject=new JSONObject(ret_str);
                        String code_str=jsonObject.getString("code");
                        String data_str=jsonObject.getString("data");
                        if(!StringUtil.isEmpty(code_str)&&!StringUtil.isEmpty(data_str)){
                            if(code_str.equals("success")){
                                JSONArray jsonArray=new JSONArray(data_str);
                                ArrayList<BookInfo> book_shelf=new ArrayList<>();
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    BookInfo bookInfo=(BookInfo) jsonArray.get(j);
                                    book_shelf.add(bookInfo);
                                }
                                getCallBack().onSuccess(1,book_shelf);
                            }else if(code_str.equals("error")){
                                getCallBack().onError(-2,data_str);
                                return;
                            }
                        }
                        getCallBack().onError(-1,"解析失败");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

}
