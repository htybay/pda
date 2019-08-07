package com.chicv.pda.ui.stock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.AddedStockListBean;
import com.chicv.pda.bean.StockLimit;
import com.chicv.pda.bean.StockPositionBean;
import com.chicv.pda.bean.StockReceiveBatch;
import com.chicv.pda.bean.param.BatchInStockParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.SPUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;
import com.chicv.pda.view.NumView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-06-10
 * email: liheyu999@163.com
 * 囤货规格入库
 */
public class BatchInStockActivity extends BaseActivity {

    @BindView(R.id.text_batch_code)
    TextView textBatchCode;
    @BindView(R.id.text_stock)
    TextView textStock;
    @BindView(R.id.text_stock_id)
    TextView textStockId;
    @BindView(R.id.num_view)
    NumView numView;
    @BindView(R.id.text_stock_rule)
    TextView textStockRule;

    private StockPositionBean mStockPositionBean;
    private StockReceiveBatch mPurchaseReceiveBatch;
    private List<AddedStockListBean.AddedStock> mAddedStockList;
    private StockLimit mStockLimit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_in_stock);
        ButterKnife.bind(this);
        initToolbar("囤货物品入库");
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        if (BarcodeUtils.isStockCode(barcode)) {
            //货位单号
            handleStockBarcode(BarcodeUtils.getBarcodeId(barcode));
        } else if (BarcodeUtils.isGoodsRuleCode(barcode)) {
            //囤货规格 扫到什么就是什么忽略大小写
            handleGoodsRuleBarcode(barcode);
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }

    private void handleGoodsRuleBarcode(String barcode) {
        getAddedStock(barcode);
    }

    //获取囤货物品所在的货位
    private void getAddedStock(final String barcode) {
        wrapHttp(apiService.getAddedStockList(barcode))
                .compose(this.<AddedStockListBean>bindToLifecycle())
                .subscribe(new RxObserver<AddedStockListBean>(true, this) {
                    @Override
                    public void onSuccess(AddedStockListBean value) {
                        getReceiveBatchLimit(barcode);
                        mAddedStockList = value.getAddedStockList();
                        int size = value.getAddedStockList().size();
                        String text;
                        if (size == 0) {
                            text = "暂无";
                        } else if (size == 1) {
                            text = value.getAddedStockList().get(0).getPositionText();
                        } else {
                            text = "多个货位(查看)";
                        }
                        textStock.setText(text);
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                        mAddedStockList = null;
                        textStock.setText("");
                    }
                });
    }

    //获取囤货规格下限定入库数量
    private void getReceiveBatchLimit(final String barcode) {
        wrapHttp(apiService.getReceiveBatch(barcode))
                .compose(this.<StockReceiveBatch>bindToLifecycle())
                .subscribe(new RxObserver<StockReceiveBatch>(true) {
                    @Override
                    public void onSuccess(StockReceiveBatch value) {
                        mPurchaseReceiveBatch = value;
                        mPurchaseReceiveBatch.setBatchCode(barcode);
                        textBatchCode.setText(barcode);
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                        mPurchaseReceiveBatch = null;
                        textBatchCode.setText("");
                    }
                });
    }

    private void handleStockBarcode(final int stockId) {
        if (mPurchaseReceiveBatch == null) {
            ToastUtils.showString("请先扫描囤货规格");
            SoundUtils.playError();
            return;
        }
        wrapHttp(apiService.getPositionByGridId(stockId))
                .compose(this.<StockPositionBean>bindToLifecycle())
                .subscribe(new RxObserver<StockPositionBean>(true, this) {
                    @Override
                    public void onSuccess(StockPositionBean value) {
                        mStockPositionBean = value;
                        mStockPositionBean.setId(stockId);
                        textStockId.setText(value.getPosition());
                        getInStockLimit();
                    }

                    @Override
                    public void onFailure(String msg) {
                        mStockPositionBean = null;
                        textStockId.setText("");
                        SoundUtils.playError();
                    }
                });
    }

    //获取入库件数的最大最小限定
    private void getInStockLimit() {
        wrapHttp(apiService.getStockLimit(mStockPositionBean.getId(), mPurchaseReceiveBatch.getBatchCode()))
                .compose(this.<StockLimit>bindToLifecycle())
                .subscribe(new RxObserver<StockLimit>(true, this) {
                    @Override
                    public void onSuccess(StockLimit value) {
                        SoundUtils.playSuccess();
                        mStockLimit = value;
                        if (value.getType() == 1) {
                            textStockRule.setText("最少入" + value.getNum());
                        }
                        if (value.getType() == 2) {
                            textStockRule.setText("最多入" + value.getNum());
                        }else {
                            textStockRule.setText("");
                        }
                        numView.setFocusable(true);
                        numView.setFocusableInTouchMode(true);
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                        mStockLimit = null;
                        textStockRule.setText("");
                    }
                });
    }

    private void checkData() {
        if (mPurchaseReceiveBatch == null) {
            ToastUtils.showString("请先扫描囤货规格");
            return;
        }
        if (mPurchaseReceiveBatch.getWaitReceiveCount() <= 0) {
            ToastUtils.showString("当前扫描囤货规格待入库物品数量有误");
            return;
        }
        if (mStockPositionBean == null || mStockLimit == null) {
            ToastUtils.showString("请扫描货位");
            return;
        }
        int i = numView.getNumValue();
        if (i == 0 || i > mPurchaseReceiveBatch.getWaitReceiveCount()) {
            ToastUtils.showString("输入的入库数量有误，请重新输入!");
            return;
        }
        if (mStockLimit.getType() == 1 && i < mStockLimit.getNum()) {
            ToastUtils.showString("最少入库" + mStockLimit.getNum());
            return;
        }
        if (mStockLimit.getType() == 2 && i > mStockLimit.getNum()) {
            ToastUtils.showString("最多入库" + mStockLimit.getNum());
            return;
        }
        commit();
    }

    private void commit() {
        BatchInStockParam param = new BatchInStockParam();
        param.setGridId(mStockPositionBean.getId());
        param.setStockReceiveBatchId(mPurchaseReceiveBatch.getStockReceiveBatchId());
        param.setStockNums(numView.getNumValue());
        param.setOperateUserId(SPUtils.getUser().getId());
        wrapHttp(apiService.batchInStock(param))
                .compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(true, this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("入库成功！");
                        SoundUtils.playSuccess();
                        clearData();
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                    }
                });
    }

    private void clearData() {
        mPurchaseReceiveBatch = null;
        mStockLimit = null;
        mStockPositionBean = null;
        mAddedStockList = null;
        textBatchCode.setText("");
        textStock.setText("");
        textStockId.setText("");
        textStockRule.setText("");
        numView.setText("0");
    }

    @OnClick({R.id.text_stock, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_stock:
                if (mAddedStockList != null && mAddedStockList.size() > 0)
                    StockInfoActivity.start(this, mAddedStockList.get(0).getGridId());
                break;
            case R.id.btn_commit:
                checkData();
                break;
        }
    }
}
