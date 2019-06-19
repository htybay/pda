package com.chicv.pda.ui.transferGoods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.TransferPick;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.PdaUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-06-14
 * email: liheyu999@163.com
 * <p>
 * 调拨发货
 */
public class TransferSendActivity extends BaseActivity {

    @BindView(R.id.text_transfer_id)
    TextView textTransferId;
    @BindView(R.id.text_transfer_status)
    TextView textTransferStatus;
    @BindView(R.id.text_transfer_num)
    TextView textTransferNum;
    @BindView(R.id.text_pick_num)
    TextView textPickNum;
    @BindView(R.id.text_out_num)
    TextView textOutNum;

    private String mBarcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_send);
        ButterKnife.bind(this);
        initToolbar("调拨发货");
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        mBarcode = barcode;
        if (BarcodeUtils.isTransferCode(barcode)) {
            //调拨单号
            transferDelivery(BarcodeUtils.getBarcodeId(barcode));
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }

    private void transferDelivery(final int barcodeId) {
        wrapHttp(apiService.transferDelivery(barcodeId))
                .compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(true, this) {
                    @Override
                    public void onSuccess(Object value) {
                        SoundUtils.playSuccess();
                        clearData();
                        textTransferId.setText(mBarcode);
                        ToastUtils.showString("发货成功");
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                        getTransfer(barcodeId);
                    }
                });
    }

    private void getTransfer(int barcodeId) {
        wrapHttp(apiService.getTransfer(barcodeId))
                .compose(this.<TransferPick>bindToLifecycle())
                .subscribe(new RxObserver<TransferPick>(true,this) {
                    @Override
                    public void onSuccess(TransferPick value) {
                        setData(value);
                    }

                    @Override
                    public void onFailure(String msg) {
                        clearData();
                    }
                });
    }

    private void setData(TransferPick value) {
        textTransferId.setText(mBarcode);
        textTransferStatus.setText(PdaUtils.getTransferStatusDes(value.getStatus()));
        textTransferNum.setText(String.valueOf(value.getTransferQuantity()));
        textPickNum.setText(String.valueOf(value.getPickCount()));
        textOutNum.setText(String.valueOf(value.getOutCount()));
    }

    private void clearData() {
        textTransferId.setText("");
        textTransferStatus.setText("");
        textTransferNum.setText("");
        textPickNum.setText("");
        textOutNum.setText("");
    }
}
