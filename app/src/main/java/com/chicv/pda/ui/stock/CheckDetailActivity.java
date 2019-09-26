package com.chicv.pda.ui.stock;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.adapter.CheckDetailAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.TakingSubTask;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.PdaUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-09-23
 * email: liheyu999@163.com
 */
public class CheckDetailActivity extends BaseActivity {

    public static final String KEY_CHECK_ID = "key_check_id";

    @BindView(R.id.text_stock)
    TextView textStock;
    @BindView(R.id.text_stock_des)
    TextView textStockDes;
    @BindView(R.id.text_wait_num)
    TextView textWaitNum;
    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;
    @BindView(R.id.text_count)
    TextView textCount;

    private int mCheckId;
    private CheckDetailAdapter mAdapter;
    private TakingSubTask mTakingSubTask;
    private int mStockId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_detial);
        ButterKnife.bind(this);
        mCheckId = getIntent().getIntExtra(KEY_CHECK_ID, 0);
        if (mCheckId == 0) finish();
        initToolbar("盘点操作");
        initView();
        initData();
    }

    private void initData() {
        wrapHttp(apiService.getSubtask(mCheckId))
                .compose(this.<TakingSubTask>bindToLifecycle())
                .subscribe(new RxObserver<TakingSubTask>(this) {
                    @Override
                    public void onSuccess(TakingSubTask value) {
                        handleData(value);
                    }

                    @Override
                    public void onFailure(String msg) {
                        finish();
                    }
                });
    }

    private void handleData(TakingSubTask value) {
        mTakingSubTask = value;
        textStock.setText(BarcodeUtils.generateHWBarcode(value.getGridId()));
        textStockDes.setText(value.getGridDescription());
        textWaitNum.setText(String.valueOf(value.getRemainderCount()));
    }

    private void refreshCountView() {
        if (mAdapter.getData().size() == 0) {
            textCount.setText("");
            textCount.setVisibility(View.GONE);
            return;
        }
        int readyCount = 0;
        int checkedCount = 0;
        int extraCount = 0;
        for (TakingSubTask.StockTakingRecord item : mAdapter.getData()) {
            if (item.getCheckStatus() == PdaUtils.CHECK_STATUS_READY) {
                readyCount++;
            } else if (item.getCheckStatus() == PdaUtils.CHECK_STATUS_CHECKED) {
                checkedCount++;
            } else if (item.getCheckStatus() == PdaUtils.CHECK_STATUS_EXTRA) {
                extraCount++;
            }
        }
        textCount.setText(String.format(Locale.CHINA, "待盘：%d  已盘：%d  多盘：%d", readyCount, checkedCount, extraCount));
        textCount.setVisibility(View.VISIBLE);
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new CheckDetailAdapter();
        rlvGoods.setAdapter(mAdapter);
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        if (BarcodeUtils.isStockCode(barcode)) {
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

    private void handleGoodsRuleBarcode(String barcode) {
        if (mStockId == 0) {
            ToastUtils.showString("请先扫描货位编号");
            SoundUtils.playError();
            return;
        }
        TakingSubTask.StockTakingRecord goods = null;
        for (TakingSubTask.StockTakingRecord item : mAdapter.getData()) {
            if (barcode.equalsIgnoreCase(item.getBatchCode()) && item.getCheckStatus() == PdaUtils.CHECK_STATUS_READY) {
                goods = item;
                break;
            }
        }

        if (goods != null) {
            goods.setCheckStatus(PdaUtils.CHECK_STATUS_CHECKED);
            goods.setGoodsType(1);
            mAdapter.notifyDataSetChanged();
            refreshCountView();
            SoundUtils.playSuccess();
        } else {
            showConfirmBatchCodeDialog(barcode);
        }
    }

    private void showConfirmBatchCodeDialog(final String barcode) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(String.format(Locale.CHINA, "多出的囤货规格:%s ?", barcode))
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addBatchCodeRecord(barcode);
                        SoundUtils.playSuccess();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SoundUtils.playError();
            }
        }).show();
    }

    private void addBatchCodeRecord(String barcode) {
        TakingSubTask.StockTakingRecord record = new TakingSubTask.StockTakingRecord();
        record.setBatchCode(barcode);
        record.setCheckStatus(PdaUtils.CHECK_STATUS_EXTRA);
        mAdapter.addData(0, record);
        refreshCountView();
    }

    private void handleGoodsBarcode(int barcodeId) {
        if (mStockId == 0) {
            ToastUtils.showString("请先扫描货位编号");
            SoundUtils.playError();
            return;
        }

        TakingSubTask.StockTakingRecord goods = null;
        for (TakingSubTask.StockTakingRecord item : mAdapter.getData()) {
            if (item.getGoodsId() == barcodeId) {
                goods = item;
                break;
            }
        }
        if (goods != null) {
            if (goods.getCheckStatus() != PdaUtils.CHECK_STATUS_READY) {
                ToastUtils.showString("该物品已扫描！");
                SoundUtils.playError();
                return;
            }
            goods.setCheckStatus(PdaUtils.CHECK_STATUS_CHECKED);
            goods.setGoodsType(0);
            mAdapter.notifyDataSetChanged();
            refreshCountView();
            SoundUtils.playSuccess();
        } else {
            showConfirmWPDialog(barcodeId);
        }
    }

    private void showConfirmWPDialog(final int barcodeId) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(String.format(Locale.CHINA, "物品%s不属于此货物，将物品盘入此货位?", BarcodeUtils.generateWPBarcode(barcodeId)))
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addWpRecord(barcodeId);
                        SoundUtils.playSuccess();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SoundUtils.playError();
            }
        }).show();
    }

    private void addWpRecord(int barcodeId) {
        TakingSubTask.StockTakingRecord record = new TakingSubTask.StockTakingRecord();
        record.setGoodsId(barcodeId);
        record.setCheckStatus(PdaUtils.CHECK_STATUS_EXTRA);
        mAdapter.addData(0, record);
        refreshCountView();
    }

    private void handleStockBarcode(int barcodeId) {
        if (mTakingSubTask != null && mTakingSubTask.getGridId() != barcodeId) {
            ToastUtils.showString("请扫描正确的货位");
            SoundUtils.playError();
            return;
        }
        if (mAdapter.getData().size() > 0) return;
        mAdapter.setNewData(mTakingSubTask.getDetails());
        refreshCountView();
        mStockId = barcodeId;
    }

    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        showConfirmCommitDialog();
    }

    private void showConfirmCommitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确认提交?")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        commit();
                    }
                }).setNegativeButton("取消", null)
                .show();
    }

    private void commit() {
        mTakingSubTask.setDetails(mAdapter.getData());
        wrapHttp(apiService.submitSubtaskRecords(mTakingSubTask))
                .compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(this) {
                    @Override
                    public void onSuccess(Object value) {
                        if (mTakingSubTask.getRemainderCount() == 1) {
                            ToastUtils.showString("此盘点任务完成");
                            finish();
                        } else {
                            mStockId = 0;
                            mAdapter.setNewData(null);
                            refreshCountView();
                            initData();
                        }
                    }
                });
    }
}
