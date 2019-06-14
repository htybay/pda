package com.chicv.pda.ui.stock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.base.Constant;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: liheyu
 * date: 2019-06-04
 * email: liheyu999@163.com
 * <p>
 * 分类入库
 */
public class TypeInSockActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_in_stock);
        ButterKnife.bind(this);
        initToolbar("分类入库");
    }

    private void startActivity(int type, String title) {
        InStockActivity.start(this, type, title);
    }

    @OnClick({R.id.text_buy, R.id.text_not_standard, R.id.text_back_goods, R.id.text_change_goods, R.id.text_check_extra, R.id.text_retry_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_buy:
                startActivity(Constant.STOCK_TYPE_IN_BUY, "采购入库");
                break;
            case R.id.text_not_standard:
                startActivity(Constant.STOCK_TYPE_IN_NOT_STANTARD, "不合格入库");
                break;
            case R.id.text_back_goods:
                startActivity(Constant.STOCK_TYPE_IN_BACK_GOODS, "退货入库");
                break;
            case R.id.text_change_goods:
                startActivity(Constant.STOCK_TYPE_IN_CHANGE_GOODS, "换款入库");
                break;
            case R.id.text_check_extra:
                startActivity(Constant.STOCK_TYPE_IN_CHECK_EXTRA, "盘盈入库");
                break;
            case R.id.text_retry_check:
                startActivity(Constant.STOCK_TYPE_IN_RETRY_CHECK, "重新质检入库");
                break;
        }
    }

//    @OnClick({R.id.text_buy, R.id.text_not_standard})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.text_buy:
//                startActivity(Constant.STOCK_TYPE_IN_BUY);
//                break;
//            case R.id.text_not_standard:
//                startActivity(Constant.STOCK_TYPE_IN_NOT_STANTARD);
//                break;
//        }
//    }
}
