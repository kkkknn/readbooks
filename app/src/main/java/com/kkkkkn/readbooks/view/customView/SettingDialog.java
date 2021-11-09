package com.kkkkkn.readbooks.view.customView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.kkkkkn.readbooks.R;

import java.util.Objects;

public class SettingDialog extends Dialog {
    //控件
    private TextView add_sizeTextView,subtract_sizeTextView,font_sizeTextView;
    private SeekBar light_seekBar;

    private int font_size;
    private int light_count;
    private final static int max_fontSize=80;
    private final static int min_fontSize=10;
    private EventListener eventListener;
    private final static String TAG="SettingDialog";

    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            //获取当前大小
            font_size=Integer.parseInt(font_sizeTextView.getText().toString());
            if(id==R.id.add_fontSize){
                if((font_size+10)<=max_fontSize){
                    font_size=font_size+10;
                    eventListener.changeFontSize(font_size);
                    font_sizeTextView.setText(Integer.toString(font_size));
                }
            }else if(id==R.id.subtract_fontSize){
                if((font_size-10)>=min_fontSize){
                    font_size=font_size-10;
                    eventListener.changeFontSize(font_size);
                    font_sizeTextView.setText(Integer.toString(font_size));
                }
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            int id = radioGroup.getCheckedRadioButtonId();
            if(id==R.id.setting_radio_retro){
                eventListener.changeBackground(1);
            }else if(id==R.id.setting_radio_gray){
                eventListener.changeBackground(2);
            }else if(id==R.id.setting_radio_green){
                eventListener.changeBackground(3);
            }else if(id==R.id.setting_radio_yellow){
                eventListener.changeBackground(4);
            }

        }
    };

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener=new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            light_count=20*i;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            eventListener.changeLight(light_count);
        }
    };

    public interface EventListener{
        void changeFontSize(float size);
        void changeBackground(int style);
        void changeLight(int count);
        void getSystemLight();
    }

    public SettingDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.setting_dialog);
        initView();
    }

    public SettingDialog setEventListener(EventListener listener){
        this.eventListener=listener;
        return this;
    }


    //初始化view
    private void initView(){
        Window window=getWindow();
        if(window==null){
            return;
        }
        //获取相应的组件并添加相应的事件监听
        light_seekBar=window.findViewById(R.id.setting_lightSeekBar);
        light_seekBar.setMax(10);
        light_seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        add_sizeTextView=window.findViewById(R.id.add_fontSize);
        font_sizeTextView=window.findViewById(R.id.fontSize);
        subtract_sizeTextView=window.findViewById(R.id.subtract_fontSize);
        add_sizeTextView.setOnClickListener(onClickListener);
        subtract_sizeTextView.setOnClickListener(onClickListener);
        RadioGroup radioGroup=window.findViewById(R.id.setting_radioGroup);
        radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);


        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        window.setBackgroundDrawable(null);

        //设置弹出属性动画
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(300);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        View view=window.findViewById(R.id.setting_dialog);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }


}
