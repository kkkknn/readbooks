package com.kkkkkn.readbooks.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.kkkkkn.readbooks.R;

public class BackgroundRadioButton extends AppCompatRadioButton {
    private int color;
    private int resourceId;

    public BackgroundRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BackgroundRadioButton(Context context) {
        super(context);

    }
    public BackgroundRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setButtonDrawable(android.R.drawable.btn_radio);
        setGravity(Gravity.CENTER);
        setClickable(true);
        init(attrs);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void init(AttributeSet attrs) {
        if (attrs == null){
            color = 0x00000000;
            resourceId = 0;
            return;
        }
        Drawable drawable;
        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.BackgroundRadioButton);
        resourceId = typedArray.getResourceId(R.styleable.BackgroundRadioButton_imgLabel,0);
        if(resourceId!=0){
             drawable=getResources().getDrawable(resourceId, null);
        }else{
            //从xml中解析出color id
            color = typedArray.getColor(R.styleable.BackgroundRadioButton_colorLabel, 0x22917A7A);
            drawable = new ColorDrawable(color);
        }

        drawable.setBounds(0,0, 50, 50);//必须设置大小，否则drawable显示不出来

        setCompoundDrawables(drawable, null, null, null);//这里设置drawableleft

        typedArray.recycle();
    }
}
