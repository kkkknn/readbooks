package com.kkkkkn.readbooks.model.clientsetting;

import android.content.Context;
import android.graphics.Color;

import java.io.Serializable;

/**
 * 系统配置信息，有默认值
 */
public class SettingConf implements Serializable {
    //字体大小
    public float fontSize=40f;
    //字体颜色
    public int fontColor=Color.BLACK;
    //背景图样式,示例
    public int backgroundStyle=1;
    //当前亮度 为0 情况则使用系统亮度
    public int brightness=0;
    //是否使用系统亮度
    public boolean useSystem=true;

}
