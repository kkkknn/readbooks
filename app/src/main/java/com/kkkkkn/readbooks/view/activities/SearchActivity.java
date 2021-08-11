package com.kkkkkn.readbooks.view.activities;

import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.model.adapter.SearchBookResultAdapter;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.presenter.Presenter_Login;
import com.kkkkkn.readbooks.presenter.Presenter_Search;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;
import com.kkkkkn.readbooks.view.view.SearchActivityView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity implements SearchActivityView {
    private final static String TAG="SearchActivity";
    private SearchView searchView;
    private ArrayList<BookInfo> arrayList=new ArrayList<BookInfo>();
    private SearchBookResultAdapter adapter;
    private Presenter_Search presenter_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        presenter_search=new Presenter_Search(getApplicationContext(),this);
        presenter_search.init();
        //listview相关初始化
        adapter=new SearchBookResultAdapter(arrayList,this);
        ListView listView=findViewById(R.id.search_listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toBrowsingActivity(arrayList.get(position));
            }
        });
        searchView=findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //请求字符串不为空，开始进行网络请求
                if(!query.isEmpty()){
                    //请求搜索
                    presenter_search.searchBook(query);
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

    public void eventCallBack(MessageEvent event){
        switch (event.message){
            case SYNC_SEARCH_RESULT:
                arrayList.clear();
                if(event.value instanceof ArrayList<?>){
                    ArrayList<?> list=(ArrayList<?>) event.value;
                    for (Object o:list) {
                        arrayList.add((BookInfo) o);
                        adapter.notifyDataSetChanged();
                    }
                }
                Log.i(TAG, "eventCallBack: sssssssssssssss   "+arrayList.size());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter_search.release();
    }


    private void toBrowsingActivity(BookInfo bookInfo) {
        Intent intent=new Intent(getApplicationContext(),BookInfoActivity.class);
        intent.putExtra("bookInfo",bookInfo);
        startActivity(intent);
    }

    @Override
    public void syncBookList(final ArrayList<BookInfo> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (arrayList != null) {
                    arrayList.clear();
                    arrayList.addAll(list);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void toLoginActivity() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));

    }
}