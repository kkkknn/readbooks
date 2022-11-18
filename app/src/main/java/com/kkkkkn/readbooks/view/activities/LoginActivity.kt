package com.kkkkkn.readbooks.view.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.kkkkkn.readbooks.R
import com.kkkkkn.readbooks.databinding.ActivityLoginBinding
import com.kkkkkn.readbooks.presenter.PresenterLogin
import com.kkkkkn.readbooks.view.view.LoginActivityView
import es.dmoral.toasty.Toasty
import java.util.*


class LoginActivity : BaseActivity<ActivityLoginBinding>(), LoginActivityView {
    private var presenterLogin: PresenterLogin? = null
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
        presenterLogin = PresenterLogin(applicationContext, this)
    }

    private fun initView() {
        mViewBinding.loginBtn.setOnClickListener {
            val name: String =
                mViewBinding.editAccountName.text.toString()
            val password: String =
                mViewBinding.editAccountPassword.text.toString()
            presenterLogin?.login(name, password)
        }
        mViewBinding.jumpToRsg.setOnClickListener { toRegisterActivity() }

        mViewBinding.editAccountPassword.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.isNullOrEmpty()){
                    mViewBinding.ivAccountPasswordShow.visibility=View.GONE
                    mViewBinding.editAccountPassword.inputType= InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
                    mViewBinding.ivAccountPasswordShow.setImageResource(R.drawable.ic_eye_show_24)
                }else{
                    mViewBinding.ivAccountPasswordShow.visibility=View.VISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        mViewBinding.ivAccountPasswordShow.setOnClickListener(object :OnClickListener{
            override fun onClick(p0: View?) {
                if(mViewBinding.editAccountPassword.inputType==InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT){
                    mViewBinding.ivAccountPasswordShow.setImageResource(R.drawable.ic_eye_hide_24)
                    mViewBinding.editAccountPassword.inputType=InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    mViewBinding.editAccountPassword.setSelection(mViewBinding.editAccountPassword.text!!.length)
                }else{
                    mViewBinding.ivAccountPasswordShow.setImageResource(R.drawable.ic_eye_show_24)
                    mViewBinding.editAccountPassword.inputType= InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
                    mViewBinding.editAccountPassword.setSelection(mViewBinding.editAccountPassword.text!!.length)
                }
            }
        })
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
            val info = presenterLogin!!.accountInfo
            mViewBinding.editAccountName.setText(info.accountName)
            mViewBinding.editAccountPassword.setText(info.accountPassword)
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