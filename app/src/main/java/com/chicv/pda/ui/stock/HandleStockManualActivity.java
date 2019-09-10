package com.chicv.pda.ui.stock;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.adapter.HandleStockManualAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.PositionGoods;
import com.chicv.pda.bean.StockLimit;
import com.chicv.pda.bean.param.GoodsBatchParam;
import com.chicv.pda.bean.param.HandleStockParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
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
 * date: 2019-08-28
 * email: liheyu999@163.com
 * 手动理库
 */
public class HandleStockManualActivity extends BaseActivity {

    @BindView(R.id.text_stock_handle)
    TextView textStockHandle;
    @BindView(R.id.text_stock_new)
    TextView textStockNew;
    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;

    private HandleStockManualAdapter mAdapter;
    private String mBarcode;
    private List<PositionGoods> mOriginList;
    private int mStockHandleId;
    private int mStockNewId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_manual);
        ButterKnife.bind(this);
        initToolbar("手动理库");
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new HandleStockManualAdapter();
        rlvGoods.setAdapter(mAdapter);
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        mBarcode = barcode;
        if (BarcodeUtils.isStockCode(barcode)) {
            if (mOriginList == null) {
                //理库货位
                getBatchGoods(BarcodeUtils.getBarcodeId(barcode));
            } else {
                //新货位
                getLimitInfo(BarcodeUtils.getBarcodeId(barcode));
            }
        } else if (BarcodeUtils.isGoodsRuleCode(barcode)) {
            //囤货规格
            handleGoodsRuleBarcode(barcode);
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }

    private void getLimitInfo(final int stockId) {
        if (mAdapter.getData().size() == 0) {
            ToastUtils.showString("请先扫描囤货规格");
            SoundUtils.playError();
            return;
        }
        if (mStockHandleId == stockId) {
            ToastUtils.showString("新旧货位不能相同");
            SoundUtils.playError();
            return;
        }
        final PositionGoods currentGoods = mAdapter.getData().get(0);
        wrapHttp(apiService.getStockLimit(stockId, currentGoods.getBatchCode()))
                .compose(this.<StockLimit>bindToLifecycle())
                .subscribe(new RxObserver<StockLimit>(true, this) {
                    @Override
                    public void onSuccess(StockLimit value) {
                        if (value.getType() == 1 && currentGoods.getScanCount() <= value.getNum()) {
                            ToastUtils.showString("理库数量小于此货位最少入库数量");
                            SoundUtils.playError();
                            textStockNew.setText("");
                            mStockNewId = 0;
                            return;
                        }
                        if (value.getType() == 2 && currentGoods.getScanCount() >= value.getNum()) {
                            ToastUtils.showString("理库数量大于此货位最多入库数量");
                            SoundUtils.playError();
                            textStockNew.setText("");
                            mStockNewId = 0;
                            return;
                        }
                        textStockNew.setText(mBarcode);
                        mStockNewId = stockId;
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                        textStockNew.setText("");
                        mStockNewId = 0;
                    }
                });
    }

    private void handleGoodsRuleBarcode(final String barcode) {
        if (mOriginList == null) {
            ToastUtils.showString("请先扫描理库货位");
            SoundUtils.playError();
            return;
        }
        boolean isExist = false;
        PositionGoods scanGoods = null;
        for (PositionGoods goodsDetail : mOriginList) {
            if (barcode.equalsIgnoreCase(goodsDetail.getBatchCode())) {
                isExist = true;
                if (!goodsDetail.isReturn()) {
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
            ToastUtils.showString("该囤货规格已扫完");
            SoundUtils.playError();
            return;
        }

        List<PositionGoods> data = mAdapter.getData();
        if (data.size() == 0) {
            //第一次扫描
            scanGoods.setScanCount(1);
            scanGoods.setIsReturn(true);
            scanGoods.setTotalCount(getSameBatchCount(scanGoods.getBatchCode()));
            mAdapter.addData(scanGoods);
            SoundUtils.playSuccess();
        } else {
            PositionGoods item = mAdapter.getItem(0);
            if (TextUtils.equals(item.getBatchCode(), barcode)) {
                scanGoods.setIsReturn(true);
                mAdapter.setScanBatch(barcode);
                mAdapter.notifyDataSetChanged();
                SoundUtils.playSuccess();
            } else {
                showChangeDialog(scanGoods);
            }
        }
    }

    private void showChangeDialog(final PositionGoods scanGoods) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("更换囤货规格?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //更换时全换设为未扫 参考WINCE
                        for (PositionGoods item : mOriginList) {
                            item.setIsReturn(false);
                        }
                        scanGoods.setScanCount(1);
                        scanGoods.setTotalCount(getSameBatchCount(scanGoods.getBatchCode()));
                        scanGoods.setIsReturn(true);
                        mAdapter.setNewData(null);
                        mAdapter.addData(scanGoods);
                        SoundUtils.playSuccess();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtils.showString("囤货规格不一致,重新扫描");
                        SoundUtils.playError();
                    }
                })
                .show();
    }

    private int getSameBatchCount(String barcode) {
        int count = 0;
        if (mOriginList != null) {
            for (PositionGoods item : mOriginList) {
                if (TextUtils.equals(item.getBatchCode().toLowerCase(), barcode.toLowerCase())) {
                    count++;
                }
            }
        }
        return count;
    }


    private void getBatchGoods(final int barcodeId) {
        wrapHttp(apiService.stockCardingHandleGridInfo(barcodeId))
                .compose(this.<List<PositionGoods>>bindToLifecycle())
                .subscribe(new RxObserver<List<PositionGoods>>(this) {
                    @Override
                    public void onSuccess(List<PositionGoods> value) {
                        if (value.size() == 0) {
                            ToastUtils.showString("该货位不存在需要理库的物品");
                            SoundUtils.playError();
                            return;
                        }
                        mOriginList = value;
                        mStockHandleId = barcodeId;
                        textStockHandle.setText(mBarcode);
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        super.onFailure(msg);
                        mOriginList = null;
                        mStockHandleId = 0;
                        textStockHandle.setText("");
                        SoundUtils.playError();
                    }
                });
    }


    @OnClick({R.id.btn_commit, R.id.btn_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                commit();
                break;
            case R.id.btn_clear:
                clearData();
                break;
        }
    }

    private void commit() {
        if (mStockHandleId == 0 || mStockNewId == 0) {
            ToastUtils.showString("请扫描货位后再提交！");
            SoundUtils.playError();
            return;
        }
        if (mOriginList == null || mOriginList.size() == 0 || mAdapter.getData().size() == 0) {
            ToastUtils.showString("请先扫描囤货规格");
            SoundUtils.playError();
            return;
        }

        HandleStockParam param = new HandleStockParam();
        param.setNewGrid(mStockNewId);
        param.setOldGrid(mStockHandleId);
        List<GoodsBatchParam> goodsList = new ArrayList<>();
        PositionGoods scanGoods = mAdapter.getData().get(0);
        for (PositionGoods item : mOriginList) {
            if (TextUtils.equals(scanGoods.getBatchCode(), item.getBatchCode()) && item.isReturn()) {
                GoodsBatchParam goodsBatchParam = new GoodsBatchParam();
                goodsBatchParam.setGoodsId(item.getGoodsId());
                goodsBatchParam.setBatchCode(item.getBatchCode());
                goodsList.add(goodsBatchParam);
            }
        }
        param.setGoodsBatchList(goodsList);
        wrapHttp(apiService.stockCardingHandleMove(param)).compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("操作成功");
                        SoundUtils.playSuccess();
                        clearData();
                    }

                    @Override
                    public void onFailure(String msg) {
                        super.onFailure(msg);
                        SoundUtils.playError();
                    }
                });
    }

    private void clearData() {
        mStockHandleId = 0;
        mStockNewId = 0;
        mOriginList = null;
        mAdapter.setNewData(null);
        textStockHandle.setText("");
        textStockNew.setText("");
    }
}
