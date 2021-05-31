package com.kkkkkn.readbooks.presenter;

import android.util.Log;

import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.model.jsoup.JsoupUtil;
import com.kkkkkn.readbooks.model.jsoup.JsoupUtilImp;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Presenter_Search {
    private  volatile static Presenter_Search presenter_search=null;

    private Presenter_Search() {
    }

    public static Presenter_Search getInstance(){
        if(presenter_search==null){
            synchronized (Presenter_Browsing.class){
                if(presenter_search==null){
                    presenter_search=new Presenter_Search();
                }
            }
        }
        return presenter_search;
    }

    //根据关键字/作者搜索图书，添加到list中并展示  eventbus 发送
    public void searchBook(String str,int sourceId){
        Log.i("asdasd", "searchBook: 开始搜索" +str +"||" +sourceId);
        if (str==null||str.isEmpty()){
            return;
        }
        JsoupUtil jsoupUtil= JsoupUtilImp.getInstance().setSource(sourceId);
        try {
            String resultStr=jsoupUtil.searchBook(str);
            ArrayList<BookInfo> arrayList=new ArrayList<BookInfo>();
            //解析搜索结果并填充到arrayList中
            JSONObject jsonObject=new JSONObject(resultStr);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject object=(JSONObject) jsonArray.get(i);
                BookInfo bookInfo=new BookInfo();
                bookInfo.setAuthorName(object.getString("authorName"));
                bookInfo.setBookName(object.getString("bookName"));
                bookInfo.setBookUrl(object.getString("bookUrl"));
                bookInfo.setBookImgUrl(object.getString("bookImgUrl"));
                bookInfo.setNewChapterName(object.getString("newChapterName"));
                bookInfo.setBookFromType(jsoupUtil.getSource());
                arrayList.add(bookInfo);
            }
            EventBus.getDefault().post(new MessageEvent(EventMessage.SYNC_SEARCH_RESULT,arrayList));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


}
