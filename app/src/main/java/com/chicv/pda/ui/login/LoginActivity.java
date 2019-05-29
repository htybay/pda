package com.chicv.pda.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.chicv.pda.BuildConfig;
import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.base.Constant;
import com.chicv.pda.bean.User;
import com.chicv.pda.bean.param.LoginParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.ui.main.MainActivity;
import com.chicv.pda.utils.CommonUtils;
import com.chicv.pda.utils.RxUtils;
import com.chicv.pda.utils.SPUtils;
import com.chicv.pda.utils.SoundUtils;
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
    @BindView(R.id.text_version)
    TextView textVersion;
    @BindView(R.id.cb_remember)
    AppCompatCheckBox cbRemember;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        SoundUtils.init(getApplicationContext());
    }

    private void initView() {
        textVersion.setText("版本号:V" + BuildConfig.VERSION_NAME);
        boolean rememberPwd = SPUtils.getBoolean(Constant.KEY_REMEMBER_PWD);
        if (rememberPwd) {
            cbRemember.setChecked(rememberPwd);
            User user = SPUtils.getUser();
            editAccount.setText(user.getLoginAccount());
            editPwd.setText(user.getPwd());
        }
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        if (checkData()) {
            login();
        }
    }

    private void login() {
        LoginParam param = new LoginParam();
        param.setAccount(CommonUtils.getString(editAccount));
        param.setPassword(CommonUtils.getString(editPwd));
        RxUtils.wrapHttp(apiService.login(param)).subscribe(new RxObserver<User>(true, this) {
            @Override
            public void onSuccess(User value) {
                if (cbRemember.isChecked()) {
                    value.setPwd(CommonUtils.getString(editPwd));
                }
                SPUtils.putBoolean(Constant.KEY_REMEMBER_PWD, cbRemember.isChecked());
                SPUtils.saveUser(value);
                Logger.d(new Gson().toJson(SPUtils.getUser()));
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }

    private boolean checkData() {
        if (TextUtils.isEmpty(CommonUtils.getString(editAccount))) {
            ToastUtils.showString(R.string.please_input_account);
            return false;
        }
        if (TextUtils.isEmpty(CommonUtils.getString(editPwd))) {
            ToastUtils.showString(R.string.please_input_pwd);
            return false;
        }
        return true;
    }
}
