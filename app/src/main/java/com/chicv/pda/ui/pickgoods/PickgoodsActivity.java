package com.chicv.pda.ui.pickgoods;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.PickGoods;
import com.chicv.pda.bean.param.PickGoodsParam;
import com.chicv.pda.repository.remote.RxObserver;

import butterknife.ButterKnife;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

public class PickgoodsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_goods);
        initToolbar("拣货");
        getGoodsInfo("");

    }

    private void getGoodsInfo(String barcode) {
        PickGoodsParam param = new PickGoodsParam();
        param.setPickId(barcode);
        wrapHttp(apiService.pickGoods(param)).subscribe(new RxObserver<PickGoods>() {
            @Override
            public void onSuccess(PickGoods value) {

            }
        });
    }

}
