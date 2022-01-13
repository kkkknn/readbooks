package com.kkkkkn.readbooks.presenter;

import android.content.Context;

import com.kkkkkn.readbooks.model.BaseModel;
import com.kkkkkn.readbooks.model.Model_Register;
import com.kkkkkn.readbooks.util.StringUtil;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.events.RegisterEvent;
import com.kkkkkn.readbooks.view.view.RegisterActivityView;

import org.greenrobot.eventbus.EventBus;

public class Presenter_Register extends BasePresenter implements BaseModel.CallBack {
    private RegisterActivityView registerActivityView;
    private Model_Register model_register;
    private String name,password;

    public Presenter_Register(Context context, RegisterActivityView view) {
        super(context,new Model_Register());
        this.registerActivityView =view;
        this.model_register=(Model_Register) getBaseModel();
        this.model_register.setCallback(this);
    }

    /**
     * 注册用户
     * @param password 用户密码
     * @param name 用户姓名
     * @return  成功失败 boolean类型
     */
    public void register(final String name, final String password,final String passwordCheck){
        if(!StringUtil.equals(password,passwordCheck)){
            registerActivityView.showTip(-1,"两次输入密码不一致");
        }else if(!StringUtil.checkAccountName(name)){
            registerActivityView.showTip(-2,"用户名仅支持英文、数字、下划线,长度3-10之间");
            return;
        }else if (!StringUtil.checkAccountPassword(password)){
            registerActivityView.showTip(-3,"密码必须包含大小写字母和数字的组合，可以使用特殊字符，长度在8-10之间");
            return;
        }
        //赋值暂存 请求注册
        this.name=name;
        this.password=password;
        EventBus.getDefault().post(new RegisterEvent(EventMessage.REGISTER,name,password));

    }

    @Override
    public void onSuccess(int type, Object object) {
        if (type == 1) {
            setAccountCache(this.name,this.password);
            registerActivityView.back2Login();
            registerActivityView.showMsgDialog(1, (String) object);
        } else {
            this.name=this.password=null;
            registerActivityView.showMsgDialog(-1, "注册失败");
        }
    }

    @Override
    public void onError(int type, Object object) {
        this.name=this.password=null;
        registerActivityView.showTip(-2,"用户名重复");
    }
}
