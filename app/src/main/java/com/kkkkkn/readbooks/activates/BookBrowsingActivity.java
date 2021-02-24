package com.kkkkkn.readbooks.activates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    private String[] chapterContent;
    private BrowsingVIew browsingVIew;
    private Handler mHandler= new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what){
                case 22:
                    chapterContent=(String[])msg.obj;
                    if(chapterContent!=null && chapterContent.length>0 ){
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

    //静态广播
    private final BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                Log.i(TAG, "onReceive: 时间变化广播接收");
                //获得系统的时间，单位为毫秒,转换为妙
                long totalMilliSeconds = System.currentTimeMillis();
                long totalSeconds = totalMilliSeconds / 1000;
                //求出现在的分
                long totalMinutes = totalSeconds / 60;
                long currentMinute = totalMinutes % 60;
                //求出现在的小时
                long totalHour = totalMinutes / 60;
                long currentHour = totalHour % 24;
                //开始设置浏览界面时间
                String timeStr=currentHour+":"+currentMinute;
                //browsingVIew.setTimeStr(timeStr);
            }else if(action.equals(Intent.ACTION_BATTERY_CHANGED)){
                //获取当前电量
                int level = intent.getIntExtra("level", 0);
                Log.i(TAG, "onReceive: 电量变化广播接收"+level);
                //开始设置浏览界面电量
                //browsingVIew.setBatteryStr(level+"%");
            }
        }
    };
    
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
            browsingVIew.setChapterNameStr(chapterList.get(arrayCount)[0]);
            browsingVIew.setProgressStr(arrayCount+"/"+chapterList.size());
        }
        //注册静态广播
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(broadcastReceiver,filter);
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
                JSONObject jsonObject=util.getChapterContent(url);
                String[] arr_text=(String[])jsonObject.get("chapterContent");
                Message msg=mHandler.obtainMessage();
                msg.obj=arr_text;
                msg.what=22;
                mHandler.sendMessage(msg);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册静态广播
    }
}