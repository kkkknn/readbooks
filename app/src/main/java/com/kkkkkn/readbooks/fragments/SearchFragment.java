package com.kkkkkn.readbooks.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.kkkkkn.readbooks.R;

public class SearchFragment extends Fragment {
    private static final String TAG = "搜索页面" ;
    private SearchView searchView;
    private TextView tv;

    public static SearchFragment newInstance() {
        Log.i(TAG, "newInstance: ");
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        Log.i(TAG, "onCreateView: ");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated: ");
        //控件绑定
        searchView=(SearchView) view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("文字提交");
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
    }

}
