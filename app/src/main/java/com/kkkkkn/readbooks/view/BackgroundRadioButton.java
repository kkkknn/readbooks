package com.kkkkkn.readbooks.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.kkkkkn.readbooks.R;

public class BackgroundRadioButton extends AppCompatRadioButton {
    private int color;

    public BackgroundRadioButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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

    private void init(AttributeSet attrs) {

        if (attrs == null){
            color = 0x00000000;
            return;
        }

        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.BackgroundRadioButton);
        //从xml中解析出color
        color = typedArray.getResourceId(R.styleable.BackgroundRadioButton_btnImg, 0x00000000);

        //我这里图方便，直接用了ColorDrawable，其实还有更强的拓展性
        ColorDrawable drawable = new ColorDrawable(color);
        setBackgroundColor(drawable.getColor());
        typedArray.recycle();
    }
}
