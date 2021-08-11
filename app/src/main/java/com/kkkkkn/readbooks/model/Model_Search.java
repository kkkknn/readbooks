package com.kkkkkn.readbooks.model;

import androidx.annotation.NonNull;

import com.kkkkkn.readbooks.ServerConfig;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.model.entity.SearchInfo;
import com.kkkkkn.readbooks.model.network.HttpUtil;
import com.kkkkkn.readbooks.model.scrap.jsoup.JsoupUtil;
import com.kkkkkn.readbooks.model.scrap.jsoup.JsoupUtilImp;
import com.kkkkkn.readbooks.util.StringUtil;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class Model_Search extends BaseModel  {

    @Subscribe
    @Override
    public void syncProgress(MessageEvent event) {
        switch (event.message){
            case SEARCH_BOOK:
                SearchInfo searchInfo=(SearchInfo) event.value;
                if(searchInfo==null||searchInfo.getKey_word().isEmpty()){
                    getCallBack().onError(-1,"搜索失败，请重新输入");
                    return;
                }
                //id token 页面数 页码
                searchBook(searchInfo);
                break;
        }

    }

    private void searchBook(SearchInfo info){
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("str", info.getKey_word());
        formBody.add("pageCount",Integer.toString(info.getPage_count()));
        formBody.add("pageSize",Integer.toString(info.getPage_size()));

        Request request = new Request.Builder()
                .url(ServerConfig.IP+ServerConfig.searchBook)
                .post(formBody.build())//传递请求体
                .addHeader("accountId",Integer.toString(info.getAccount_id()))
                .addHeader("token",info.getToken())
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
                        return;
                    }else if(code.equals("error")){
                        getCallBack().onError(-2,data);
                        return;
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
