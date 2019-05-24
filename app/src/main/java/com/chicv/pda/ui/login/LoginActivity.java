package com.chicv.pda.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.User;
import com.chicv.pda.bean.param.LoginParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.ui.main.MainActivity;
import com.chicv.pda.utils.CommonUtils;
import com.chicv.pda.utils.RxUtils;
import com.chicv.pda.utils.SPUtils;
import com.chicv.pda.utils.ToastUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.edit_account)
    EditText editAccount;
    @BindView(R.id.edit_pwd)
    EditText editPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        editAccount.setText("liheyu");
        editPwd.setText("LHYlhy123");
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        if(checkData()){
            login();
        }
    }

    private void login() {
        LoginParam param = new LoginParam();
        param.setAccount(CommonUtils.getString(editAccount));
        param.setPassword(CommonUtils.getString(editAccount));
        RxUtils.wrapHttp(apiService.login(param)).subscribe(new RxObserver<User>(true,this) {
            @Override
            public void onSuccess(User value) {
                SPUtils.saveUser(value);
                Logger.d(new Gson().toJson(SPUtils.getUser()));
                startActivity(new Intent(LoginActivity.this,MainActivity.class));

            }
        });
    }

    private boolean checkData() {
        if(TextUtils.isEmpty(CommonUtils.getString(editAccount))){
            ToastUtils.showString(R.string.please_input_account);
            return false;
        }
        if(TextUtils.isEmpty(CommonUtils.getString(editPwd))){
            ToastUtils.showString(R.string.please_input_pwd);
            return false;
        }
        return true;
    }
}
