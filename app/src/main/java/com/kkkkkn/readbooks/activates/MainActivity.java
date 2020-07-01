package com.kkkkkn.readbooks.activates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.adapter.ViewPagerAdapter;
import com.kkkkkn.readbooks.fragments.MainFragment;
import com.kkkkkn.readbooks.fragments.SearchFragment;
import com.kkkkkn.readbooks.fragments.SetFragment;
import com.kkkkkn.readbooks.util.BackgroundUtil;
import com.kkkkkn.readbooks.util.BackgroundUtilListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 程序主界面，每次进入的时候获取SharedPreferences中的accountId和token,并进行网络请求，看是否登录成功
 */
public class MainActivity extends BaseActivity implements BackgroundUtilListener {
    private final static String PRE_NAME="ReadBooksShared";
    private final static String KEY_ID="AccountId";
    private final static String KEY_TOKEN="AccountToken";
    private final static String TAG="主界面";
    private BottomNavigationView bottomNavigationView;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private BackgroundUtil backgroundUtil;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取服务器接口类并进行初始化
        backgroundUtil=BackgroundUtil.getInstance(this).setListener(this);

        //检测SharedPreferences 有没有accountid和token
        if(!checkLogin()){
            //跳转到登录界面
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivityForResult(intent,301);
        }

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        viewPager = (ViewPager) findViewById(R.id.vp);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //滑动时调用
                hideInput();
            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        List<Fragment> list = new ArrayList<>();
        list.add(SearchFragment.newInstance());
        list.add(MainFragment.newInstance());
        list.add(SetFragment.newInstance("卡片"));
        viewPagerAdapter.setList(list);
        viewPager.setCurrentItem(1);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    private void hideInput(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    private boolean checkLogin(){
        if(backgroundUtil==null){
            return false;
        }
        int id=backgroundUtil.getAccountId();
        String token=backgroundUtil.getTokenStr();
        if(id==0||token==null||token.isEmpty()){
            Log.i(TAG, "checkLogin: 没有查询到数据");
            return false;
        }else{
            Log.i(TAG, "checkLogin:查询到的数据"+id+"||"+token);
            //开始连接服务器进行查询

            return true;
        }
    }

    @Override
    protected void onActivityResult(int reqcode,int rescode,Intent data) {
        super.onActivityResult(reqcode, rescode, data);
        switch (reqcode){
            case 301:
                if(rescode==1){
                    //登录成功，开始加载图书数据及用户相关数据存储
                }else {
                    //登录失败，请检查填写数据是否有误
                }
            break;
        }

    }

    @Override
    public void success(int codeFlag,String str) {

    }

    @Override
    public void error(int codeId) {

    }

    @Override
    public void timeOut(int requestId) {

    }
}
