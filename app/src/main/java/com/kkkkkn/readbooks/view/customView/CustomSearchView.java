package com.kkkkkn.readbooks.view.customView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import com.kkkkkn.readbooks.R;

public class CustomSearchView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "CustomSearchView";
    private boolean isEnable=true;
    //输入框
    private AppCompatEditText etInput;
    //删除键
    private AppCompatImageView ivMenu;
    private Context mContext;
    private final static int BAN_STATE=-2,FOCUS_STATE=1,NO_FOCUS_STATE=-1;
    //搜索回调接口
    private SearchViewListener mListener;
    //设置搜索回调接口
    public void setSearchViewListener(SearchViewListener listener) {
        mListener = listener;
    }

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.custom_searchview_layout, this);
        initViews();
    }

    public void setEnable(boolean flag){
        isEnable=flag;
        if(!isEnable&&etInput!=null){
            etInput.setEnabled(false);
            /*etInput.setFocusable(false);
            etInput.setFocusableInTouchMode(false);*/
        }
    }

    private void initViews() {
        etInput =  findViewById(R.id.search_et_input);
        ivMenu = findViewById(R.id.search_iv_menu);
        ivMenu.setOnClickListener(this);
        etInput.addTextChangedListener((TextWatcher) new EditChangedListener());
        etInput.setOnClickListener(this);
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    notifyStartSearching(etInput.getText().toString());
                }
                return true;
            }
        });


    }

    public void onKeyBoardState(boolean isShow){
        Drawable drawable;
        if(isShow){
            drawable=AppCompatResources.getDrawable(mContext,R.drawable.ic_close);
        }else {
            etInput.clearFocus();
            drawable=AppCompatResources.getDrawable(mContext,R.drawable.ic_scanning);
        }
        ivMenu.setImageDrawable(drawable);
        ivMenu.setVisibility(VISIBLE);
        Log.i(TAG, "onKeyBoardState: "+isShow);
    }

    //通知监听者 进行搜索操作
    private void notifyStartSearching(String text){
        if (mListener != null) {
            mListener.onSearch(etInput.getText().toString());
        }
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //清除焦点
        etInput.clearFocus();
    }



    private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!"".equals(charSequence.toString())) {
                ivMenu.setVisibility(VISIBLE);
                //更新autoComplete数据
                if (mListener != null) {
                    mListener.onRefreshAutoComplete(charSequence + "");
                }
            } else {
                ivMenu.setVisibility(GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    @Override
    public void onClick(View view) {
        int state = getEditViewState();
        if(state==BAN_STATE){
            return;
        }
        int id=view.getId();

        if(id==R.id.search_et_input){
            Log.i("TAG", "onClick: search_et_input");
        }else if(id==R.id.search_iv_menu){
            if(state==NO_FOCUS_STATE){
                if(!(mListener==null)){
                    mListener.onScancode();
                    Log.i(TAG, "onClick: onScancode");
                }
            }else if(state==FOCUS_STATE){
                etInput.setText("");

                ivMenu.setVisibility(GONE);
            }
        }
    }


    private int getEditViewState(){
        if(!isEnable){
            return BAN_STATE;
        }
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()){
            return FOCUS_STATE;
        }else {
            return NO_FOCUS_STATE;
        }
    }

    //search view回调方法
    public interface SearchViewListener {
        //更新自动补全内容
        void onRefreshAutoComplete(String text);

        //开始搜索
        void onSearch(String text);

        //打开扫码
        void onScancode();
    }


}