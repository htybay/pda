package com.chicv.pda.ui.stock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.AddedStockListBean;
import com.chicv.pda.bean.PurchaseBatch;
import com.chicv.pda.bean.StockInfo;
import com.chicv.pda.bean.StockMoveBean;
import com.chicv.pda.repository.remote.RxObserver;
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
 * 囤货规格入库
 */
public class BatchInStockActivity extends BaseActivity {

    public static final String KEY_STOCK_ID = "key_stock_id";

    @BindView(R.id.text_batch_code)
    TextView textBatchCode;
    @BindView(R.id.text_stock)
    TextView textStock;
    @BindView(R.id.text_stock_id)
    TextView textStockId;
    @BindView(R.id.edit_in_num)
    EditText editInNum;
    @BindView(R.id.text_stock_rule)
    TextView textStockRule;

    private StockMoveBean mStockMoveBean;
    private PurchaseBatch mPurchaseBatch;
    private List<AddedStockListBean.AddedStock> mAddedStockList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_in_stock);
        ButterKnife.bind(this);
        initToolbar("囤货物品入库");
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        if (BarcodeUtils.isContainerCode(barcode)) {
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
        wrapHttp(apiService.getAddedStockList(barcode))
                .compose(this.<AddedStockListBean>bindToLifecycle())
                .subscribe(new RxObserver<AddedStockListBean>(true) {
                    @Override
                    public void onSuccess(AddedStockListBean value) {
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
                });

        wrapHttp(apiService.getPurchaseBatch(barcode))
                .compose(this.<PurchaseBatch>bindToLifecycle())
                .subscribe(new RxObserver<PurchaseBatch>(true, this) {
                    @Override
                    public void onSuccess(PurchaseBatch value) {
                        mPurchaseBatch = value;
                    }
                });
    }

    private void handleStockBarcode(final int stockId) {
        if(mPurchaseBatch==null){
            ToastUtils.showString("请先扫描囤货规格");
            return;
        }
        wrapHttp(apiService.getStockMoveInfoByGridId(stockId))
                .compose(this.<StockMoveBean>bindToLifecycle())
                .subscribe(new RxObserver<StockMoveBean>(true, this) {
                    @Override
                    public void onSuccess(StockMoveBean value) {
                        mStockMoveBean = value;
                        mStockMoveBean.setId(stockId);
                        textStockId.setText(BarcodeUtils.generateHWBarcode(stockId));
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                    }
                });
    }

    private void clearData() {
    }

    private void setData(StockInfo value) {

    }

    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
    }
}
