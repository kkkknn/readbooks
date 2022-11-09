package com.kkkkkn.readbooks.view.view

interface RegisterActivityView :BaseView{
    fun showTip(type: Int, msg: String)
    fun back2Login()
    fun clearAccountCache()
}