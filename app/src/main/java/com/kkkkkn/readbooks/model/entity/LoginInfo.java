package com.kkkkkn.readbooks.model.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.io.Serializable;

public class LoginInfo extends BaseObservable {
    private String login_name;
    private String login_password;

    @Bindable
    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
        notifyPropertyChanged(BR.login_name);
    }

    @Bindable
    public String getLogin_password() {
        return login_password;
    }

    public void setLogin_password(String login_password) {
        this.login_password = login_password;
        notifyPropertyChanged(BR.login_password);
    }
}
