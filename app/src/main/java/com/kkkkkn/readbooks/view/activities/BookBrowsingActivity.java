package com.kkkkkn.readbooks.view.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.model.clientsetting.SettingConf;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.model.entity.ChapterInfo;
import com.kkkkkn.readbooks.presenter.Presenter_Browsing;
import com.kkkkkn.readbooks.view.customView.BrowsingVIew;
import com.kkkkkn.readbooks.view.customView.CustomToast;
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
    private ProgressDialog loadingDialog;
    private BookInfo bookInfo;
    private float readProgress;
    private Presenter_Browsing presenterBrowsing;
    private BrowsingVIew.FlushType flushType;


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
                Log.i(TAG, "showSetting: 展示弹窗111");
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


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_browsing);

        initView();

        presenterBrowsing=new Presenter_Browsing(getApplicationContext(),this);
        presenterBrowsing.init();

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

        //读取文字背景存储设置信息
        SettingConf config=presenterBrowsing.getConfig();
        if(config!=null){
            browsingVIew.setTextColor(config.fontColor);
            browsingVIew.setTextSize(config.fontSize);
        }else {
            browsingVIew.setTextColor(Color.BLACK);
            browsingVIew.setTextSize(40F);
        }
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
        }else {
            chapterCount=0;
        }
        //todo 判断是否超出章节限制，超出限制，清除一部分章节,防止内存过大
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
    public void setLoading(boolean type) {
        //todo 根据设置显示、隐藏加载框
        if(type){

        }else{

        }
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
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.setting_dialog);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setBackgroundDrawable(null);
        dialog.show();
        //设置弹出属性动画
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(500);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        View view=dialog.getWindow().findViewById(R.id.liner_test1);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //获取相应的组件并添加相应的时间监听
        TextView add_sizeTextView,subtract_sizeTextView,font_sizeTextView;
        add_sizeTextView=dialog.getWindow().findViewById(R.id.add_fontSize);
        font_sizeTextView=dialog.getWindow().findViewById(R.id.fontSize);
        subtract_sizeTextView=dialog.getWindow().findViewById(R.id.subtract_fontSize);
        add_sizeTextView.setOnClickListener(onClickListener);
        subtract_sizeTextView.setOnClickListener(onClickListener);
        RadioGroup radioGroup=dialog.getWindow().findViewById(R.id.setting_radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.i(TAG, "onCheckedChanged: "+i);
            }
        });
    }
    
    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.add_fontSize:
                    Log.i(TAG, "onClick: 文字增加");
                    //通过eventbus通知修改文字大小，并重新绘制
                    break;
                case R.id.subtract_fontSize:
                    Log.i(TAG, "onClick: 文字减小");
                    //通过eventbus通知修改文字大小，并重新绘制

                    break;
            }
        }
    };





}