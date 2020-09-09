package com.kkkkkn.readbooks.activates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.adapter.SearchBookResultAdapter;
import com.kkkkkn.readbooks.entity.SearchBookItem;
import com.kkkkkn.readbooks.util.jsoup.JsoupUtil;
import com.kkkkkn.readbooks.util.jsoup.JsoupUtilImp_xbqg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends BaseActivity {
    private final static String TAG="SearchActivity";
    private SearchView searchView;
    private static final int SHOW_BOOKLIST=11,LOAD_NEXTPAGE=12;
    private ArrayList<SearchBookItem> arrayList=new ArrayList<SearchBookItem>();
    private SearchBookResultAdapter adapter;
    private Handler mHandle=new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case SHOW_BOOKLIST:
                    adapter.notifyDataSetChanged();
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
        //listview相关初始化
        adapter=new SearchBookResultAdapter(arrayList,this);
        ListView listView=findViewById(R.id.search_listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击选项进行跳转图书详情
                SearchBookItem item=arrayList.get(position);
                Intent intent=new Intent(getApplicationContext(),BookInfoActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("bookUrl",item.getBookUrl());
                bundle.putString("bookAuthorName",item.getAuthorName());
                bundle.putString("bookName",item.getBookName());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        searchView=findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //请求字符串不为空，开始进行网络请求
                if(!query.isEmpty()){
                    //读取缓存数据，获取当前图书来源设置

                    //请求搜索
                    new RequestThread(query,1).start();
                    //防止抬起落下都触发此事件
                    searchView.setIconified(true);
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

    private class RequestThread extends Thread{
        int sourceId;
        String str;
        public RequestThread(String string,int id) {
            super();
            this.str=string;
            this.sourceId=id;
        }

        @Override
        public void run() {
            JsoupUtil jsoupUtil=new JsoupUtilImp_xbqg();
            try {
                String str=jsoupUtil.searchBook(this.str);
                //解析搜索结果并填充到arrayList中
                JSONObject jsonObject=new JSONObject(str);
                JSONArray jsonArray=jsonObject.getJSONArray("data");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object=(JSONObject) jsonArray.get(i);
                    SearchBookItem searchBookItem=new SearchBookItem();
                    searchBookItem.setBookUrl(object.getString("bookUrl"));
                    searchBookItem.setBookName(object.getString("bookName"));
                    searchBookItem.setAuthorName(object.getString("authorName"));
                    arrayList.add(searchBookItem);
                }
                mHandle.sendEmptyMessage(SHOW_BOOKLIST);
                System.out.println("搜索结果："+str);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }
}