package com.kkkkkn.readbooks.presenter

import android.content.Context
import com.kkkkkn.readbooks.model.BaseModel
import com.kkkkkn.readbooks.model.ModelRegister
import com.kkkkkn.readbooks.util.StringUtil.checkAccountName
import com.kkkkkn.readbooks.util.StringUtil.checkAccountPassword
import com.kkkkkn.readbooks.util.StringUtil.equals
import com.kkkkkn.readbooks.util.eventBus.EventMessage
import com.kkkkkn.readbooks.util.eventBus.events.RegisterEvent
import com.kkkkkn.readbooks.view.view.RegisterActivityView
import org.greenrobot.eventbus.EventBus

class PresenterRegister(
    context: Context?,
    private val registerActivityView: RegisterActivityView
) :
    BasePresenter(context!!, ModelRegister()), BaseModel.CallBack {
    private val modelRegister: ModelRegister = baseModel as ModelRegister

    init {
        modelRegister.setCallback(this)
    }

    /**
     * 注册用户
     * @param password 用户密码
     * @param name 用户姓名
     * @return  成功失败 boolean类型
     */
    fun register(name: String?, password: String?, passwordCheck: String?) {
        if (!equals(password, passwordCheck)) {
            registerActivityView.showTip(-1, "两次输入密码不一致")
            registerActivityView.clearAccountCache()
            return
        } else if (!checkAccountName(name)) {
            registerActivityView.showTip(-2, "用户名仅支持英文、数字、下划线,长度3-10之间")
            registerActivityView.clearAccountCache()
            return
        } else if (!checkAccountPassword(password)) {
            registerActivityView.showTip(-3, "密码必须包含大小写字母和数字的组合，可以使用特殊字符，长度在8-10之间")
            registerActivityView.clearAccountCache()
            return
        }
        EventBus.getDefault().post(
            RegisterEvent(
                EventMessage.REGISTER,
                name!!, password!!
            )
        )
    }

    override fun onSuccess(type: Int, `object`: Any) {
        if (type == 1) {
            registerActivityView.back2Login()
            registerActivityView.showMsgDialog(1, (`object` as String))
        } else {
            registerActivityView.showMsgDialog(-1, "注册失败")
            registerActivityView.clearAccountCache()
        }
    }

    override fun onError(type: Int, `object`: Any) {
        registerActivityView.showTip(-2, "用户名重复")
        registerActivityView.clearAccountCache()
    }
}