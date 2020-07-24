package com.kkkkkn.readbooks.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.activates.BookInfoActivity;
import com.kkkkkn.readbooks.activates.LoginActivity;
import com.kkkkkn.readbooks.activates.MainActivity;
import com.kkkkkn.readbooks.adapter.SearchBookResultAdapter;
import com.kkkkkn.readbooks.entity.SearchBookItem;
import com.kkkkkn.readbooks.util.BackgroundUtil;
import com.kkkkkn.readbooks.util.BackgroundUtilListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class SearchFragment extends Fragment implements BackgroundUtilListener {
    private static final String TAG = "搜索页面" ;
    private static final int UPDATE_LISTVIEW = 1;
    private static volatile SearchFragment searchFragment;
    private ArrayList<SearchBookItem> searchBooksList;
    private BackgroundUtilListener listener;
    private SearchBookResultAdapter searchBookResultAdapter;
    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what){
                case UPDATE_LISTVIEW:
                    if(searchBookResultAdapter!=null){
                        searchBookResultAdapter.notifyDataSetChanged();
                    }
                    break;
            }
            return true;
        }
    });

    private SearchFragment() {
        this.searchBooksList=new ArrayList<>();
        this.listener=this;
    }

    public static SearchFragment newInstance() {
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
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        Log.i(TAG, "onCreateView: ");
        //设置当前来源
        /*SharedPreferences sharedPreferences= Objects.requireNonNull(getContext()).getSharedPreferences("bookSource", Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt("bookSource",1).apply();*/

        //控件绑定
        ListView listView=(ListView) view.findViewById(R.id.search_listView);
        SearchView searchView=(SearchView) view.findViewById(R.id.searchView);

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

        //设置listview数据及监听事件
        searchBookResultAdapter=new SearchBookResultAdapter(searchBooksList,getContext());
        listView.setAdapter(searchBookResultAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchBookItem item=searchBooksList.get(position);
                Toast.makeText(getContext(),item.getBookUrl(),Toast.LENGTH_SHORT).show();
                //跳转到图书详情页面,携带图书对象/图书链接
                Intent intent=new Intent(getContext(),BookInfoActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("bookUrl",item.getBookUrl());
                bundle.putString("bookAuthorName",item.getAuthorName());
                bundle.putString("bookName",item.getBookName());
                intent.putExtras(bundle);
                startActivity(intent);

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
    public void success(int codeFlag,String str) {
        if(str.isEmpty()){
            Log.i(TAG, "success: 返回为空");
            return;
        }
        String code;
        try {
            JSONObject jsonObject=new JSONObject(str);
            code=(String)jsonObject.get("code");
            if(!code.isEmpty()&&code.equals("success")){
                JSONObject dataObject=(JSONObject) jsonObject.get("data");
                int now_page=(Integer) dataObject.get("page");
                int sum_page=(Integer) dataObject.get("allPage");
                JSONArray booksObject=(JSONArray)dataObject.get("data");
                if(booksObject.length()==0){
                    Log.i(TAG, "success: 没有查询到数据");
                }else{
                    //查询到数据，添加到listview中
                    searchBooksList.clear();
                    for (int i = 0; i <booksObject.length(); i++) {
                        SearchBookItem bookItem=new SearchBookItem();
                        JSONObject object=(JSONObject) booksObject.get(i);
                        bookItem.setAuthorName((String) object.get("author_name"));
                        bookItem.setBookName((String) object.get("book_name"));
                        bookItem.setBookUrl((String) object.get("book_url"));
                        searchBooksList.add(bookItem);
                    }
                    Log.i(TAG, "success: "+booksObject.length());
                    //handler 消息通知更新UI
                    mHandler.sendEmptyMessage(UPDATE_LISTVIEW);
                }
            }else if(!code.isEmpty()&&code.equals("error")){
                String tip=(String)jsonObject.get("data");
                if("令牌验证失败，请重新尝试".equals(tip))
                {
                    //跳转回登录界面
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                Log.i(TAG, "error: "+tip);
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
