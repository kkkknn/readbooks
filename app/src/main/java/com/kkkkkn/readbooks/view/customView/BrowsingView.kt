package com.kkkkkn.readbooks.view.customView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.kkkkkn.readbooks.R
import com.kkkkkn.readbooks.view.activities.BookBrowsingActivity.BookCallback
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor

class BrowsingView : View {
    //行距
    private val rowSpace = 1.5f
    fun setBackgroundStyle(style: Int) {
        backgroundStyle = style
        text2bitmap(FlushType.FLUSH_PAGE)
    }

    enum class FlushType {
        THIS_PAGE, LAST_PAGE, NEXT_PAGE, FLUSH_PAGE
    }

    //当前划屏位置
    private var mClipX = 0f

    //左右滑动偏移量 变量
    private var offsetX = 0f

    //控件宽高
    private var mViewHeight = 0
    private var mViewWidth = 0

    //控件距离
    private var marginVertical:Float = 0f
    private var marginHorizontal:Float = 20f

    //当前章节字符串
    private var contentArr: ArrayList<String> =ArrayList<String>()

    //文字大小
    private var textSize = 40f

    //文字颜色
    private var textColor = Color.BLACK
    private var backgroundStyle = 0

    //每页最大显示行数
    private var linePageSum = 0

    //展示模式相关 true 左滑动绘制下一页  false 右滑动绘制上一页
    private var drawStyle = 0

    //绘图相关变量
    private var bookCallback: BookCallback? = null
    private var mPaint: Paint? = null
    private val parchmentBitmap = BitmapFactory.decodeResource(resources, R.drawable.browsingview)
        .copy(Bitmap.Config.ARGB_8888, true)
    private var bitmapLinkedList: LinkedList<Bitmap?> = LinkedList()
    private var bitmap_flag = 0

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    //初始化view
    private fun initView(context: Context) {
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.setShadowLayer(10f, 0f, 0f, Color.GRAY)
        mPaint!!.color = textColor
        mPaint!!.isAntiAlias = true
        mPaint!!.textSize = textSize

        post{
            //获取view宽高，然后绘制
            mViewWidth = measuredWidth
            mViewHeight = measuredHeight
            marginVertical = getStatusBarHeight(context)
            marginHorizontal= floor(((mViewWidth-(marginVertical*2))%textSize.toDouble())/2).toFloat()

            //计算偏移量及行数，每行字数
            linePageSum =
                ceil((mViewHeight - marginVertical *2 ) / textSize.toDouble() / rowSpace)
                    .toInt()
            if(contentArr.isNotEmpty()&&bitmapLinkedList.isEmpty()){
                text2bitmap(FlushType.THIS_PAGE)
            }

        }
    }

    fun setTextSize(textSize: Float) {
        mPaint!!.textSize = textSize
        this.textSize = textSize
        if (mViewWidth > 0 && mViewHeight > 0) {
            marginHorizontal=floor((mViewWidth-(marginVertical*2)%textSize.toDouble())/2).toFloat()
            linePageSum =
                ceil((mViewHeight - marginVertical *2) / textSize.toDouble() / rowSpace)
                    .toInt()
            text2bitmap(FlushType.FLUSH_PAGE)
        }
    }

    private fun text2bitmap(type: FlushType) {
        if (contentArr.isEmpty()) {
            return
        }
        val canvas = Canvas()
        //根据行数创建字符串数组 每页
        val line_list = LinkedList<String>()
        for (s in contentArr!!) {
            //压缩行首空格 4个为2个
            //s= StringUtil.Text2Indent(s);
            val ss = FloatArray(s.length)
            mPaint!!.getTextWidths(s, ss)
            var line_width = 0f
            var start = 0
            for (l in ss.indices) {
                line_width += ss[l]
                if (line_width >= mViewWidth - marginHorizontal * 2) {
                    line_list.add(s.substring(start, l))
                    start = l
                    line_width = ss[l]
                }
                //每行结尾拆分
                if (l == ss.size - 1 && line_width > 0) {
                    line_list.add(s.substring(start, ss.size))
                }
            }
        }
        bitmapClear(bitmapLinkedList)
        var line_count = 0
        var bitmap = drawBackground(canvas, backgroundStyle)
        val height = marginVertical + textSize
        for (i in line_list.indices) {
            val str = line_list[i]
            canvas.drawText(
                str,
                0,
                str.length,
                marginHorizontal.toFloat(),
                height + line_count * textSize * rowSpace,
                mPaint!!
            )
            line_count++
            if (line_count == linePageSum) {
                line_count = 0
                //绘制当前页所有文字，并添加到list中
                bitmapLinkedList.add(bitmap)
                bitmap = drawBackground(canvas, backgroundStyle)
            }
        }
        if (line_count > 0) {
            bitmapLinkedList.add(bitmap)
        }
        when (type) {
            FlushType.LAST_PAGE -> bitmap_flag = bitmapLinkedList.size - 1
            FlushType.THIS_PAGE, FlushType.NEXT_PAGE -> bitmap_flag = 0
            FlushType.FLUSH_PAGE -> {}
            else -> {}
        }

        Log.i(TAG, "text2bitmap: 不为空了 大小"+bitmapLinkedList.size)
        post { this.invalidate() }
    }

    private fun bitmapClear(bitmapLinkedList: LinkedList<Bitmap?>?) {
        var bitmapLinkedList = bitmapLinkedList
        for (bitmap in bitmapLinkedList!!) {
            if (!bitmap!!.isRecycled) {
                bitmap.recycle()
            } else {
                Log.i(TAG, "bitmapClear: 销毁失败")
            }
        }
        bitmapLinkedList.clear()
        bitmapLinkedList = null
    }

    private fun drawBackground(canvas: Canvas, backgroundStyle: Int): Bitmap? {
        var bitmap: Bitmap? = null
        when (backgroundStyle) {
            2 -> {
                bitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.ARGB_8888)
                canvas.setBitmap(bitmap)
                canvas.drawColor(Color.parseColor("#7B7070"))
            }
            3 -> {
                bitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.ARGB_8888)
                canvas.setBitmap(bitmap)
                canvas.drawColor(Color.parseColor("#3FAA98"))
            }
            4 -> {
                bitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.ARGB_8888)
                canvas.setBitmap(bitmap)
                canvas.drawColor(Color.parseColor("#B49D42"))
            }
            else -> {
                bitmap = Bitmap.createScaledBitmap(parchmentBitmap, mViewWidth, mViewHeight, false)
                canvas.setBitmap(bitmap)
            }
        }
        return bitmap
    }

    fun setTextColor(textColor: Int) {
        mPaint!!.color = textColor
        this.textColor = textColor
    }

    fun setTextContent(content: Array<String>, type: FlushType) {
        Log.i(TAG, "setTextContent: 11  "+contentArr.toList().toString())
        Log.i(TAG, "setTextContent: 22  "+content.toList().toString())
        contentArr.clear()
        contentArr.addAll(content)
        //根据章节重新设置3个页面的进度
        mClipX = -1f
        offsetX = 0f
        drawStyle = 0
        if (mViewWidth > 0 && mViewHeight > 0) {
            //处理章节函数，章节转换为bitmap
            //根据设置的章节方向，决定渲染图添加到前页还是后页
            text2bitmap(type)
        }
    }


    //手势判断
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> mClipX = event.x
            MotionEvent.ACTION_UP -> {
                //判断是否需要变化当前页
                //首先判断滑动距离是否超出宽度1/6
                if (Math.abs(offsetX) > mViewWidth / 6f) {
                    if (drawStyle == 1) {
                        //锚点赋值
                        if (bitmap_flag + 1 < bitmapLinkedList!!.size) {
                            bitmap_flag++
                        } else if (bookCallback != null) {
                            //通知activity跳转下一章节
                            bookCallback!!.jump2nextChapter()
                        }
                    } else if (drawStyle == 2) {
                        if (bitmap_flag > 0) {
                            bitmap_flag--
                        } else if (bookCallback != null) {
                            //通知activity跳转上一章节
                            bookCallback!!.jump2lastChapter()
                        }
                    }
                } else {
                    if (event.x >= mViewWidth / 3f * 2) {
                        Log.i(TAG, "onTouchEvent: 点击1")
                        /*showSlide(false,(int) event.getX());
                        return true;*/
                    } else if (event.x <= mViewWidth / 3f) {
                        Log.i(TAG, "onTouchEvent: 点击2")
                        /*showSlide(true,(int)event.getX());
                        return true;*/
                    } else {
                        //落点在中央，显示阅读设置view
                        bookCallback?.showSetting()
                    }
                }
                mClipX = -1f
                offsetX = 0f
                drawStyle = 0
                performClick()
            }
            MotionEvent.ACTION_MOVE -> {
                //判断向左还是向右滑动  1左  2右
                val x = event.x
                if (x >=0 && x < mViewWidth) {
                    offsetX += x - mClipX
                    if (drawStyle == 0) {
                        if (offsetX < 0) {
                            drawStyle = 1
                        } else if (offsetX > 0) {
                            drawStyle = 2
                        }
                    }
                    //offsetX = x;
                    mClipX = x
                }
            }
        }
        //重新绘制界面
        invalidate()
        return true
    }

    private fun showSlide(type: Boolean, x: Int) {
        if (type) {
            //左滑动
            drawStyle = 1
            while (offsetX > -mViewWidth) {
                offsetX--
                //重新绘制界面
                invalidate()
            }
        } else {
            //右滑动
            drawStyle = 2
            while (offsetX < mViewWidth) {
                offsetX++
                //重新绘制界面
                invalidate()
            }
        }
        drawStyle = 0
    }

    //绘制阅读界面 2个页面  1，当前页面  2，根据当前手势判断绘制上一页/下一页
    private fun drawBitmap(canvas: Canvas) {
        //判断是否需要绘制
        if (bitmapLinkedList.isEmpty()) {
            Log.i(
                TAG,
                "drawBitmap: 超出长度或没有文字内容，忽略绘制" + (contentArr == null) + "||" + bitmapLinkedList.size
            )
            return
        }
        canvas.save()
        //绘制偏移量 X Y坐标
        val draw_offsetX = 0f
        val draw_offsetY = 0f
        canvas.translate(draw_offsetX, draw_offsetY)
        when (drawStyle) {
            1 ->                 //判断是否需要绘制下一页面
                if (bitmap_flag + 1 == bitmapLinkedList.size || offsetX > 0) {
                    //绘制当前页面
                    canvas.drawBitmap(bitmapLinkedList!![bitmap_flag]!!, 0f, 0f, mPaint)
                } else {
                    //定义绘制区间 offsetX此处是负数，所以直接相加即可
                    val rect = Rect((mViewWidth + offsetX).toInt(), 0, mViewWidth, mViewHeight)
                    //绘制下一页面
                    canvas.drawBitmap(bitmapLinkedList!![bitmap_flag + 1]!!, rect, rect, mPaint)
                    //绘制当前页面
                    canvas.drawBitmap(bitmapLinkedList[bitmap_flag]!!, offsetX, 0f, mPaint)
                }
            2 ->                 //判断是否绘制上一页面
                if (bitmap_flag > 0) {
                    //定义绘制区间
                    val rect = Rect(offsetX.toInt(), 0, mViewWidth, mViewHeight)
                    //绘制当前页面
                    canvas.drawBitmap(bitmapLinkedList!![bitmap_flag]!!, rect, rect, mPaint)

                    //绘制上一页面
                    canvas.drawBitmap(
                        bitmapLinkedList[bitmap_flag - 1]!!,
                        offsetX - mViewWidth,
                        0f,
                        mPaint
                    )
                } else {
                    canvas.drawBitmap(bitmapLinkedList[bitmap_flag]!!, 0f, 0f, mPaint)
                }
            else -> if (bitmapLinkedList.size > 0 && bitmapLinkedList.size > bitmap_flag) {
                //绘制当前页面
                canvas.drawBitmap(bitmapLinkedList[bitmap_flag]!!, 0f, 0f, mPaint)
            }
        }
        canvas.restore()
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        mViewHeight = h
        mViewWidth = w
        super.onSizeChanged(w, h, oldW, oldH)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBitmap(canvas)
    }

    private fun getStatusBarHeight(context: Context): Float {
        val statusBarHeight = ceil((25 * context.resources.displayMetrics.density).toDouble())

        return statusBarHeight.toFloat()
    }

    fun setListener(callback: BookCallback) {
        bookCallback = callback
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        //销毁view视图时，销毁所有bitmap
        bitmapClear(bitmapLinkedList)
    }

    companion object {
        private const val TAG = "BrowsingVIew"
    }
}