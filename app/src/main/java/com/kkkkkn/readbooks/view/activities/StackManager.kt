package com.kkkkkn.readbooks.view.activities

import android.app.Activity

class StackManager {
    private val activityList: MutableList<Activity> = ArrayList()
    fun addActivity(activity: Activity) {
        activityList.add(activity)
    }

    fun exitAllActivity() {
        for (i in activityList.indices) {
            activityList[i].finish()
        }
        activityList.clear()
    }

    fun removeActivity(activity: Activity) {
        activityList.remove(activity)
    }

    companion object {
        @Volatile
        private var stackManager: StackManager? = null
        val instance: StackManager?
            get() {
                if (stackManager == null) {
                    synchronized(StackManager::class.java) {
                        if (stackManager == null) {
                            stackManager =
                                StackManager()
                        }
                    }
                }
                return stackManager
            }
    }
}