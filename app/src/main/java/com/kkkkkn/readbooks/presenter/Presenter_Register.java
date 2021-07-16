package com.kkkkkn.readbooks.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.JsonReader;
import android.util.Log;

import com.kkkkkn.readbooks.model.BaseModel;
import com.kkkkkn.readbooks.model.Model_Register;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.util.StringUtil;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;
import com.kkkkkn.readbooks.view.view.RegisterView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class Presenter_Register extends BasePresenter implements BaseModel.CallBack {
    private RegisterView registerView;
    private Model_Register model_register;
    private String name,password;

    public Presenter_Register(Context context,RegisterView view) {
        super(context,new Model_Register());
        this.registerView=view;
        model_register=(Model_Register) getBaseModel();
        model_register.setCallback(this);
    }

    /**
     * 注册用户
     * @param password 用户密码
     * @param name 用户姓名
     * @return  成功失败 boolean类型
     */
    public void register(final String name, final String password){
        if(!StringUtil.checkAccountName(name)){
            registerView.showTip(0,"用户名仅支持英文、数字、下划线,长度3-10之间");
            return;
        }else if (!StringUtil.checkAccountPassword(password)){
            registerView.showTip(0,"密码必须包含大小写字母和数字的组合，可以使用特殊字符，长度在8-10之间");
            return;
        }
        String[] arr={name,password};
        this.name=name;
        this.password=password;
        EventBus.getDefault().post(new MessageEvent(EventMessage.REGISTER,arr));

    }

    @Override
    public void onSuccess(int type, Object object) {
        if (type == 1) {
            setAccountCache(this.name,this.password);
            registerView.back2Login();
            registerView.showMsgDialog(1, (String) object);
        } else {
            this.name=this.password=null;
            registerView.showMsgDialog(-1, "注册失败");
        }
    }

    @Override
    public void onError(int type, Object object) {
        this.name=this.password=null;
        registerView.showTip(-1,(String)object);
    }
}
