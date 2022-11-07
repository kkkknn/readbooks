package com.kkkkkn.readbooks.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.kkkkkn.readbooks.databinding.ActivityLoginBinding
import com.kkkkkn.readbooks.presenter.Presenter_Login
import com.kkkkkn.readbooks.view.view.LoginActivityView
import es.dmoral.toasty.Toasty
import java.util.*


class LoginActivity : BaseActivity<ActivityLoginBinding>(), LoginActivityView {
    private var presenterLogin: Presenter_Login? = null
    private var lastBackClick: Long = 0
    private var regActivityResultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        regActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val intent = result.data
            val resultCode = result.resultCode
            //判断注册是否成功
            if (resultCode == RESULT_OK && intent != null) {
                val name = intent.getStringExtra("accountName")
                val password = intent.getStringExtra("accountPassword")
                mViewBinding.editAccountName.setText(name)
                mViewBinding.editAccountPassword.setText(password)
            }
        }
        presenterLogin = Presenter_Login(applicationContext, this)
        presenterLogin!!.init()
    }

    private fun initView() {
        mViewBinding.loginBtn.setOnClickListener {
            val name: String =
                Objects.requireNonNull(mViewBinding.editAccountName.text).toString()
            val password: String =
                Objects.requireNonNull(mViewBinding.editAccountPassword.text).toString()
            presenterLogin?.login(name, password)
        }
        mViewBinding.jumpToRsg.setOnClickListener { toRegisterActivity() }
    }

    override fun onStart() {
        super.onStart()
        flushEditView()
    }

    override fun showMsgDialog(type: Int, msg: String) {
        runOnUiThread {
            if (type > 0) {
                //不显示成功toast，
                Toasty.success(applicationContext, msg, Toast.LENGTH_SHORT, true).show()
            } else {
                Toasty.error(applicationContext, msg, Toast.LENGTH_SHORT, true).show()
            }
        }
    }

    override fun toRegisterActivity() {
        regActivityResultLauncher!!.launch(Intent(this, RegisterActivity::class.java))
    }

    override fun toMainActivity() {
        val intent = intent
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun flushEditView() {
        if (presenterLogin != null) {
            val info = presenterLogin!!.accountCache
            mViewBinding.editAccountName.setText(info.account_name)
            mViewBinding.editAccountPassword.setText(info.account_password)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (presenterLogin != null) {
            presenterLogin!!.release()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val nowBackClick = System.currentTimeMillis()
            if (lastBackClick != 0L && nowBackClick - lastBackClick < 1500) {
                //程序退出
                exitAll()
            } else {
                //500ms以上，弹窗不处理
                lastBackClick = nowBackClick
                Toast.makeText(applicationContext, "请再按一次以退出程序", Toast.LENGTH_SHORT).show()
            }
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun getViewBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

}