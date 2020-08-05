package com.kkkkkn.readbooks.activates;

import android.app.Activity;
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


public class BaseActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        int statusBarHeight = getStatusBarHeight();
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

    private int getStatusBarHeight() {
        int result = 0;
        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = getResources().getDimensionPixelSize(resId);
        }
        return result;
    }

    public static void setStatusBarColor(Activity activity, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(colorId));
        }
    }
}
