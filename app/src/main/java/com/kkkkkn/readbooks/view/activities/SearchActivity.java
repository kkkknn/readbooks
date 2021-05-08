package com.kkkkkn.readbooks.view.activities;

import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.model.adapter.SearchBookResultAdapter;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.model.jsoup.JsoupUtil;
import com.kkkkkn.readbooks.model.jsoup.JsoupUtilImp;
import com.kkkkkn.readbooks.presenter.Presenter_Search;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {
    private final static String TAG="SearchActivity";
    private SearchView searchView;
    private ArrayList<BookInfo> arrayList=new ArrayList<BookInfo>();
    private SearchBookResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Log.i("TAG", "onCreate: 开始创建");
        //listview相关初始化
        adapter=new SearchBookResultAdapter(arrayList,this);
        ListView listView=findViewById(R.id.search_listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击选项进行跳转图书详情
                BookInfo item=arrayList.get(position);
                Intent intent=new Intent(getApplicationContext(),BookInfoActivity.class);
                intent.putExtra("bookInfo",item);
                startActivity(intent);
            }
        });
        searchView=findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //请求字符串不为空，开始进行网络请求
                if(!query.isEmpty()){
                    //请求搜索
                    final String str=query;
                    new Thread(){
                        @Override
                        public void run() {
                            Presenter_Search.getInstance().searchBook(str,1);
                        }
                    }.start();
                    //防止抬起落下都触发此事件
                    searchView.setIconified(true);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("文字改变"+newText);
                return false;
            }
        });
        searchView.setIconifiedByDefault(false);

    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void eventCallBack(MessageEvent event){
        switch (event.message){
            case SYNC_SEARCH_RESULT:
                arrayList.clear();
                if(event.value instanceof ArrayList<?>){
                    ArrayList<?> list=(ArrayList<?>) event.value;
                    for (Object o:list) {
                        arrayList.add((BookInfo) o);
                    }
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }


}