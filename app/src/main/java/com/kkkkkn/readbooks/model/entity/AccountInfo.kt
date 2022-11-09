package com.kkkkkn.readbooks.model.entity

import com.kkkkkn.readbooks.util.StringUtil.isEmpty

class AccountInfo {
    var accountId = 0
    var accountName: String? = null
    var accountPassword: String? = null
    var accountToken: String? = null
    val isHasToken: Boolean
        get() = accountId > 0 && !isEmpty(accountToken)
}