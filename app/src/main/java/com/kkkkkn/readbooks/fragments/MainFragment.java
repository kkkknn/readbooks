package com.kkkkkn.readbooks.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kkkkkn.readbooks.R;

public class MainFragment extends Fragment  {
    private static final String TAG = "主页页面" ;
    private TextView tv;

    public static MainFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name", name);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        Log.i(TAG, "newInstance: ");
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        Log.i(TAG, "onCreateView: ");
        //绑定相关控件
        tv = (TextView) view.findViewById(R.id.fragment_test_tv);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.get("name").toString();
            tv.setText(name);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated: ");


    }

}