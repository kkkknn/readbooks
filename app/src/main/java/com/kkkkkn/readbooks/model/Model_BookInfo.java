package com.kkkkkn.readbooks.model;

import androidx.annotation.NonNull;

import com.kkkkkn.readbooks.ServerConfig;
import com.kkkkkn.readbooks.model.entity.ChapterInfo;
import com.kkkkkn.readbooks.model.network.HttpUtil;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;

import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
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
        switch (event.message){
            case GET_BOOK_CHAPTER_LIST:
                int id= (int) event.value;

                break;
        }
    }

    //todo 获取图书章节列表
    private LinkedList<ChapterInfo> getChapterList(int book_id,int page_count,int page_size){
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("accountName", String.valueOf(book_id));
        formBody.add("accountPassword", String.valueOf(book_id));
        Request request = new Request.Builder()
                .url(ServerConfig.IP+ServerConfig.getChapterList)
                .post(formBody.build())//传递请求体
                .build();
        HttpUtil.getInstance().post(request, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
        });
        return null;
    }
}
