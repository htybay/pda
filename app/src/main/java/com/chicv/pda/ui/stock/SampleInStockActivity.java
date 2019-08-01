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
import com.chicv.pda.adapter.SampleInStockAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.StockInfo;
import com.chicv.pda.bean.StockReceiveBatch;
import com.chicv.pda.bean.User;
import com.chicv.pda.bean.param.SampleInStockParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.SPUtils;
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
 * date: 2019-07-29
 * email: liheyu999@163.com
 * <p>
 * 样品入库
 */
public class SampleInStockActivity extends BaseActivity {

    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;
    @BindView(R.id.text_batch)
    TextView textBatch;
    @BindView(R.id.text_stock_id)
    TextView textStockId;
    @BindView(R.id.text_count)
    TextView textCount;

    private SampleInStockAdapter mAdapter;
    private String mBarcode;
    private StockInfo mStockInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_in);
        ButterKnife.bind(this);
        initToolbar("调样入库");
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new SampleInStockAdapter();
        rlvGoods.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showDeleteDailog(position);
            }
        });
    }

    private void showDeleteDailog(final int position) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定要删除此条记录吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAdapter.remove(position);
                        refreshCountView();
                    }
                }).show();
    }

    private void refreshCountView() {
        textCount.setText(String.valueOf(mAdapter.getTotalScanCount()));
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        mBarcode = barcode;
        if (BarcodeUtils.isStockCode(barcode)) {
            //货位单号
            handleStockBarcode(BarcodeUtils.getBarcodeId(barcode));
        } else if (BarcodeUtils.isGoodsRuleCode(barcode)) {
            //囤货规格 扫到什么就是什么忽略大小写
            handleGoodsRuleBarcode(barcode);
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }

    private void handleGoodsRuleBarcode(String barcode) {
        int position = mAdapter.findPositionByBatchNo(barcode);
        if (position < 0) {
            getReceiveBatch(barcode);
            return;
        }
        StockReceiveBatch stockReceiveBatch = mAdapter.getData().get(position);
        if (stockReceiveBatch.getScanCount() >= stockReceiveBatch.getWaitReceiveCount()) {
            ToastUtils.showString("此囤货规格已扫完,不需要再扫了！");
            SoundUtils.playError();
        } else {
            stockReceiveBatch.setScanCount(stockReceiveBatch.getScanCount() + 1);
            mAdapter.notifyDataSetChanged();
            rlvGoods.getLayoutManager().smoothScrollToPosition(rlvGoods, null, position);
            refreshCountView();
            SoundUtils.playSuccess();
        }
    }

    private void handleStockBarcode(final int barcodeId) {
        wrapHttp(apiService.getViewGrid(barcodeId))
                .compose(this.<StockInfo>bindToLifecycle())
                .subscribe(new RxObserver<StockInfo>(this) {
                    @Override
                    public void onSuccess(StockInfo value) {
                        if (value.getRoomId() != 9) {
                            ToastUtils.showString("货位不属于调样库房");
                            SoundUtils.playError();
                            return;
                        }
                        textStockId.setText(mBarcode);
                        mStockInfo = value;
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        super.onFailure(msg);
                        textStockId.setText("");
                        mStockInfo = null;
                        SoundUtils.playError();
                    }
                });
    }

    private void getReceiveBatch(final String barcode) {
        wrapHttp(apiService.getReceiveBatch(barcode))
                .compose(this.<StockReceiveBatch>bindToLifecycle())
                .subscribe(new RxObserver<StockReceiveBatch>(this) {
                    @Override
                    public void onSuccess(StockReceiveBatch value) {
                        if (!value.isSampleOrder()) {
                            ToastUtils.showString("物品不属于样衣物品");
                            textBatch.setText("");
                            SoundUtils.playError();
                            return;
                        }
                        value.setScanCount(1);
                        value.setBatchCode(barcode);
                        mAdapter.addData(0, value);
                        rlvGoods.getLayoutManager().smoothScrollToPosition(rlvGoods, null, 0);
                        textBatch.setText(mBarcode);
                        refreshCountView();
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        textBatch.setText("");
                        SoundUtils.playError();
                    }
                });
    }

    @OnClick({R.id.btn_clear, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_clear:
                clearData();
                break;
            case R.id.btn_commit:
                commit();
                break;
        }
    }

    private void clearData() {
        mAdapter.setNewData(null);
        mStockInfo = null;
        textBatch.setText("");
        textStockId.setText("");
        textCount.setText("");
    }

    private void commit() {
        if (mAdapter.getData().size() == 0) {
            ToastUtils.showString("没有待上架的物品");
            return;
        }
        if (mStockInfo == null) {
            ToastUtils.showString("请扫描上架货位");
            return;
        }
        wrapHttp(apiService.sampleInStock(getSampleInStockParam()))
                .compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("操作成功");
                        clearData();
                    }
                });
    }

    private List<SampleInStockParam> getSampleInStockParam() {
        User user = SPUtils.getUser();
        List<SampleInStockParam> list = new ArrayList<>();
        for (StockReceiveBatch item : mAdapter.getData()) {
            SampleInStockParam sample = new SampleInStockParam();
            sample.setStockReceiveBatchId(item.getStockReceiveBatchId());
            sample.setBatchCode(item.getBatchCode());
            sample.setStockNums(item.getScanCount());
            sample.setOperateUserId(user.getId());
            sample.setGridId(String.valueOf(mStockInfo.getId()));
            list.add(sample);
        }
        return list;
    }
}
