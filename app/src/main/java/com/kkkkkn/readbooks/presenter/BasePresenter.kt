package com.kkkkkn.readbooks.presenter

import android.content.Context
import com.kkkkkn.readbooks.model.BaseModel
import com.kkkkkn.readbooks.model.entity.AccountInfo

open class BasePresenter(val context: Context, val baseModel: BaseModel) {

    /**
     * 获取当前用户token及用户id
     * @return 用户对象
     */
    val accountInfo: AccountInfo
        get() {
            val sharedPreferences =
                context.getSharedPreferences("AccountInfo", Context.MODE_PRIVATE)
            val accountInfo = AccountInfo()
            accountInfo.accountId = sharedPreferences.getInt("account_id", -1)
            accountInfo.accountToken = sharedPreferences.getString("accountToken", "")
            accountInfo.accountName = sharedPreferences.getString("accountName", "")
            accountInfo.accountPassword = sharedPreferences.getString("accountPassword", "")
            return accountInfo
        }


    fun setAccountCache(name: String?, password: String?) {
        if (name == null || password == null || name.isEmpty() || password.isEmpty()) return
        val editor = context.getSharedPreferences("AccountInfo", Context.MODE_PRIVATE).edit()
        editor.putString("accountName", name)
        editor.putString("accountPassword", password)
        editor.apply()
    }

    fun setTokenCache(id: Int, token: String?) {
        if (id <= 0 || token == null || token.isEmpty()) return
        val editor = context.getSharedPreferences("AccountInfo", Context.MODE_PRIVATE).edit()
        editor.putInt("account_id", id)
        editor.putString("accountToken", token)
        editor.apply()
    }
}