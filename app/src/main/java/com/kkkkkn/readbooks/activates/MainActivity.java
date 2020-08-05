package com.kkkkkn.readbooks.activates;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.adapter.BookShelfAdapter;
import com.kkkkkn.readbooks.entity.MainBooks;

import java.util.ArrayList;


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

        //读取本地目录，生成已下载图书列表
        GridView mGridView=findViewById(R.id.main_booksGridView);
        ArrayList<MainBooks> list=new ArrayList<MainBooks>();
        list.add(new MainBooks(R.drawable.user,"图书1"));
        list.add(new MainBooks(R.drawable.user,"图书2"));
        list.add(new MainBooks(R.drawable.user,"图书3"));
        list.add(new MainBooks(R.drawable.user,"图书4"));
        list.add(new MainBooks(R.drawable.user,"图书4"));
        list.add(new MainBooks(R.drawable.user,"图书4"));
        list.add(new MainBooks(R.drawable.user,"图书4"));
        list.add(new MainBooks(R.drawable.user,"图书4"));
        list.add(new MainBooks(R.drawable.user,"图书4"));
        BookShelfAdapter mAdapter = new BookShelfAdapter(getApplicationContext(),list);
        mGridView.setAdapter(mAdapter);

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
