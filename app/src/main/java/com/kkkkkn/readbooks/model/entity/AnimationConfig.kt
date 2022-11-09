package com.kkkkkn.readbooks.model.entity

import android.view.View
import java.io.Serializable

class AnimationConfig : Serializable {
    var view: View? = null
    var moveX = 0f
    var moveY = 0f
    var scaleX = 0f
    var scaleY = 0f
    val isEmpty: Boolean
        get() = view == null || moveX == 0f || moveY == 0f || scaleX == 0f || scaleY == 0f
}