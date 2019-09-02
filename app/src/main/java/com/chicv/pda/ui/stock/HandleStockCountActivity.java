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
import com.chicv.pda.adapter.HandleStockCountAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.PositionGoods;
import com.chicv.pda.bean.param.GoodsBatchParam;
import com.chicv.pda.bean.param.HandleStockParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-08-29
 * email: liheyu999@163.com
 */
public class HandleStockCountActivity extends BaseActivity {

    @BindView(R.id.text_stock)
    TextView textStock;
    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;
    @BindView(R.id.text_count)
    TextView textCount;

    private HandleStockCountAdapter mAdapter;
    private String mBarcode;
    private List<PositionGoods> mOriginList = new ArrayList<>();
    private int mStockId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_stock_count);
        ButterKnife.bind(this);
        initToolbar("理库点数");
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new HandleStockCountAdapter();
        rlvGoods.setAdapter(mAdapter);
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        mBarcode = barcode;
        if (BarcodeUtils.isStockCode(barcode)) {
            int stockId = BarcodeUtils.getBarcodeId(barcode);
            if (mOriginList != null && mOriginList.size() > 0) {
                showAlertDialog(stockId);
            } else {
                getBatchGoods(stockId);
            }
        } else if (BarcodeUtils.isGoodsRuleCode(barcode)) {
            //囤货规格 扫到什么就是什么忽略大小写
            handleGoodsRuleBarcode(barcode);
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }

    private void handleGoodsRuleBarcode(String barcode) {
        if (mOriginList == null) {
            ToastUtils.showString("请扫描货位");
            SoundUtils.playError();
            return;
        }
        boolean isExist = false;
        PositionGoods scanGoods = null;
        for (PositionGoods goodsDetail : mOriginList) {
            if (TextUtils.equals(goodsDetail.getBatchCode().toLowerCase(), barcode.toLowerCase())) {
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
        SoundUtils.playSuccess();
        scanGoods.setIsReturn(true);
        mAdapter.setScanBatch(scanGoods.getBatchCode());
        mAdapter.notifyDataSetChanged();
        refreshCountText();
    }

    private void showAlertDialog(final int stockId) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确认重新加载货位数据？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getBatchGoods(stockId);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void getBatchGoods(final int barcodeId) {
        wrapHttp(apiService.stockCardingHandleGridInfo(barcodeId))
                .compose(this.<List<PositionGoods>>bindToLifecycle())
                .subscribe(new RxObserver<List<PositionGoods>>(this) {
                    @Override
                    public void onSuccess(List<PositionGoods> value) {
                        handleGoods(value);
                        SoundUtils.playSuccess();
                        mStockId = barcodeId;
                    }

                    @Override
                    public void onFailure(String msg) {
                        super.onFailure(msg);
                        clearData();
                        SoundUtils.playError();
                    }
                });
    }

    private void clearData() {
        mStockId = 0;
        mOriginList.clear();
        textStock.setText("");
        mAdapter.setNewData(null);
        refreshCountText();
    }

    private void handleGoods(List<PositionGoods> value) {
        mOriginList = value;
        List<PositionGoods> list = new ArrayList<>();
        List<String> batchs = new ArrayList<>();
        Map<String, PositionGoods> batchMap = new HashMap<>();
        for (PositionGoods item : value) {
            String batchCode = item.getBatchCode();
            if (batchs.contains(batchCode)) {
                PositionGoods positionGoods = batchMap.get(batchCode);
                positionGoods.setTotalCount(positionGoods.getTotalCount() + 1);
                if (item.isReturn()) {
                    positionGoods.setScanCount(positionGoods.getScanCount() + 1);
                }
            } else {
                if (item.isReturn()) {
                    item.setScanCount(1);
                }
                item.setTotalCount(1);
                batchs.add(batchCode);
                list.add(item);
                batchMap.put(batchCode, item);
            }
        }
        mAdapter.setNewData(list);
        textStock.setText(mBarcode);
        refreshCountText();
    }

    private void refreshCountText() {
        List<PositionGoods> data = mAdapter.getData();
        if (data.size() == 0) {
            textCount.setText("");
            textCount.setVisibility(View.GONE);
            return;
        }
        int totalCount = 0;
        int scanCount = 0;
        for (PositionGoods item : data) {
            totalCount += item.getTotalCount();
            scanCount += item.getScanCount();
        }
        textCount.setVisibility(View.VISIBLE);
        textCount.setText(String.format(Locale.CHINA, "总件数:%d 已扫：%d 未扫:%d", totalCount, scanCount, totalCount - scanCount));
    }


    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        if (mOriginList == null || mOriginList.size() == 0) {
            ToastUtils.showString("没有需要提交的数据");
            return;
        }
        if (!isScanOver()) {
            showConfirmDialog();
        } else {
            commit();
        }
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("存在未扫描的数据,是否提交？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        commit();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void commit() {
        HandleStockParam param = new HandleStockParam();
        param.setOldGrid(mStockId);
        List<GoodsBatchParam> goodslist = new ArrayList<>();
        for (PositionGoods goods : mOriginList) {
            GoodsBatchParam batchParam = new GoodsBatchParam();
            batchParam.setBatchCode(goods.getBatchCode());
            batchParam.setGoodsId(goods.getGoodsId());
            batchParam.setIsMoveDown(goods.isReturn());
            goodslist.add(batchParam);
        }
        param.setGoodsBatchList(goodslist);
        wrapHttp(apiService.stockCardingHandleCount(param))
                .compose(bindToLifecycle())
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

    private boolean isScanOver() {
        if (mOriginList != null) {
            for (PositionGoods item : mOriginList) {
                if (!item.isReturn()) {
                    return false;
                }
            }
        }
        return true;
    }
}
