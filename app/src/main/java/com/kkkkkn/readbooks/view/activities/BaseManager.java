package com.kkkkkn.readbooks.view.activities;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

public class BaseManager implements LifecycleEventObserver {
    private static final String TAG="BaseManager";

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        switch (event){
            case ON_CREATE:
                Log.i(TAG, "onStateChanged: ON_CREATE");
                break;
            case ON_DESTROY:
                Log.i(TAG, "onStateChanged: ON_DESTROY");
                break;
        }
    }


}
