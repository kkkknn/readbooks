package com.kkkkkn.readbooks.activates;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.entity.BookInfo;
import com.kkkkkn.readbooks.util.jsoup.JsoupUtilImp;
import com.kkkkkn.readbooks.util.sqlite.SqlBookUtil;
import com.kkkkkn.readbooks.view.BrowsingVIew;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;

public class BookBrowsingActivity extends BaseActivity {
    private final static String TAG = "BookBrowsingActivity";
    private LinkedList<String[]> chapterList = new LinkedList<>();
    private int chapterFlag = 0;
    private int lineFlag = 0;
    private int pageFlag = 0;
    private String[] chapterContent;
    private BrowsingVIew browsingVIew;
    private ProgressDialog progressDialog;
    private BookInfo bookInfo;
    private ImageView imageView;

    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 22:
                    chapterContent = (String[]) msg.obj;
                    if (chapterContent != null && chapterContent.length > 0) {
                        browsingVIew.setTextContent(chapterContent);
                        browsingVIew.setTextColor(Color.BLACK);
                        browsingVIew.setTextSize(40f);
                        browsingVIew.setProgress(lineFlag, false);
                        browsingVIew.invalidate();

                    }
                    //接收完成,隐藏遮罩层
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    break;
                case 23:
                    chapterContent = (String[]) msg.obj;
                    if (chapterContent != null && chapterContent.length > 0) {
                        browsingVIew.setTextContent(chapterContent);
                        browsingVIew.setTextColor(Color.BLACK);
                        browsingVIew.setTextSize(40f);
                        browsingVIew.setProgress(lineFlag, true);
                        browsingVIew.invalidate();
                    }
                    //接收完成,隐藏遮罩层
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    break;
                case 11://显示加载框遮罩层
                    if (progressDialog != null) {
                        progressDialog.show();
                    }
                    break;
                case 12://出错，隐藏加载框遮罩层
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    break;

            }
            return false;
        }
    });

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_browsing);

        browsingVIew = findViewById(R.id.browView);

        browsingVIew.setListener(new BookCallback() {
            @Override
            public void jump2nextChapter() {
                if (chapterFlag < chapterList.size()) {
                    new GetContentThread(chapterList.get(++chapterFlag)[1], 1).start();
                    Log.i(TAG, "jump2nextChapter: chapterFlag：" + chapterFlag);
                    //显示遮罩层

                } else if (pageFlag < bookInfo.getPageSum()) {
                    //弹窗或者提示阅读已经完成
                    //查看是否还有下一页面，并重置相关数据
                    new Thread() {
                        @Override
                        public void run() {
                            resetChapterData(++pageFlag, 2);
                        }
                    }.start();
                }
            }

            @Override
            public void jump2lastChapter() {
                if (chapterFlag > 0) {
                    new GetContentThread(chapterList.get(--chapterFlag)[1], 2).start();
                    Log.i(TAG, "jump2lastChapter: chapterFlag" + chapterFlag);
                } else if (pageFlag > 0) {
                    //查看是否还有上一页面，并重置相关数据
                    new Thread() {
                        @Override
                        public void run() {
                            resetChapterData(--pageFlag, 1);
                        }
                    }.start();
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


        //获取携带信息
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            //没有携带信息
            finish();
        } else {
            //序列化处理，防止转换警告
            bookInfo = (BookInfo) bundle.getSerializable("bookInfo");
            chapterFlag = bundle.getInt("chapterFlag");
            lineFlag = bundle.getInt("lineFlag");
            pageFlag = bundle.getInt("pageFlag");
            Log.i(TAG, "onCreate: chapterFlag: " + chapterFlag + "  || lineFlag  " + lineFlag + " || " + pageFlag);

            //开启线程进行数据初始化
            new Thread() {
                @Override
                public void run() {
                    resetChapterData(pageFlag, 0);
                }
            }.start();


        }
        //注册静态广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(broadcastReceiver, filter);

    }


    //初始化相关数据:章节列表，文字内容
    private void resetChapterData(int page, int type) {
        //获取图书的当前页章节名及链接并添加chapterList
        String thisPageUrl = getPageUrl(bookInfo, page);
        //开启线程，获取当前页章节列表
        JsoupUtilImp util = JsoupUtilImp.getInstance().setSource(bookInfo.getBookFromType());
        try {
            String value = util.getBookChapterList(thisPageUrl);
            JSONArray jsonArray = (JSONArray) new JSONObject(value).get("chapters");
            chapterList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String[] arr = new String[2];
                arr[0] = (String) jsonObject.get("chapterName");
                arr[1] = (String) jsonObject.get("chapterUrl");
                chapterList.add(arr);
            }
            switch (type){
                case 1:
                    chapterFlag = chapterList.size() - 1;
                    break;
                case 2:
                    chapterFlag = 0;
                    break;
                default:
                    break;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            //获取章节内容
            if (chapterList.size() > 0) {
                new GetContentThread(chapterList.get(chapterFlag)[1], bookInfo.getBookFromType()).start();
                //只显示当前章节名字 和当前时间，
                //browsingVIew.setChapterNameStr(chapterList.get(chapterFlag)[0]);
                //browsingVIew.setProgressStr(chapterFlag +"/"+chapterList.size());
            }
        }
    }

    //获取章节文字的网络线程
    private class GetContentThread extends Thread {
        private String url;
        private int type;

        public GetContentThread(String url, int type) {
            this.url = url;
            this.type = type;
        }

        @Override
        public void run() {
            //利用handle 通知显示加载框
            mHandler.sendEmptyMessage(11);
            //获取当前点击章节文字
            JsoupUtilImp util = JsoupUtilImp.getInstance().setSource(type);
            try {
                JSONObject jsonObject = util.getChapterContent(url);
                String[] arr_text = (String[]) jsonObject.get("chapterContent");
                if (arr_text.length > 0) {
                    Message suc_msg = mHandler.obtainMessage();
                    suc_msg.obj = arr_text;
                    if (type == 2) {
                        suc_msg.what = 23;
                    } else {
                        suc_msg.what = 22;
                    }
                    lineFlag=0;
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
        unregisterReceiver(broadcastReceiver);


    }

    @Override
    protected void onStop() {
        super.onStop();
        //写入浏览进度到数据库
        SqlBookUtil sqlBookUtil= SqlBookUtil.getInstance(getApplicationContext()).initDataBase();

        boolean flag=sqlBookUtil.setReadProgress(pageFlag,chapterFlag,browsingVIew.getlineFlag(),bookInfo.getBookId());
        if(flag){
            Log.i(TAG, "onStop: 写入成功");
        }else{
            Log.i(TAG, "onStop: 写入失败");
        }
    }

    public interface BookCallback {
        void jump2nextChapter();

        void jump2lastChapter();

        void showSetting();

    }

    //获取当前页章节链接
    private String getPageUrl(BookInfo bookInfo, int count) {
        if (bookInfo == null || bookInfo.getChapterPagesUrlStr().isEmpty()) {
            return null;
        }
        String str = bookInfo.getChapterPagesUrlStr();
        String[] values = str.substring(1, str.length() - 1).split(", ");
        return values[count];
    }


    //弹出设置dialog
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
        popUp(dialog.getWindow().findViewById(R.id.liner_test1));
    }

    private void popUp(View view) {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(500);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
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
    }


}