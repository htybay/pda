package com.chicv.pda.ui.transferGoods;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.adapter.TransferReceiveAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.TransferPick;
import com.chicv.pda.bean.TransferPickGoods;
import com.chicv.pda.bean.param.TransferReceiveParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.SPUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-06-14
 * email: liheyu999@163.com
 * <p>
 * 调拨收货
 */
public class TransferReceiveActivity extends BaseActivity {

    @BindView(R.id.text_pick_id)
    TextView textPickId;
    @BindView(R.id.text_total)
    TextView textTotal;
    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;

    private String mBarcode;
    private TransferReceiveAdapter mAdapter;
    private TransferPick mTransferPick;
    //囤货规格物品集合
    private Collection<TransferPickGoods> batchList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_receive);
        ButterKnife.bind(this);
        initToolbar("调拨收货");
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new TransferReceiveAdapter();
        rlvGoods.setAdapter(mAdapter);
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        mBarcode = barcode;
        if (BarcodeUtils.isTransferCode(barcode)) {
            //调拨单号
            getTransfer(BarcodeUtils.getBarcodeId(barcode));
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


    //扫描到物品条码
    private void handleGoodsBarcode(long goodsId) {
        if (mTransferPick == null) {
            ToastUtils.showString("请先扫描调拨单！");
            SoundUtils.playError();
            return;
        }

        List<TransferPickGoods> data = mAdapter.getData();

        TransferPickGoods scanGoods = null;
        for (TransferPickGoods goodsDetail : data) {
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

        if (scanGoods.isIsSign()) {
            ToastUtils.showString("该物品已扫描!");
            SoundUtils.playError();
            return;
        }
        SoundUtils.playSuccess();
        scanGoods.setIsSign(true);
        mAdapter.notifyDataSetChanged();
        if (isScanOver()) {
            ToastUtils.showString("已全部收货,请提交");
        }
        //扫描的物品ID刚好是囤货规格的物品ID，询问后得知有囤货规格不可能扫到物品，此种情况暂未处理，
    }

    // 扫描到囤货规格条码
    private void handleGoodsRuleBarcode(String barcode) {
        if (mTransferPick == null) {
            ToastUtils.showString("请先扫描调拨单！");
            SoundUtils.playError();
            return;
        }

        TransferPickGoods scanGoods = null;
        for (TransferPickGoods goodsDetail : batchList) {
            if (barcode.equalsIgnoreCase(goodsDetail.getBatchCode())) {
                scanGoods = goodsDetail;
                break;
            }
        }

        if (scanGoods == null) {
            ToastUtils.showString("该货位上没有此囤货规格的物品！");
            SoundUtils.playError();
            return;
        }
        if (scanGoods.getLocalTotalCount() == scanGoods.getLocalScanCount()) {
            ToastUtils.showString("该囤货规格已拣完");
            SoundUtils.playError();
            return;
        }
        SoundUtils.playSuccess();
        scanGoods.setLocalScanCount(scanGoods.getLocalScanCount() + 1);
        mAdapter.notifyDataSetChanged();
        if (isScanOver()) {
            ToastUtils.showString("已全部收货,请提交");
        }
    }

    private void getTransfer(int barcodeId) {
        wrapHttp(apiService.getTransfer(barcodeId))
                .compose(this.<TransferPick>bindToLifecycle())
                .subscribe(new RxObserver<TransferPick>(true,this) {
                    @Override
                    public void onSuccess(TransferPick value) {
                        handleData(value);
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
        batchList = null;
        textPickId.setText("");
        textTotal.setText("");
        mAdapter.setNewData(null);
    }

    //过虑掉已删除的，没有规格的放到一起，相同规格的只放一个，但是数量相加
    private void handleData(TransferPick value) {
        int normalTotal = 0;
        List<TransferPickGoods> details = value.getDetails();
        List<TransferPickGoods> goodsList = new ArrayList<>();
        Map<String, TransferPickGoods> batchMap = new HashMap<>();
        for (TransferPickGoods detail : details) {
            if (detail.isIsDelete()) {
                continue;
            }
            normalTotal++;
            String batchCode = detail.getBatchCode();
            if (TextUtils.isEmpty(batchCode)) {
                goodsList.add(detail);
            } else {
                if (batchMap.containsKey(batchCode)) {
                    TransferPickGoods goods = batchMap.get(batchCode);
                    goods.setLocalTotalCount(goods.getLocalTotalCount() + 1);
                    if (detail.isIsSign()) {
                        goods.setLocalScanCount(goods.getLocalScanCount() + 1);
                    }
                } else {
                    detail.setLocalTotalCount(1);
                    if (detail.isIsSign()) {
                        detail.setLocalScanCount(1);
                    }
                    batchMap.put(batchCode, detail);
                }
            }
        }
        mTransferPick = value;
        batchList = batchMap.values();
        goodsList.addAll(batchList);
        Collections.sort(goodsList);
        mAdapter.setNewData(goodsList);
        textTotal.setText(String.valueOf(normalTotal));
        textPickId.setText(mBarcode);
    }

    private boolean isScanOver() {
        boolean over = true;
        List<TransferPickGoods> data = mAdapter.getData();
        for (TransferPickGoods item : data) {
            if (TextUtils.isEmpty(item.getBatchCode())) {
                if (!item.isIsSign()) {
                    over = false;
                    break;
                }
            } else {
                if (item.getLocalTotalCount() != item.getLocalScanCount()) {
                    over = false;
                    break;
                }
            }
        }
        return over;
    }


    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        if (mTransferPick == null) {
            ToastUtils.showString("请先扫描调拨单！");
            return;
        }
        if (!isScanOver()) {
            ToastUtils.showString("存在未扫描物品！");
            return;
        }
        TransferReceiveParam param = new TransferReceiveParam(mTransferPick.getId(), SPUtils.getUser().getName());
        wrapHttp(apiService.transferReceive(param))
                .compose(this.bindToLifecycle())
                .subscribe(new RxObserver<Object>(true, this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("收货完成");
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
        if (mTransferPick == null) {
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
