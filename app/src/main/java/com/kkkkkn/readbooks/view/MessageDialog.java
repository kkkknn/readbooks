package com.kkkkkn.readbooks.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.kkkkkn.readbooks.R;

public  class MessageDialog  {
    private final static String TAG = "MessageDialog";



    /**
     * 弹窗工具类，
     *
     * @param context   context对象
     * @param title   弹窗窗口标题
     * @param message   弹窗消息
     * @param onClickListener_positive 按钮监听
     * @param onClickListener_neutral   按钮监听
     */
    public static void showDialog(Context context, String title, String message, DialogInterface.OnClickListener onClickListener_positive, DialogInterface.OnClickListener onClickListener_neutral){
        if(context==null){
            Log.e(TAG, "showDialog: context 参数为空!,函数退出");
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);
        if(onClickListener_positive!=null){
            builder.setPositiveButton(R.string.ok_btn,onClickListener_positive);
        }
        if(onClickListener_neutral!=null){
            builder.setNeutralButton(R.string.cancel_btn, onClickListener_neutral);
        }
        AlertDialog dialog=builder.create();
        dialog.show();
    }


}
