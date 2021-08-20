package com.kkkkkn.readbooks.presenter;

import android.content.Context;

import com.kkkkkn.readbooks.model.BaseModel;
import com.kkkkkn.readbooks.model.Model_BookInfo;
import com.kkkkkn.readbooks.model.Model_Browsing;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.model.entity.ChapterInfo;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;
import com.kkkkkn.readbooks.view.view.BookInfoActivityView;
import com.kkkkkn.readbooks.view.view.BrowsingActivityView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Presenter_Browsing extends BasePresenter implements BaseModel.CallBack {
    private BrowsingActivityView browsingActivityView;
    private Model_Browsing model_browsing;
    private final static int PAGE_SIZE=20;

    public Presenter_Browsing(Context context, BrowsingActivityView view) {
        super(context,new Model_Browsing());
        this.browsingActivityView=view;
        this.model_browsing=(Model_Browsing) getBaseModel();
        this.model_browsing.setCallback(this);
    }

    //获取章节列表 ，页码数
    public void getChapterList(int book_id,int chapter_count){
        AccountInfo accountInfo=getAccountCache();
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("account_id",accountInfo.getAccount_id());
            jsonObject.put("token",accountInfo.getAccount_token());
            jsonObject.put("book_id",book_id);
            jsonObject.put("page_size",PAGE_SIZE);
            jsonObject.put("page_count",(chapter_count/PAGE_SIZE)+1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        EventBus.getDefault().post(new MessageEvent(EventMessage.GET_BOOK_CHAPTER_LIST,jsonObject.toString()));
    }

    //获取章节内容
    public void getChapterContent(String chapterUrl){

    }

    //获取保存的章节进度
    public int getBookProgress(int book_id){

        return 0;
    }

    @Override
    public void onSuccess(int type, Object object) {
        switch (type){
            case 1001:
                browsingActivityView.syncChapterList((ArrayList<ChapterInfo>) object);
                break;

        }
    }

    @Override
    public void onError(int type, Object object) {
        switch (type){
            case -1001:
                browsingActivityView.showMsgDialog(type,(String) object);
                break;
            case -1002:
                browsingActivityView.showMsgDialog(type,(String) object);
                break;
            case -2:
                browsingActivityView.toLoginActivity();
                break;
            default:
                break;
        }
    }
}
