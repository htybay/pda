package com.chicv.pda.ui.goods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.adapter.GoodsMoveStockAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.GoodsMoveBean;
import com.chicv.pda.bean.StockPositionBean;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.ui.stock.StockInfoActivity;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-06-10
 * email: liheyu999@163.com
 * 物品移位
 */
public class GoodsMoveStockActivity extends BaseActivity {

    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;
    @BindView(R.id.text_stock)
    TextView textStock;

    private GoodsMoveStockAdapter mAdapter;
    private StockPositionBean mStockPositionBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_move_stock);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initToolbar("物品移位");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new GoodsMoveStockAdapter();
        rlvGoods.setAdapter(mAdapter);
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        if (BarcodeUtils.isStockCode(barcode)) {
            //货位单号
            handleStockBarcode(BarcodeUtils.getBarcodeId(barcode));
        } else if (BarcodeUtils.isGoodsCode(barcode)) {
            //物品单号
            handleGoodsBarcode(BarcodeUtils.getBarcodeId(barcode));
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }

    private void handleGoodsBarcode(int goodsId) {
        wrapHttp(apiService.getGoodsMove(goodsId))
                .compose(this.<GoodsMoveBean>bindToLifecycle())
                .subscribe(new RxObserver<GoodsMoveBean>(true, this) {
                    @Override
                    public void onSuccess(GoodsMoveBean value) {
                        SoundUtils.playSuccess();
                        mAdapter.addData(value);
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                    }
                });
    }

    private void handleStockBarcode(final int gridId) {
        wrapHttp(apiService.getPositionByGridId(gridId))
                .compose(this.<StockPositionBean>bindToLifecycle())
                .subscribe(new RxObserver<StockPositionBean>(true, this) {
                    @Override
                    public void onSuccess(StockPositionBean value) {
                        mStockPositionBean = value;
                        mStockPositionBean.setId(gridId);
                        textStock.setText(mStockPositionBean.getPosition());
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        mStockPositionBean = null;
                        textStock.setText("");
                        SoundUtils.playError();
                    }
                });

    }


    @OnClick({R.id.text_stock, R.id.btn_remove})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_stock:
                if (mStockPositionBean == null) {
                    return;
                }
                StockInfoActivity.start(this, mStockPositionBean.getId());
                break;
            case R.id.btn_remove:
                remove();
                break;
        }
    }

    private void remove() {
        if (mStockPositionBean == null) {
            ToastUtils.showString("请扫描货位!");
            return;
        }
        if (mAdapter.getData().size() == 0) {
            ToastUtils.showString("请扫描物品!");
            return;
        }

        List<GoodsMoveBean> data = mAdapter.getData();
        for (GoodsMoveBean item : data) {
            item.setGridId(mStockPositionBean.getId());
        }
        wrapHttp(apiService.moveGoods(data))
                .compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(true, this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("移动成功");
                        clearData();
                    }
                });
    }

    private void clearData() {
        mStockPositionBean = null;
        mAdapter.setNewData(null);
        textStock.setText("");
    }
}
