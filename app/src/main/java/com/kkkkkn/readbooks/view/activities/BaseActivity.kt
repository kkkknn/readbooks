package com.kkkkkn.readbooks.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ImmersionBar
import com.kkkkkn.readbooks.R

abstract class BaseActivity<T:ViewBinding>: AppCompatActivity() {
    lateinit var mViewBinding: T
    lateinit var tag:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = getViewBinding()
        setContentView(mViewBinding.root)
        //设置tag名字
        tag = this.localClassName
        //将当前activity加入栈
        StackManager.instance?.addActivity(this)

        //沉浸式状态栏
        editStatusBar()
    }

    open fun editStatusBar(){
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .navigationBarDarkIcon(true)
            .barColor(R.color.toolbar)
            .init()
    }

    abstract fun getViewBinding():T

    fun exitAll(){
        StackManager.instance?.exitAllActivity()
    }

    override fun onDestroy() {
        super.onDestroy()
        StackManager.instance?.removeActivity(this)
    }
}