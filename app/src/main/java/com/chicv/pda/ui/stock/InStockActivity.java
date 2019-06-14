package com.chicv.pda.ui.stock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.adapter.InStockAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.base.Constant;
import com.chicv.pda.bean.LocationGoods;
import com.chicv.pda.bean.PurchaseGoods;
import com.chicv.pda.bean.RecommendStock;
import com.chicv.pda.bean.StockRecord;
import com.chicv.pda.bean.User;
import com.chicv.pda.bean.param.InStockParam;
import com.chicv.pda.bean.param.RecommendStockParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.CommonUtils;
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
 * date: 2019-06-04
 * email: liheyu999@163.com
 *
 * 分类入库
 */
public class InStockActivity extends BaseActivity {


    /**
     * 入库类型
     */
    public static final String IN_STOCK_TYPE = "in_stock_type";
    public static final String IN_STOCK_TITLE = "in_stock_title";

    /**
     * 快发区最多装的件数
     */
    public static final int FAST_STOCK_MAX_NUM = 12;

    @BindView(R.id.edit_barcode)
    EditText editBarcode;
    @BindView(R.id.text_recommend_stock)
    TextView textRecommendStock;
    @BindView(R.id.text_stock)
    TextView textStock;
    @BindView(R.id.text_count)
    TextView textCount;
    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;

    private int mInStockType;

    private InStockAdapter mAdapter;
    private LocationGoods mLocationGoods;
    private User user;


    public static void start(Context context,int type, String title) {
        Intent intent = new Intent(context, InStockActivity.class);
        intent.putExtra(InStockActivity.IN_STOCK_TYPE, type);
        intent.putExtra(InStockActivity.IN_STOCK_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_stock);
        ButterKnife.bind(this);
        mInStockType = getIntent().getIntExtra(IN_STOCK_TYPE, -1);
        String title = getIntent().getStringExtra(IN_STOCK_TITLE);
        if (title != null) {
            initToolbar(title);
        } else {
            finish();
        }
        user = SPUtils.getUser();
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new InStockAdapter();
        rlvGoods.setAdapter(mAdapter);
    }


    @Override
    protected void onReceiveBarcode(String barcode) {
        handleBarcode(barcode);
    }

    //处理条码
    protected void handleBarcode(String barcode) {
        if (BarcodeUtils.isStockCode(barcode)) {
            //货位号
            handleStockBarcode(BarcodeUtils.getBarcodeId(barcode));
        } else if (BarcodeUtils.isGoodsCode(barcode)) {
            //物品单号
            handleGoodsBarcode(BarcodeUtils.getBarcodeId(barcode));
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }

    //扫到货位条码
    private void handleStockBarcode(long barcodeId) {
        wrapHttp(apiService.getLocationGoods(barcodeId))
                .compose(this.<LocationGoods>bindToLifecycle())
                .subscribe(new RxObserver<LocationGoods>(true, this) {
                    @Override
                    public void onSuccess(LocationGoods value) {
                        //扫到了快发区条码
                        if (value.getStockGrid().getAreaId() == Constant.FAST_STOCK_TAG) {
                            int totalCount = mAdapter.getData().size() + value.getLocationGoodsList().size();
                            if (totalCount > FAST_STOCK_MAX_NUM) {
                                ToastUtils.showString("你扫描的货位为快发区，最多装12件物品，请先删除物品后再扫描该货位");
                                SoundUtils.playError();
                                return;
                            }
                        }
                        mLocationGoods = value;
                        textStock.setText(mLocationGoods.getStockGrid().getDescription());
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        SoundUtils.playError();
                    }
                });
    }

    //第一次扫物品获取推荐货位号再次扫物品前必须要先扫货位号
    private void handleGoodsBarcode(long goodId) {
        if (TextUtils.isEmpty(CommonUtils.getString(textRecommendStock))) {
            // 第一次扫物品
            getRecommendStock(goodId);
            return;
        }
        if (mLocationGoods == null) {
            ToastUtils.showString("获取推荐货位后先扫描货位信息，再扫描物品！");
            SoundUtils.playError();
            return;
        }

        //快发区 只能装12件货
        if (mLocationGoods.getStockGrid().getAreaId() == Constant.FAST_STOCK_TAG) {
            int totalCount = mAdapter.getData().size() + mLocationGoods.getLocationGoodsList().size();
            if (totalCount >= FAST_STOCK_MAX_NUM) {
                ToastUtils.showString("货位已满，请入库后再操作！");
                SoundUtils.playError();
                return;
            }
        }
        getRecommendStock(goodId);
    }

    private List<PurchaseGoods> copyGoods(List<LocationGoods.LocationGoodsDetail> data) {
        List<PurchaseGoods> list = new ArrayList<>();
        if (data != null) {
            for (LocationGoods.LocationGoodsDetail item : data) {
                PurchaseGoods purchaseGoods = new PurchaseGoods();
                purchaseGoods.setId(item.getGoodsId());
                purchaseGoods.setSkuId(item.getSkuId());
                purchaseGoods.setSpecification(item.getSpecification());
                list.add(purchaseGoods);
            }
        }
        return list;
    }

    //获取推荐货位和物品信息
    private void getRecommendStock(final long goodId) {
        RecommendStockParam param = new RecommendStockParam();
        param.setGoodsId(goodId);
        param.setInStockType(mInStockType);
        wrapHttp(apiService.getRecommendStockInfo(param))
                .compose(this.<RecommendStock>bindToLifecycle())
                .subscribe(new RxObserver<RecommendStock>(true, this) {
                    @Override
                    public void onSuccess(RecommendStock value) {
                        checkGoods(value);
                        SoundUtils.playSuccess();
                        textRecommendStock.setText(value.getLocationRecommend() == null ? "" : value.getLocationRecommend().getAreaName());
                    }

                    @Override
                    public void onFailure(String msg) {
                        super.onFailure(msg);
                        SoundUtils.playError();
                    }
                });
    }

    private void checkGoods(RecommendStock value) {
        PurchaseGoods goods = value.getPurchaseGoods();
        if (goods == null) {
            ToastUtils.showString("物品不存在!");
            return;
        }
        if (goods.getStatus() == Constant.GOODS_STATUS_DELETE) {
            ToastUtils.showString("物品已删除，无法入库");
            return;
        }
        if (goods.getStockStatus() == Constant.GOODS_STOCK_STATUS_READY_IN) {
            ToastUtils.showString("物品当前的仓储状态为【待入库】，不符合入库条件");
            return;
        }
        if (goods.getPurchaseStatus() == Constant.GOODS_PUCHASE_STATUS_OVER) {
            ToastUtils.showString("物品当前的采购状态为【已完成】，不符合入库条件");
            return;
        }

        switch (mInStockType) {
            case Constant.STOCK_TYPE_IN_BUY:
                //采购入库
                if (goods.getGoodsSource() != Constant.GOODS_SOURCE_BUY || goods.isIsReturn()) {
                    ToastUtils.showString("物品不是采购物品");
                } else {
                    getInRecord(goods);
                }
                break;
            case Constant.STOCK_TYPE_IN_BACK_GOODS:
                //客户退货
                if (!goods.isIsReturn()) {
                    ToastUtils.showString("物品不是退货物品");
                } else {
                    getOutRecord(goods);
                }
                break;
            case Constant.STOCK_TYPE_IN_RETRY_CHECK:
                //重新质检
                getOutRecord(goods);
                break;
            case Constant.STOCK_TYPE_IN_NOT_STANTARD:
                //不合格入库
                if (goods.getQCStatus() != Constant.QC_STATUS_FAIL) {
                    ToastUtils.showString("物品不是不合格品");
                } else {
                    updateView(goods);
                }
                break;
            case Constant.STOCK_TYPE_IN_CHANGE_GOODS:
                //换款入库
                if (goods.getGoodsSource() != Constant.GOODS_SOURCE_EXCHANGE) {
                    ToastUtils.showString("物品不是换款物品");
                } else {
                    updateView(goods);
                }
                break;
            case Constant.STOCK_TYPE_IN_CHECK_EXTRA:
                //盘盈入库
                if (goods.getGoodsSource() != Constant.GOODS_SOURCE_CHECK) {
                    ToastUtils.showString("物品不是盘盈物品");
                } else {
                    updateView(goods);
                }
                break;
        }

    }

    private void updateView(PurchaseGoods goods) {
        mAdapter.addData(0, goods);
        textCount.setText(String.valueOf(mAdapter.getData().size()));
    }

    private void getInRecord(final PurchaseGoods goods) {
        wrapHttp(apiService.getInRecord(goods.getId())).compose(this.<List<StockRecord>>bindToLifecycle())
                .subscribe(new RxObserver<List<StockRecord>>(true) {
                    @Override
                    public void onSuccess(List<StockRecord> value) {
                        checkStockRecord(value, goods);
                    }
                });
    }


    private void getOutRecord(final PurchaseGoods goods) {
        wrapHttp(apiService.getOutRecord(goods.getId())).compose(this.<List<StockRecord>>bindToLifecycle())
                .subscribe(new RxObserver<List<StockRecord>>(true) {
                    @Override
                    public void onSuccess(List<StockRecord> value) {
                        checkStockRecord(value, goods);
                    }
                });
    }


    private void checkStockRecord(List<StockRecord> value, PurchaseGoods goods) {
        boolean success = true;
        switch (mInStockType) {
            case Constant.STOCK_TYPE_IN_BUY:
                if (value.size() == 0) {
                    ToastUtils.showString("物品不是采购物品");
                    success = false;
                }
                break;
            case Constant.STOCK_TYPE_IN_BACK_GOODS:
                //客户退货
                if (value.size() == 0
                        || value.get(0).getOutStockType() != Constant.STOCK_OUT_TYPE_XIAOSOU) {
                    ToastUtils.showString("物品不是退货物品");
                    success = false;
                }
                break;
            case Constant.STOCK_TYPE_IN_RETRY_CHECK:
                //重新质检
                if (value.size() == 0
                        || value.get(0).getOutStockType() != Constant.STOCK_OUT_TYPE_ZHIJIAN) {
                    ToastUtils.showString("物品不能质检入库");
                    success = false;
                }
                break;
        }
        if (success) {
            updateView(goods);
        }

    }

    @OnClick(R.id.btn_in_stock)
    public void onViewClicked() {
        if (mAdapter.getData().size() == 0) {
            ToastUtils.showString("先扫描物品");
            return;
        }
        if (mLocationGoods == null) {
            ToastUtils.showString("请扫描货位");
            return;
        }

        submit();
    }

    private void submit() {
        InStockParam param = new InStockParam();
        param.setStockInStockType(mInStockType);
        param.setOperateUserId(user.getId());
//        param.setGoodsType(); 不传
        List<InStockParam.DetailsBean> list = new ArrayList<>();
        long gridId = mLocationGoods.getStockGrid().getId();
        List<PurchaseGoods> data = mAdapter.getData();
        for (PurchaseGoods item : data) {
            InStockParam.DetailsBean bean = new InStockParam.DetailsBean();
            bean.setGoodsId(item.getId());
            bean.setGridId(gridId);
            list.add(bean);
        }
        param.setDetails(list);

        wrapHttp(apiService.inStock(param)).compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(true, this) {
                    @Override
                    public void onSuccess(Object value) {
                        mAdapter.setNewData(null);
                        mLocationGoods = null;
                        textRecommendStock.setText("");
                        textStock.setText("");
                        textCount.setText("");
                    }
                });
    }
}
