package com.kkkkkn.readbooks.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.activates.AboutActivity;
import com.kkkkkn.readbooks.view.RoundImageView;

public class SetFragment extends Fragment {
    private static final String TAG = "设置界面";
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
        final View view = inflater.inflate(R.layout.fragment_set, container, false);
        Log.i(TAG, "onCreateView: ");
        tv = (TextView) view.findViewById(R.id.fragment_set);
        TextView about=view.findViewById(R.id.set_about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击关于，跳转到新的界面
                Intent intent = new Intent(view.getContext(), AboutActivity.class);
                startActivity(intent);
            }
        });

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
