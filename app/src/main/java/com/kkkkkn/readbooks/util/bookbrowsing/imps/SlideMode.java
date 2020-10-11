package com.kkkkkn.readbooks.util.bookbrowsing.imps;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.kkkkkn.readbooks.util.bookbrowsing.BrowsingViewInfo;
import com.kkkkkn.readbooks.util.bookbrowsing.BrowsingViewInterface;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SlideMode implements BrowsingViewInterface {
    private BrowsingViewInfo info;
    public SlideMode(BrowsingViewInfo info) {
        this.info=info;
    }

    //绘制当前页面
    @Override
    public void drawThisPage(Canvas canvas,char[] textArr,int offset,int style) {
        //无文字情况直接退出
        if (info == null) {
            Log.i(TAG, "info 对象为空: 直接退出");
            return;
        }
        //计算要绘制文字数量
        int textPageSum = info.getLinePageSum() * info.getTextLineSum();
        int drawLineNum = info.getLinePageSum();
        //绘制偏移量
        /*int drawOffset=0;
        //判断是否需要裁切绘制
        canvas.save();
        if ((textContentCount + textPageSum) > textSum) {
            textPageSum = textSum - textPageSum;
            drawLineNum = textPageSum % textLineSum == 0 ? textPageSum / textLineSum : textPageSum / textLineSum + 1;
        }

        if(style==1){
            canvas.clipRect(0, 0, offset, info.si);
            Log.i(TAG, "center_drawBitmap: 左滑动");
            drawOffset=-(int)(mViewWidth-mClipX);
        }else if(style==2){
            canvas.clipRect(offset, 0, mViewWidth, mViewHeight);
            Log.i(TAG, "center_drawBitmap: 右滑动");
            drawOffset=(int)mClipX;
        }
        canvas.drawBitmap(info.getBackgroundBitmap(), 0, 0, info.getmPaint());
        //绘制文字
        for (int i = 0; i < drawLineNum; i++) {
            canvas.drawText(textContent, textContentCount+textLineSum*i, textLineSum, drawOffset, (float) (textSize * i + statusBarHeight), mTextPaint);
        }*/
        canvas.restore();
    }

    @Override
    public void drawUpPage(Canvas canvas,char[] textArr,int offset,int style) {

    }

    @Override
    public void drawDownPage(Canvas canvas,char[] textArr) {

    }

    @Override
    public void initDrawSet(BrowsingViewInfo info) {

    }
}
