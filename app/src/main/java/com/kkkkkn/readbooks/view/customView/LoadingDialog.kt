package com.kkkkkn.readbooks.view.customView

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.kkkkkn.readbooks.databinding.LoadingDialogBinding

class LoadingDialog(context: Context) : Dialog(context) {
    private var viewBinding: LoadingDialogBinding

    init {
        viewBinding= LoadingDialogBinding.inflate( LayoutInflater.from(context),null,false )
        this.setContentView(viewBinding.root)
        setCanceledOnTouchOutside(false)
    }

    fun showLoading(content: String?) {
        super.show()
        viewBinding.loadingMsg.text = content
    }

    fun hideLoading() {
        super.dismiss()
    }
}