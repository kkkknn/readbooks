package com.kkkkkn.readbooks.model.entity;

import com.kkkkkn.readbooks.util.StringUtil;

public class AccountInfo {
    private int account_id;
    private String account_name;
    private String account_password;
    private String account_token;

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_password() {
        return account_password;
    }

    public void setAccount_password(String account_password) {
        this.account_password = account_password;
    }

    public String getAccount_token() {
        return account_token;
    }

    public void setAccount_token(String account_token) {
        this.account_token = account_token;
    }

    public boolean isHasToken(){
        return account_id>0 && !StringUtil.isEmpty(account_token);
    }

}
