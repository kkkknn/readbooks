package com.kkkkkn.readbooks.view.customView;

import android.content.Context;
import android.print.PrintAttributes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.kkkkkn.readbooks.R;

public class CustomToast extends Toast {
    private static CustomToast customToast;

    public CustomToast(Context context) {
        super(context);
    }

    public static void showToast(Context context,CharSequence text,int time,int resID){
        cancelToast();

        customToast=new CustomToast(context);
        //创建一个填充物,用于填充Toast
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //填充物来自的xml文件,在这个改成一个view
        //实现xml到view的转变哦
        View view =inflater.inflate(R.layout.view_toast_custom,null);
        //不一定需要，找到xml里面的组件，设置组件里面的具体内容
        ImageView imageView=view.findViewById(R.id.ivToast);
        TextView textView=view.findViewById(R.id.tvToast);
        textView.setText(text);
        imageView.setImageResource(resID);
        customToast.setDuration(time);
        customToast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 50);
        customToast.setView(view);
        customToast.show();
    }

    public static void showToast(Context context,CharSequence text,int time){
        cancelToast();
        customToast=new CustomToast(context);
        //创建一个填充物,用于填充Toast
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //填充物来自的xml文件,在这个改成一个view
        //实现xml到view的转变哦
        View view =inflater.inflate(R.layout.view_toast_custom,null);

        //不一定需要，找到xml里面的组件，设置组件里面的具体内容
        AppCompatImageView imageView=view.findViewById(R.id.ivToast);
        AppCompatTextView textView=view.findViewById(R.id.tvToast);
        textView.setText(text);
        imageView.setVisibility(View.GONE);
        customToast.setDuration(time);
        customToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        customToast.setView(view);
        customToast.show();
    }


    public static void cancelToast(){
        if(customToast!=null){
            customToast.cancel();
        }
    }

}
