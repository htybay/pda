package com.chicv.pda.ui.pickgoods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.adapter.PickGoodsAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.PickGoods;
import com.chicv.pda.bean.param.PickGoodsParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

public class PickgoodsActivity extends BaseActivity {

    @BindView(R.id.rlv_pick_goods)
    RecyclerView rlvPickGoods;
    @BindView(R.id.edit_barcode)
    EditText editBarcode;
    private PickGoodsAdapter mPickGoodsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_goods);
        ButterKnife.bind(this);
        initToolbar("拣货");
        getGoodsInfo("");
        initView();
        test();
    }

    private void test() {
        List<PickGoods> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(new PickGoods());
        }
        mPickGoodsAdapter.setNewData(list);
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvPickGoods.setLayoutManager(layoutManager);
        mPickGoodsAdapter = new PickGoodsAdapter();
        rlvPickGoods.setAdapter(mPickGoodsAdapter);
        editBarcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event.getAction()==KeyEvent.KEYCODE_SEARCH){
                    Logger.d("搜索");
                }
                return false;
            }
        });
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
