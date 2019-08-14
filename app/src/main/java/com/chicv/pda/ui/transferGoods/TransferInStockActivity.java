package com.chicv.pda.ui.transferGoods;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.adapter.TransferInStockAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.base.Constant;
import com.chicv.pda.bean.StockPositionBean;
import com.chicv.pda.bean.TransferIn;
import com.chicv.pda.bean.User;
import com.chicv.pda.bean.param.TransferInStockParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.PdaUtils;
import com.chicv.pda.utils.SPUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-06-14
 * email: liheyu999@163.com
 * <p>
 * 调拨入库
 */
public class TransferInStockActivity extends BaseActivity {

    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.text_pick_id)
    TextView textPickId;
    @BindView(R.id.text_stock_id)
    TextView textStockId;
    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;

    private TransferInStockAdapter mAdapter;
    private TransferIn mTransferIn;
    private String mBarcode;
    private StockPositionBean mStockPositionBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_in_stock);
        ButterKnife.bind(this);
        initToolbar("调拨入库");
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new TransferInStockAdapter();
        rlvGoods.setAdapter(mAdapter);
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        mBarcode = barcode;
        if (BarcodeUtils.isTransferCode(barcode)) {
            //调拨单号
            getTransfer(BarcodeUtils.getBarcodeId(barcode));
        } else if (BarcodeUtils.isStockCode(barcode)) {
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

    private void handleStockBarcode(final int stockId) {
        if (mTransferIn == null) {
            ToastUtils.showString("请先扫描调拨单！");
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
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                        mStockPositionBean = null;
                        textStockId.setText("");
                    }
                });
    }


    //扫描到物品条码
    private void handleGoodsBarcode(long goodsId) {
        if (mTransferIn == null) {
            ToastUtils.showString("请先扫描调拨单！");
            SoundUtils.playError();
            return;
        }

        if (mStockPositionBean == null) {
            ToastUtils.showString("请先扫描货位号！");
            SoundUtils.playError();
            return;
        }

        List<TransferIn.DetailsBean> data = mAdapter.getData();
        TransferIn.DetailsBean scanGoods = null;
        for (TransferIn.DetailsBean goodsDetail : data) {
            if (goodsDetail.getGoodsId() == goodsId) {
                scanGoods = goodsDetail;
                break;
            }
        }
        if (scanGoods == null) {
            ToastUtils.showString("该调拨单上没有此物品！");
            SoundUtils.playError();
            return;
        }

        if (scanGoods.isIsIn()) {
            ToastUtils.showString("该物品已扫描!");
            SoundUtils.playError();
            return;
        }
        scanGoods.setIsIn(true);
        mAdapter.notifyDataSetChanged();
        SoundUtils.playSuccess();

        if (isScanOver()) {
            ToastUtils.showString("已全部扫描,请提交");
        }
    }

    // 扫描到囤货规格条码
    private void handleGoodsRuleBarcode(String barcode) {
        if (mTransferIn == null) {
            ToastUtils.showString("请先扫描调拨单！");
            SoundUtils.playError();
            return;
        }

        if (mStockPositionBean == null) {
            ToastUtils.showString("请先扫描货位号！");
            SoundUtils.playError();
            return;
        }

        List<TransferIn.DetailsBean> data = mAdapter.getData();
        boolean isExist = false;
        TransferIn.DetailsBean scanGoods = null;
        for (TransferIn.DetailsBean goodsDetail : data) {
            if (TextUtils.equals(goodsDetail.getBatchCode().toLowerCase(), barcode.toLowerCase())) {
                isExist = true;
                if (!goodsDetail.isIsIn()) {
                    scanGoods = goodsDetail;
                    break;
                }
            }
        }
        if (!isExist) {
            ToastUtils.showString("该货位上没有此囤货规格的物品！");
            SoundUtils.playError();
            return;
        }
        if (scanGoods == null) {
            ToastUtils.showString("该囤货规格已拣完");
            SoundUtils.playError();
            return;
        }
        scanGoods.setIsIn(true);
        mAdapter.notifyDataSetChanged();
        SoundUtils.playSuccess();

        if (isScanOver()) {
            ToastUtils.showString("已全部扫描,请提交");
        }
    }

    private void getTransfer(final int barcodeId) {
        if (mTransferIn != null && !isScanOver()) {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("调拨单存在未入库的数据，是否忽略？")
                    .setPositiveButton("忽略", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getTransferIn(barcodeId);
                        }
                    }).setNegativeButton("取消", null)
                    .show();
        } else {
            getTransferIn(barcodeId);
        }
    }

    private void getTransferIn(int barcodeId) {
        wrapHttp(apiService.getTransferIn(barcodeId))
                .compose(this.<TransferIn>bindToLifecycle())
                .subscribe(new RxObserver<TransferIn>(true, this) {
                    @Override
                    public void onSuccess(TransferIn value) {
                        if (value.getStatus() != Constant.TRANSFER_STATUS_UN_IN) {
                            clearData();
                            ToastUtils.showString("调拨单状态错误,当前状态:" + PdaUtils.getTransferStatusDes(value.getStatus()));
                            SoundUtils.playError();
                            return;
                        }
                        mTransferIn = value;
                        List<TransferIn.DetailsBean> details = mTransferIn.getDetails();
                        Collections.sort(filterHaveInStock(details));
                        mAdapter.setNewData(details);
                        textPickId.setText(mBarcode);
                    }

                    @Override
                    public void onFailure(String msg) {
                        clearData();
                        SoundUtils.playError();
                    }
                });
    }

    //获取调拨单数据后过滤掉已扫描的
    private List<TransferIn.DetailsBean> filterHaveInStock(List<TransferIn.DetailsBean> details) {
        List<TransferIn.DetailsBean> list = new ArrayList<>();
        if (details != null) {
            for (TransferIn.DetailsBean detail : details) {
                if (!detail.isIsIn()) {
                    list.add(detail);
                }
            }
        }
        return list;
    }

    private void clearData() {
        mTransferIn = null;
        mStockPositionBean = null;
        textPickId.setText("");
        textStockId.setText("");
        mAdapter.setNewData(null);
    }

    private boolean isScanOver() {
        boolean over = true;
        List<TransferIn.DetailsBean> data = mAdapter.getData();
        for (TransferIn.DetailsBean item : data) {
            if (!item.isIsIn()) {
                over = false;
                break;
            }
        }
        return over;
    }


    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        if (mTransferIn == null) {
            ToastUtils.showString("请先扫描调拨单！");
            return;
        }

        if (mStockPositionBean == null) {
            ToastUtils.showString("请先扫描货位号！");
            return;
        }
        List<TransferInStockParam.DetailBean> list = new ArrayList<>();
        for (TransferIn.DetailsBean item : mAdapter.getData()) {
            if (item.isIsIn()) {
                TransferInStockParam.DetailBean detailBean = new TransferInStockParam.DetailBean();
                detailBean.setGoodsId(item.getGoodsId());
                detailBean.setTransferDetailId(item.getDetailId());
                list.add(detailBean);
            }
        }
        if (list.size() == 0) {
            ToastUtils.showString("没有扫描的物品！");
            return;
        }
        TransferInStockParam param = new TransferInStockParam();
        param.setGridId(mStockPositionBean.getId());
        param.setTransferId(mTransferIn.getId());
        User user = SPUtils.getUser();
        param.setOperateUserId(user.getId());
        param.setOperateUserName(user.getName());
        param.setInDetails(list);
        wrapHttp(apiService.transferIn(param))
                .compose(this.bindToLifecycle())
                .subscribe(new RxObserver<Object>(true, this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("操作成功");
                        clearData();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) doBack();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        doBack();
    }

    private void doBack() {
        if (mTransferIn == null) {
            finish();
            return;
        }
        String msg = "";
        if (isScanOver()) {
            msg = "您还未提交，是否确认退出？";
        } else {
            msg = "存在未扫描的物品，是否确认退出？";
        }
        new AlertDialog.Builder(this)
                .setTitle("提示！")
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("否", null)
                .show();
    }
}
