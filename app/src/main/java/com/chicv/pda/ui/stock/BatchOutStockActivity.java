package com.chicv.pda.ui.stock;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.ApiResult;
import com.chicv.pda.bean.StockBackPickInfo;
import com.chicv.pda.bean.StockPosition;
import com.chicv.pda.bean.User;
import com.chicv.pda.bean.param.StockBackOutParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.repository.remote.exception.ApiException;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.SPUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;
import com.chicv.pda.view.NumView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-09-17
 * email: liheyu999@163.com
 * 囤货出库
 */
public class BatchOutStockActivity extends BaseActivity {

    @BindView(R.id.text_pick_num)
    TextView textPickNum;
    @BindView(R.id.text_stock_next)
    TextView textStockNext;
    @BindView(R.id.text_stock)
    TextView textStock;
    @BindView(R.id.text_batch_code)
    TextView textBatchCode;
    @BindView(R.id.num_view)
    NumView numView;
    @BindView(R.id.text_wait_num)
    TextView textWaitNum;
    @BindView(R.id.text_extra_num)
    TextView textExtraNum;

    private User mUser;
    private String mBarcode;
    private int mPickId;
    private int mStockId;
    private String mBatchCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_out_stock);
        ButterKnife.bind(this);
        initToolbar("囤货物品出库");
        mUser = SPUtils.getUser();
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        mBarcode = barcode;
        if (BarcodeUtils.isPickCode(barcode)) {
            checkPickId(BarcodeUtils.getBarcodeId(barcode));
        } else if (BarcodeUtils.isStockCode(barcode)) {
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

    private void checkPickId(final int barcodeId) {
        if (mPickId != 0 && mPickId != barcodeId) {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("您确定更换退货单么? \n 更换后当前的数据将被清除")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            clearAllData();
                            handlePickBarcode(barcodeId);
                        }
                    })
                    .show();
        } else {
            handlePickBarcode(barcodeId);
        }
    }

    private void handleStockBarcode(final int barcodeId) {
        if (mPickId == 0) {
            ToastUtils.showString("请先扫描囤货退货拣货单号");
            SoundUtils.playError();
            return;
        }
        wrapHttp(apiService.getPositionText(mPickId, barcodeId))
                .compose(this.<StockPosition>bindToLifecycle())
                .subscribe(new RxObserver<StockPosition>(this) {
                    @Override
                    public void onSuccess(StockPosition value) {
                        mStockId = barcodeId;
                        textStock.setText(value.getPositionText());
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        mStockId = 0;
                        textStock.setText("");
                        SoundUtils.playError();
                    }
                });
    }

    private void handleGoodsRuleBarcode(final String barcode) {
        if (mPickId == 0) {
            ToastUtils.showString("请先扫描囤货退货拣货单号");
            SoundUtils.playError();
            return;
        }
        if (mStockId == 0) {
            ToastUtils.showString("请请扫描货位编号");
            SoundUtils.playError();
            return;
        }
        wrapHttp(apiService.getWaitOutQuantity(mPickId, mStockId, barcode))
                .compose(this.<Integer>bindToLifecycle())
                .subscribe(new RxObserver<Integer>(this) {
                    @Override
                    public void onSuccess(Integer value) {
                        mBatchCode = barcode;
                        textBatchCode.setText(barcode);
                        textWaitNum.setText(String.valueOf(value));
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mBatchCode = null;
                        textBatchCode.setText("");
                        textWaitNum.setText("");
                        SoundUtils.playError();
                    }
                });

        wrapHttp(apiService.getStockQuantity(mStockId, barcode))
                .compose(this.<Integer>bindToLifecycle())
                .subscribe(new RxObserver<Integer>() {
                    @Override
                    public void onSuccess(Integer value) {
                        textExtraNum.setText(String.valueOf(value));
                    }

                    @Override
                    public void onFailure(String msg) {
                        textExtraNum.setText("");
                    }
                });
    }

    private void handlePickBarcode(final int barcodeId) {
        wrapHttp(apiService.getHasStockBackPick(barcodeId, mUser.getName()))
                .compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(this) {
                    @Override
                    public void onSuccess(Object value) {
                        textPickNum.setText(mBarcode);
                        mPickId = barcodeId;
                        isStockBackPickEnd(barcodeId);
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        textPickNum.setText("");
                        mPickId = 0;
                        SoundUtils.playError();
                    }
                });
    }

    private void isStockBackPickEnd(int barcodeId) {
        wrapHttp(apiService.isStockBackPickEnd(barcodeId))
                .compose(this.<StockBackPickInfo>bindToLifecycle())
                .subscribe(new RxObserver<StockBackPickInfo>() {
                    @Override
                    public void onSuccess(StockBackPickInfo value) {
                        if (!value.isPickingComplete()) {
                            textStockNext.setText(value.getNextGridName());
                        } else {
                            textStockNext.setText("");
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        textStockNext.setText("");
                    }
                });
    }

    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        if (mPickId == 0) {
            ToastUtils.showString("请先扫描囤货退货拣货单号");
            return;
        }
        if (mStockId == 0) {
            ToastUtils.showString("请扫描货位编号");
            return;
        }
        if (TextUtils.isEmpty(mBatchCode)) {
            ToastUtils.showString("请扫描囤货规格");
            return;
        }
        int numValue = numView.getNumValue();
        if (numValue <= 0) {
            ToastUtils.showString("请输入出库数量");
            return;
        }

        StockBackOutParam param = new StockBackOutParam();
        param.setStockPickId(mPickId);
        param.setGridId(mStockId);
        param.setBatchCode(mBatchCode);
        param.setBackQuantity(numValue);
        param.setOperateUserId(mUser.getId());
        wrapHttp(apiService.stockBackOut(param).flatMap(new Function<ApiResult<Object>, ObservableSource<ApiResult<StockBackPickInfo>>>() {
            @Override
            public ObservableSource<ApiResult<StockBackPickInfo>> apply(ApiResult<Object> objectApiResult) throws Exception {
                if (objectApiResult.isSuccess()) {
                    return apiService.isStockBackPickEnd(mPickId);
                } else {
                    throw new ApiException(objectApiResult.getMessage());
                }
            }
        })).compose(this.<StockBackPickInfo>bindToLifecycle())
                .subscribe(new RxObserver<StockBackPickInfo>(this) {
                    @Override
                    public void onSuccess(StockBackPickInfo value) {
                        if (value.isPickingComplete()) {
                            ToastUtils.showString("出库已完成");
                            clearAllData();
                        } else {
                            ToastUtils.showString("出库成功，请继续下一个货位！");
                            clearPartData();
                        }
                    }
                });
    }

    private void clearPartData() {
        mStockId = 0;
        mBatchCode = null;
        textBatchCode.setText("");
        textStock.setText("");
        textExtraNum.setText("");
        textWaitNum.setText("");
        textStockNext.setText("");
        numView.setText("0");
    }

    private void clearAllData() {
        mPickId = 0;
        mStockId = 0;
        mBatchCode = null;
        textPickNum.setText("");
        textBatchCode.setText("");
        textStock.setText("");
        textExtraNum.setText("");
        textWaitNum.setText("");
        textStockNext.setText("");
        numView.setText("0");
    }
}
