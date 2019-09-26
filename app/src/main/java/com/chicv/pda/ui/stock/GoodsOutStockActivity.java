package com.chicv.pda.ui.stock;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chicv.pda.R;
import com.chicv.pda.adapter.BackStockAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.OutGoodsInfo;
import com.chicv.pda.bean.User;
import com.chicv.pda.bean.param.GoodsOutParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.SPUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-09-17
 * email: liheyu999@163.com
 */
public class GoodsOutStockActivity extends BaseActivity {

    @BindView(R.id.text_pick_num)
    TextView textPickNum;
    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;
    @BindView(R.id.text_count)
    TextView textCount;

    private String mBarcode;
    private BackStockAdapter mAdapter;
    private int mWaitCount;
    private int mPickId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_out_stock);
        ButterKnife.bind(this);
        initToolbar("退货物品出库");
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new BackStockAdapter();
        rlvGoods.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.btn_delete) {
                    adapter.remove(position);
                    refreshTotalText();
                }
            }
        });
    }

    private void refreshTotalText() {
        if (mWaitCount > 0) {
            textCount.setVisibility(View.VISIBLE);
            textCount.setText(String.format(Locale.CHINA, "当前%d件，剩余%d件物品待出库", mAdapter.getData().size(), mWaitCount - mAdapter.getData().size()));
        } else {
            textCount.setVisibility(View.GONE);
            textCount.setText("");
        }
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        mBarcode = barcode;
        if (BarcodeUtils.isPickCode(barcode)) {
            //捡货单号
            checkPickId(BarcodeUtils.getBarcodeId(barcode));
        } else if (BarcodeUtils.isGoodsCode(barcode)) {
            //物品单号
            handleGoodsBarcode(BarcodeUtils.getBarcodeId(barcode));
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }

    private void handleGoodsBarcode(int barcodeId) {
        if (mPickId == 0) {
            ToastUtils.showString("请先扫描退货单");
            SoundUtils.playError();
            return;
        }
        if (mWaitCount != 0 && mAdapter.getData().size() >= mWaitCount) {
            ToastUtils.showString("已扫描物品数量大于该拣货单特出库物品数量，请核实");
            SoundUtils.playError();
            return;
        }

        wrapHttp(apiService.getGoodsOut(barcodeId, mPickId))
                .compose(this.<OutGoodsInfo>bindToLifecycle())
                .subscribe(new RxObserver<OutGoodsInfo>(this) {
                    @Override
                    public void onSuccess(OutGoodsInfo value) {
                        if (mAdapter.existGoods(value.getGoodsId())) {
                            ToastUtils.showString("物品已扫描，不需要重复扫描！");
                            SoundUtils.playError();
                            return;
                        }
                        mAdapter.addData(0, value);
                        refreshTotalText();
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        super.onFailure(msg);
                        SoundUtils.playError();
                    }
                });
    }

    private void getBackPickInfo(final int barcodeId) {
        wrapHttp(apiService.getBackPick(barcodeId))
                .compose(this.<Integer>bindToLifecycle())
                .subscribe(new RxObserver<Integer>(this) {
                    @Override
                    public void onSuccess(Integer value) {
                        if (value == 0) {
                            ToastUtils.showString("该拣货单上没有待出库的物品!");
                            SoundUtils.playError();
                            clearView();
                            return;
                        }
                        mWaitCount = value;
                        mPickId = barcodeId;
                        textPickNum.setText(mBarcode);
                        refreshTotalText();
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        super.onFailure(msg);
                        clearView();
                        SoundUtils.playError();
                    }
                });
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
                            getBackPickInfo(barcodeId);
                        }
                    })
                    .show();
        } else {
            getBackPickInfo(barcodeId);
        }
    }

    private void clearView() {
        mWaitCount = 0;
        mPickId = 0;
        mAdapter.setNewData(null);
        textPickNum.setText("");
        refreshTotalText();
    }


    @OnClick({R.id.btn_clear, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_clear:
                clearView();
                break;
            case R.id.btn_commit:
                commit();
                break;
        }
    }

    private void commit() {
        if (mPickId == 0) {
            ToastUtils.showString("请先扫描拣货单!");
            return;
        }
        if (mAdapter.getData().size() == 0) {
            ToastUtils.showString("请先扫描要出库的物品!");
            return;
        }

        User user = SPUtils.getUser();
        GoodsOutParam outParam = new GoodsOutParam();
        outParam.setOperator(user.getName());
        List<GoodsOutParam.GoodsParam> goodsList = new ArrayList<>();
        outParam.setParameter(goodsList);
        for (OutGoodsInfo item : mAdapter.getData()) {
            GoodsOutParam.GoodsParam goodsParam = new GoodsOutParam.GoodsParam();
            goodsParam.setGoodsId(item.getGoodsId());
            goodsParam.setOperateUserId(user.getId());
            goodsParam.setPickId(mPickId);
            goodsList.add(goodsParam);
        }
        wrapHttp(apiService.goodsManyOut(outParam))
                .compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("出库成功");
                        clearView();
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        super.onFailure(msg);
                        SoundUtils.playError();
                    }
                });
    }
}
