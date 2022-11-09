package com.kkkkkn.readbooks.model;

import androidx.annotation.NonNull;

import com.kkkkkn.readbooks.util.ServerConfig;
import com.kkkkkn.readbooks.model.entity.ChapterInfo;
import com.kkkkkn.readbooks.util.eventBus.events.BookInfoEvent;
import com.kkkkkn.readbooks.util.network.HttpUtil;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class Model_BookInfo extends BaseModel {


    @Subscribe
    public void syncProgress(BookInfoEvent event) {
        switch (event.getMessage()){
            case GET_BOOK_CHAPTER_LIST:
                getChapterList(event.getAccountId(), event.getBookId(), event.getToken(), event.getPageSize(), event.getPageCount());
                break;
            case ADD_BOOK:
                addEnjoyBook(event.getAccountId(), event.getBookId(), event.getToken());
                break;
        }
    }

    private void addEnjoyBook(int account_id,int book_id,String token){
        FormBody.Builder formBody = new FormBody.Builder();

        formBody.add("book_id", Integer.toString(book_id));
        formBody.add("account_id", Integer.toString(account_id));

        Request request = new Request.Builder()
                .url(ServerConfig.IP+ServerConfig.addFavoriteBook)
                .addHeader("accountId",Integer.toString(account_id))
                .addHeader("token",token)
                .post(formBody.build())//传递请求体
                .build();
        Objects.requireNonNull(HttpUtil.Companion.getInstance()).post(request, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String ret_str=response.body().string();
                try {
                    JSONObject ret_json=new JSONObject(ret_str);
                    String ret_code=ret_json.getString("code");
                    String ret_data=ret_json.getString("data");
                    if(ret_code.equals("success")){
                        getCallBack().onSuccess(1002,"收藏成功");
                    }else if(ret_code.equals("error")){
                        getCallBack().onError(-1002,ret_data);
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

    }

    private void getChapterList(int account_id,int book_id,String token,int page_size,int page_count){
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("bookId", Integer.toString(book_id));
        formBody.add("pageCount", Integer.toString(page_count));
        formBody.add("pageSize", Integer.toString(page_size));

        Request request = new Request.Builder()
                .url(ServerConfig.IP+ServerConfig.getChapterList)
                .addHeader("accountId",Integer.toString(account_id))
                .addHeader("token",token)
                .post(formBody.build())//传递请求体
                .build();

        Objects.requireNonNull(HttpUtil.Companion.getInstance()).post(request, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String ret_str= Objects.requireNonNull(response.body()).string();
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
                        getCallBack().onSuccess(1001,arrayList);
                    }else if(ret_code.equals("error")){
                        getCallBack().onError(-1001,ret_data);
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



    }
}
