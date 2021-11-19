package com.kkkkkn.readbooks.view.customView;

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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.view.activities.BookBrowsingActivity;

import java.util.ArrayList;
import java.util.LinkedList;


public class BrowsingVIew extends View {
    //行距
    private float rowSpace=1.5f;
    public enum FlushType{
        THIS_PAGE,
        LAST_PAGE,
        NEXT_PAGE,
        FLUSH_PAGE;
    }
    private final static String TAG="BrowsingVIew";
    //当前划屏位置
    private float mClipX = 0;
    //左右滑动偏移量 变量
    private float offsetX=0;
    //绘制偏移量 X Y坐标
    private float draw_offsetX=0;
    private float draw_offsetY=0;
    //控件宽高
    private int mViewHeight = 0, mViewWidth = 0;
    //当前章节字符串
    private String[] contentArr;
    //文字大小
    private float textSize = 40f;
    //文字颜色
    private int textColor = Color.BLACK;
    //每页最大显示行数
    private int linePageSum;
    //展示模式相关 true 左滑动绘制下一页  false 右滑动绘制上一页
    private int drawStyle = 0;
    //状态栏高度
    private int statusBarHeight;
    //绘图相关变量
    private BookBrowsingActivity.BookCallback bookCallback;

    private Paint mPaint;
    private Bitmap backBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.browsingview).copy(Bitmap.Config.ARGB_8888, true);
    private LinkedList<Bitmap> bitmapLinkedList=new LinkedList<>();
    private int bitmap_flag=0;


    public BrowsingVIew(Context context) {
        super(context);
        initView(context);
    }

    public BrowsingVIew(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BrowsingVIew(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

    }

    public void setBackGroundStyle(int style){
        Canvas canvas;
        switch (style){
            case 1:
                backBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.browsingview).copy(Bitmap.Config.ARGB_8888, true);
                break;
            case 2:
                canvas=new Canvas(backBitmap);
                canvas.drawColor(Color.parseColor("#7B7070"));
                break;
            case 3:
                canvas=new Canvas(backBitmap);
                canvas.drawColor(Color.parseColor("#3FAA98"));
                break;
            case 4:
                canvas=new Canvas(backBitmap);
                canvas.drawColor(Color.parseColor("#B49D42"));
                break;
            default:
                backBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.browsingview);
                break;
        }
        text2bitmap(FlushType.FLUSH_PAGE);
    }

    //初始化view
    private void initView(final Context context) {
        mPaint = new Paint();

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setShadowLayer(10f, 0, 0, Color.GRAY);
        mPaint.setColor(textColor);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);

        this.post(new Runnable() {
            @Override
            public void run() {
                //获取view宽高，然后绘制
                mViewWidth = getMeasuredWidth();
                mViewHeight = getMeasuredHeight();
                statusBarHeight = getStatusBarHeight(context);

                //textSize=(float) mViewWidth/12;

                //计算偏移量及行数，每行字数
                linePageSum = (int) Math.ceil((mViewHeight - statusBarHeight-((int)textSize>>1)) / (double)textSize/rowSpace);

                if(contentArr!=null&&bitmapLinkedList.size()==0){
                    text2bitmap(FlushType.THIS_PAGE);
                }
            }
        });
    }

    public void setTextSize(float textSize) {
        mPaint.setTextSize(textSize);
        this.textSize = textSize;
        if(mViewWidth>0&&mViewHeight>0){
            linePageSum = (int) Math.ceil((mViewHeight - statusBarHeight -((int)textSize>>1)) / (double)textSize/rowSpace);
            text2bitmap(FlushType.THIS_PAGE);
        }

    }

    private void text2bitmap(FlushType type){
        if(contentArr==null||contentArr.length==0){
            return;
        }
        Canvas canvas=new Canvas();
        //根据行数创建字符串数组 每页
        LinkedList<String> line_list=new LinkedList<>();
        for (int i = 0; i < contentArr.length; i++) {
            float[] ss=new float[contentArr[i].length()];
            mPaint.getTextWidths(contentArr[i],ss);
            float line_width=0;
            int start=0;
            for (int l = 0; l < ss.length; l++) {
                line_width+=ss[l];
                if(line_width>=mViewWidth){
                    line_list.add(contentArr[i].substring(start,l));
                    start=l;
                    line_width=ss[l];
                }
                //每行结尾拆分
                if((l==(ss.length-1))&&line_width>0){
                    line_list.add(contentArr[i].substring(start,ss.length));
                }
            }

        }
        bitmapLinkedList.clear();
        canvas.translate(0,statusBarHeight+ ((int) textSize >> 1));
        int line_count=0;
        Bitmap bitmap=Bitmap.createScaledBitmap(backBitmap, mViewWidth, mViewHeight, false);
        canvas.setBitmap(bitmap);
        for (int i = 0; i < line_list.size(); i++) {
            String str=line_list.get(i);
            canvas.drawText(str,0,str.length(),0,line_count*textSize*rowSpace,mPaint);
            line_count++;
            if(line_count==linePageSum){
                line_count=0;
                //绘制当前页所有文字，并添加到list中
                bitmapLinkedList.add(bitmap);
                bitmap=Bitmap.createScaledBitmap(backBitmap, mViewWidth, mViewHeight, false);
                canvas.setBitmap(bitmap);
            }
        }
        if(line_count>0){
            bitmapLinkedList.add(bitmap);
        }
        switch (type){
            case LAST_PAGE:
                bitmap_flag=bitmapLinkedList.size()-1;
                break;
            case THIS_PAGE:
            case NEXT_PAGE:
                bitmap_flag=0;
                break;
            case FLUSH_PAGE:
            default:
                break;
        }

        this.post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    public void setTextColor(int textColor) {
        mPaint.setColor(textColor);
        this.textColor = textColor;
    }


    public void setTextContent(String[] content,FlushType type) {
        this.contentArr=content;
        //根据章节重新设置3个页面的进度
        mClipX = -1;
        offsetX=0;
        drawStyle=0;
        if(mViewWidth>0&&mViewHeight>0){
            //处理章节函数，章节转换为bitmap
            //根据设置的章节方向，决定渲染图添加到前页还是后页
            text2bitmap(type);
        }
    }



    @Override
    public boolean performClick() {
        return super.performClick();
    }


    //手势判断
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mClipX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                //判断是否需要变化当前页
                //首先判断滑动距离是否超出宽度1/6
                if(Math.abs(offsetX) > mViewWidth / 6){
                    if(drawStyle==1){
                        //锚点赋值
                        if((bitmap_flag+1)<bitmapLinkedList.size()){
                            bitmap_flag++;
                        }else if(bookCallback!=null){
                            //通知activity跳转下一章节
                            bookCallback.jump2nextChapter();
                        }
                    }else if(drawStyle==2){
                        if(bitmap_flag>0){
                            bitmap_flag--;
                        }else if(bookCallback!=null){
                            //通知activity跳转上一章节
                            bookCallback.jump2lastChapter();
                        }
                    }
                }else {
                    if(event.getX()>=mViewWidth/3*2){
                        showSlide(false,(int) event.getX());
                        Toast.makeText(getContext(),"下一页面",Toast.LENGTH_SHORT).show();
                    }else if(event.getX()<=mViewWidth/3){
                        showSlide(true,(int)event.getX());
                        Toast.makeText(getContext(),"上一页面",Toast.LENGTH_SHORT).show();
                    }else {
                        //落点在中央，显示阅读设置view
                        bookCallback.showSetting();
                    }
                }

                mClipX = -1;
                offsetX=0;
                drawStyle=0;
                performClick();
                break;
            case MotionEvent.ACTION_MOVE:
                //判断向左还是向右滑动  1左  2右
                float x = event.getX();
                if(x<0||x>mViewWidth){
                    break;
                }
                offsetX += x-mClipX;
                if(drawStyle == 0){
                    if(offsetX< 0){
                        drawStyle = 1;
                    }else if(offsetX > 0){
                        drawStyle = 2;
                        //计算初始锚点
                        //offsetX=-(mViewWidth-x);
                    }
                }
                //offsetX = x;
                mClipX = x;
                break;
        }
        //重新绘制界面
        invalidate();
        return true;
    }

    private void showSlide(boolean type,int x){
        if(type){
            //左滑动
            while (offsetX>-mViewWidth){
                offsetX--;
                //重新绘制界面
                invalidate();
            }
        }else{
            //右滑动
            while (offsetX<mViewWidth){
                offsetX++;
                //重新绘制界面
                invalidate();
            }
        }
    }
    //绘制阅读界面 2个页面  1，当前页面  2，根据当前手势判断绘制上一页/下一页
    private void drawBitmap(Canvas canvas) {
        //判断是否需要绘制
        if(contentArr==null||bitmapLinkedList.size()==0){
            Log.i(TAG, "drawBitmap: 超出长度或没有文字内容，忽略绘制"+Boolean.toString(contentArr==null)+"||"+bitmapLinkedList.size());
            return;
        }
        canvas.save();
        canvas.translate(draw_offsetX, draw_offsetY);
        //根据drawstyle 决定绘制左边还是右边
        switch (drawStyle) {
            case 1:
                //判断是否需要绘制下一页面
                if((bitmap_flag+1)==bitmapLinkedList.size()||offsetX>0){
                    //绘制当前页面
                    canvas.drawBitmap(bitmapLinkedList.get(bitmap_flag), 0, 0, mPaint);
                }else {
                    //绘制下一页面
                    canvas.drawBitmap(bitmapLinkedList.get(bitmap_flag+1), 0, 0, mPaint);
                    //绘制当前页面
                    canvas.drawBitmap(bitmapLinkedList.get(bitmap_flag), offsetX, 0, mPaint);
                }
                break;

            case 2:
                //判断是否绘制上一页面
                if(bitmap_flag>0){
                    //绘制当前页面
                    canvas.drawBitmap(bitmapLinkedList.get(bitmap_flag), 0, 0, mPaint);
                    //绘制上一页面
                    canvas.drawBitmap(bitmapLinkedList.get(bitmap_flag-1), offsetX-mViewWidth, 0, mPaint);
                }else{
                    canvas.drawBitmap(bitmapLinkedList.get(bitmap_flag), 0, 0, mPaint);
                }
                break;
            default:
                if(bitmapLinkedList!=null&&bitmapLinkedList.size()>0){
                    //绘制当前页面
                    canvas.drawBitmap(bitmapLinkedList.get(bitmap_flag), 0, 0, mPaint);

                }

                break;
        }
        canvas.restore();
    }




    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewHeight = h;
        mViewWidth = w;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBitmap(canvas);

    }

    private int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    public void setListener(BookBrowsingActivity.BookCallback callback){
        bookCallback=callback;
    }

}
