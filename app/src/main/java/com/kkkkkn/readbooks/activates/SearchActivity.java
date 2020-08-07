package com.kkkkkn.readbooks.activates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.util.jsoup.JsoupUtil;
import com.kkkkkn.readbooks.util.jsoup.JsoupUtilImp_xbqg;

import org.json.JSONException;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends BaseActivity {
    private SearchView searchView;
    private static final int SHOW_BOOKLIST=11,LOAD_NEXTPAGE=12;
    private Handler mHandle=new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case SHOW_BOOKLIST:

                    break;
                case LOAD_NEXTPAGE:

                    break;

            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Log.i("TAG", "onCreate: 开始创建");
        searchView=findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //请求字符串不为空，开始进行网络请求
                if(!query.isEmpty()){
                    //读取缓存数据，获取当前图书来源设置

                    //listview相关初始化

                    //请求搜索
                    new RequestThread(query).start();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("文字改变");
                return false;
            }
        });
        searchView.setIconifiedByDefault(false);

    }

    private static class RequestThread extends Thread{
        String str;
        public RequestThread(String string) {
            super();
            this.str=string;
        }

        @Override
        public void run() {
            JsoupUtil jsoupUtil=new JsoupUtilImp_xbqg();
            try {
                String str=jsoupUtil.searchBook(this.str);
                System.out.println("搜索结果："+str);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }
}