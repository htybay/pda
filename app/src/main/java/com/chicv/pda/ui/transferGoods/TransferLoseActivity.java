package com.chicv.pda.ui.transferGoods;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.adapter.TransferLoseAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.base.Constant;
import com.chicv.pda.bean.TransferPick;
import com.chicv.pda.bean.TransferPickGoods;
import com.chicv.pda.bean.param.TransferLoseParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.PdaUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-05-31
 * email: liheyu999@163.com
 * 调拨丢失
 */
public class TransferLoseActivity extends BaseActivity {


    @BindView(R.id.text_transfer_id)
    TextView textTransferId;
    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;

    private TransferLoseAdapter mAdapter;
    private String mBarcode;
    private TransferPick mTransferPick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_lose);
        ButterKnife.bind(this);
        initToolbar("调拨丢失");
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new TransferLoseAdapter();
        rlvGoods.setAdapter(mAdapter);
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        mBarcode = barcode;
        if (BarcodeUtils.isTransferCode(barcode)) {
            getTransfer(BarcodeUtils.getBarcodeId(barcode));
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }

    private void getTransfer(int barcodeId) {
        wrapHttp(apiService.getTransfer(barcodeId))
                .compose(this.<TransferPick>bindToLifecycle())
                .subscribe(new RxObserver<TransferPick>(true,this) {
                    @Override
                    public void onSuccess(TransferPick value) {
                        if (value.getStatus() == Constant.TRANSFER_STATUS_UN_PICK) {
                            mAdapter.setNewData(getNotPickGoods(value.getDetails()));
                        } else if (value.getStatus() == Constant.TRANSFER_STATUS_UN_IN) {
                            mAdapter.setNewData(getNotInStockGoods(value.getDetails()));
                        } else {
                            ToastUtils.showString("当前状态:" + PdaUtils.getTransferStatusDes(value.getStatus()) + ",不能丢失");
                            SoundUtils.playError();
                            return;
                        }
                        mTransferPick = value;
                        textTransferId.setText(mBarcode);
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        clearData();
                        SoundUtils.playError();
                    }
                });
    }

    private void clearData() {
        mTransferPick = null;
        mAdapter.setNewData(null);
        textTransferId.setText("");
    }

    private List<TransferPickGoods> getNotPickGoods(List<TransferPickGoods> data) {
        List<TransferPickGoods> list = new ArrayList<>();
        if (data != null) {
            for (TransferPickGoods item : data) {
                if (!item.isIsPick()) {
                    list.add(item);
                }
            }
        }
        return list;
    }

    private List<TransferPickGoods> getNotInStockGoods(List<TransferPickGoods> data) {
        List<TransferPickGoods> list = new ArrayList<>();
        if (data != null) {
            for (TransferPickGoods item : data) {
                if (!item.isIsIn()) {
                    list.add(item);
                }
            }
        }
        return list;
    }

    @OnClick(R.id.btn_lose)
    public void onViewClicked() {
        if (mTransferPick == null) {
            ToastUtils.showString("请先扫描调拨单！");
            return;
        }
        if (mAdapter.getData().size() == 0) {
            ToastUtils.showString("没有丢失的数据！");
            return;
        }
        showAlertDialog();
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("是否确认丢失？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        commit();
                    }
                }).show();
    }

    private void commit() {
        TransferLoseParam param = new TransferLoseParam();
        param.setTransferId(mTransferPick.getId());
        // 入库丢失 拣货丢失
        param.setLoseType(mTransferPick.getStatus() == Constant.TRANSFER_STATUS_UN_PICK ? 1 : 2);
        List<TransferLoseParam.DetailBean> list = new ArrayList<>();
        for (TransferPickGoods datum : mAdapter.getData()) {
            TransferLoseParam.DetailBean detailBean = new TransferLoseParam.DetailBean();
            detailBean.setGoodsId(datum.getGoodsId());
            detailBean.setTransferDetailId(datum.getId());
            list.add(detailBean);
        }
        param.setDetails(list);
        wrapHttp(apiService.transferLose(param))
                .compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(true, this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("成功！");
                        clearData();
                    }
                });
    }
}
