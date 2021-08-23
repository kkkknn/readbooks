package com.kkkkkn.readbooks.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.kkkkkn.readbooks.ServerConfig;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.util.network.HttpUtil;
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
        JSONObject jsonObject=(JSONObject)event.value;
        int id;
        String token;
        try {
            if(jsonObject.getInt("accountId")==0
                    ||StringUtil.isEmpty(jsonObject.getString("token"))){
                getCallBack().onError(-2,"获取登录信息异常");
                return;
            }
            id=jsonObject.getInt("accountId");
            token=jsonObject.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
            getCallBack().onError(-2,"获取登录信息异常");
            return;
        }

        switch (event.message){
            case SYNC_BOOKSHELF:
                //获取书架
                getBookShelf(id,token);
                break;
            case GET_VERSION:
                getVersion(id,token);
                break;
        }
    }

    private void getVersion(int id, String token){
        //todo 请求最新版本信息

    }

    private void getBookShelf(int id,String token){
        //okhttp post请求网络
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("accountId", Integer.toString(id));
        Request request = new Request.Builder()
                .addHeader("accountId",Integer.toString(id))
                .addHeader("token",token)
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
                                    JSONObject object=(JSONObject) jsonArray.get(j);
                                    BookInfo bookInfo=BookInfo.changeObject(object);
                                    if(!bookInfo.isEmpty()){

                                        book_shelf.add(bookInfo);
                                        Log.i("TAG", "onResponse: "+bookInfo.getBookName());
                                    }
                                }
                                getCallBack().onSuccess(1,book_shelf);
                                return;
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
