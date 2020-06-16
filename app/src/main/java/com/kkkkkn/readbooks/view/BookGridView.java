package com.kkkkkn.readbooks.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.kkkkkn.readbooks.R;

public class BookGridView extends GridView {
    Drawable mInterlayer = this.getResources().getDrawable(R.drawable.bookshelf);//书架图片
    Rect mMyDrawRect = new Rect();//书架的矩形位置
    public BookGridView(Context context) {
        super(context);
    }

    public BookGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BookGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int count = getChildCount();
        Log.i("TAG", "dispatchDraw: "+count);
        if (count > 0) {  //当有内容时
            View v = getChildAt(0);//获取屏幕的第一个可见的View

            if (v != null) {
                int gridview_height = this.getHeight();
                int interlayerHeight = mInterlayer.getIntrinsicHeight();
                int blockGapHeight = v.getHeight();
                mMyDrawRect.left = 0;
                mMyDrawRect.right = getWidth();
                int initPos = v.getTop();

                for (int i = initPos; i <= gridview_height; i += blockGapHeight) {
                    mMyDrawRect.top = i;
                    mMyDrawRect.bottom = (mMyDrawRect.top + blockGapHeight);
                    mInterlayer.setBounds(mMyDrawRect);
                    mInterlayer.draw(canvas);//画书架图片
                }
            }

            super.dispatchDraw(canvas);//画每一个Item
        }
    }

}
