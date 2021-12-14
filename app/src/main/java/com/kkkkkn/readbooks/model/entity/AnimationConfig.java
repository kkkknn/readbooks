package com.kkkkkn.readbooks.model.entity;

import android.view.View;

import java.io.Serializable;

public class AnimationConfig implements Serializable {
    public View view;
    public float moveX;
    public float moveY;
    public float scaleX;
    public float scaleY;


    public boolean isEmpty() {
        return view == null
                || moveX == 0
                || moveY == 0
                || scaleX == 0
                || scaleY == 0;
    }

}
