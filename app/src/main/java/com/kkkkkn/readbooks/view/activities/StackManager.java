package com.kkkkkn.readbooks.view.activities;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class StackManager {
    private static volatile StackManager stackManager;
    private final List<Activity> activityList=new ArrayList<>();

    public static StackManager getInstance(){
        if(stackManager==null){
            synchronized (StackManager.class){
                if(stackManager==null){
                    stackManager=new StackManager();
                }
            }
        }
        return stackManager;
    }

    public void addActivity(Activity activity){
        activityList.add(activity);
    }

    public void exitAllActivity(){
        for (int i = 0; i <activityList.size() ; i++) {
            activityList.get(i).finish();

        }
        activityList.clear();
    }

    public void removeActivity(Activity activity){
        activityList.remove(activity);
    }
}
