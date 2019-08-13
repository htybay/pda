package com.chicv.pda.ui.goods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.adapter.GoodsStockAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.GoodsStock;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-06-10
 * email: liheyu999@163.com
 * 物品货位
 */
public class GoodsStockActivity extends BaseActivity {

    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;
    @BindView(R.id.text_content)
    TextView textContent;
    @BindView(R.id.text_code)
    TextView textCode;

    private GoodsStockAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_stock);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initToolbar("物品货位");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new GoodsStockAdapter();
        rlvGoods.setAdapter(mAdapter);
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        textCode.setText(barcode);
        if (BarcodeUtils.isStockCode(barcode)) {
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
        wrapHttp(apiService.getGoodsStockByBatchCode(barcode))
                .compose(this.<List<GoodsStock>>bindToLifecycle())
                .subscribe(new RxObserver<List<GoodsStock>>(true, this) {
                    @Override
                    public void onSuccess(List<GoodsStock> value) {
                        SoundUtils.playSuccess();
                        if (value.size() == 0) {
                            ToastUtils.showString("无数据！");
                            return;
                        }

                        for (GoodsStock goodsStock : value) {
                            goodsStock.setGoodsRule(true);
                        }
                        setViewData(value);
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                        clearViewData();
                    }
                });
    }

    private void handleGoodsBarcode(int goodsId) {
        wrapHttp(apiService.getGoodsStockByGoodsId(goodsId))
                .compose(this.<GoodsStock>bindToLifecycle())
                .subscribe(new RxObserver<GoodsStock>(true, this) {
                    @Override
                    public void onSuccess(GoodsStock value) {
                        SoundUtils.playSuccess();
                        textContent.setText(value.getPosition());
                        textContent.setVisibility(View.VISIBLE);
                        mAdapter.setNewData(null);
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                        clearViewData();
                    }
                });
    }

    private void handleStockBarcode(int gridId) {
        wrapHttp(apiService.getGoodsStockByGridId((gridId)))
                .compose(this.<List<GoodsStock>>bindToLifecycle())
                .subscribe(new RxObserver<List<GoodsStock>>(true, this) {
                    @Override
                    public void onSuccess(List<GoodsStock> value) {
                        if (value.size() == 0) {
                            ToastUtils.showString("没有数据！");
                            SoundUtils.playError();
                            clearViewData();
                            return;
                        }
                        setViewData(handleSampleGoods(value));
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        clearViewData();
                        SoundUtils.playError();
                    }
                });
    }

    private void setViewData(List<GoodsStock> list) {
        Collections.sort(list);
        mAdapter.setNewData(list);
        textContent.setText("");
        textContent.setVisibility(View.GONE);
    }

    private void clearViewData() {
        mAdapter.setNewData(null);
        textContent.setText("");
        textCode.setText("");
        textContent.setVisibility(View.GONE);
    }


    private List<GoodsStock> handleSampleGoods(List<GoodsStock> list) {
        if (list == null) return new ArrayList<>();
        Map<String, GoodsStock> batchCodes = new HashMap<>();
        List<GoodsStock> copyStocks = new ArrayList<>();
        for (GoodsStock item : list) {
            if (TextUtils.isEmpty(item.getBatchCode()) || !TextUtils.isEmpty(item.getBatchCode()) && item.isReturn()) {
                copyStocks.add(item);
            } else {
                if (batchCodes.containsKey(item.getBatchCode())) {
                    GoodsStock goodsStock = batchCodes.get(item.getBatchCode());
                    goodsStock.setCount(goodsStock.getCount() + 1);
                } else {
                    item.setCount(1);
                    batchCodes.put(item.getBatchCode(), item);
                    copyStocks.add(item);
                }
            }
        }
        return copyStocks;
    }


}
