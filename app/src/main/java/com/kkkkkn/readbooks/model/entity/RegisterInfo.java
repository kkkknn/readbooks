package com.kkkkkn.readbooks.model.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class RegisterInfo extends BaseObservable {
    private String reg_name;
    private String reg_password;
    private String reg_msg;

    @Bindable
    public String getReg_msg() {
        return reg_msg;
    }

    public void setReg_msg(String reg_msg) {
        this.reg_msg = reg_msg;
        notifyPropertyChanged(BR.reg_msg);
    }

    @Bindable
    public String getReg_name() {
        return reg_name;
    }

    public void setReg_name(String reg_name) {
        this.reg_name = reg_name;
        notifyPropertyChanged(BR.reg_name);
    }

    @Bindable
    public String getReg_password() {
        return reg_password;
    }

    public void setReg_password(String reg_password) {
        this.reg_password = reg_password;
        notifyPropertyChanged(BR.reg_password);
    }


}
