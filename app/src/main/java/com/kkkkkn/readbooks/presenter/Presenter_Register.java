package com.kkkkkn.readbooks.presenter;

import android.content.Context;
import android.util.Log;

import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.util.StringUtil;
import com.kkkkkn.readbooks.view.view.RegisterView;

public class Presenter_Register extends BasePresenter {
    private RegisterView registerView;

    public Presenter_Register(Context context,RegisterView view) {
        super(context);
        this.registerView=view;
    }

    /**
     * 注册用户
     * @param password 用户密码
     * @param name 用户姓名
     * @return  成功失败 boolean类型
     */
    public boolean register(String name,String password){
        if(!StringUtil.checkAccountName(name)||!StringUtil.checkAccountPassword(password)){

            System.out.println("11111111");
            return false;
        }
        //获取model层，进行注册操作
        registerView.showMsgDialog(1,"注册成功");
        System.out.println("注册成功"+name+"||"+password);
        return true;
    }
}
