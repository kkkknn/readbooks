package com.kkkkkn.readbooks.view.activities

import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.kkkkkn.readbooks.R
import com.kkkkkn.readbooks.databinding.ActivityRegisterBinding
import com.kkkkkn.readbooks.presenter.Presenter_Register
import com.kkkkkn.readbooks.view.view.RegisterActivityView
import es.dmoral.toasty.Toasty
import java.util.*

class RegisterActivity : BaseActivity<ActivityRegisterBinding>(), RegisterActivityView {
    private var presenterRegister: Presenter_Register? = null
    private var cacheAccount: String? = null
    private var cachePassword: String? = null
    private val onFocusChangeListener =
        OnFocusChangeListener { view, b ->
            if (!b) {
                return@OnFocusChangeListener
            }
            when (view.id) {
                R.id.edit_reg_name -> {
                    mViewBinding.textRegNameTip.text = ""
                    mViewBinding.textRegNameTip.visibility = View.GONE
                }
                R.id.edit_reg_password -> {
                    mViewBinding.textRegPasswordTip.text = ""
                    mViewBinding.textRegPasswordTip.visibility = View.GONE
                }
                R.id.edit_reg_password_check -> {
                    mViewBinding.textRegPasswordCheckTip.text = ""
                    mViewBinding.textRegPasswordCheckTip.visibility = View.GONE
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        presenterRegister = Presenter_Register(applicationContext, this)
        presenterRegister!!.init()
    }

    private fun initView() {
        mViewBinding.registerBtn.setOnClickListener {
            mViewBinding.textRegNameTip.text = ""
            mViewBinding.textRegPasswordTip.text = ""
            mViewBinding.textRegPasswordCheckTip.text = ""
            val name = Objects.requireNonNull(mViewBinding.editRegName.text).toString()
            val password =
                Objects.requireNonNull(mViewBinding.editRegPassword.text).toString()
            val passwordCheck =
                Objects.requireNonNull(mViewBinding.editRegPasswordCheck.text)
                    .toString()
            cacheAccount = name
            cachePassword = password
            presenterRegister!!.register(name, password, passwordCheck)
        }
        mViewBinding.editRegName.onFocusChangeListener = onFocusChangeListener
        mViewBinding.editRegPassword.onFocusChangeListener = onFocusChangeListener
        mViewBinding.editRegPasswordCheck.onFocusChangeListener = onFocusChangeListener
    }

    override fun showMsgDialog(type: Int, msg: String) {
        runOnUiThread {
            if (type > 0) {
                Toasty.success(applicationContext, msg, Toast.LENGTH_SHORT, true).show()
            } else {
                Toasty.error(applicationContext, msg, Toast.LENGTH_SHORT, true).show()
            }
        }
    }

    override fun showTip(type: Int, msg: String) {
        if (msg.isEmpty()) return
        runOnUiThread {
            when (type) {
                -1 -> {
                    mViewBinding.textRegPasswordCheckTip.visibility = View.VISIBLE
                    mViewBinding.textRegPasswordCheckTip.text = msg
                }
                -2 -> {
                    mViewBinding.textRegNameTip.visibility = View.VISIBLE
                    mViewBinding.textRegNameTip.text = msg
                }
                -3 -> {
                    mViewBinding.editRegPassword.visibility = View.VISIBLE
                    mViewBinding.editRegPassword.setText(msg)
                }
            }
        }
    }

    override fun back2Login() {
        val intent = intent
        intent.putExtra("accountName", cacheAccount)
        intent.putExtra("accountPassword", cachePassword)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun clearAccountCache() {
        cacheAccount = null
        cachePassword = null
    }

    override fun onDestroy() {
        super.onDestroy()
        presenterRegister!!.release()
    }

    override fun getViewBinding(): ActivityRegisterBinding {
        return ActivityRegisterBinding.inflate(layoutInflater)
    }
}