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

public class SearchFragment extends Fragment {
    private static final String TAG = "搜索页面" ;
    private TextView tv;

    public static SearchFragment newInstance(String name) {
        Log.i(TAG, "newInstance: ");
        Bundle args = new Bundle();
        args.putString("name", name);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
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
        tv = (TextView) view.findViewById(R.id.fragment_search);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.get("name").toString();
            tv.setText(name);
        }

    }
}
