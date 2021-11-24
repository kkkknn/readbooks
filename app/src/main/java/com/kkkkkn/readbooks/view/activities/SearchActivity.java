package com.kkkkkn.readbooks.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.model.adapter.SearchBookResultAdapter;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.presenter.Presenter_Search;
import com.kkkkkn.readbooks.view.customView.CustomToast;
import com.kkkkkn.readbooks.view.view.SearchActivityView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity implements SearchActivityView {
    private final static String TAG="SearchActivity";
    private SearchView searchView;
    private ArrayList<BookInfo> arrayList=new ArrayList<BookInfo>();
    private Presenter_Search presenter_search;
    private String searchStr;
    private boolean isEnd=false;
    private ProgressBar loading;
    private ListView listView;
    private TextView nothing_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        presenter_search=new Presenter_Search(getApplicationContext(),this);
        presenter_search.init();
        initView();


    }

    private void initView(){
        //listview相关初始化
        SearchBookResultAdapter adapter=new SearchBookResultAdapter(arrayList,this);
        listView=findViewById(R.id.search_listView);
        View footView;
        LayoutInflater inflater = LayoutInflater.from(this);
        footView = inflater.inflate(R.layout.loading_layout, null);
        loading = footView.findViewById(R.id.loading_view);
        nothing_tv = footView.findViewById(R.id.nothing_view);
        listView.addFooterView(footView);//添加底部加载框，在setAdapter之前add
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toBrowsingActivity(arrayList.get(position));
            }
        });
        //滑动到底部监听
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (absListView.getLastVisiblePosition() == absListView.getCount() - 1) {
                        //是否还有未加载的数据
                        if(isEnd){
                            //listview 展示没有更多标签
                            nothing_tv.setVisibility(View.VISIBLE);
                        }else {
                            //显示加载框
                            nothing_tv.setVisibility(View.GONE);
                            loading.setVisibility(View.VISIBLE);
                            //加载更多功能的代码
                            presenter_search.searchBook(arrayList.size(),searchStr);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }

        } );
        searchView=findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //请求字符串不为空，开始进行网络请求
                if(!query.isEmpty()){
                    isEnd=false;
                    //清空当前列表
                    arrayList.clear();
                    ((SearchBookResultAdapter)((HeaderViewListAdapter)listView.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();
                    //请求搜索
                    searchStr=query;
                    presenter_search.searchBook(arrayList.size(),searchStr);
                    //防止抬起落下都触发此事件
                    searchView.setIconified(true);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setIconifiedByDefault(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        arrayList.clear();
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
                    if(list.size()<presenter_search.getPageSize()){
                        isEnd=true;
                    }
                    loading.setVisibility(View.GONE);
                    arrayList.addAll(list);
                    ((SearchBookResultAdapter)((HeaderViewListAdapter)listView.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();

                }
            }
        });
    }

    @Override
    public void toLoginActivity() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));

    }

    @Override
    public void showMsgDialog(final int type,final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(type>0){
                    CustomToast.showToast(getApplicationContext(),msg, Toast.LENGTH_SHORT,R.drawable.icon_msg_succese);
                }else {
                    CustomToast.showToast(getApplicationContext(),msg,Toast.LENGTH_SHORT,R.drawable.icon_msg_error);
                }
            }
        });
    }
}