package com.kkkkkn.readbooks.view.customView;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.kkkkkn.readbooks.R;

public class LoadingDialog extends Dialog {
    private AppCompatTextView tv_msg;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.loading_dialog);
        setCanceledOnTouchOutside(false);
        tv_msg = findViewById(R.id.loading_msg);
    }

    public void showLoading(String content) {
        super.show();
        tv_msg.setText(content);
    }
    public void hideLoading(){
        super.dismiss();
    }

}
