package com.chicv.pda.ui.stock;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.repository.remote.RxObserver;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-08-12
 * email: liheyu999@163.com
 */
public class HandleStockActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hadnle_stock);
//        test();
    }

    private void test() {
        wrapHttp(apiService.getCardingList()).compose(bindToLifecycle()).subscribe(new RxObserver<Object>(this) {
            @Override
            public void onSuccess(Object value) {

            }
        });


    }
}
