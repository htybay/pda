package com.chicv.pda.ui.pickGoods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.adapter.TransferPickAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.TransferPick;
import com.chicv.pda.bean.TransferPickGoods;
import com.chicv.pda.bean.User;
import com.chicv.pda.bean.param.TransferPickGoodsParam;
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

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * 内部拣货单
 */
public class TransferPickActivity extends BaseActivity {

    @BindView(R.id.text_pick_id)
    TextView textPickId;
    @BindView(R.id.text_stock_current)
    TextView textStockCurrent;
    @BindView(R.id.text_stock_next)
    TextView textStockNext;
    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;
    @BindView(R.id.text_count)
    TextView textCount;

    private String mBarcode;
    private int mStockId;

    private User mUser;
    private TransferPickAdapter mAdapter;
    private TransferPick mTransferPick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_pick);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initToolbar("调拨拣货");
        mUser = SPUtils.getUser();
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new TransferPickAdapter();
        rlvGoods.setAdapter(mAdapter);
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        handleBarcode(barcode);
    }

    //处理条码
    protected void handleBarcode(String barcode) {
        mBarcode = barcode;
        if (BarcodeUtils.isTransferCode(barcode)) {
            //调拨单号
            getTransfePick(BarcodeUtils.getBarcodeId(barcode));
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
        if (mTransferPick == null) {
            ToastUtils.showString("请先扫描拣货单！");
            SoundUtils.playError();
            return;
        }
        if (mStockId == 0) {
            ToastUtils.showString("请先扫描货位号！");
            SoundUtils.playError();
            return;
        }

        List<TransferPickGoods> data = mAdapter.getData();

        TransferPickGoods scanGoods = null;
        for (TransferPickGoods goodsDetail : data) {
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
        if (mTransferPick == null) {
            ToastUtils.showString("请先扫描拣货单！");
            SoundUtils.playError();
            return;
        }
        if (mStockId == 0) {
            ToastUtils.showString("请先扫描货位号！");
            SoundUtils.playError();
            return;
        }

        List<TransferPickGoods> data = mAdapter.getData();
        boolean isExist = false;
        TransferPickGoods scanGoods = null;
        for (TransferPickGoods goodsDetail : data) {
            if (TextUtils.equals(goodsDetail.getBatchCode().toLowerCase(), barcode.toLowerCase())) {
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
    private void uploadGoodInfo(final TransferPickGoods scanGoods) {
        TransferPickGoodsParam param = new TransferPickGoodsParam();
        param.setTransferId(scanGoods.getTransferId());
        param.setGoodsId(scanGoods.getGoodsId());
        param.setTransferDetailId(scanGoods.getId());
        param.setOperateUserId(mUser.getId());
        param.setOperateUserName(mUser.getName());
        wrapHttp(apiService.transferPickGoods(param)).compose(this.<Object>bindToLifecycle())
                .subscribe(new RxObserver<Object>(true, this) {
                    @Override
                    public void onSuccess(Object value) {
                        scanGoods.setIsPick(true);
                        refreshTextCount();
                        List<TransferPickGoods> notPickGoods = getNotPickGoods(mTransferPick.getDetails());
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
        if (mTransferPick == null) {
            ToastUtils.showString("请先扫描拣货单！");
            SoundUtils.playError();
            return;
        }

        List<TransferPickGoods> details = mTransferPick.getDetails();
        List<TransferPickGoods> goodsDetails = queryGoodsByStockId(details, stockId);
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
    private void getTransfePick(int pickId) {
        wrapHttp(apiService.getTransferPick(pickId))
                .compose(this.<TransferPick>bindToLifecycle())
                .subscribe(new RxObserver<TransferPick>(true, this) {
                    @Override
                    public void onSuccess(TransferPick value) {
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

    private void setData(TransferPick value) {
        List<TransferPickGoods> details = filterGoods(value.getDetails());
        mTransferPick = value;
        textPickId.setText(mBarcode);
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
        mTransferPick = null;
        mAdapter.setNewData(null);
        textPickId.setText("");
        textCount.setText("");
        textStockCurrent.setText("");
        textStockNext.setText("");
    }

    private void refreshTextCount() {
        if (mTransferPick == null || mTransferPick.getDetails() == null) {
            textCount.setText("");
            return;
        }
        List<TransferPickGoods> details = mTransferPick.getDetails();
        int total = details.size();
        int notPickCount = getNotPickGoods(details).size();
        textCount.setText(String.format(Locale.CHINA, "总件数:%d 未扫:%d", total, notPickCount));
    }

    //过滤掉已经出库的数据
    private List<TransferPickGoods> filterGoods(List<TransferPickGoods> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        Iterator<TransferPickGoods> iterator = list.iterator();
        while (iterator.hasNext()) {
            TransferPickGoods next = iterator.next();
            if (next.isIsOut()) {
                iterator.remove();
            }
        }
        Collections.sort(list);
        return list;
    }

    // 获取未拣的物品集合
    private List<TransferPickGoods> getNotPickGoods(List<TransferPickGoods> data) {
        List<TransferPickGoods> list = new ArrayList<>();
        if (data != null) {
            for (TransferPickGoods item : data) {
                if (!item.isIsPick()) {
                    list.add(item);
                }
            }
        }
        return list;
    }

    //获取未拣完货位信息集合
    private List<String> getNotPickStock(List<TransferPickGoods> data) {
        List<String> list = new ArrayList<>();
        if (data != null) {
            for (TransferPickGoods item : data) {
                if (!item.isIsPick() && !list.contains(item.getGridName())) {
                    list.add(item.getGridName());
                }
            }
        }
        Logger.d(new Gson().toJson(list));
        return list;
    }

    private List<TransferPickGoods> queryGoodsByStockId(List<TransferPickGoods> data, int stockId) {
        List<TransferPickGoods> list = new ArrayList<>();
        if (data != null) {
            for (TransferPickGoods item : data) {
                if (item.getGridId() == stockId) {
                    list.add(item);
                }
            }
        }
        Collections.sort(list);
        return list;
    }
}
