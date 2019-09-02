package com.chicv.pda.ui.stock;

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
 * date: 2019-08-12
 * email: liheyu999@163.com
 */
public class HandleStockActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_stock);
        ButterKnife.bind(this);
        initToolbar("理库=");
    }

    @OnClick({R.id.text_handle_list, R.id.text_handle_num, R.id.text_handle_manual})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_handle_list:
                startActivity(new Intent(this,HandleListActivity.class));
                break;
            case R.id.text_handle_num:
                startActivity(new Intent(this,HandleStockCountActivity.class));
                break;
            case R.id.text_handle_manual:
                startActivity(new Intent(this, HandleStockManualActivity.class));
                break;
        }
    }
}
