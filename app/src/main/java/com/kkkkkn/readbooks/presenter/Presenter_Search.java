package com.kkkkkn.readbooks.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.SearchView;

import com.kkkkkn.readbooks.model.BaseModel;
import com.kkkkkn.readbooks.model.Model_Login;
import com.kkkkkn.readbooks.model.Model_Search;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.model.entity.SearchInfo;
import com.kkkkkn.readbooks.model.scrap.jsoup.JsoupUtil;
import com.kkkkkn.readbooks.model.scrap.jsoup.JsoupUtilImp;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;
import com.kkkkkn.readbooks.view.view.SearchActivityView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Presenter_Search extends BasePresenter implements BaseModel.CallBack {
    private SearchActivityView searchActivityView;
    private Model_Search model_search;
    public Presenter_Search(Context context, SearchActivityView view) {
        super(context,new Model_Search());
        this.searchActivityView=view;
        model_search=(Model_Search) getBaseModel();
        model_search.setCallback(this);
    }



    //根据关键字/作者搜索图书，添加到list中并展示  eventbus 发送
    public void searchBook(String str){
        Log.i("asdasd", "searchBook: 开始搜索" +str );
        if (str==null||str.isEmpty()){
            return;
        }
        SearchInfo info=new SearchInfo();
        AccountInfo accountInfo=getAccountCache();
        info.setAccount_id(accountInfo.getAccount_id());
        info.setToken(accountInfo.getAccount_token());
        info.setKey_word(str);
        info.setPage_count(1);
        info.setPage_size(20);

        EventBus.getDefault().post(new MessageEvent(EventMessage.SEARCH_BOOK,info));

    }


    @Override
    public void onSuccess(int type, Object object) {
        switch (type){
            case 1:
                break;
            default:
                break;
        }
    }

    @Override
    public void onError(int type, Object object) {
        switch (type){
            case -1:
                searchActivityView.syncBookList((ArrayList<BookInfo>)object);
                break;
            case -2:
                Log.i("TAG", "onError: "+object);
                break;
            default:
                break;
        }
    }
}
