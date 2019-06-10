package com.chicv.pda.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chicv.pda.BuildConfig;
import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.base.BaseApplication;
import com.chicv.pda.base.BaseFragment;
import com.chicv.pda.bean.User;
import com.chicv.pda.ui.login.LoginActivity;
import com.chicv.pda.utils.SPUtils;
import com.chicv.pda.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * author: liheyu
 * date: 2019-06-03
 * email: liheyu999@163.com
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_version)
    TextView textVersion;

    Unbinder unbinder;
    private User user;

    public static MineFragment newInstance() {

        Bundle args = new Bundle();

        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, null);
        unbinder = ButterKnife.bind(this, view);
        user = SPUtils.getUser();
        initView();
        return view;
    }

    private void initView() {
        textName.setText(user.getName());
        textVersion.setText("版本号：v" + BuildConfig.VERSION_NAME);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.text_login_out, R.id.text_update, R.id.text_change_user})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_change_user:
                BaseActivity activity = (BaseActivity) getActivity();
                BaseApplication.getInstance().finishOtherActivity(activity.getClass());
                startActivity(new Intent(activity, LoginActivity.class));
                activity.finish();
            case R.id.text_login_out:
                BaseApplication.getInstance().closeActivity();
                System.exit(0);
                break;
            case R.id.text_update:
                ToastUtils.showString("更新");
                break;
        }
    }
}
