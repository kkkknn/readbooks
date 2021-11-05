package com.kkkkkn.readbooks.model;

import androidx.annotation.NonNull;

import com.kkkkkn.readbooks.util.ServerConfig;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.util.eventBus.events.SearchEvent;
import com.kkkkkn.readbooks.util.network.HttpUtil;
import com.kkkkkn.readbooks.util.StringUtil;

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

public class Model_Search extends BaseModel  {

    @Subscribe
    public void syncProgress(SearchEvent event) {
        switch (event.message){
            case SEARCH_BOOK:

                //搜索图书
                searchBook(event.keyWord,
                        event.pageCount,
                        event.pageSize,
                        event.accountId,
                        event.token);
                break;
        }

    }

    private void searchBook(String key_word,int page_count,int page_size,int account_id,String token){
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("str", key_word);
        formBody.add("pageCount",Integer.toString(page_count));
        formBody.add("pageSize",Integer.toString(page_size));

        Request request = new Request.Builder()
                .url(ServerConfig.IP+ServerConfig.searchBook)
                .post(formBody.build())//传递请求体
                .addHeader("accountId",Integer.toString(account_id))
                .addHeader("token",token)
                .build();
        HttpUtil.getInstance().post(request, new Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //解析返回值
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    String code=jsonObject.getString("code");
                    String data=jsonObject.getString("data");
                    if(StringUtil.isEmpty(code)){
                        getCallBack().onError(-1,"网络请求错误");
                        return;
                    }
                    if(code.equals("success")){
                        //解析json数组
                        JSONArray jsonArray=new JSONArray(data);
                        ArrayList<BookInfo> arrayList=new ArrayList<BookInfo>();
                        for (int j = 0; j < jsonArray.length(); j++) {
                            BookInfo bookInfo=new BookInfo();
                            JSONObject jsonObject1= (JSONObject) jsonArray.get(j);
                            bookInfo.setAuthorName(jsonObject1.getString("author_name"));
                            bookInfo.setBookAbout(jsonObject1.getString("book_about"));
                            bookInfo.setChapterSum(jsonObject1.getInt("book_chapter_sum"));
                            bookInfo.setBookId(jsonObject1.getInt("book_id"));
                            bookInfo.setBookImgUrl(jsonObject1.getString("book_img_url"));
                            bookInfo.setBookName(jsonObject1.getString("book_name"));
                            bookInfo.setBookUrl(jsonObject1.getString("book_url"));
                            bookInfo.setSourceName(jsonObject1.getString("source_name"));
                            arrayList.add(bookInfo);
                        }
                        getCallBack().onSuccess(1,arrayList);
                    }else if(code.equals("error")){
                        getCallBack().onError(-2,data);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getCallBack().onError(-1,"网络请求错误");
            }
        });
    }
}
