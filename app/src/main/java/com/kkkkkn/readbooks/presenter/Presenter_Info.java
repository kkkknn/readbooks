package com.kkkkkn.readbooks.presenter;

import android.content.Context;

import com.kkkkkn.readbooks.model.BaseModel;
import com.kkkkkn.readbooks.model.Model_BookInfo;
import com.kkkkkn.readbooks.model.Model_Login;
import com.kkkkkn.readbooks.model.Model_Main;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;
import com.kkkkkn.readbooks.view.view.BookInfoActivityView;
import com.kkkkkn.readbooks.view.view.LoginActivityView;

import org.greenrobot.eventbus.EventBus;

public class Presenter_Info extends BasePresenter implements BaseModel.CallBack {
    private BookInfoActivityView bookInfoActivityView;
    private Model_BookInfo model_bookInfo;

    public Presenter_Info(Context context,BookInfoActivityView view) {
        super(context,new Model_BookInfo());
        this.bookInfoActivityView=view;
        this.model_bookInfo=(Model_BookInfo) getBaseModel();
    }



    //读取图书信息，返回相关对象，然后进行展示 eventbus 返回
    public void getBookChapters(int bookid){
        //根据bookid读取图书章节列表
        if(bookid==0||bookid==-1){
            onError(-1,"获取章节失败");
            return;
        }
        EventBus.getDefault().post(new MessageEvent(EventMessage.GET_BOOK_CHAPTER_LIST,bookid));

    }

    @Override
    public void onSuccess(int type, Object object) {

    }

    @Override
    public void onError(int type, Object object) {

    }
}
