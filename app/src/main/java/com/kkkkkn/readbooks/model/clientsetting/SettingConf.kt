package com.kkkkkn.readbooks.model.clientsetting

import android.graphics.Color
import java.io.Serializable

/**
 * 系统配置信息，有默认值
 */
class SettingConf : Serializable {
    //字体大小
    var fontSize = 40f

    //字体颜色
    var fontColor = Color.BLACK

    //背景图样式,示例
    var backgroundStyle = 1

    //当前亮度 为0 情况则使用系统亮度
    var brightness = 0

    //是否使用系统亮度
    var useSystem = true
}