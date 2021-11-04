package com.kkkkkn.readbooks.presenter;

import android.content.Context;
import android.util.Log;

import com.kkkkkn.readbooks.model.BaseModel;
import com.kkkkkn.readbooks.model.Model_BookInfo;
import com.kkkkkn.readbooks.model.Model_Browsing;
import com.kkkkkn.readbooks.model.clientsetting.SettingConf;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.model.entity.ChapterInfo;
import com.kkkkkn.readbooks.util.ChapterUtil;
import com.kkkkkn.readbooks.util.StringUtil;
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
    private final static String TAG="Presenter_Browsing";

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
        return model_browsing.getReadConfig(getContext());
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
        //显示加载框
        browsingActivityView.setLoading(true);
        //读取判断是否有缓存
        String[] arr=null;
        String fileName=null;
        fileName= StringUtil.Url2fileName(chapterUrl);
        arr=ChapterUtil.readCacheChapter(getContext().getFilesDir().getAbsolutePath(),fileName);
        if(arr==null){
            //请求网络获取文章内容
            EventBus.getDefault().post(
                    new BrowsingEvent(
                            EventMessage.GET_CHAPTER_CONTENT,
                            accountInfo.getAccount_token(),
                            accountInfo.getAccount_id(),
                            chapterUrl));
        }else {
            JSONArray jsonArray=new JSONArray();
            for (String str:arr) {
                jsonArray.put(str);
            }
            arr=null;
            browsingActivityView.syncReadView(jsonArray);
            browsingActivityView.setLoading(false);
        }
    }


    public int chapterCount2listCount(int count){
        if((count/PAGE_SIZE)>0){
            Log.i(TAG, "chapterCount2listCount: "+count%PAGE_SIZE);
            return count%PAGE_SIZE;
        }else {
            Log.i(TAG, "chapterCount2listCount: "+count);
            return count;
        }
    }

    /**
     * 获取保存的章节进度
     * @param book_id 图书id
     * @return  缓存的阅读进度
     */
    public int getBookProgress(int book_id){
        return model_browsing.getReadProgress(book_id,getContext());
    }


    /**
     * 设置图书的阅读章节进度
     * @param book_id   图书id
     * @param progress  章节进度
     * @return  是否成功
     */
    public boolean setBookProgress(int book_id,int progress){
        return model_browsing.setReadProgress(book_id,progress,getContext());
    }

    @Override
    public void onSuccess(int type, Object object) {
        switch (type){
            case 1001:
                browsingActivityView.syncChapterList((ArrayList<ChapterInfo>) object);
                break;
            case 1002:
                //写入读取到的章节缓存
                JSONObject jsonObject=(JSONObject)object;
                JSONArray jsonArray= null;
                String chapterUrl=null;
                try {
                    chapterUrl=jsonObject.getString("url");
                    jsonArray = jsonObject.getJSONArray("data");
                    String fileName= StringUtil.Url2fileName(chapterUrl);
                    if(!ChapterUtil.cacheChapter(jsonArray,getContext().getFilesDir().getAbsolutePath(),fileName)){
                        Log.e(TAG, "onSuccess:  缓存章节失败");
                    }else {
                        Log.i(TAG, "onSuccess: 缓存章节成功");
                    }
                    browsingActivityView.syncReadView((JSONArray) jsonArray);
                    browsingActivityView.setLoading(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


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
                browsingActivityView.setLoading(false);
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
