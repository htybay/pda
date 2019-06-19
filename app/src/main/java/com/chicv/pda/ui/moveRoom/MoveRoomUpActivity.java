package com.chicv.pda.ui.moveRoom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.adapter.MoveRoomUpAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.StockMoveRoom;
import com.chicv.pda.bean.param.MoveRoomUpParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-06-17
 * email: liheyu999@163.com
 * 移库上架
 */
public class MoveRoomUpActivity extends BaseActivity {

    @BindView(R.id.text_pick_id)
    TextView textPickId;
    @BindView(R.id.text_stock_id)
    TextView textStockId;
    @BindView(R.id.text_total)
    TextView textTotal;
    @BindView(R.id.text_diff)
    TextView textDiff;
    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;

    private String mBarcode;
    private Collection<StockMoveRoom.DetailsBean> batchList;
    private MoveRoomUpAdapter mAdapter;
    private StockMoveRoom mStockMoveRoom;
    private int mStockId;
    private long mScanCount;
    private int mNormalTotal;
    private Map<String, List<StockMoveRoom.DetailsBean>> batchListMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_up);
        ButterKnife.bind(this);
        initToolbar("移库上架");
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new MoveRoomUpAdapter();
        rlvGoods.setAdapter(mAdapter);
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        mBarcode = barcode;
        if (BarcodeUtils.isMoveCode(barcode)) {
            //移库单号
            getStockMoveRoom(BarcodeUtils.getBarcodeId(barcode));
        } else if (BarcodeUtils.isStockCode(barcode)) {
            //货位单号
            handleStockBarcode(BarcodeUtils.getBarcodeId(barcode));
        } else if (BarcodeUtils.isGoodsCode(barcode)) {
            //物品单号
            handleGoodsBarcode(BarcodeUtils.getBarcodeId(barcode));
        } else if (BarcodeUtils.isGoodsRuleCode(barcode)) {
            //囤货规格 扫到什么就是什么忽略大小写
            handleGoodsRuleBarcode(barcode);
        } else if (BarcodeUtils.isNum(barcode)) {
            //数字
            handleNumCode(barcode);
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }

    private void handleNumCode(String barcode) {
        if (mAdapter.getmSelectedItem() == null) {
            ToastUtils.showString("请先扫描囤货规格");
            SoundUtils.playError();
            return;
        }
        int i;
        try {
            i = Integer.parseInt(barcode);
        } catch (Exception e) {
            ToastUtils.showString("请输入正确的数字");
            SoundUtils.playError();
            return;
        }
        if (i == 0) {
            ToastUtils.showString("数字不能为0");
            SoundUtils.playError();
            return;
        }

        StockMoveRoom.DetailsBean detailsBean = mAdapter.getmSelectedItem();
        int notScanCount = detailsBean.getLocalTotalCount() - detailsBean.getLocalScanCount();
        if (i > notScanCount) {
            ToastUtils.showString("输入的数量大于当前移规格未上架数量：" + notScanCount);
            SoundUtils.playError();
            return;
        }

        mScanCount += i;
        detailsBean.setLocalScanCount(detailsBean.getLocalScanCount() + i);
        mAdapter.setSelected(null);
        textDiff.setText(String.valueOf(mNormalTotal - mScanCount));
        SoundUtils.playSuccess();
    }

    private void handleStockBarcode(int barcodeId) {
        mStockId = barcodeId;
        textStockId.setText(mBarcode);
        SoundUtils.playSuccess();
    }

    //扫描到物品条码
    private void handleGoodsBarcode(long goodsId) {
        if (mStockMoveRoom == null) {
            ToastUtils.showString("请先扫描移库单！");
            SoundUtils.playError();
            return;
        }
        if (mStockId == 0) {
            ToastUtils.showString("请先扫描货位号！");
            SoundUtils.playError();
            return;
        }

        List<StockMoveRoom.DetailsBean> data = mAdapter.getData();

        StockMoveRoom.DetailsBean scanGoods = null;
        for (StockMoveRoom.DetailsBean goodsDetail : data) {
            if (goodsDetail.getGoodsId() == goodsId) {
                scanGoods = goodsDetail;
                break;
            }
        }
        if (scanGoods == null) {
            ToastUtils.showString("该移库单上没有此物品！");
            SoundUtils.playError();
            return;
        }

        if (scanGoods.isMatch()) {
            ToastUtils.showString("该物品已扫描!");
            SoundUtils.playError();
            return;
        }
        SoundUtils.playSuccess();
        scanGoods.setMatch(true);
        mScanCount++;
        mAdapter.notifyDataSetChanged();
    }


    // 扫描到囤货规格条码 只是选出来变色，不做任何事情，在输入数字时处理
    private void handleGoodsRuleBarcode(String barcode) {
        if (mStockMoveRoom == null) {
            ToastUtils.showString("请先扫描移库单！");
            SoundUtils.playError();
            return;
        }
        if (mStockId == 0) {
            ToastUtils.showString("请先扫描货位号！");
            SoundUtils.playError();
            return;
        }

        StockMoveRoom.DetailsBean scanGoods = null;
        if (batchList != null) {
            for (StockMoveRoom.DetailsBean goodsDetail : batchList) {
                if (TextUtils.equals(goodsDetail.getBatchCode().toLowerCase(), barcode.toLowerCase())) {
                    scanGoods = goodsDetail;
                    break;
                }
            }
        }

        if (scanGoods == null) {
            ToastUtils.showString("该移库单没有此囤货规格的物品！");
            SoundUtils.playError();
            return;
        }
        if (scanGoods.getLocalTotalCount() == scanGoods.getLocalScanCount()) {
            ToastUtils.showString("该囤货规格已扫完");
            SoundUtils.playError();
            return;
        }

        mAdapter.setSelected(scanGoods);
        SoundUtils.playSuccess();
    }

    private void getStockMoveRoom(int barcodeId) {
        wrapHttp(apiService.getStockMoveRoom(barcodeId))
                .compose(this.<StockMoveRoom>bindToLifecycle())
                .subscribe(new RxObserver<StockMoveRoom>(this) {
                    @Override
                    public void onSuccess(StockMoveRoom value) {
                        clearData();
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
        mStockMoveRoom = null;
        batchList = null;
        mStockId = 0;
        mScanCount = 0;
        mNormalTotal = 0;
        textPickId.setText("");
        textStockId.setText("");
        textTotal.setText("");
        textDiff.setText("");
        mAdapter.setNewData(null);
        mAdapter.setSelected(null);
    }

    //只保留正常且移库中的数据，有三个集合，1，物品集体，2囤货规格的集合（去重统计），3，囤货规格的集合，全部
    private void handleData(StockMoveRoom value) {
        mNormalTotal = 0;
        batchListMap = new HashMap<>();
        List<StockMoveRoom.DetailsBean> details = value.getDetails();
        List<StockMoveRoom.DetailsBean> goodsList = new ArrayList<>();
        Map<String, StockMoveRoom.DetailsBean> batchMap = new HashMap<>();
        for (StockMoveRoom.DetailsBean item : details) {
            if (item.getStatus() != 1 || item.getMoveStatus() != 10) {
                continue;
            }
            mNormalTotal++;
            String batchCode = item.getBatchCode();
            if (TextUtils.isEmpty(batchCode)) {
                goodsList.add(item);
            } else {
                if (batchMap.containsKey(batchCode)) {
                    StockMoveRoom.DetailsBean goods = batchMap.get(batchCode);
                    goods.setLocalTotalCount(goods.getLocalTotalCount() + 1);
                    if (item.isMatch()) {
                        goods.setLocalScanCount(goods.getLocalScanCount() + 1);
                    }
                } else {
                    item.setLocalTotalCount(1);
                    if (item.isMatch()) {
                        item.setLocalScanCount(1);
                    }
                    batchMap.put(batchCode, item);
                }

                if (batchListMap.containsKey(batchCode)) {
                    batchListMap.get(batchCode).add(item);
                } else {
                    List<StockMoveRoom.DetailsBean> objects = new ArrayList<>();
                    objects.add(item);
                    batchListMap.put(batchCode, objects);
                }
            }
        }
        mStockMoveRoom = value;
        batchList = batchMap.values();
        goodsList.addAll(batchList);
//        Collections.sort(goodsList);
        mAdapter.setNewData(goodsList);
        textTotal.setText(String.valueOf(mNormalTotal));
        textDiff.setText(String.valueOf(mNormalTotal));
        textPickId.setText(mBarcode);
    }

    @OnClick({R.id.btn_over, R.id.btn_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_over:
                moveComplete();
                break;
            case R.id.btn_up:
                moveUp();
                break;
        }
    }

    private void moveUp() {
        if (mStockMoveRoom == null) {
            ToastUtils.showString("请先扫描移库单！");
            return;
        }
        if (mStockId == 0) {
            ToastUtils.showString("请先扫描货位号！");
            return;
        }
        if (mAdapter.getData().size() == 0) {
            ToastUtils.showString("无数据！");
            return;
        }

        MoveRoomUpParam param = new MoveRoomUpParam();
        param.setGridId(mStockId);
        param.setMoveId(mStockMoveRoom.getId());

        List<MoveRoomUpParam.UpperShelfDetailBean> list = new ArrayList<>();
        List<StockMoveRoom.DetailsBean> data = mAdapter.getData();
        for (StockMoveRoom.DetailsBean item : data) {
            if (TextUtils.isEmpty(item.getBatchCode())) {
                if (item.isMatch()) {
                    MoveRoomUpParam.UpperShelfDetailBean detial = new MoveRoomUpParam.UpperShelfDetailBean();
                    detial.setGoodsId(item.getGoodsId());
                    detial.setDetailId(item.getId());
                    list.add(detial);
                }
            } else {
                if (item.getLocalScanCount() > 0) {
                    list.addAll(getScanBatchDetial(item.getBatchCode(), item.getLocalScanCount()));
                }
            }
        }
        param.setUpperShelfDetail(list);

        wrapHttp(apiService.moveRoomUp(param))
                .compose(this.bindToLifecycle())
                .subscribe(new RxObserver<Object>(this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("移库上架成功！");
                        clearData();
                    }
                });
    }

    private List<MoveRoomUpParam.UpperShelfDetailBean> getScanBatchDetial(String batchCode, int count) {
        List<MoveRoomUpParam.UpperShelfDetailBean> list = new ArrayList<>();
        List<StockMoveRoom.DetailsBean> detailsBeans = batchListMap.get(batchCode);
        for (int i = 0; i < count; i++) {
            StockMoveRoom.DetailsBean bean = detailsBeans.get(0);
            MoveRoomUpParam.UpperShelfDetailBean detial = new MoveRoomUpParam.UpperShelfDetailBean();
            detial.setGoodsId(bean.getGoodsId());
            detial.setDetailId(bean.getId());
            detial.setBatchCode(bean.getBatchCode());
            list.add(detial);
        }
        return list;
    }

    private void moveComplete() {
        if (mStockMoveRoom == null) {
            ToastUtils.showString("请先扫描移库单！");
            return;
        }

        wrapHttp(apiService.moveRoomComplete(mStockMoveRoom.getId()))
                .compose(this.bindToLifecycle())
                .subscribe(new RxObserver<Object>(this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("移库完成成功");
                        clearData();
                    }
                });
    }
}
