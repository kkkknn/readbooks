package com.kkkkkn.readbooks.presenter;

import android.content.Context;

import com.kkkkkn.readbooks.model.BaseModel;
import com.kkkkkn.readbooks.model.Model_BookInfo;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.model.entity.ChapterInfo;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.events.BookInfoEvent;
import com.kkkkkn.readbooks.view.view.BookInfoActivityView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class Presenter_Info extends BasePresenter implements BaseModel.CallBack {
    private BookInfoActivityView bookInfoActivityView;
    private Model_BookInfo model_bookInfo;
    private final static int PAGE_SIZE=20;

    public Presenter_Info(Context context,BookInfoActivityView view) {
        super(context,new Model_BookInfo());
        this.bookInfoActivityView=view;
        this.model_bookInfo=(Model_BookInfo) getBaseModel();
        this.model_bookInfo.setCallback(this);
    }

    //添加到用户收藏列表
    public void addBookShelf(int book_id){
        AccountInfo accountInfo=getAccountCache();
        if(!accountInfo.isHasToken()){
            onError(-2,"用户信息错误");
            return;
        }
        EventBus.getDefault().post(
                new BookInfoEvent(
                        EventMessage.ADD_BOOK,
                        accountInfo.getAccountToken(),
                        accountInfo.getAccountId(),
                        book_id));
    }

    //读取图书信息，返回相关对象，然后进行展示 eventbus 返回
    public void getBookChapters(int size,int bookid){
        //根据bookid读取图书章节列表
        if(bookid==0||bookid==-1){
            onError(-1,"图书信息错误");
            return;
        }
        //获取token缓存
        AccountInfo accountInfo=getAccountCache();
        if(!accountInfo.isHasToken()){
            onError(-2,"用户信息错误");
            return;
        }
        EventBus.getDefault().post(
                new BookInfoEvent(
                        EventMessage.GET_BOOK_CHAPTER_LIST,
                        accountInfo.getAccountToken(),
                        accountInfo.getAccountId(),
                        bookid,
                        PAGE_SIZE,
                        (size/PAGE_SIZE)+1));

    }

    @Override
    public void onSuccess(int type, Object object) {
        switch (type){
            case 1001:
                bookInfoActivityView.syncChapterList((ArrayList<ChapterInfo>) object);
                break;
            case 1002:
                bookInfoActivityView.showMsgDialog(type,(String) object);
                break;
            default:
                break;
        }
    }

    @Override
    public void onError(int type, Object object) {
        switch (type){
            case -1001:
                bookInfoActivityView.showMsgDialog(type,(String) object);
                break;
            case -1002:
                bookInfoActivityView.showMsgDialog(type,(String) object);
                break;
            case -2:
                bookInfoActivityView.toLoginActivity();
                break;
            default:
                break;
        }
    }

    public int getPageSize() {
        return PAGE_SIZE;
    }
}
