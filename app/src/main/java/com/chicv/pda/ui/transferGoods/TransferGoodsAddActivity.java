package com.chicv.pda.ui.transferGoods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.StockBean;
import com.chicv.pda.bean.TransferBathBean;
import com.chicv.pda.bean.param.TransferGoodsAddParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-06-13
 * email: liheyu999@163.com
 * 调拨单添加
 */
public class TransferGoodsAddActivity extends BaseActivity {

    @BindView(R.id.sp_origin)
    Spinner spOrigin;
    @BindView(R.id.sp_destination)
    Spinner spDestination;
    @BindView(R.id.text_count)
    TextView textCount;

    @BindArray(R.array.stock_type)
    String[] stocks;

    private TransferBathBean mTransferBatchBean;
    private List<StockBean> mStocks;
    private StockBean mOriginStockBean;
    private StockBean mDestinatonStockBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tranfer_goods_add);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        mStocks = new ArrayList<>();
        mStocks.add(new StockBean(1, stocks[0]));
        mStocks.add(new StockBean(7, stocks[1]));
        mStocks.add(new StockBean(8, stocks[2]));
    }

    private void initView() {
        initToolbar("调拨单添加");
        spOrigin.setSelection(0);
        spDestination.setSelection(1);
        spOrigin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mOriginStockBean = mStocks.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDestinatonStockBean = mStocks.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        if (BarcodeUtils.isBatchRule(barcode)) {
            //囤货批次
//            getGoodsNum(BarcodeUtils.getBarcodeId(barcode));
        } else if (BarcodeUtils.isGoodsRuleCode(barcode)) {
            //囤货规格 扫到什么就是什么忽略大小写
            getGoodsNum(barcode);
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }

    private void getGoodsNum(String barcode) {
        wrapHttp(apiService.getTransferBath(barcode))
                .compose(this.<TransferBathBean>bindToLifecycle())
                .subscribe(new RxObserver<TransferBathBean>() {
                    @Override
                    public void onSuccess(TransferBathBean value) {
                        mTransferBatchBean = value;
                        textCount.setText(String.valueOf(mTransferBatchBean.getQCNotPassCount()));
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        mTransferBatchBean = null;
                        textCount.setText("");
                        SoundUtils.playError();
                    }
                });
    }

    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        if (mTransferBatchBean == null) {
            ToastUtils.showString("请扫描批次号");
            return;
        }
        if (mOriginStockBean == null) {
            ToastUtils.showString("请选择来源仓");
            return;
        }
        if (mDestinatonStockBean == null) {
            ToastUtils.showString("请选择目标仓");
            return;
        }
        if (mDestinatonStockBean.getId() == mOriginStockBean.getId()) {
            ToastUtils.showString("来源仓、目标仓不能是同一个");
            return;
        }

        TransferGoodsAddParam param = new TransferGoodsAddParam();
        param.setBatchCode(mTransferBatchBean.getBatchCode());
        param.setFromRoomId(mOriginStockBean.getId());
        param.setToRoomId(mOriginStockBean.getId());

        wrapHttp(apiService.transferBatchAdd(param)).compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>() {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("添加成功");
                        mTransferBatchBean = null;
                        textCount.setText("");
                    }
                });
    }

}
