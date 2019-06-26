package com.chicv.pda.ui.login;

import android.Manifest;
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
import com.chicv.pda.bean.UpdateInfo;
import com.chicv.pda.bean.User;
import com.chicv.pda.bean.param.LoginParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.ui.main.MainActivity;
import com.chicv.pda.utils.CommonUtils;
import com.chicv.pda.utils.DownloadManager;
import com.chicv.pda.utils.RxUtils;
import com.chicv.pda.utils.SPUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

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
        initToolbar("登陆",false);
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
//            login();
            checkUpdate();
        }
    }

    private void checkUpdate() {
        RxUtils.wrapHttp(apiService.checkUpdate())
                .compose(this.<UpdateInfo>bindToLifecycle())
                .subscribe(new RxObserver<UpdateInfo>(this) {
                    @Override
                    public void onSuccess(UpdateInfo value) {
                        if (value.getVersionCode() > BuildConfig.VERSION_CODE) {
                            checkPermission(value);
                        } else {
                            login();
                        }
                    }
                });
    }

    private void checkPermission(final UpdateInfo value) {
        RxPermissions rxPermissions = new RxPermissions(this);
        Disposable disposable = rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .compose(this.<Boolean>bindToLifecycle())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Logger.d(aBoolean);
                        if (aBoolean) {
                            update(value);
                        } else {
                            ToastUtils.showString("无读写SD卡权限");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    private void update(UpdateInfo value) {
        DownloadManager downloadManager = new DownloadManager(LoginActivity.this);
        downloadManager.updateAPP(value.getUpdateUrl());
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
