package com.kkkkkn.readbooks.activates;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

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
import java.util.concurrent.TimeoutException;

public class BookBrowsingActivity extends BaseActivity {
    private final static String TAG="BookBrowsingActivity";
    private ArrayList<String[]> chapterList=new ArrayList<>();
    private int arrayCount=0;
    private String[] chapterContent;
    private BrowsingVIew browsingVIew;
    private ProgressDialog progressDialog;
    private Handler mHandler= new Handler(Looper.getMainLooper(),new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what){
                case 22:
                    chapterContent=(String[])msg.obj;
                    if(chapterContent!=null && chapterContent.length>0 ){
                        browsingVIew.setTextContent(chapterContent);
                        browsingVIew.setTextColor(Color.BLACK);
                        browsingVIew.setTextSize(20f);
                        browsingVIew.setProgress(0,false);
                        browsingVIew.invalidate();
                    }
                    //接收完成,隐藏遮罩层
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                    break;
                case 23:
                    chapterContent=(String[])msg.obj;
                    if(chapterContent!=null && chapterContent.length>0 ){
                        browsingVIew.setTextContent(chapterContent);
                        browsingVIew.setTextColor(Color.BLACK);
                        browsingVIew.setTextSize(20f);
                        browsingVIew.setProgress(0,true);
                        browsingVIew.invalidate();
                    }
                    //接收完成,隐藏遮罩层
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                    break;
                case 11://显示加载框遮罩层
                    if(progressDialog!=null){
                        progressDialog.show();
                    }
                    break;
                case 12://出错，隐藏加载框遮罩层
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
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
                //Log.i(TAG, "onReceive: 时间变化广播接收");
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
                //Log.i(TAG, "onReceive: 电量变化广播接收"+level);
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

        browsingVIew.setListener(new BookCallback() {
            @Override
            public void jump2nextChapter() {
                if(arrayCount<chapterList.size()){
                    new GetContentThread(chapterList.get(++arrayCount)[1],1).start();
                    Log.i(TAG, "jump2nextChapter: arrayCount："+arrayCount);
                }
            }

            @Override
            public void jump2lastChapter() {
                if(arrayCount>0){
                    new GetContentThread(chapterList.get(--arrayCount)[1],2).start();
                    Log.i(TAG, "jump2lastChapter: arrayCount"+arrayCount);
                }
            }
        });
        //加载框设置
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//转盘
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("正在加载，请稍后……");


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
            Log.i(TAG, "onCreate: arrayCount: "+arrayCount);
            new GetContentThread(chapterList.get(arrayCount)[1],0).start();
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
        private int type;

        public GetContentThread(String url,int type){
            this.url=url;
            this.type=type;
        }
        @Override
        public void run() {
            //利用handle 通知显示加载框
            mHandler.sendEmptyMessage(11);
            //获取当前点击章节文字
            JsoupUtil util=new JsoupUtilImp_xbqg();
            try {
                JSONObject jsonObject=util.getChapterContent(url);
                String[] arr_text=(String[])jsonObject.get("chapterContent");
                if(arr_text.length>0){
                    Message suc_msg=mHandler.obtainMessage();
                    suc_msg.obj=arr_text;
                    if(type==2){
                        suc_msg.what=23;
                    }else {
                        suc_msg.what=22;
                    }
                    mHandler.sendMessage(suc_msg);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                //取消显示加载框
                mHandler.sendEmptyMessage(12);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册静态广播
    }

    public interface BookCallback{
        void jump2nextChapter();
        void jump2lastChapter();
    }
}