package com.kkkkkn.readbooks.activates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;


/**
 * 程序主界面，每次进入的时候获取SharedPreferences中的accountId和token,并进行网络请求，看是否登录成功
 */
public class MainActivity extends BaseActivity {
    private final static String PRE_NAME="ReadBooksShared";
    private final static String KEY_ID="AccountId";
    private final static String KEY_TOKEN="AccountToken";
    private final static String TAG="主界面";
    private BottomNavigationView bottomNavigationView;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //检测SharedPreferences 有没有accountid和token
        checkLogin();

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
        list.add(MainFragment.newInstance("首页"));
        list.add(SearchFragment.newInstance());
        list.add(SetFragment.newInstance("卡片"));
        viewPagerAdapter.setList(list);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
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
        SharedPreferences sharedPreferences=getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        int id=sharedPreferences.getInt(KEY_ID,0);
        String token=sharedPreferences.getString(KEY_TOKEN,"");
        if(id==0||token==null||token.isEmpty()){
            return false;
        }
        //开始连接服务器进行查询

        return false;
    }

}
