package com.kkkkkn.readbooks.view.customView

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import com.kkkkkn.readbooks.R

class BackgroundRadioButton : AppCompatRadioButton {
    private var color = 0
    private var resourceId = 0

    constructor(context: Context?) : super(context) {}

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        //setButtonDrawable(android.R.drawable.btn_radio);
        //setGravity(Gravity.CENTER);
        isClickable = true
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs == null) {
            color = 0x00000000
            resourceId = 0
            return
        }
        val drawable: Drawable
        val typedArray = resources.obtainAttributes(attrs, R.styleable.BackgroundRadioButton)
        resourceId = typedArray.getResourceId(R.styleable.BackgroundRadioButton_imgLabel, 0)
        if (resourceId != 0) {
            drawable = resources.getDrawable(resourceId, null)
        } else {
            //从xml中解析出color id
            color = typedArray.getColor(R.styleable.BackgroundRadioButton_colorLabel, 0x22917A7A)
            drawable = ColorDrawable(color)
        }
        drawable.setBounds(0, 0, 50, 50) //必须设置大小，否则drawable显示不出来
        setCompoundDrawables(drawable, null, null, null) //这里设置drawableleft
        typedArray.recycle()
    }
}