package com.kkkkkn.readbooks.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.adapter.BookShelfAdapter;
import com.kkkkkn.readbooks.entity.MainBooks;
import com.kkkkkn.readbooks.view.BookGridView;

import java.util.ArrayList;

public class MainFragment extends Fragment  {
    private static final String TAG = "主页页面" ;;
    private static MainFragment mainFragment;

    private MainFragment() {
    }

    public static MainFragment newInstance() {
        if(mainFragment==null){
            synchronized (MainFragment.class){
                if(mainFragment==null){
                    mainFragment = new MainFragment();
                    Log.i(TAG, "newInstance: ");
                }
            }
        }
        return mainFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        Log.i(TAG, "onCreateView: ");
        //绑定相关控件
        GridView mGridView=view.findViewById(R.id.booksGridview);

        ArrayList<MainBooks> list=new ArrayList<MainBooks>();
        list.add(new MainBooks(R.drawable.user,"图书1"));
        list.add(new MainBooks(R.drawable.user,"图书2"));
        list.add(new MainBooks(R.drawable.user,"图书3"));
        list.add(new MainBooks(R.drawable.user,"图书4"));
        list.add(new MainBooks(R.drawable.user,"图书4"));
        list.add(new MainBooks(R.drawable.user,"图书4"));
        list.add(new MainBooks(R.drawable.user,"图书4"));
        list.add(new MainBooks(R.drawable.user,"图书4"));
        list.add(new MainBooks(R.drawable.user,"图书4"));
        list.add(new MainBooks(R.drawable.user,"图书4"));
        BookShelfAdapter mAdapter = new BookShelfAdapter(view.getContext(),list);

        mGridView.setAdapter(mAdapter);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated: ");


    }

}