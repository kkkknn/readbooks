package com.kkkkkn.readbooks.view.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.model.clientsetting.SettingConf;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.model.entity.ChapterInfo;
import com.kkkkkn.readbooks.presenter.Presenter_Browsing;
import com.kkkkkn.readbooks.view.customView.BrowsingVIew;
import com.kkkkkn.readbooks.view.customView.CustomToast;
import com.kkkkkn.readbooks.view.customView.LoadingDialog;
import com.kkkkkn.readbooks.view.customView.SettingDialog;
import com.kkkkkn.readbooks.view.view.BrowsingActivityView;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

public class BookBrowsingActivity extends BaseActivity implements BrowsingActivityView {
    private final static String TAG = "BookBrowsingActivity";
    private ArrayList<ChapterInfo> chapterList = new ArrayList<>();
    private int chapterCount = 0;
    private BrowsingVIew browsingVIew;
    private ProgressDialog progressDialog;
    private LoadingDialog loadingDialog;
    private BookInfo bookInfo;
    private Presenter_Browsing presenterBrowsing;
    private BrowsingVIew.FlushType flushType= BrowsingVIew.FlushType.FLUSH_PAGE;
    private SettingConf settingConf;
    private LinearLayoutCompat linearLayoutCompat;

    //静态广播
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
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
                String timeStr = currentHour + ":" + currentMinute;
                //browsingVIew.setTimeStr(timeStr);
            } else if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                //获取当前电量
                int level = intent.getIntExtra("level", 0);
                //Log.i(TAG, "onReceive: 电量变化广播接收"+level);
                //开始设置浏览界面电量
                //browsingVIew.setBatteryStr(level+"%");
            }
        }
    };

    private void initView(){
        linearLayoutCompat=findViewById(R.id.book_brow_background);
        browsingVIew = findViewById(R.id.browView);
        browsingVIew.setListener(new BookCallback() {
            @Override
            public void jump2nextChapter() {
                Log.i(TAG, "jump2nextChapter: 跳转到下一章节");
                ChapterInfo chapterInfo=chapterList.get(chapterCount);
                int count=chapterInfo.getChapter_num();
                if(count==bookInfo.getChapterSum()){
                    Toast.makeText(getApplicationContext(),"已无更多章节",Toast.LENGTH_SHORT).show();
                }else {
                    flushType=BrowsingVIew.FlushType.NEXT_PAGE;
                    if(chapterCount<(chapterList.size()-1)){
                        ChapterInfo next_chapter_info=chapterList.get(++chapterCount);
                        presenterBrowsing.getChapterContent(next_chapter_info.getChapter_path());
                    }else{
                        presenterBrowsing.getChapterList(bookInfo.getBookId(), count+1);
                    }
                }



            }

            @Override
            public void jump2lastChapter() {
                Log.i(TAG, "jump2nextChapter: 跳转到上一章节");
                if(chapterCount==0){
                    ChapterInfo chapterInfo=chapterList.get(chapterCount);
                    int count=chapterInfo.getChapter_num();
                    if(count==0){
                        Toast.makeText(getApplicationContext(),"已是第一章节",Toast.LENGTH_SHORT).show();
                    }else {
                        flushType=BrowsingVIew.FlushType.LAST_PAGE;
                        presenterBrowsing.getChapterList(bookInfo.getBookId(), count-1);
                    }
                }else {
                    flushType=BrowsingVIew.FlushType.LAST_PAGE;
                    ChapterInfo last_chapter_info=chapterList.get(--chapterCount);
                    presenterBrowsing.getChapterContent(last_chapter_info.getChapter_path());
                }
            }

            @Override
            public void showSetting() {
                showSettingDialog();
            }

        });

        //加载框设置
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//转盘
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("正在加载，请稍后……");

        loadingDialog=new LoadingDialog(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_book_browsing);
        //setStatusBarColor(this,getResources().getDrawable(R.drawable.browsingview));
        initView();
        presenterBrowsing=new Presenter_Browsing(getApplicationContext(),this);
        presenterBrowsing.init();

        //读取默认配置信息
        settingConf=presenterBrowsing.loadConfig();
        if(settingConf!=null){
            loadReadConf(settingConf);
        }

        //获取携带信息
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            //没有携带信息
            finish();
            return;
        }
        //序列化处理，防止转换警告
        bookInfo = (BookInfo) bundle.getSerializable("bookInfo");
        ChapterInfo chapterInfo = (ChapterInfo) bundle.getSerializable("chapterInfo");

        if(bookInfo==null){
            //无图书信息，直接退出
            finish();
            return;
        }
        flushChapterContent(chapterInfo);


        //注册静态广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(broadcastReceiver, filter);


    }

    //刷新当前章节内容
    private void flushChapterContent(ChapterInfo chapterInfo){
        //请求并获取章节内容
        if(chapterInfo!=null){
            Log.i(TAG, "onCreate: 请求获取章节内容");
            presenterBrowsing.getChapterContent(chapterInfo.getChapter_path());
        }else{
            //获取缓存内是否有浏览章节进度
            int count=presenterBrowsing.getBookProgress(bookInfo.getBookId());
            chapterCount=presenterBrowsing.chapterCount2listCount(count);
            presenterBrowsing.getChapterList(bookInfo.getBookId(), count);
        }

    }

    @Override
    public void showMsgDialog(final int type, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(type>0){
                    CustomToast.showToast(getApplicationContext(),msg, Toast.LENGTH_SHORT,R.drawable.icon_msg_succese);
                }else {
                    CustomToast.showToast(getApplicationContext(),msg,Toast.LENGTH_SHORT,R.drawable.icon_msg_error);
                }
            }
        });
    }

    @Override
    public void syncChapterList(ArrayList<ChapterInfo> list) {
        ChapterInfo info=null;
        if(chapterList.size()>0){
            //获取当前章节，并重新进行排序
            info=chapterList.get(chapterCount);
        }
        //刷新章节列表
        chapterList.addAll(list);
        //修改后的章节列表 根据章节ID进行从新排序，并更新chapterCount的值
        Collections.sort(chapterList);
        if(info!=null){
            switch (flushType){
                case LAST_PAGE:
                    chapterCount=chapterList.indexOf(info)-1;
                    break;
                case NEXT_PAGE:
                    chapterCount=chapterList.indexOf(info)+1;
                    break;
                case THIS_PAGE:
                default:
                    chapterCount=chapterList.indexOf(info);
                    break;
            }
        }
        presenterBrowsing.getChapterContent(chapterList.get(chapterCount).getChapter_path());
    }

    @Override
    public void toLoginActivity() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    @Override
    public void syncReadView(JSONArray jsonArray) {
        Log.i(TAG, "syncReadView: 刷新章节内容");
        //设置view的字符串，并且刷新并重新绘制
        int len=jsonArray.length();
        final String[] arrs=new String[len];
        for (int i = 0; i < len; i++) {
            try {
                arrs[i]=jsonArray.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        browsingVIew.setTextContent(arrs, flushType);
    }

    @Override
    public void setLoading(final boolean type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(type){
                    loadingDialog.showLoading("加载中");
                }else{
                    loadingDialog.hideLoading();
                }
            }
        });
    }

    public void loadReadConf(SettingConf settingConf) {
        browsingVIew.setTextColor(settingConf.fontColor);
        browsingVIew.setTextSize(settingConf.fontSize);
        setBackgroundStyle(settingConf.backgroundStyle);
        setBrightness(settingConf.brightness);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //写入当前浏览记录到数据库
        ChapterInfo info=chapterList.get(chapterCount);
        presenterBrowsing.setBookProgress(bookInfo.getBookId(),info.getChapter_num());
        presenterBrowsing.release();
        //取消注册静态广播
        unregisterReceiver(broadcastReceiver);
    }

    public interface BookCallback {
        void jump2nextChapter();
        void jump2lastChapter();
        void showSetting();
    }


    //弹出设置dialog 动画弹出
    private void showSettingDialog() {
        SettingDialog dialog = new SettingDialog(this).setEventListener(new SettingDialog.EventListener() {
            @Override
            public void changeFontSize(float size) {
                browsingVIew.setTextSize(size);
                settingConf.fontSize=size;
                presenterBrowsing.setReadConfig(settingConf);
            }

            @Override
            public void changeBackground(int style) {
                setBackgroundStyle(style);
                settingConf.backgroundStyle=style;
                presenterBrowsing.setReadConfig(settingConf);
            }

            @Override
            public void changeLight(float count) {
                setBrightness(count);
                settingConf.brightness=count;
                presenterBrowsing.setReadConfig(settingConf);
            }

            @Override
            public int resetSystemLight() {
                float count=getSystemBrightness();
                settingConf.brightness=count;
                presenterBrowsing.setReadConfig(settingConf);
                return (int) count/10;
            }

        });
        //获取并设置系统亮度，参数值
        dialog.setConfData(settingConf);
        dialog.show();

    }

    //设置阅读背景颜色
    private void setBackgroundStyle(int style){
        switch (style){
            case 1:
                linearLayoutCompat.setBackgroundResource(R.drawable.browsingview);
                break;
            case 2:
                linearLayoutCompat.setBackgroundColor(Color.parseColor("#7B7070"));
                break;
            case 3:
                linearLayoutCompat.setBackgroundColor(Color.parseColor("#3FAA98"));
                break;
            case 4:
                linearLayoutCompat.setBackgroundColor(Color.parseColor("#B49D42"));
                break;
            default:
                linearLayoutCompat.setBackgroundResource(R.drawable.browsingview);
                break;
        }
    }


    //设置当前系统亮度
    private void setBrightness(float count){
        Window window = ((Activity) this).getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (count <=0) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = count;
        }
        window.setAttributes(lp);
    }

    //获取当前系统亮度
    private float getSystemBrightness(){
        int systemBrightness = 0;
        try {
            systemBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return systemBrightness;
    }






}