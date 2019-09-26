package com.chicv.pda.ui.moveRoom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.adapter.MoveRoomDownAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.LocationGoods;
import com.chicv.pda.bean.StockMoveRoom;
import com.chicv.pda.bean.param.MoveRoomDownParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.PdaUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import java.util.ArrayList;
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
 * 移库下架
 */
public class MoveRoomDownActivity extends BaseActivity {

    @BindView(R.id.text_pick_id)
    TextView textPickId;
    @BindView(R.id.text_stock_id)
    TextView textStockId;
    @BindView(R.id.text_total)
    TextView textTotal;
    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;

    private String mBarcode;
    private int mScanTotal;
    private List<MoveRoomDownParam.LowerShelfDetailBean> mScanList = new ArrayList<>();
    private List<Integer> mScanIdList = new ArrayList<>();

    private MoveRoomDownAdapter mAdapter;
    private StockMoveRoom mStockMoveRoom;
    private LocationGoods mLocationGoods;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_down);
        ButterKnife.bind(this);
        initToolbar("移库下架");
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new MoveRoomDownAdapter();
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

    private void getStockMoveRoom(int barcodeId) {
        wrapHttp(apiService.getStockMoveRoom(barcodeId))
                .compose(this.<StockMoveRoom>bindToLifecycle())
                .subscribe(new RxObserver<StockMoveRoom>(this) {
                    @Override
                    public void onSuccess(StockMoveRoom value) {
                        clearData();
                        if (value.getMoveStatus() != 10 && value.getMoveStatus() != 0) {
                            ToastUtils.showString("移库单状态不对 当前状态:" + PdaUtils.getMoveStatusDes(value.getMoveStatus()));
                            SoundUtils.playError();
                            return;
                        }
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
        mLocationGoods = null;
        mScanList.clear();
        mScanIdList.clear();
        mScanTotal = 0;
        textPickId.setText("");
        textStockId.setText("");
        textTotal.setText("");
        mAdapter.setNewData(null);
        mAdapter.setSelected(null);
    }

    //只保留正常且移库中的数据，有三个集合，1，物品集体，2囤货规格的集合（去重统计），3，囤货规格的集合，全部
    private void handleData(StockMoveRoom value) {
        List<StockMoveRoom.DetailsBean> details = value.getDetails();
        List<StockMoveRoom.DetailsBean> goodsList = new ArrayList<>();
        Map<String, StockMoveRoom.DetailsBean> batchMap = new HashMap<>();
        for (StockMoveRoom.DetailsBean item : details) {
            String batchCode = item.getBatchCode();
            if (TextUtils.isEmpty(batchCode)) {
                goodsList.add(item);
            } else {
                if (batchMap.containsKey(batchCode)) {
                    StockMoveRoom.DetailsBean goods = batchMap.get(batchCode);
                    goods.setLocalTotalCount(goods.getLocalTotalCount() + 1);
                } else {
                    item.setLocalTotalCount(1);
                    batchMap.put(batchCode, item);
                }
            }
        }
        mStockMoveRoom = value;
        goodsList.addAll(batchMap.values());
//        Collections.sort(goodsList);
        mAdapter.setNewData(goodsList);
        textTotal.setText("0");
        textPickId.setText(mBarcode);
    }


    //货位条码
    private void handleStockBarcode(int barcodeId) {
        if (mStockMoveRoom == null) {
            ToastUtils.showString("请先扫描移库单！");
            SoundUtils.playError();
            return;
        }
        wrapHttp(apiService.getLocationGoods(barcodeId))
                .compose(this.<LocationGoods>bindToLifecycle())
                .subscribe(new RxObserver<LocationGoods>(true, this) {
                    @Override
                    public void onSuccess(LocationGoods value) {
                        if (mStockMoveRoom.getFromRoomId() != value.getStockGrid().getRoomId()) {
                            ToastUtils.showString("调拨库存和货位库存不一致");
                            SoundUtils.playError();
                            return;
                        }
                        mLocationGoods = value;
                        textStockId.setText(mLocationGoods.getStockGrid().getDescription());
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        SoundUtils.playError();
                        mLocationGoods = null;
                        textStockId.setText("");
                    }
                });
    }

    //扫描到物品条码
    private void handleGoodsBarcode(final int goodsId) {
        if (mStockMoveRoom == null) {
            ToastUtils.showString("请先扫描移库单！");
            SoundUtils.playError();
            return;
        }
        if (mLocationGoods == null) {
            ToastUtils.showString("请先扫描货位号！");
            SoundUtils.playError();
            return;
        }
        //是否在已扫描列表中
        if (mScanIdList.contains(goodsId)) {
            ToastUtils.showString("物品已经存在,不要重复扫描！");
            SoundUtils.playError();
            return;
        }
        //是否在货位上
        List<LocationGoods.LocationGoodsDetail> locationGoodsList = mLocationGoods.getLocationGoodsList();
        LocationGoods.LocationGoodsDetail scanStockGoods = null;
        for (LocationGoods.LocationGoodsDetail stockGoods : locationGoodsList) {
            if (stockGoods.getGoodsId() == goodsId) {
                scanStockGoods = stockGoods;
                break;
            }
        }
        if (scanStockGoods == null) {
            ToastUtils.showString("当前物品不在货位上！");
            SoundUtils.playError();
            return;
        }

        wrapHttp(apiService.isClearanceSku(scanStockGoods.getSkuId()))
                .compose(this.<Boolean>bindToLifecycle())
                .subscribe(new RxObserver<Boolean>(this) {
                    @Override
                    public void onSuccess(Boolean value) {
                        if (value) {
                            ToastUtils.showString("清仓物品不能移库");
                            SoundUtils.playError();
                        } else {
                            StockMoveRoom.DetailsBean bean = new StockMoveRoom.DetailsBean();
                            bean.setGoodsId(goodsId);
                            bean.setScan(true);
                            mAdapter.addData(0,bean);
                            rlvGoods.getLayoutManager().smoothScrollToPosition(rlvGoods,null,0);
                            mScanTotal++;
                            textTotal.setText(String.valueOf(mScanTotal));

                            MoveRoomDownParam.LowerShelfDetailBean paramBean = new MoveRoomDownParam.LowerShelfDetailBean();
                            paramBean.setGoodsId(goodsId);
                            mScanList.add(paramBean);
                            mScanIdList.add(goodsId);
                            SoundUtils.playSuccess();
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        super.onFailure(msg);
                        SoundUtils.playError();
                    }
                });
    }

    // 扫描到囤货规格条码
    private void handleGoodsRuleBarcode(final String barcode) {
        if (mStockMoveRoom == null) {
            ToastUtils.showString("请先扫描移库单！");
            SoundUtils.playError();
            return;
        }
        if (mLocationGoods == null) {
            ToastUtils.showString("请先扫描货位号！");
            SoundUtils.playError();
            return;
        }

        //是否在货位上
        List<LocationGoods.LocationGoodsDetail> locationGoodsList = mLocationGoods.getLocationGoodsList();
        LocationGoods.LocationGoodsDetail scanStockGoods = null;
        for (LocationGoods.LocationGoodsDetail stockGoods : locationGoodsList) {
            if (barcode.equalsIgnoreCase(stockGoods.getBatchCode())) {
                scanStockGoods = stockGoods;
                break;
            }
        }
        if (scanStockGoods == null) {
            ToastUtils.showString("囤货规格不在当前货位！");
            SoundUtils.playError();
            return;
        }

        wrapHttp(apiService.isClearanceSku(scanStockGoods.getSkuId()))
                .compose(this.<Boolean>bindToLifecycle())
                .subscribe(new RxObserver<Boolean>(this) {
                    @Override
                    public void onSuccess(Boolean value) {
                        if (value) {
                            ToastUtils.showString("清仓物品不能移库");
                            SoundUtils.playError();
                        } else {
                            mAdapter.setSelected(barcode);
                            rlvGoods.getLayoutManager().smoothScrollToPosition(rlvGoods,null,0);
                            SoundUtils.playSuccess();
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        super.onFailure(msg);
                        SoundUtils.playError();
                    }
                });
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

        StockMoveRoom.DetailsBean selectedItem = mAdapter.getmSelectedItem();
        List<LocationGoods.LocationGoodsDetail> batchGoods = getBatchGoods(selectedItem.getBatchCode());

        if (i > batchGoods.size()) {
            ToastUtils.showString("输入的数量大于当前货位系统库存数量：" + batchGoods.size());
            SoundUtils.playError();
            return;
        }

        for (int j = 0; j < i; j++) {
            LocationGoods.LocationGoodsDetail detail = batchGoods.get(j);
            mScanIdList.add(detail.getGoodsId());
            mScanList.add(new MoveRoomDownParam.LowerShelfDetailBean(detail.getBatchCode(), detail.getGoodsId()));
        }

        mScanTotal += i;
        selectedItem.setLocalTotalCount(selectedItem.getLocalTotalCount() + i);
        selectedItem.setScan(true);
        mAdapter.setSelected(null);
        textTotal.setText(String.valueOf(mScanTotal));
        SoundUtils.playSuccess();
    }

    private List<LocationGoods.LocationGoodsDetail> getBatchGoods(String batchCode) {
        List<LocationGoods.LocationGoodsDetail> locationGoodsList = mLocationGoods.getLocationGoodsList();
        List<LocationGoods.LocationGoodsDetail> list = new ArrayList<>();
        for (LocationGoods.LocationGoodsDetail item : locationGoodsList) {
            if (TextUtils.equals(item.getBatchCode(), batchCode) && !mScanIdList.contains(item.getGoodsId())) {
                list.add(item);
            }
        }
        return list;
    }


    @OnClick(R.id.btn_down)
    public void onViewClicked() {
        if (mStockMoveRoom == null) {
            ToastUtils.showString("请先扫描移库单！");
            return;
        }
        if (mLocationGoods == null) {
            ToastUtils.showString("请先扫描货位号！");
            return;
        }
        MoveRoomDownParam param = new MoveRoomDownParam();
        param.setMoveId(mStockMoveRoom.getId());
        param.setLowerShelfDetail(mScanList);
        wrapHttp(apiService.moveRoomDown(param))
                .compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("操作成功");
                        clearData();
                    }
                });


    }
}
