package com.chicv.pda.ui.goods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.adapter.BatchManyMovelAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.BatchMoveBean;
import com.chicv.pda.bean.param.BatchManyMoveStockParam;
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
 * date: 2019-06-12
 * email: liheyu999@163.com
 * <p>
 * 囤货物品批量移位
 */
public class BatchManyMoveStockActivity extends BaseActivity {


    @BindView(R.id.text_stock_old)
    TextView textStockOld;
    @BindView(R.id.text_stock_new)
    TextView textStockNew;
    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;

    private int mOldStockId;
    private int mNewStockId;

    private BatchManyMovelAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_many_move_stock);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initToolbar("囤货批量移位");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new BatchManyMovelAdapter();
        rlvGoods.setAdapter(mAdapter);
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
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

    private void handleGoodsRuleBarcode(final String barcode) {
        if (mOldStockId <= 0) {
            //必须扫了旧货位
            ToastUtils.showString("请扫描旧货位编号");
            SoundUtils.playError();
            return;
        }
        BatchMoveBean goods = null;
        for (BatchMoveBean item : mAdapter.getData()) {
            if (barcode.equalsIgnoreCase(item.getBatchCode())) {
                goods = item;
                break;
            }
        }
        if (goods != null) {
            if (goods.getScanCount() < goods.getWaitMoveCount()) {
                goods.setScanCount(goods.getScanCount() + 1);
                mAdapter.notifyDataSetChanged();
                SoundUtils.playSuccess();
            } else {
                ToastUtils.showString("该囤货规格已超过最大移位数量，请检查!");
                SoundUtils.playError();
            }
        } else {
            getWaitMoveCount(barcode);
        }
    }

    private void getWaitMoveCount(final String barcode) {
        wrapHttp(apiService.getWaitMoveQuantity(mOldStockId, barcode))
                .compose(this.<Integer>bindToLifecycle())
                .subscribe(new RxObserver<Integer>(true, this) {
                    @Override
                    public void onSuccess(Integer value) {
                        if (value == 0) {
                            ToastUtils.showString("该囤货规格待移位数量为0，请检查");
                            SoundUtils.playError();
                            return;
                        }

                        BatchMoveBean bean = new BatchMoveBean();
                        bean.setScanCount(1);
                        bean.setWaitMoveCount(value);
                        bean.setBatchCode(barcode);
                        mAdapter.addData(0, bean);
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                    }
                });
    }


    private void handleStockBarcode(final int stockId) {
        if (mOldStockId <= 0) {
            //扫第一次货位为旧货位
            mOldStockId = stockId;
            textStockOld.setText(BarcodeUtils.generateHWBarcode(stockId));
            SoundUtils.playSuccess();
            return;
        }

        //第二次扫货位
        if (mAdapter.getData().size() == 0) {
            ToastUtils.showString("请先扫描囤货规格");
            SoundUtils.playError();
            return;
        }

        if (stockId == mOldStockId) {
            ToastUtils.showString("新旧货位不能相同");
            SoundUtils.playError();
            return;
        }
        mNewStockId = stockId;
        textStockNew.setText(BarcodeUtils.generateHWBarcode(stockId));
        SoundUtils.playSuccess();
    }

    private void checkData() {
        if (mOldStockId == 0) {
            ToastUtils.showString("请扫描旧货位编号");
            return;
        }
        if (mNewStockId == 0) {
            ToastUtils.showString("请扫描新货位编号");
            return;
        }
        if (mAdapter.getData().size() == 0) {
            ToastUtils.showString("请扫描囤货规格");
            return;
        }
        commit();
    }

    private void commit() {
        BatchManyMoveStockParam param = new BatchManyMoveStockParam();
        param.setOldGridId(mOldStockId);
        param.setNewGridId(mNewStockId);
        param.setOperateUserId(SPUtils.getUser().getId());
        List<BatchManyMoveStockParam.MoveDetail> list = new ArrayList<>();
        param.setDetails(list);
        for (BatchMoveBean item : mAdapter.getData()) {
            BatchManyMoveStockParam.MoveDetail moveDetail = new BatchManyMoveStockParam.MoveDetail();
            moveDetail.setBatchCode(item.getBatchCode());
            moveDetail.setQuantity(item.getScanCount());
            list.add(moveDetail);
        }
        wrapHttp(apiService.batchManyMoveStock(param))
                .compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(true, this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("移库成功！");
                        clearData();
                    }
                });
    }

    private void clearData() {
        mNewStockId = 0;
        mOldStockId = 0;
        mAdapter.setNewData(null);
        textStockOld.setText("");
        textStockNew.setText("");
    }


    @OnClick(R.id.btn_remove)
    public void onViewClicked() {
        checkData();
    }
}
