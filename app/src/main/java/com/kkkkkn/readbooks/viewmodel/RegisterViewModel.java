package com.kkkkkn.readbooks.viewmodel;

import android.text.Editable;
import android.util.Log;

import com.kkkkkn.readbooks.databinding.ActivityLoginBinding;
import com.kkkkkn.readbooks.databinding.ActivityRegisterBinding;
import com.kkkkkn.readbooks.model.entity.LoginInfo;
import com.kkkkkn.readbooks.model.entity.RegisterInfo;

public class RegisterViewModel {
    private ActivityRegisterBinding activityRegisterBinding;
    private RegisterInfo registerInfo;

    public RegisterViewModel(ActivityRegisterBinding activityRegisterBinding) {
        this.activityRegisterBinding = activityRegisterBinding;
        registerInfo=new RegisterInfo();
        activityRegisterBinding.setRegInfo(registerInfo);
        activityRegisterBinding.setRegisterViewmodel(this);
    }


    public void regClick(RegisterInfo regInfo){
        Log.i("TAG", "getReg_name: "+regInfo.getReg_name());
        Log.i("TAG", "getReg_password: "+regInfo.getReg_password());
        registerInfo.setReg_msg("我干，注册失败");
    }


    public void afterUserNameChanged(Editable editable) {
        registerInfo.setReg_name(editable.toString());
    }

    public void afterPasswordChanged(Editable editable) {
        registerInfo.setReg_password(editable.toString());
    }
}
