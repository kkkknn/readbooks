package com.kkkkkn.readbooks.util.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kkkkkn.readbooks.R;

public class ViewUtil {

    public static void showToast(Context context,String str) {
        if(context==null){
            return;
        }

        View view= LayoutInflater.from(context).inflate(R.layout.view_toast_custom,null);
        TextView tv_msg = (TextView) view.findViewById(R.id.tvToast);
        tv_msg.setText(str);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 20);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();

    }
}
