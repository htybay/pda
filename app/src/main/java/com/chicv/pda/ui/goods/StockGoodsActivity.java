package com.chicv.pda.ui.goods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chicv.pda.R;
import com.chicv.pda.adapter.GoodsStockAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: liheyu
 * date: 2019-06-10
 * email: liheyu999@163.com
 * 货物信息查询
 */
public class StockGoodsActivity extends BaseActivity {

    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;

    private GoodsStockAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_change_stock);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initToolbar("货位信息查询");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new GoodsStockAdapter();
        rlvGoods.setAdapter(mAdapter);
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        if (BarcodeUtils.isContainerCode(barcode)) {
            //货位单号
            handleStockBarcode(BarcodeUtils.getBarcodeId(barcode));
        } else if (BarcodeUtils.isGoodsCode(barcode)) {
            //物品单号
            handleGoodsBarcode(BarcodeUtils.getBarcodeId(barcode));
        } else if (BarcodeUtils.isGoodsRuleCode(barcode)) {
            //囤货规格 扫到什么就是什么忽略大小写
            handleGoodsRuleBarcode(barcode);
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }

    private void handleGoodsRuleBarcode(String barcode) {

    }

    private void handleGoodsBarcode(long barcodeId) {

    }

    private void handleStockBarcode(long barcodeId) {

    }


}
