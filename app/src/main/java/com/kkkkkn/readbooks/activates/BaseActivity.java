package com.kkkkkn.readbooks.activates;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.kkkkkn.readbooks.R;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;


public class BaseActivity extends AppCompatActivity {
    private boolean logRun=false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化日志框架
        /*FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)      //（可选）是否显示线程信息。 默认值为true
                .methodCount(2)             // （可选）要显示的方法行数。 默认2
                .methodOffset(7)            // （可选）设置调用堆栈的函数偏移值，0的话则从打印该Log的函数开始输出堆栈信息，默认是0
                //.logStrategy(customLog)     //（可选）更改要打印的日志策略。 默认LogCat
                .tag("MyTAG")               //（可选）每个日志的全局标记。 默认PRETTY_LOGGER（如上图）
                .build();*/
        if(!logRun){
            Logger.addLogAdapter(new AndroidLogAdapter());
            logRun=true;
        }

        //隐藏导航栏
        /*ActionBar bar=getSupportActionBar();
        if(bar!=null){
            bar.hide();
        }*/
        // 隐藏状态栏
        //setStatusBarUpperAPI19();
        /*getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorWhite));
        View decor = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }*/
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //将当前activity加入栈
        StackManager stackManager=StackManager.getInstance();
        stackManager.addActivity(this);
    }

    public void exitAll(){
        StackManager stackManager=StackManager.getInstance();
        stackManager.exitAllActivity();
    }

    private void setStatusBarUpperAPI19() {
        Window window = getWindow();
        //设置悬浮透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        int statusBarHeight = getStatusBarHeight(this);
        int statusColor = getResources().getColor(R.color.colorBlue);

        View mTopView = mContentView.getChildAt(0);
        if (mTopView != null && mTopView.getLayoutParams() != null &&
                mTopView.getLayoutParams().height == statusBarHeight) {
            mTopView.setBackgroundColor(statusColor);
            return;
        }

        //制造一个和状态栏等尺寸的 View
        mTopView = new View(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        mTopView.setBackgroundColor(statusColor);
        //将view添加到第一个位置
        mContentView.addView(mTopView, 0, lp);
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    public static void setStatusBarColor(Activity activity, int colorId) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(colorId));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(logRun){
            Logger.clearLogAdapters();
            logRun=false;
        }
    }
}
