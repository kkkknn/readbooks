package com.kkkkkn.readbooks.view.customView

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.kkkkkn.readbooks.R
import com.kkkkkn.readbooks.databinding.SettingDialogBinding
import com.kkkkkn.readbooks.model.clientsetting.SettingConf
import java.util.*
import kotlin.math.roundToInt

class SettingDialog(context: Context) : Dialog(context) {
    private lateinit var viewBinding: SettingDialogBinding
    private var fontSize = 0
    private var lightCount = 0
    private var eventListener: EventListener? = null

    private val onClickListener =
        View.OnClickListener { view ->
            val id = view.id
            //获取当前大小
            fontSize = viewBinding.fontSize.text.toString().toInt()
            if (id == R.id.add_fontSize) {
                if (fontSize + 10 <= max_fontSize) {
                    fontSize += 10
                    eventListener!!.changeFontSize(fontSize.toFloat())
                    viewBinding.fontSize.text = String.format("%s", fontSize)
                }
            } else if (id == R.id.subtract_fontSize) {
                if (fontSize - 10 >= min_fontSize) {
                    fontSize -= 10
                    eventListener!!.changeFontSize(fontSize.toFloat())
                    viewBinding.fontSize.text = String.format("%s", fontSize)
                }
            } else if (id == R.id.setting_lightSystem_textView) {
                val count = eventListener!!.resetSystemLight()
                eventListener!!.changeLight(count)
                viewBinding.settingLightSeekBar.progress = count
            }
        }
    private val onCheckedChangeListener =
        RadioGroup.OnCheckedChangeListener { radioGroup, _ ->
            when (radioGroup.checkedRadioButtonId) {
                R.id.setting_radio_retro -> {
                    eventListener!!.changeBackground(1)
                }
                R.id.setting_radio_gray -> {
                    eventListener!!.changeBackground(2)
                }
                R.id.setting_radio_green -> {
                    eventListener!!.changeBackground(3)
                }
                R.id.setting_radio_yellow -> {
                    eventListener!!.changeBackground(4)
                }
            }
        }
    private val onSeekBarChangeListener: OnSeekBarChangeListener =
        object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                lightCount = i
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                eventListener!!.changeLight(lightCount)
            }
        }

    interface EventListener {
        fun changeFontSize(size: Float)
        fun changeBackground(style: Int)
        fun changeLight(count: Int)
        fun resetSystemLight(): Int
    }

    init {
        viewBinding = SettingDialogBinding.inflate( LayoutInflater.from(context),null,false )
        this.setContentView(viewBinding.root)
        //初始化控件
        initView()
    }

    fun setEventListener(listener: EventListener?): SettingDialog {
        eventListener = listener
        return this
    }

    override fun show() {
        super.show()
        val layoutParams = window?.attributes
        if (layoutParams != null) {
            layoutParams.gravity = Gravity.BOTTOM
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        }
        window?.attributes=layoutParams
        window?.setBackgroundDrawable(null)
    }

    //初始化view
    private fun initView() {
        viewBinding.settingLightSeekBar.max = 10
        viewBinding.settingLightSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener)
        viewBinding.addFontSize.setOnClickListener(onClickListener)
        viewBinding.subtractFontSize.setOnClickListener(onClickListener)
        viewBinding.settingRadioGroup.setOnCheckedChangeListener(onCheckedChangeListener)
        viewBinding.settingLightSystemTextView.setOnClickListener(onClickListener)


/*
        viewBinding.root.gravity=Gravity.BOTTOM
        viewBinding.root.updateLayoutParams {
            this.height=WindowManager.LayoutParams.WRAP_CONTENT
            this.width=WindowManager.LayoutParams.MATCH_PARENT
        }
        viewBinding.root.background = null*/

        //设置弹出属性动画
        val animation: Animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        )
        animation.duration = 300
        animation.fillAfter = true
        animation.isFillEnabled = true
        viewBinding.root.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    fun setConfData(settingConf: SettingConf?) {
        if (settingConf == null) {
            return
        }
        //填充数据
        viewBinding.settingLightSeekBar.progress = settingConf.brightness
        viewBinding.fontSize.text =
            String.format(Locale.CHINA, "%s", settingConf.fontSize.roundToInt())
        when (settingConf.backgroundStyle) {
            1 -> viewBinding.settingRadioRetro.isChecked = true
            2 -> viewBinding.settingRadioGray.isChecked = true
            3 -> viewBinding.settingRadioGreen.isChecked = true
            4 -> viewBinding.settingRadioYellow.isChecked = true
        }
    }

    companion object {
        private const val max_fontSize = 90
        private const val min_fontSize = 10
    }
}