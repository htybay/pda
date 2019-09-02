package com.chicv.pda.ui.stock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chicv.pda.R;
import com.chicv.pda.adapter.HandleStockDetailAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.GoodsBatchCode;
import com.chicv.pda.bean.StockCardingBean;
import com.chicv.pda.bean.StockLimit;
import com.chicv.pda.bean.param.GoodsBatchParam;
import com.chicv.pda.bean.param.HandleStockParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.CommonUtils;
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
 * date: 2019-08-29
 * email: liheyu999@163.com
 * 理库详情
 */
public class HandleStockDetailActivity extends BaseActivity {

    public static final String KEY_LK = "key_lk";

    @BindView(R.id.text_stock_handle)
    TextView textStockHandle;
    @BindView(R.id.text_batch_code)
    TextView textBatchCode;
    @BindView(R.id.text_stock_new)
    TextView textStockNew;
    @BindView(R.id.text_count)
    TextView textCount;
    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;

    private StockCardingBean mStockCardingBean;
    private String mBarcode;
    private int mStockHandleId;
    private int mStockNewId;
    private List<GoodsBatchCode> mOriginList;
    private HandleStockDetailAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_stock_detail);
        ButterKnife.bind(this);
        initToolbar("理库操作");
        mStockCardingBean = getIntent().getParcelableExtra(KEY_LK);
        if (mStockCardingBean == null) {
            finish();
        }
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new HandleStockDetailAdapter();
        rlvGoods.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                textBatchCode.setText(mAdapter.getItem(position).getBatchCode());
            }
        });
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        mBarcode = barcode;
        if (BarcodeUtils.isStockCode(barcode)) {
            //货位单号
            if (mStockHandleId == 0) {
                handleStockBarcode(BarcodeUtils.getBarcodeId(barcode));
            } else {
                getLimitInfo(BarcodeUtils.getBarcodeId(barcode));
            }
        } else if (BarcodeUtils.isGoodsRuleCode(barcode)) {
            //囤货规格 扫到什么就是什么忽略大小写
            handleGoodsRuleBarcode(barcode);
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }

    //根据WINCE代码 原始数据不直接显示在列表中，而是扫描后再显示，相同规格加1 ，不同添加进列表中
    private void handleGoodsRuleBarcode(String barcode) {
        if (mOriginList == null) {
            ToastUtils.showString("请先扫描理库货位");
            SoundUtils.playError();
            return;
        }
        boolean isExist = false;
        GoodsBatchCode scanGoods = null;
        for (GoodsBatchCode goodsDetail : mOriginList) {
            if (TextUtils.equals(goodsDetail.getBatchCode().toLowerCase(), barcode.toLowerCase())) {
                isExist = true;
                if (!goodsDetail.isMoveDown()) {
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
        scanGoods.setIsMoveDown(true);
        SoundUtils.playSuccess();

        //判断在目前列表中是否存在 ，存在加1，不存在添加进去
        List<GoodsBatchCode> data = mAdapter.getData();
        GoodsBatchCode existGoods = null;
        for (GoodsBatchCode item : data) {
            if (TextUtils.equals(item.getBatchCode(), scanGoods.getBatchCode())) {
                existGoods = item;
                break;
            }
        }
        if (existGoods == null) {
            scanGoods.setScanCount(1);
            mAdapter.addData(0, scanGoods);
        } else {
            existGoods.setScanCount(existGoods.getScanCount() + 1);
            mAdapter.notifyDataSetChanged();
        }

        textBatchCode.setText(barcode);
        textCount.setText(String.valueOf(mAdapter.getScanCout()));
    }

    private void handleStockBarcode(final int barcodeId) {
        wrapHttp(apiService.getGridGoods(barcodeId, mStockCardingBean.getId()))
                .compose(this.<List<GoodsBatchCode>>bindToLifecycle())
                .subscribe(new RxObserver<List<GoodsBatchCode>>() {
                    @Override
                    public void onSuccess(List<GoodsBatchCode> value) {
                        if (value.size() == 0) {
                            ToastUtils.showString("该货位不存在需要理库的物品");
                            SoundUtils.playError();
                            return;
                        }
                        mStockHandleId = barcodeId;
                        mOriginList = value;
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

    private void getLimitInfo(final int stockId) {
        if (TextUtils.isEmpty(CommonUtils.getString(textBatchCode))) {
            ToastUtils.showString("请先扫描囤货规格");
            SoundUtils.playError();
            return;
        }
        if (mStockHandleId == stockId) {
            ToastUtils.showString("新旧货位不能相同");
            SoundUtils.playError();
            return;
        }

        final int scanCount = mAdapter.getScanCout();
        wrapHttp(apiService.getStockLimit(stockId, CommonUtils.getString(textBatchCode)))
                .compose(this.<StockLimit>bindToLifecycle())
                .subscribe(new RxObserver<StockLimit>(true, this) {
                    @Override
                    public void onSuccess(StockLimit value) {
                        if (value.getType() == 1 && scanCount <= value.getNum()) {
                            ToastUtils.showString("理库数量小于此货位最少入库数量");
                            SoundUtils.playError();
                            textStockNew.setText("");
                            mStockNewId = 0;
                            return;
                        }
                        if (value.getType() == 2 && scanCount >= value.getNum()) {
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
            ToastUtils.showString("请选择要提交的囤货规格");
            SoundUtils.playError();
            return;
        }

        HandleStockParam param = new HandleStockParam();
        param.setOldGrid(mStockHandleId);
        param.setNewGrid(mStockNewId);
        param.setCardingId(mStockCardingBean.getId());
        param.setFinish(mAdapter.getData().size() == 1);

        List<GoodsBatchParam> list = new ArrayList<>();
        String currentBatchCode = CommonUtils.getString(textBatchCode);
        for (GoodsBatchCode item : mOriginList) {
            if (TextUtils.equals(item.getBatchCode(), currentBatchCode) && item.isMoveDown()) {
                GoodsBatchParam goodsBatchParam = new GoodsBatchParam();
                goodsBatchParam.setBatchCode(item.getBatchCode());
                goodsBatchParam.setGoodsId(item.getGoodsId());
                goodsBatchParam.setIsMoveDown(item.isMoveDown());
                list.add(goodsBatchParam);
            }
        }
        param.setGoodsBatchList(list);

        wrapHttp(apiService.stockCardingMove(param)).compose(bindToLifecycle())
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
        textCount.setText("");
        textStockHandle.setText("");
        textStockNew.setText("");
        textBatchCode.setText("");
        mOriginList = null;
        mAdapter.setNewData(null);
    }

}
