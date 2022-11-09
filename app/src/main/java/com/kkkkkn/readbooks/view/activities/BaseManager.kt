package com.kkkkkn.readbooks.view.activities

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class BaseManager : LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> Log.i(TAG, "onStateChanged: ON_CREATE")
            Lifecycle.Event.ON_DESTROY -> Log.i(TAG, "onStateChanged: ON_DESTROY")
            else -> {}
        }
    }

    companion object {
        private const val TAG = "BaseManager"
    }
}