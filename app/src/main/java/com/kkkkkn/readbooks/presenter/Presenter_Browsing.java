package com.kkkkkn.readbooks.presenter;

import android.content.Context;

import com.kkkkkn.readbooks.model.BaseModel;
import com.kkkkkn.readbooks.model.Model_BookInfo;
import com.kkkkkn.readbooks.model.Model_Browsing;
import com.kkkkkn.readbooks.model.clientsetting.SettingConf;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.model.entity.ChapterInfo;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.events.BrowsingEvent;
import com.kkkkkn.readbooks.view.view.BookInfoActivityView;
import com.kkkkkn.readbooks.view.view.BrowsingActivityView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
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

    /**
     * 获取阅读设置
     * @return
     */
    public SettingConf getConfig(){
        return null;
    }


    //获取章节列表 ，页码数
    public void getChapterList(int book_id,int chapter_count){
        AccountInfo accountInfo=getAccountCache();
        if(!accountInfo.isHasToken()){
            onError(-2,"获取用户信息失败");
            return;
        }
        EventBus.getDefault().post(
                new BrowsingEvent(
                        EventMessage.GET_BOOK_CHAPTER_LIST,
                        accountInfo.getAccount_token(),
                        accountInfo.getAccount_id(),
                        book_id,
                        PAGE_SIZE,
                        (chapter_count/PAGE_SIZE)+1));
    }

    //获取章节内容
    public void getChapterContent(String chapterUrl){
        AccountInfo accountInfo=getAccountCache();
        if(!accountInfo.isHasToken()){
            onError(-2,"获取用户信息失败");
            return;
        }
        EventBus.getDefault().post(
                new BrowsingEvent(
                        EventMessage.GET_CHAPTER_CONTENT,
                        accountInfo.getAccount_token(),
                        accountInfo.getAccount_id(),
                        chapterUrl));
    }

    //获取保存的章节进度
    public int getBookProgress(int book_id){
        //TODO  获取保存的章节进度
        return 0;
    }

    @Override
    public void onSuccess(int type, Object object) {
        switch (type){
            case 1001:
                browsingActivityView.syncChapterList((ArrayList<ChapterInfo>) object);
                break;
            case 1002:
                browsingActivityView.syncReadView((JSONArray) object);
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
