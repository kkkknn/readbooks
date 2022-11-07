package com.kkkkkn.readbooks.view.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ImmersionBar
import com.kkkkkn.readbooks.R

abstract class BaseActivity<T:ViewBinding>: AppCompatActivity() {
    lateinit var mViewBinding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding=getViewBinding()
        setContentView(mViewBinding.root)

        //将当前activity加入栈
        val stackManager = StackManager.getInstance()
        stackManager.addActivity(this)

        //沉浸式状态栏
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .navigationBarDarkIcon(true)
            .barColor(R.color.toolbar)
            .init()
    }


    abstract fun getViewBinding():T

    fun exitAll(){
        val stackManager = StackManager.getInstance()
        stackManager.exitAllActivity()
    }

    override fun onDestroy() {
        super.onDestroy()
        val stackManager = StackManager.getInstance()
        stackManager.removeActivity(this)
    }
}