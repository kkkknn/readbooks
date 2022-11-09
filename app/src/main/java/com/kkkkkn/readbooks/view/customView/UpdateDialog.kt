package com.kkkkkn.readbooks.view.customView

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.kkkkkn.readbooks.databinding.UpdateDialogBinding

class UpdateDialog(context: Context) : Dialog(context) {
    private var viewBinding:UpdateDialogBinding
    private var onClickBottomListener: OnClickBottomListener? = null

    init {
        viewBinding = UpdateDialogBinding.inflate( LayoutInflater.from(context),null,false )
        setContentView(viewBinding.root)
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false)

        viewBinding.downloadProgress.max = 100

        //初始化事件
        initEvent()
    }

    //回调事件
    interface OnClickBottomListener {
        fun onOkClick()
        fun onCancelClick()
    }

    private fun initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        viewBinding.btnOk.setOnClickListener {
            if (onClickBottomListener != null) {
                onClickBottomListener!!.onOkClick()
            }
        }
        //设置取消按钮被点击后，向外界提供监听
        viewBinding.btnCancel.setOnClickListener {
            if (onClickBottomListener != null) {
                onClickBottomListener!!.onCancelClick()
            }
        }
    }

    fun setOnClickListener(onClickBottomListener: OnClickBottomListener?): UpdateDialog {
        this.onClickBottomListener = onClickBottomListener
        return this
    }


    fun setInfo(title: String?, msg: String?): UpdateDialog {
        viewBinding.updateTitle.text = title
        viewBinding.updateInfo.text = msg
        return this
    }

    fun setProgressType(flag: Boolean) {
        if (flag) {
            viewBinding.downloadProgress.visibility = View.VISIBLE
        } else {
            viewBinding.downloadProgress.visibility = View.GONE
        }
    }

    fun setProgress(count: Int) {
        viewBinding.downloadProgress.progress = count
    }
}