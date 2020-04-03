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

public class SetFragment extends Fragment {
    private static final String TAG = "设置界面" ;
    private TextView tv;

    public static SetFragment newInstance(String name) {
        Log.i(TAG, "newInstance: ");
        Bundle args = new Bundle();
        args.putString("name", name);
        SetFragment fragment = new SetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set, container, false);
        Log.i(TAG, "onCreateView: ");
        tv = (TextView) view.findViewById(R.id.fragment_set);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.get("name").toString();
            tv.setText(name);
        }
        //相关布局创建

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated: ");


    }
}
