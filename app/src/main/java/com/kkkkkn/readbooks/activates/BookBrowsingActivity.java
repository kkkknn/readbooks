package com.kkkkkn.readbooks.activates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.util.jsoup.JsoupUtil;
import com.kkkkkn.readbooks.util.jsoup.JsoupUtilImp_xbqg;
import com.kkkkkn.readbooks.view.BrowsingVIew;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.helper.ChangeNotifyingArrayList;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookBrowsingActivity extends BaseActivity {
    private final static String TAG="BookBrowsingActivity";
    private ArrayList<String[]> chapterList=new ArrayList<>();
    private int arrayCount;
    private String chapterContent;
    private BrowsingVIew browsingVIew;
    private Handler mHandler= new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what){
                case 22:
                    chapterContent=(String)msg.obj;
                    if(chapterContent!=null && !chapterContent.isEmpty() ){
                        browsingVIew.setTextContent(chapterContent);
                        browsingVIew.setTextColor(Color.BLACK);
                        browsingVIew.setTextSize(20f);
                        browsingVIew.invalidate();
                    }
                    break;
                case 23:

                    break;
            }
            return false;
        }
    });
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_browsing);

        browsingVIew=findViewById(R.id.browView);

        //获取携带信息
        Bundle bundle=getIntent().getExtras();
        if(bundle==null){
            //没有携带信息
            finish();
        }else{
            //序列化处理，防止转换警告
            Object obj=(Object) bundle.getSerializable("chapterList");
            if (obj instanceof ArrayList<?>) {
                for (Object o : (List<?>) obj) {
                    chapterList.add((String[]) o);
                }
            }
            arrayCount=bundle.getInt("chapterPoint");
            new GetContentThread(chapterList.get(arrayCount)[1]).start();
        }

    }

    //获取章节文字的网络线程
    private class GetContentThread extends Thread{
        private String url;

        public GetContentThread(String url){
            this.url=url;
        }
        @Override
        public void run() {
            //获取当前点击章节文字
            JsoupUtil util=new JsoupUtilImp_xbqg();
            try {
                String retStr=util.getChapterContent(url);
                JSONObject jsonObject=new JSONObject(retStr);
                String text=jsonObject.getString("chapterContent");
                Message msg=mHandler.obtainMessage();
                msg.obj=text;
                msg.what=22;
                mHandler.sendMessage(msg);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

}