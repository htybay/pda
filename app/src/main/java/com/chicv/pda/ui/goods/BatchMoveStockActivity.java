package com.chicv.pda.ui.goods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.StockLimit;
import com.chicv.pda.bean.param.BatchMoveStockParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.CommonUtils;
import com.chicv.pda.utils.SPUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-06-12
 * email: liheyu999@163.com
 * <p>
 * 囤货物品移库
 */
public class BatchMoveStockActivity extends BaseActivity {

    @BindView(R.id.text_old_stock)
    TextView textOldStock;
    @BindView(R.id.text_batch_code)
    TextView textBatchCode;
    @BindView(R.id.text_stock_id)
    TextView textStockId;
    @BindView(R.id.edit_in_num)
    EditText editInNum;
    @BindView(R.id.text_total)
    TextView textTotal;
    @BindView(R.id.text_limit)
    TextView textLimit;

    private int mOldStockId;
    private int mNewStockId;
    private int mMoveMaxTotal;
    private String mBatchCode;

    private StockLimit mStockLimit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_move_stock);
        ButterKnife.bind(this);
        initView();
    }

    @OnClick({R.id.btn_clear, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_clear:
                clearData();
                break;
            case R.id.btn_commit:
                checkData();
                break;
        }
    }

    private void initView() {
        initToolbar("囤货物品移位");
//        editInNum.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!TextUtils.isEmpty(s) && mStockLimit != null) {
//                    try {
//                        int i = Integer.parseInt(s.toString());
//                        if (mStockLimit.getType() == 1 && i < mStockLimit.getNum()) {
//                            ToastUtils.showString("最少入" + mStockLimit.getNum());
//                        }
//                        if (mStockLimit.getType() == 2 && i > mStockLimit.getNum()) {
//                            ToastUtils.showString("最多入" + mStockLimit.getNum());
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
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

    private void handleGoodsRuleBarcode(final String barcode) {
        if (mOldStockId <= 0) {
            //必须扫了旧货位
            ToastUtils.showString("请扫描旧货位编号");
            SoundUtils.playError();
            return;
        }
        wrapHttp(apiService.getWaitMoveQuantity(mOldStockId, barcode))
                .compose(this.<Integer>bindToLifecycle())
                .subscribe(new RxObserver<Integer>(true, this) {
                    @Override
                    public void onSuccess(Integer value) {
                        textBatchCode.setText(barcode);
                        textTotal.setText(String.valueOf(value));
                        mBatchCode = barcode;
                        mMoveMaxTotal = value;
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                        mBatchCode = null;
                        mMoveMaxTotal = 0;
                        textBatchCode.setText("");
                        textTotal.setText("");
                    }
                });
    }


    private void handleStockBarcode(final int stockId) {
        if (mOldStockId <= 0) {
            //扫第一次货位为旧货位
            mOldStockId = stockId;
            textOldStock.setText(BarcodeUtils.generateHWBarcode(stockId));
            SoundUtils.playSuccess();
            return;
        }

        //第二次扫货位
        if (mBatchCode == null) {
            ToastUtils.showString("请先扫描囤货规格");
            SoundUtils.playError();
            return;
        }

        if (stockId == mOldStockId) {
            ToastUtils.showString("新旧货位不能相同");
            SoundUtils.playError();
            return;
        }

        wrapHttp(apiService.getStockLimit(stockId, mBatchCode))
                .compose(this.<StockLimit>bindToLifecycle())
                .subscribe(new RxObserver<StockLimit>(true,this) {
                    @Override
                    public void onSuccess(StockLimit value) {
                        SoundUtils.playSuccess();
                        mStockLimit = value;
                        if (value.getType() == 1) {
                            textLimit.setText(String.format(Locale.CHINA, "（>=%d）", value.getNum()));
                        }
                        if (value.getType() == 2) {
                            textLimit.setText(String.format(Locale.CHINA, "（<=%d）", value.getNum()));
                        }
                        textStockId.setText(BarcodeUtils.generateHWBarcode(stockId));
                        mNewStockId = stockId;
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                        textStockId.setText("");
                        mNewStockId = 0;
                    }
                });
    }

    private void checkData() {
        if (mOldStockId == 0) {
            ToastUtils.showString("请扫描旧货位编号");
            return;
        }
        if (mNewStockId == 0) {
            ToastUtils.showString("请扫描新货位编号");
            return;
        }
        if (TextUtils.isEmpty(mBatchCode)) {
            ToastUtils.showString("请扫描囤货规格");
            return;
        }
        String num = CommonUtils.getString(editInNum);
        if (TextUtils.isEmpty(num)) {
            ToastUtils.showString("请输入移位数量!");
            return;
        }
        int i = 0;
        try {
            i = Integer.parseInt(num);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (i == 0) {
            ToastUtils.showString("请输入移位数量!");
            return;
        }
        if (i > mMoveMaxTotal) {
            ToastUtils.showString("移位数量超出在位数量!");
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
        BatchMoveStockParam param = new BatchMoveStockParam();
        param.setOldGridId(mOldStockId);
        param.setNewGridId(mNewStockId);
        param.setBatchCode(mBatchCode);
        param.setQuantity(Integer.parseInt(CommonUtils.getString(editInNum)));
        param.setOperateUserId(SPUtils.getUser().getId());
        wrapHttp(apiService.batchMoveStock(param))
                .compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(true, this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("移库成功！");
                        clearData();
                    }
                });
    }


    private void clearData() {
        mOldStockId = 0;
        mNewStockId = 0;
        mBatchCode = null;
        mMoveMaxTotal = 0;
        mStockLimit = null;
        textOldStock.setText("");
        textBatchCode.setText("");
        textStockId.setText("");
        textLimit.setText("");
        textTotal.setText("0");
        editInNum.setText("0");
    }
}
