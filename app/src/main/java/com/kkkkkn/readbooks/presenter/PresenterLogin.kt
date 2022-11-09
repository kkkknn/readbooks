package com.kkkkkn.readbooks.presenter

import android.content.Context
import com.kkkkkn.readbooks.model.BaseModel
import com.kkkkkn.readbooks.model.ModelLogin
import com.kkkkkn.readbooks.model.entity.AccountInfo
import com.kkkkkn.readbooks.util.StringUtil.checkAccountName
import com.kkkkkn.readbooks.util.StringUtil.checkAccountPassword
import com.kkkkkn.readbooks.util.eventBus.EventMessage
import com.kkkkkn.readbooks.util.eventBus.events.LoginEvent
import com.kkkkkn.readbooks.view.view.LoginActivityView
import org.greenrobot.eventbus.EventBus

class PresenterLogin(context: Context?, private val loginActivityView: LoginActivityView) :
    BasePresenter(context!!, ModelLogin()), BaseModel.CallBack {
    private val modelLogin: ModelLogin = baseModel as ModelLogin
    private var name: String? = null
    private var password: String? = null

    init {
        modelLogin.setCallback(this)
    }

    /**
     * 登录,返回token 7天有效期
     * @param name 用户名
     * @param password 密码
     * @return 成功失败 boolean 类型
     */
    fun login(name: String?, password: String?) {
        if (!checkAccountName(name) || !checkAccountPassword(password)) {
            loginActivityView.showMsgDialog(-1, "用户名或密码错误，请重新输入")
            return
        }
        this.name = name
        this.password = password
        EventBus.getDefault().post(
            LoginEvent(
                EventMessage.LOGIN,
                name!!, password!!
            )
        )
    }

    override fun onSuccess(type: Int, `object`: Any) {
        when (type) {
            1 -> {
                //存储ID和token
                val info = `object` as AccountInfo
                setTokenCache(info.accountId, info.accountToken)
                setAccountCache(name, password)
                loginActivityView.showMsgDialog(1, "登录成功")
                loginActivityView.toMainActivity()
            }
            -1 -> {
                run {
                    this.password = null
                    this.name = this.password
                }
                loginActivityView.showMsgDialog(-1, (`object` as String))
            }
            else -> {
                run {
                    this.password = null
                    this.name = this.password
                }
                loginActivityView.showMsgDialog(-1, "登录失败")
            }
        }
    }

    override fun onError(type: Int, `object`: Any) {
        password = null
        name = password
        loginActivityView.showMsgDialog(-1, (`object` as String))
    }

    companion object {
        private const val TAG = "PresenterLogin"
    }
}