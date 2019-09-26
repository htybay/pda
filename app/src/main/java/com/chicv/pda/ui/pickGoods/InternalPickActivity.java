package com.chicv.pda.ui.pickGoods;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.adapter.InternalPickAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.InternalPick;
import com.chicv.pda.bean.InternalPickGoods;
import com.chicv.pda.bean.User;
import com.chicv.pda.bean.param.PickInternalGoodsParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.SPUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

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
 * 内部拣货单
 */
public class InternalPickActivity extends BaseActivity {

    @BindView(R.id.text_pick_id)
    TextView textPickId;
    @BindView(R.id.text_type)
    TextView textType;
    @BindView(R.id.text_stock_current)
    TextView textStockCurrent;
    @BindView(R.id.text_stock_next)
    TextView textStockNext;
    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;
    @BindView(R.id.text_count)
    TextView textCount;

    private User mUser;
    private InternalPickAdapter mAdapter;
    private String mBarcode;
    private InternalPick mInternalGoods;
    private int mStockId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_pick);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initToolbar("内部流转拣货");
        mUser = SPUtils.getUser();
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new InternalPickAdapter();
        rlvGoods.setAdapter(mAdapter);
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        handleBarcode(barcode);
    }

    //处理条码
    protected void handleBarcode(String barcode) {
        mBarcode = barcode;
        if (BarcodeUtils.isPickInternalCode(barcode)) {
            //捡货单号
            getPickGoodsInfo(BarcodeUtils.getBarcodeId(barcode));
        } else if (BarcodeUtils.isStockCode(barcode)) {
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

    //扫描到物品条码
    private void handleGoodsBarcode(long goodsId) {
        if (mInternalGoods == null) {
            ToastUtils.showString("请先扫描拣货单！");
            SoundUtils.playError();
            return;
        }
        if (mStockId == 0) {
            ToastUtils.showString("请先扫描货位号！");
            SoundUtils.playError();
            return;
        }

        List<InternalPickGoods> data = mAdapter.getData();

        InternalPickGoods scanGoods = null;
        for (InternalPickGoods goodsDetail : data) {
            if (goodsDetail.getGoodsId() == goodsId) {
                scanGoods = goodsDetail;
                break;
            }
        }
        if (scanGoods == null) {
            ToastUtils.showString("该货位上没有此物品！");
            SoundUtils.playError();
            return;
        }

        if (scanGoods.isIsPick()) {
            ToastUtils.showString("该物品已扫描!");
            SoundUtils.playError();
            return;
        }
        uploadGoodInfo(scanGoods);
    }

    // 扫描到囤货规格条码
    private void handleGoodsRuleBarcode(String barcode) {
        if (mInternalGoods == null) {
            ToastUtils.showString("请先扫描拣货单！");
            SoundUtils.playError();
            return;
        }
        if (mStockId == 0) {
            ToastUtils.showString("请先扫描货位号！");
            SoundUtils.playError();
            return;
        }

        List<InternalPickGoods> data = mAdapter.getData();
        boolean isExist = false;
        InternalPickGoods scanGoods = null;
        for (InternalPickGoods goodsDetail : data) {
            if (barcode.equalsIgnoreCase(goodsDetail.getBatchCode())) {
                isExist = true;
                if (!goodsDetail.isIsPick()) {
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
            ToastUtils.showString("该囤货规格已拣完");
            SoundUtils.playError();
            return;
        }
        uploadGoodInfo(scanGoods);
    }

    //扫描到货物后上传货物信息
    private void uploadGoodInfo(final InternalPickGoods scanGoods) {
        PickInternalGoodsParam param = new PickInternalGoodsParam();
        param.setPickId(scanGoods.getPickId());
        param.setGoodsId(scanGoods.getGoodsId());
        param.setOperateUserId(mUser.getId());
        param.setOperateUserName(mUser.getName());
        List<PickInternalGoodsParam> list = new ArrayList<>();
        list.add(param);
        wrapHttp(apiService.internalPickGoods(list)).compose(this.<Object>bindToLifecycle())
                .subscribe(new RxObserver<Object>(true, this) {
                    @Override
                    public void onSuccess(Object value) {
                        scanGoods.setIsPick(true);
                        refreshTextCount();
                        List<InternalPickGoods> notPickGoods = getNotPickGoods(mInternalGoods.getDetails());
                        if (notPickGoods.size() == 0) {
                            ToastUtils.showString("恭喜你，物品已扫齐，拣货完成！");
                            clearData();
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                    }
                });
    }


    //扫到货位条码
    private void handleStockBarcode(int stockId) {
        if (mInternalGoods == null) {
            ToastUtils.showString("请先扫描拣货单！");
            SoundUtils.playError();
            return;
        }

        List<InternalPickGoods> details = mInternalGoods.getDetails();
        List<InternalPickGoods> goodsDetails = queryGoodsByStockId(details, stockId);
        //不存在该货位的物品
        if (goodsDetails.size() == 0) {
            ToastUtils.showString("扫描货柜错误！");
            SoundUtils.playError();
            return;
        }

        //存在该货物的物品
        mAdapter.setNewData(goodsDetails);

        //查询下一货位
        String gridName = goodsDetails.get(0).getGridName();
        String nextGridName = "";
        List<String> notPickStock = getNotPickStock(details);
        for (String s : notPickStock) {
            if (!TextUtils.equals(s, gridName)) {
                nextGridName = s;
                break;
            }
        }
        textStockCurrent.setText(gridName);
        textStockNext.setText(nextGridName);
        mStockId = stockId;
        SoundUtils.playSuccess();
    }

    //扫描到拣货单条码 ，根据拣货单ID查找拣货信息
    private void getPickGoodsInfo(int pickId) {
        wrapHttp(apiService.getInternalPickDetail(pickId))
                .compose(this.<InternalPick>bindToLifecycle())
                .subscribe(new RxObserver<InternalPick>(true, this) {
                    @Override
                    public void onSuccess(InternalPick value) {
                        setData(value);
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        clearData();
                        SoundUtils.playError();
                    }
                });
    }

    private void setData(InternalPick value) {
        List<InternalPickGoods> details = filterGoods(value.getDetails());
        mInternalGoods = value;
        textPickId.setText(mBarcode);
        textType.setText(mInternalGoods.getTypeName());
        mAdapter.setNewData(details);
        List<String> notPickStock = getNotPickStock(details);
        if (notPickStock.size() > 0) {
            textStockNext.setText(notPickStock.get(0));
        } else {
            textStockNext.setText("");
        }
        textStockCurrent.setText("");
        refreshTextCount();
    }

    private void clearData() {
        mStockId = 0;
        mInternalGoods = null;
        mAdapter.setNewData(null);
        textPickId.setText("");
        textType.setText("");
        textCount.setText("");
        textStockCurrent.setText("");
        textStockNext.setText("");
    }

    private void refreshTextCount() {
        if (mInternalGoods == null || mInternalGoods.getDetails() == null) {
            textCount.setText("");
            return;
        }
        List<InternalPickGoods> details = mInternalGoods.getDetails();
        int total = details.size();
        int notPickCount = getNotPickGoods(details).size();
        textCount.setText(String.format(Locale.CHINA, "总件数:%d 未扫:%d", total, notPickCount));
    }

    //过滤掉已经出库的数据
    private List<InternalPickGoods> filterGoods(List<InternalPickGoods> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        Iterator<InternalPickGoods> iterator = list.iterator();
        while (iterator.hasNext()) {
            InternalPickGoods next = iterator.next();
            if (next.isIsOut()) {
                iterator.remove();
            }
        }
        Collections.sort(list);
        return list;
    }

    // 获取未拣的物品集合
    private List<InternalPickGoods> getNotPickGoods(List<InternalPickGoods> data) {
        List<InternalPickGoods> list = new ArrayList<>();
        if (data != null) {
            for (InternalPickGoods item : data) {
                if (!item.isIsPick()) {
                    list.add(item);
                }
            }
        }
        return list;
    }

    //获取未拣完货位信息集合
    private List<String> getNotPickStock(List<InternalPickGoods> data) {
        List<String> list = new ArrayList<>();
        if (data != null) {
            for (InternalPickGoods item : data) {
                if (!item.isIsPick() && !list.contains(item.getGridName())) {
                    list.add(item.getGridName());
                }
            }
        }
        Logger.d(new Gson().toJson(list));
        return list;
    }

    private List<InternalPickGoods> queryGoodsByStockId(List<InternalPickGoods> data, int stockId) {
        List<InternalPickGoods> list = new ArrayList<>();
        if (data != null) {
            for (InternalPickGoods item : data) {
                if (item.getGridId() == stockId) {
                    list.add(item);
                }
            }
        }
        Collections.sort(list);
        return list;
    }

    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        if (mInternalGoods == null) {
            ToastUtils.showString("请先扫描拣货单");
            return;
        }

        List<PickInternalGoodsParam> pickedGoods = getOutGoods(mInternalGoods.getDetails());
        if (pickedGoods.size() == 0) {
            ToastUtils.showString("没有要出库的物品");
            return;
        }

        List<InternalPickGoods> notPickGoods = getNotPickGoods(mInternalGoods.getDetails());
        if (notPickGoods.size() > 0) {
            showAlertDialog(pickedGoods);
        }
    }

    private void showAlertDialog(final List<PickInternalGoodsParam> pickedGoods) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("您有未拣货的物品，是否确定出库？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        commit(pickedGoods);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void commit(List<PickInternalGoodsParam> pickedGoods) {
        wrapHttp(apiService.internalGoodsOut(pickedGoods))
                .compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(true, this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("出库成功！");
                        clearData();
                    }
                });
    }

    // 获取已拣的物品集合
    private List<PickInternalGoodsParam> getOutGoods(List<InternalPickGoods> data) {
        List<PickInternalGoodsParam> list = new ArrayList<>();
        if (data != null) {
            for (InternalPickGoods item : data) {
                if (item.isIsPick()) {
                    PickInternalGoodsParam pa = new PickInternalGoodsParam();
                    pa.setGoodsId(item.getGoodsId());
                    pa.setPickId(item.getPickId());
                    pa.setOperateUserId(mUser.getId());
                    pa.setOperateUserName(mUser.getName());
                    list.add(pa);
                }
            }
        }
        return list;
    }
}
