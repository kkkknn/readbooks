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
import com.kkkkkn.readbooks.adapter.OnItemClickSearchFragmentAdapter;
import com.kkkkkn.readbooks.adapter.SearchFragmentAdapter;
import com.kkkkkn.readbooks.entity.BookInfo;
import com.kkkkkn.readbooks.util.BackgroundUtil;
import com.kkkkkn.readbooks.util.BackgroundUtilListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchFragment extends Fragment implements BackgroundUtilListener {
    private static final String TAG = "搜索页面" ;
    private static SearchFragment searchFragment;
    private SearchView searchView;
    private RecyclerView listView;
    private BackgroundUtilListener listener;
    private SearchFragmentAdapter adapter;


    private SearchFragment() {
        this.listener=this;
    }

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
                if(!query.isEmpty()){
                    BackgroundUtil backgroundUtil=BackgroundUtil.getInstance(getContext()).setListener(listener);
                    //获取id和token,获取失败弹窗并跳转到登录界面
                    int id=backgroundUtil.getAccountId();
                    String token=backgroundUtil.getTokenStr();
                    if(id==0||token.isEmpty()){
                        Log.i(TAG, "onQueryTextSubmit: 查询用户信息失败，跳转至登录界面");
                        return false;
                    }
                    //开始搜索图书
                    backgroundUtil.searchBooks(query,backgroundUtil.getAccountId(),token);
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
    public void success(String str) {
        if(str.isEmpty()){
            Log.i(TAG, "success: 返回为空");
            return;
        }
        String code;
        try {
            JSONObject jsonObject=new JSONObject(str);
            code=(String)jsonObject.get("code");
            if(!code.isEmpty()&&code.equals("success")){
                JSONObject dataobject=(JSONObject) jsonObject.get("data");
                int now_page=(Integer) dataobject.get("page");
                int sum_page=(Integer) dataobject.get("allPage");
                JSONArray booksObject=(JSONArray)dataobject.get("data");
                if(booksObject.length()==0){
                    Log.i(TAG, "success: 没有查询到数据");
                }else{
                    //查询到数据，添加到listview中

                    Log.i(TAG, "success: "+booksObject.length());
                }
            }else if(!code.isEmpty()&&code.equals("error")){
                String tip=(String)jsonObject.get("data");
                Log.i(TAG, "success: "+tip);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void error(int codeId) {

    }

    @Override
    public void timeOut(int requestId) {

    }
}
