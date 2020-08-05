package com.kkkkkn.readbooks.activates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.os.Bundle;
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
    private String searchStr;

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
                    searchStr=query;
                    //listview相关初始化
                    Thread thread=new Thread(){
                        @Override
                        public void run() {
                            JsoupUtil jsoupUtil=new JsoupUtilImp_xbqg();
                            try {
                                String str=jsoupUtil.searchBook(searchStr);
                                System.out.println("搜索结果："+str);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
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
}