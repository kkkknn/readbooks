package com.kkkkkn.readbooks.activates;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.adapter.BookShelfAdapter;
import com.kkkkkn.readbooks.entity.BookInfo;
import com.kkkkkn.readbooks.entity.MainBooks;
import com.kkkkkn.readbooks.util.jsoup.JsoupUtilImp;
import com.kkkkkn.readbooks.util.sqlite.SqlBookUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import javax.security.auth.login.LoginException;


/**
 * 程序主界面，每次进入的时候获取读取本地图书并进行加载
 */
public class MainActivity extends BaseActivity  {
    private final static String TAG="主界面";
    private long lastBackClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //隐藏APP title
        /*ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(false);
        }*/

        GridView mGridView=findViewById(R.id.main_booksGridView);
        SwipeRefreshLayout swipeRefreshLayout=findViewById(R.id.main_SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SqlBookUtil sqlBookUtil=SqlBookUtil.getInstance(getApplicationContext()).initDataBase();
                ArrayList<BookInfo> list=sqlBookUtil.getEnjoyBook();
                if(list!=null){
                    BookShelfAdapter mAdapter = new BookShelfAdapter(getApplicationContext(),list);
                    mGridView.setAdapter(mAdapter);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //读取本地数据库，获取已加入收藏的图书 并添加到主页相应位置
        SqlBookUtil sqlBookUtil=SqlBookUtil.getInstance(getApplicationContext()).initDataBase();
        ArrayList<BookInfo> list=sqlBookUtil.getEnjoyBook();
        BookShelfAdapter mAdapter = new BookShelfAdapter(getApplicationContext(),list);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BookInfo bookInfo=(BookInfo) adapterView.getAdapter().getItem(i);
                if(bookInfo!=null){
                    jump2ReadView(bookInfo);
                    Log.i(TAG, "onItemClick: "+bookInfo.getBookId());
                }
            }
        });
    }

    //跳转到阅读页面
    private void jump2ReadView(BookInfo info){
        //根据图书ID确定当前读书进度
        SqlBookUtil sqlBookUtil=SqlBookUtil.getInstance(getApplicationContext()).initDataBase();
        String str=sqlBookUtil.getReadProgress(info.getBookId());
        int pageCount=0;
        int chapterCount=0;
        int lineFlag=0;
        try {
            if(str!=null){
                JSONObject jsonObject=new JSONObject(str);
                pageCount=jsonObject.getInt("chapterPageCount");
                chapterCount=jsonObject.getInt("chapterCount");
                lineFlag=jsonObject.getInt("chapterLineCount");
                Log.i(TAG, "jump2ReadView: pageCount "+pageCount+" || "+chapterCount+" || "+lineFlag);
            }

            //跳转到阅读页面
            Intent intent=new Intent(getApplicationContext(),BookBrowsingActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("bookInfo",info);
            bundle.putInt("chapterFlag",chapterCount);
            bundle.putInt("lineFlag",lineFlag);
            bundle.putInt("pageFlag",pageCount);
            intent.putExtras(bundle);
            startActivity(intent);

        } catch ( JSONException e) {
            e.printStackTrace();
        }
    }

    //监听返回键，连续按2次直接退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            long nowBackClick=System.currentTimeMillis();
            if(lastBackClick!=0&&(nowBackClick-lastBackClick)<1500){
                //程序退出
                this.exitAll();
            }else{
                //500ms以上，弹窗不处理
                lastBackClick=nowBackClick;
                Toast.makeText(getApplicationContext(),"请再按一次以退出程序",Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.navigation_notifications:
                Toast.makeText(this,"点击了1",Toast.LENGTH_SHORT).show();
                break;
            case R.id.navigation_dashboard:
                //点击了搜索框，开始跳转
                Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
