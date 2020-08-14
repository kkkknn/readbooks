package com.kkkkkn.readbooks.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import com.kkkkkn.readbooks.R;

import java.util.ArrayList;

public class BrowsingVIew extends View {
    private Scroller scroller;
    private ArrayList<Bitmap> bitmapArrayList=new ArrayList<>();
    public BrowsingVIew(Context context) {
        super(context);
        scroller=new Scroller(context);
    }

    public BrowsingVIew(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        scroller=new Scroller(context);
    }

    public BrowsingVIew(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller=new Scroller(context);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        /*StaticLayout layoutopen = new StaticLayout(str,  point,  getWidth() , Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        //这里的参数300，表示字符串的长度，当满300时，就会换行，也可以使用“\r\n”来实现换行
        canvas.save();
        canvas.translate(0,50);
        layoutopen.draw(canvas);
        canvas.restore();*/
        super.onDraw(canvas);
        testDraw(canvas);
    }


    //章节生成bitmap并保存到对象中
    public boolean chapter2Bitmap(String chapterContent){
        return false;
    }

    private void testDraw(Canvas canvas){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.browsingview);
        Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, getWidth(), getHeight(), true);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawBitmap(bitmap2, 0, 0, paint);
        TextPaint point = new TextPaint();
        point.setTextSize(40f);
        point.setColor(Color.BLACK);
        point.setAntiAlias(true);
        int height = getStatusBarHeight(getContext());
        String str = "生命中的痛苦就像是盐的咸味一样，就这么多。而我们所能感受和体验的程度，取决于我们将它放在多大的容器里。你的心量越小，烦恼就多了，心量大了，能承受的就多了，生活就不那么苦了。用敞亮的心去看待世界，世界就是闪闪发光的，相反，用阴暗的心去看待世界，世界就永无发光之日。";
        //将字体大小向下取整
        int size = (int) point.getTextSize();
        char[] arr = str.toCharArray();
        int num = getWidth() / size;
        //行数需要向上取整， 不能强制转换
        int line = arr.length / num == 0 ? arr.length / num : arr.length / num + 1;
        for (int i = 0; i < line; i++) {
            int count = arr.length - i * num;
            canvas.drawText(arr, i * num, Math.min(count, num), 0, size * i + height, point);
        }
        Log.i("TAG", "onDraw: " + num + "||arr" + arr.length + "||line" + line);
    }

    private int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

}
