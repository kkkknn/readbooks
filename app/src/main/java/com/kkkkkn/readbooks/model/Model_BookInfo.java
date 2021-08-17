package com.kkkkkn.readbooks.model;

import androidx.annotation.NonNull;

import com.kkkkkn.readbooks.ServerConfig;
import com.kkkkkn.readbooks.model.entity.ChapterInfo;
import com.kkkkkn.readbooks.model.network.HttpUtil;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class Model_BookInfo extends BaseModel {


    @Subscribe
    @Override
    public void syncProgress(MessageEvent event) {
        JSONObject jsonObject;
        switch (event.message){
            case GET_BOOK_CHAPTER_LIST:
                jsonObject= (JSONObject) event.value;
                getChapterList(jsonObject);
                break;
            case ADD_BOOK:
                jsonObject= (JSONObject) event.value;
                addEnjoyBook(jsonObject);
                break;
        }
    }

    private void addEnjoyBook(JSONObject jsonObject){
        FormBody.Builder formBody = new FormBody.Builder();
        try {
            formBody.add("bookId", Integer.toString(jsonObject.getInt("book_id")));

            Request request = new Request.Builder()
                    .url(ServerConfig.IP+ServerConfig.getChapterList)
                    .addHeader("accountId",Integer.toString(jsonObject.getInt("account_id")))
                    .addHeader("token",jsonObject.getString("token"))
                    .post(formBody.build())//传递请求体
                    .build();

            HttpUtil.getInstance().post(request, new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String ret_str=response.body().string();
                    try {
                        JSONObject ret_json=new JSONObject(ret_str);
                        String ret_code=ret_json.getString("code");
                        String ret_data=ret_json.getString("data");
                        if(ret_code.equals("success")){
                            //解析返回值
                            JSONArray jsonArray=new JSONArray(ret_data);
                            ArrayList<ChapterInfo> arrayList=new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ChapterInfo chapterInfo=new ChapterInfo(jsonArray.get(i));
                                arrayList.add(chapterInfo);
                            }
                            getCallBack().onSuccess(1,arrayList);
                        }else if(ret_code.equals("error")){
                            getCallBack().onError(-1,ret_data);
                        }else{
                            getCallBack().onError(-1,"请求失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    getCallBack().onError(-1,"请求失败");
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            getCallBack().onError(-1,"解析失败");
        }
    }

    private void getChapterList(JSONObject info){
        FormBody.Builder formBody = new FormBody.Builder();
        try {
            formBody.add("bookId", Integer.toString(info.getInt("book_id")));
            formBody.add("pageCount", Integer.toString(info.getInt("page_count")));
            formBody.add("pageSize", Integer.toString(info.getInt("page_size")));

            Request request = new Request.Builder()
                    .url(ServerConfig.IP+ServerConfig.getChapterList)
                    .addHeader("accountId",Integer.toString(info.getInt("account_id")))
                    .addHeader("token",info.getString("token"))
                    .post(formBody.build())//传递请求体
                    .build();

            HttpUtil.getInstance().post(request, new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String ret_str=response.body().string();
                    try {
                        JSONObject ret_json=new JSONObject(ret_str);
                        String ret_code=ret_json.getString("code");
                        String ret_data=ret_json.getString("data");
                        if(ret_code.equals("success")){
                            //解析返回值
                            JSONArray jsonArray=new JSONArray(ret_data);
                            ArrayList<ChapterInfo> arrayList=new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ChapterInfo chapterInfo=new ChapterInfo(jsonArray.get(i));
                                arrayList.add(chapterInfo);
                            }
                            getCallBack().onSuccess(1,arrayList);
                        }else if(ret_code.equals("error")){
                            getCallBack().onError(-1,ret_data);
                        }else{
                            getCallBack().onError(-1,"请求失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    getCallBack().onError(-1,"请求失败");
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            getCallBack().onError(-1,"解析失败");
        }

    }
}
