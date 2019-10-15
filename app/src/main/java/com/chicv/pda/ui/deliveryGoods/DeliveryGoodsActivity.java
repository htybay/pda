package com.chicv.pda.ui.deliveryGoods;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.adapter.DeliveryGoodsAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.base.Constant;
import com.chicv.pda.bean.PickGoods;
import com.chicv.pda.bean.User;
import com.chicv.pda.bean.param.OutStockParam;
import com.chicv.pda.bean.param.PickGoodsParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.PdaUtils;
import com.chicv.pda.utils.SPUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-05-30
 * email: liheyu999@163.com
 */
public class DeliveryGoodsActivity extends BaseActivity {

    @BindView(R.id.text_pick_num)
    TextView textPickNum;
    @BindView(R.id.text_delivery_num)
    TextView textDeliveryNum;
    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;
    @BindView(R.id.text_count)
    TextView textCount;

    private DeliveryGoodsAdapter mDeliveryGoodsAdapter;
    private String mBarcode;
    private PickGoods mPickGoods;
    private User mUser;
    private int mNormalGoodsCount = 0;
    private int mNormalUnDeliveryCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_goods);
        ButterKnife.bind(this);
        initToolbar("配货");
        mUser = SPUtils.getUser();
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mDeliveryGoodsAdapter = new DeliveryGoodsAdapter();
        rlvGoods.setAdapter(mDeliveryGoodsAdapter);
    }

    //处理条码
    protected void handleBarcode(String barcode) {
        mBarcode = barcode;
        if (BarcodeUtils.isPickCode(barcode)) {
            //捡货单号
            getPickGoodsInfo(String.valueOf(BarcodeUtils.getBarcodeId(barcode)));
        } else if (BarcodeUtils.isGoodsCode(barcode)) {
            //物品单号
            handleGoodsBarcode(BarcodeUtils.getBarcodeId(barcode));
        } else if (BarcodeUtils.isGoodsRuleCode(barcode)) {
            // 囤货规格 扫到什么就是什么忽略大小写
            handleGoodsRuleBarcode(barcode);
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }


    //扫描到物品条码
    private void handleGoodsBarcode(long goodsId) {
        if (mPickGoods == null) {
            ToastUtils.showString("请先扫描拣货单！");
            SoundUtils.playError();
            return;
        }

        List<PickGoods.PickGoodsDetail> data = mDeliveryGoodsAdapter.getData();
        PickGoods.PickGoodsDetail scanGoods = null;
        for (PickGoods.PickGoodsDetail goodsDetail : data) {
            if (goodsDetail.getGoodsId() == goodsId) {
                scanGoods = goodsDetail;
                break;
            }
        }
        if (scanGoods == null) {
            ToastUtils.showString("该拣货单上没有此物品！");
            SoundUtils.playError();
            return;
        }
        if (scanGoods.getStatus() != 1) {
            ToastUtils.showString("物品异常：" + PdaUtils.getStatusDes(scanGoods.getStatus()));
            SoundUtils.playError();
            return;
        }
        if (scanGoods.getPickStatus() > Constant.PICK_STATUS_UNDELIVERY) {
            ToastUtils.showString("该物品已扫描!");
            SoundUtils.playError();
            return;
        }
        uploadGoodInfo(scanGoods);
    }

    // 扫描到囤货规格条码
    private void handleGoodsRuleBarcode(String barcode) {
        if (mPickGoods == null) {
            ToastUtils.showString("请先扫描拣货单！");
            SoundUtils.playError();
            return;
        }

        boolean isExist = false;
        List<PickGoods.PickGoodsDetail> data = mDeliveryGoodsAdapter.getData();
        PickGoods.PickGoodsDetail scanGoods = null;
        for (PickGoods.PickGoodsDetail goodsDetail : data) {
            if (barcode.equalsIgnoreCase(goodsDetail.getBatchCode())) {
                isExist = true;
                if (goodsDetail.getPickStatus() == Constant.PICK_STATUS_UNDELIVERY) {
                    scanGoods = goodsDetail;
                    break;
                }
            }
        }
        if (!isExist) {
            ToastUtils.showString("该拣货单上没有此物品！");
            SoundUtils.playError();
            return;
        }
        if (scanGoods == null) {
            ToastUtils.showString("该物品已扫描!");
            SoundUtils.playError();
            return;
        }

        if (scanGoods.getStatus() != 1) {
            ToastUtils.showString("异常：" + PdaUtils.getStatusDes(scanGoods.getStatus()));
            SoundUtils.playError();
            return;
        }
        uploadGoodInfo(scanGoods);
    }

    //扫描到货物后上传货物信息
    private void uploadGoodInfo(final PickGoods.PickGoodsDetail scanGoods) {
        PickGoodsParam param = new PickGoodsParam();
        param.setPickId(String.valueOf(mPickGoods.getId()));
        param.setGoodsId(String.valueOf(scanGoods.getGoodsId()));
        param.setOperateUserId(mUser.getId());
        param.setOperateUserName(mUser.getName());
        wrapHttp(apiService.deliveryGoods(param)).compose(this.<Object>bindToLifecycle())
                .subscribe(new RxObserver<Object>(true, this) {
                    @Override
                    public void onSuccess(Object value) {
                        textDeliveryNum.setText(String.valueOf(scanGoods.getGroupNo()));
                        scanGoods.setPickStatus(Constant.PICK_STATUS_UNOUT);
                        mNormalUnDeliveryCount--;
                        refreshCountText();
                        if (mNormalUnDeliveryCount == 0) {
                            ToastUtils.showString("恭喜你，物品已扫齐，配货完成！");
                        }
                        mDeliveryGoodsAdapter.notifyDataSetChanged();
                        scrollToGoodsId(scanGoods.getGoodsId());
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                        textDeliveryNum.setText("");
                    }
                });
    }

    //扫描到拣货单条码 ，根据拣货单ID查找拣货信息
    private void getPickGoodsInfo(String pickId) {
        wrapHttp(apiService.getPickGoodsInfo(pickId))
                .compose(this.<PickGoods>bindToLifecycle())
                .subscribe(new RxObserver<PickGoods>(true, this) {
                    @Override
                    public void onSuccess(PickGoods value) {
                        clearPickData();
                        if (value.getPickStatus() != Constant.PICK_STATUS_UNDELIVERY && value.getPickStatus() != Constant.PICK_STATUS_UNOUT) {
                            ToastUtils.showString(String.format("该拣货单状态为:%s,不能配货", PdaUtils.getPickStatusDes(value.getPickStatus())));
                            SoundUtils.playError();
                            return;
                        }
                        handleData(value);
                        setViewData();
                        refreshCountText();
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        clearPickData();
                        clearViewData();
                        SoundUtils.playError();
                    }
                });
    }

    //处理服务器返回的拣货单信息
    private void handleData(PickGoods value) {
        mPickGoods = value;
        mNormalUnDeliveryCount = 0;
        mNormalGoodsCount = 0;
        if (mPickGoods == null || mPickGoods.details == null) {
            return;
        }
        List<PickGoods.PickGoodsDetail> details = mPickGoods.details;
        //新 去掉状态为完成的物品
        Iterator<PickGoods.PickGoodsDetail> iterator = details.iterator();
        while (iterator.hasNext()) {
            PickGoods.PickGoodsDetail next = iterator.next();
            if (next.getPickStatus() == Constant.PICK_STATUS_OVER) {
                iterator.remove();
//                continue;
            }
//            if (detail.getStatus() == 1) {
//                mNormalGoodsCount++;
//                if (detail.getPickStatus() == Constant.PICK_STATUS_UNDELIVERY) {
//                    mNormalUnDeliveryCount++;
//                }
//            }
        }

        for (PickGoods.PickGoodsDetail detail : details) {
            if (detail.getStatus() == 1) {
                mNormalGoodsCount++;
                if (detail.getPickStatus() == Constant.PICK_STATUS_UNDELIVERY) {
                    mNormalUnDeliveryCount++;
                }
            }
        }
    }

    private void setViewData() {
        textPickNum.setText(mBarcode);
        mDeliveryGoodsAdapter.setNewData(mPickGoods.details);
    }


    private void refreshCountText() {
        List<PickGoods.PickGoodsDetail> data = mDeliveryGoodsAdapter.getData();
        if (data.size() == 0) {
            textCount.setText("");
            textCount.setVisibility(View.GONE);
        } else {
            List<PickGoods.PickGoodsDetail> details = mPickGoods.details;
            textCount.setVisibility(View.VISIBLE);
            textCount.setText(String.format(Locale.CHINA, "总件数:%d 已扫:%d 未扫:%d  异常:%d",
                    details.size(),
                    mNormalGoodsCount - mNormalUnDeliveryCount,
                    mNormalUnDeliveryCount,
                    details.size() - mNormalGoodsCount));
        }
    }

    //2170585
    public void scrollToGoodsId(long goodsId) {
        int position = -1;
        List<PickGoods.PickGoodsDetail> data = mDeliveryGoodsAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getGoodsId() == goodsId) {
                position = i;
                break;
            }
        }
        if (position > 0) {
            rlvGoods.getLayoutManager().smoothScrollToPosition(rlvGoods, null, position);
        }
    }

    //清理配货数据
    private void clearPickData() {
        mPickGoods = null;
        mNormalUnDeliveryCount = 0;
        mNormalGoodsCount = 0;
    }

    //清理VIEW数据
    private void clearViewData() {
        textDeliveryNum.setText("");
        textPickNum.setText("");
        mDeliveryGoodsAdapter.setNewData(null);
        textCount.setText("");
        textCount.setVisibility(View.GONE);
    }

    //点击出库
    @OnClick(R.id.btn_out)
    public void onViewClicked() {
        checkData();
    }

    //出库操作
    private void checkData() {
        if (mPickGoods == null) {
            ToastUtils.showString("请先扫描拣货单！");
            return;
        }

        if (mNormalGoodsCount == 0) {
            ToastUtils.showString("没有待出库的物品！");
            return;
        }

        List<Integer> groupIds = queryNormalUnDeliveryGoods();
        if (!groupIds.isEmpty()) {
            showAlertDialog(new Gson().toJson(groupIds));
        } else {
            doOutStock();
        }
    }

    private void showAlertDialog(String groupIds) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("配货位：" + groupIds + "未配齐，是否继续？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doOutStock();
                    }
                }).show();
    }


    //出库操作， 上传正常的待出库的数据
    private void doOutStock() {
        final List<PickGoods.PickGoodsDetail> pickGoodsDetails = queryNormalUnOutGoods();
        if (pickGoodsDetails.isEmpty()) {
            ToastUtils.showString("有包裹没有配齐物品，请先配货后操作!");
            return;
        }
        OutStockParam param = new OutStockParam();
        param.setPickId(mPickGoods.getId());
        param.setDetails(pickGoodsDetails);

        wrapHttp(apiService.outStock(param)).compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("出库完成！");
                        for (PickGoods.PickGoodsDetail item : pickGoodsDetails) {
                            item.setPickStatus(Constant.PICK_STATUS_OVER);
                        }
                        handleData(mPickGoods);
                        textDeliveryNum.setText("");
                        mDeliveryGoodsAdapter.setNewData(mPickGoods.getDetails());
                        refreshCountText();
                    }
                });
    }

    //查找正常的，待配货的数据货位集合
    private List<Integer> queryNormalUnDeliveryGoods() {
        List<Integer> list = new ArrayList<>();
        List<PickGoods.PickGoodsDetail> data = mDeliveryGoodsAdapter.getData();
        for (PickGoods.PickGoodsDetail item : data) {
            if (item.getStatus() == 1 && item.getPickStatus() == Constant.PICK_STATUS_UNDELIVERY) {
                if (!list.contains(item.getGroupNo())) {
                    list.add(item.getGroupNo());
                }
            }
        }
        Collections.sort(list);
        return list;
    }

    //查找正常的，待出库的，包裹扫齐 的数据
    private List<PickGoods.PickGoodsDetail> queryNormalUnOutGoods() {
        List<PickGoods.PickGoodsDetail> list = new ArrayList<>();
        List<Integer> packageIds = new ArrayList<>();

        List<PickGoods.PickGoodsDetail> details = mDeliveryGoodsAdapter.getData();

        //查找所有的包裹ID
        for (int i = 0; i < details.size(); i++) {
            PickGoods.PickGoodsDetail goodsDetail = details.get(i);
            if (!packageIds.contains(goodsDetail.getPackageId())) {
                packageIds.add(goodsDetail.getPackageId());
            }
        }

        if (packageIds.isEmpty()) {
            return list;
        }


        for (Integer packageId : packageIds) {
            if (isAllScan(packageId)) {
                list.addAll((queryGoodsByPackageId(packageId)));
            }
        }
        return list;
    }
    //9568696

    //查询包裹ID下的所有货物是否都是待出库状态
    private boolean isAllScan(int packageId) {
        boolean isAllScan = true;
        List<PickGoods.PickGoodsDetail> data = mDeliveryGoodsAdapter.getData();
        for (PickGoods.PickGoodsDetail item : data) {
            if (item.getPackageId() == packageId && item.getPickStatus() != Constant.PICK_STATUS_UNOUT) {
                isAllScan = false;
                break;
            }
        }
        return isAllScan;
    }

    //根据包裹ID查找所有的货物
    private List<PickGoods.PickGoodsDetail> queryGoodsByPackageId(int packageId) {
        List<PickGoods.PickGoodsDetail> list = new ArrayList<>();
        List<PickGoods.PickGoodsDetail> data = mDeliveryGoodsAdapter.getData();
        for (PickGoods.PickGoodsDetail item : data) {
            if (item.getPackageId() == packageId) {
                list.add(item);
            }
        }
        return list;
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        handleBarcode(barcode);
    }
}
