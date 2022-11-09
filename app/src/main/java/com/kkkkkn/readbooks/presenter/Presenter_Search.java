package com.kkkkkn.readbooks.presenter;

import android.content.Context;
import android.util.Log;

import com.kkkkkn.readbooks.model.BaseModel;
import com.kkkkkn.readbooks.model.Model_Search;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.util.StringUtil;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.events.SearchEvent;
import com.kkkkkn.readbooks.view.view.SearchActivityView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class Presenter_Search extends BasePresenter implements BaseModel.CallBack {
    private SearchActivityView searchActivityView;
    private Model_Search model_search;
    private final static int PAGE_SIZE=20;
    public Presenter_Search(Context context, SearchActivityView view) {
        super(context,new Model_Search());
        this.searchActivityView=view;
        this.model_search=(Model_Search) getBaseModel();
        this.model_search.setCallback(this);
    }



    //根据关键字/作者搜索图书，添加到list中并展示  eventbus 发送
    public void searchBook(int size,String str){
        if (StringUtil.INSTANCE.isEmpty(str)){
            Log.e("TAG", "searchBook:  搜索关键词为空");
            return;
        }
        AccountInfo accountInfo=getAccountCache();
        if(!accountInfo.isHasToken()){
            onError(-2,"获取用户信息失败");
            return;
        }
        EventBus.getDefault().post(
                new SearchEvent(
                        EventMessage.SEARCH_BOOK,
                        accountInfo.getAccountId(),
                        accountInfo.getAccountToken(),
                        str,
                        (size/PAGE_SIZE)+1,
                        PAGE_SIZE));

    }


    @Override
    public void onSuccess(int type, Object object) {
        switch (type){
            case 1:
                searchActivityView.syncBookList((ArrayList<BookInfo>)object);
                break;
            default:
                break;
        }
    }

    @Override
    public void onError(int type, Object object) {
        switch (type){
            case -1:
                Log.i("TAG", "onError: "+object);
                break;
            case -2:
                String str=(String) object;
                Log.i("TAG", "onError: "+object);
                if (str.equals("令牌验证失败，请重新尝试")) {
                    searchActivityView.toLoginActivity();
                }
                break;
            default:
                break;
        }
    }

    public int getPageSize() {
        return PAGE_SIZE;
    }
}
