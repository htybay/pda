package com.chicv.pda.ui.sampleGoods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: liheyu
 * date: 2019-10-10
 * email: liheyu999@163.com
 */
public class SampleManagerActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_manager);
        ButterKnife.bind(this);
        initToolbar("样品管理");
    }

    @OnClick({R.id.text_sample_get, R.id.text_sample_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_sample_get:
                startActivity(new Intent(this, SampleGetActivity.class));
                break;
            case R.id.text_sample_up:
                startActivity(new Intent(this, SampleUpActivity.class));
                break;
        }
    }
}
