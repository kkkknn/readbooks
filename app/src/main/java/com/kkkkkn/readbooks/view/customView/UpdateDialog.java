package com.kkkkkn.readbooks.view.customView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kkkkkn.readbooks.R;

public class UpdateDialog extends Dialog {
    private TextView tv_title,tv_info;
    private ProgressBar pb_download;
    private Button btn_ok ,btn_cancel;
    private OnClickBottomListener onClickBottomListener;

    public UpdateDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.update_dialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化view
        initView();
        //初始化事件
        initEvent();
    }


    //回调事件
    public interface OnClickBottomListener{
        public void onOkClick();
        public void onCancelClick();
    }

    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener != null) {
                    onClickBottomListener.onOkClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener != null) {
                    onClickBottomListener.onCancelClick();
                }
            }
        });

    }
    public UpdateDialog setOnClickListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }


    private void initView() {
        tv_title=findViewById(R.id.update_title);
        tv_info=findViewById(R.id.update_info);
        pb_download=findViewById(R.id.download_progress);
        btn_ok=findViewById(R.id.btn_ok);
        btn_cancel=findViewById(R.id.btn_cancel);
        pb_download.setMax(100);
    }

    public UpdateDialog setInfo(String title,String msg){
        tv_title.setText(title);
        tv_info.setText(msg);
        return this;
    }

    public void setProgressType(boolean flag){
        if(flag){
            pb_download.setVisibility(View.VISIBLE);
        }else{
            pb_download.setVisibility(View.GONE);
        }
    }

    public void setProgress( int count){
        pb_download.setProgress(count);
    }

}
