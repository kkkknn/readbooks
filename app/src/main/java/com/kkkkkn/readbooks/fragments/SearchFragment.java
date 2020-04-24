package com.kkkkkn.readbooks.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.adapter.SearchFragmentAdapter;
import com.kkkkkn.readbooks.entity.BookInfo;
import com.kkkkkn.readbooks.util.BookSourceByBQG1;
import com.kkkkkn.readbooks.util.BookSourceImp;
import com.kkkkkn.readbooks.util.BookSourceListener;
import com.kkkkkn.readbooks.util.BookSourceUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchFragment extends Fragment implements BookSourceListener {
    private static final String TAG = "搜索页面" ;
    private static SearchFragment searchFragment;
    private SearchView searchView;
    private BookSourceUtil bookSourceUtil;
    private RecyclerView listView;
    private SearchFragmentAdapter adapter;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what){
                case 1:
                    System.out.println("接收到handler了");
                    adapter.addItem((BookInfo) msg.obj);
                    break;
                case 2:
                    adapter.removeAll();
                    break;
            }
            return true;
        }
    });


    public static SearchFragment newInstance() {
        Log.i(TAG, "newInstance: ");
        if(searchFragment==null){
            synchronized (SearchFragment.class){
                if(searchFragment==null){
                    searchFragment= new SearchFragment();
                }
            }
        }
        return searchFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        Log.i(TAG, "onCreateView: ");
        //设置当前来源
        SharedPreferences sharedPreferences= Objects.requireNonNull(getContext()).getSharedPreferences("bookSource", Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt("bookSource",1).apply();

        //控件绑定
        listView=view.findViewById(R.id.search_listView);
        searchView=(SearchView) view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //请求字符串不为空，开始进行网络请求
                if(!query.equals(" ")){
                    bookSourceUtil.searchBook(query);
                }
                System.out.println("文字提交"+query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("文字改变");
                return false;
            }
        });
        //触摸焦点
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(false);

        bookSourceUtil=BookSourceUtil.getBookSourceUtil();
        bookSourceUtil.setBookSource(new BookSourceByBQG1());
        bookSourceUtil.setListener(this);


        //创建自定义Adapter的对象
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this.getContext());
        listView.setLayoutManager(linearLayoutManager);
        ArrayList<BookInfo> list=new ArrayList<>();
        adapter = new SearchFragmentAdapter(list);
        listView.setAdapter(adapter);
        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    if(listView.canScrollVertically(-1)){
                        System.out.println("到达底部了");
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated: ");

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void Success(Object object) {
        //adapter.notifyDataSetChanged();
        //将布局添加到ListView中
        Message msg=new Message();
        msg.what=1;
        msg.obj=object;
        handler.sendMessage(msg);
    }

    @Override
    public void Error(int errorCode) {

    }
}
