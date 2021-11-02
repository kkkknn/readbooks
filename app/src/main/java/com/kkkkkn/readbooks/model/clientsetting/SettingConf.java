package com.kkkkkn.readbooks.model.clientsetting;

import android.content.Context;
import android.graphics.Color;

import java.io.Serializable;

public class SettingConf implements Serializable {
    //字体大小
    public float fontSize;
    //字体
    public String fontFamily;
    //字体颜色
    public int fontColor;
    //背景图样式,示例
    public int backgroundStyle;
    //当前亮度 为0 情况则使用系统亮度
    public int brightness;

}
