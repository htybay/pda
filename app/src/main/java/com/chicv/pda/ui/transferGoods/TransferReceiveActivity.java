package com.chicv.pda.ui.transferGoods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * author: liheyu
 * date: 2019-06-14
 * email: liheyu999@163.com
 * <p>
 * 调拨收货
 */
public class TransferReceiveActivity extends BaseActivity {

    private String mBarcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_receive);
        ButterKnife.bind(this);
        initToolbar("调拨收货");
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        rlvGoods.setLayoutManager(layoutManager);
//        mAdapter = new TransferPickAdapter();
//        rlvGoods.setAdapter(mAdapter);
//    }

//    @Override
//    protected void onReceiveBarcode(String barcode) {
//        mBarcode = barcode;
//        if (BarcodeUtils.isTransferCode(barcode)) {
//            //调拨单号
//            getTransfer(BarcodeUtils.getBarcodeId(barcode));
//        } else {
//            ToastUtils.showString("无效的条码！");
//            SoundUtils.playError();
//        }
//    }
//
//    private void getTransfer(int barcodeId) {
//        wrapHttp(apiService.getTransfer(barcodeId))
//                .compose(this.<TransferPick>bindToLifecycle())
//                .subscribe(new RxObserver<TransferPick>(true) {
//                    @Override
//                    public void onSuccess(TransferPick value) {
//                        handleData(value);
//                    }
//
//                    @Override
//                    public void onFailure(String msg) {
//                        clearData();
//                    }
//                });
//    }
//
//    private void handleData(TransferPick value) {
//        List<TransferPickGoods> details = value.getDetails();
//        List<TransferPickGoods> goodsList = new ArrayList<>();
//        Map<String, TransferPickGoods> batchMap = new HashMap<>();
//        for (TransferPickGoods detail : details) {
//            String batchCode = detail.getBatchCode();
//            if (TextUtils.isEmpty(batchCode)) {
//                goodsList.add(detail);
//            } else {
//                if (batchMap.containsKey(batchCode)) {
//                    TransferPickGoods goods = batchMap.get(batchCode);
//                    goods.setLocalTotalCount(goods.getLocalScanCount() + 1);
//                    if (goods.isIsSign()) {
//                        goods.setLocalScanCount(goods.getLocalScanCount() + 1);
//                    }
//                } else {
//                    detail.setLocalTotalCount(1);
//                    if (detail.isIsSign()) {
//                        detail.setLocalScanCount(1);
//                    }
//                    batchMap.put(batchCode, detail);
//                }
//            }
//        }
    }
}
