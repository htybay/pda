package com.chicv.pda.ui.pickGoods;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.adapter.PickGoodsAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.base.Constant;
import com.chicv.pda.bean.PermissionBean;
import com.chicv.pda.bean.PickGoods;
import com.chicv.pda.bean.StockInfo;
import com.chicv.pda.bean.User;
import com.chicv.pda.bean.event.PickOverEvent;
import com.chicv.pda.bean.param.LoseGoodsParam;
import com.chicv.pda.bean.param.PickGoodsParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.EventBusUtls;
import com.chicv.pda.utils.PdaUtils;
import com.chicv.pda.utils.SPUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

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
 * 单件捡货页面
 */
public class PickHkGoodsActivity extends BaseActivity {

    public static final String KEY_PICK_ID = "key_pick_id";

    @BindView(R.id.rlv_pick_goods)
    RecyclerView rlvPickGoods;
    @BindView(R.id.text_hk_num)
    TextView textHkNum;
    @BindView(R.id.text_stock_current)
    TextView textStockCurrent;
    @BindView(R.id.text_stock_next)
    TextView textStockNext;
    @BindView(R.id.text_count)
    TextView textCount;
    @BindView(R.id.btn_over)
    Button btnOver;
    @BindView(R.id.btn_lose)
    Button btnLose;

    private PickGoodsAdapter mPickGoodsAdapter;
    private PickGoods mPickGoods;
    private String mBarcode;
    private User mUser;
    private List<StockInfo> mStockInfos = new ArrayList<>();
    private StockInfo mCurrentStockInfo;
    private int mNormalGoodsCount = 0;
    private int mNormalReadyPickCount = 0;
    private int mPickId;
    private int mHkCode;


    public static void start(Context context, int pickId) {
        Intent intent = new Intent(context, PickHkGoodsActivity.class);
        intent.putExtra(KEY_PICK_ID, pickId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_hk_goods);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mUser = SPUtils.getUser();
        mPickId = getIntent().getIntExtra(KEY_PICK_ID, 0);
        initToolbar("单件拣货");
        initView();
        getPickGoodsInfo(String.valueOf(mPickId));
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvPickGoods.setLayoutManager(layoutManager);
        mPickGoodsAdapter = new PickGoodsAdapter();
        rlvPickGoods.setAdapter(mPickGoodsAdapter);
    }

    private boolean canLose() {
        List<PermissionBean> permissions = mUser.getPermissions();
        for (PermissionBean permission : permissions) {
            if (TextUtils.equals(PdaUtils.PERMISSION_PICK_LOSE, permission.getPermissionGuid())) {
                return true;
            }
        }
        return false;
    }

    private void initButtonView() {
        if (mPickGoods == null) {
            return;
        }

        if (!TextUtils.isEmpty(mPickGoods.getPickEndTime())) {
            btnOver.setVisibility(View.GONE);
            btnLose.setVisibility(canLose() ? View.VISIBLE : View.GONE);
        } else {
            btnOver.setVisibility(View.VISIBLE);
            btnLose.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        mBarcode = barcode;
        if (BarcodeUtils.isHKCode(barcode)) {
            //扫描货框
            handleHkCode(BarcodeUtils.getBarcodeId(barcode));
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

    private void handleHkCode(final int barcodeId) {
        wrapHttp(apiService.containerBuild(mPickId, barcodeId)).compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(this) {
                    @Override
                    public void onSuccess(Object value) {
                        SoundUtils.playSuccess();
                        textHkNum.setText(mBarcode);
                        mHkCode = barcodeId;
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                        textHkNum.setText("");
                        mHkCode = 0;
                    }
                });
    }

    //扫描到物品条码
    private void handleGoodsBarcode(long goodsId) {
        if (mHkCode == 0) {
            ToastUtils.showString("请先扫描货框！");
            SoundUtils.playError();
            return;
        }
        if (mCurrentStockInfo == null) {
            ToastUtils.showString("请先扫描货位号！");
            SoundUtils.playError();
            return;
        }
        List<PickGoods.PickGoodsDetail> data = mPickGoodsAdapter.getData();
        PickGoods.PickGoodsDetail scanGoods = null;
        for (PickGoods.PickGoodsDetail goodsDetail : data) {
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
        if (scanGoods.getStatus() != 1) {
            ToastUtils.showString("物品异常：" + PdaUtils.getStatusDes(scanGoods.getStatus()));
            SoundUtils.playError();
            return;
        }
        if (scanGoods.getPickStatus() > Constant.PICK_STATUS_UNPICK) {
            ToastUtils.showString("该物品已扫描!");
            SoundUtils.playError();
            return;
        }
        uploadGoodInfo(scanGoods);
    }

    // 扫描到囤货规格条码
    private void handleGoodsRuleBarcode(String barcode) {
        if (mHkCode == 0) {
            ToastUtils.showString("请先扫描货框！");
            SoundUtils.playError();
            return;
        }
        if (mCurrentStockInfo == null) {
            ToastUtils.showString("请先扫描货位号！");
            SoundUtils.playError();
            return;
        }

        List<PickGoods.PickGoodsDetail> data = mPickGoodsAdapter.getData();
        boolean isExist = false;
        PickGoods.PickGoodsDetail scanGoods = null;
        for (PickGoods.PickGoodsDetail goodsDetail : data) {
            if (barcode.equalsIgnoreCase(goodsDetail.getBatchCode())) {
                isExist = true;
                if (goodsDetail.getPickStatus() == Constant.PICK_STATUS_UNPICK) {
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
    private void uploadGoodInfo(final PickGoods.PickGoodsDetail scanGoods) {
        PickGoodsParam param = new PickGoodsParam();
        param.setPickId(String.valueOf(mPickGoods.getId()));
        param.setGoodsId(String.valueOf(scanGoods.getGoodsId()));
        param.setOperateUserId(mUser.getId());
        param.setOperateUserName(mUser.getName());
        wrapHttp(apiService.pickGoods(param)).compose(this.<Object>bindToLifecycle())
                .subscribe(new RxObserver<Object>(true, this) {
                    @Override
                    public void onSuccess(Object value) {
                        scanGoods.setPickStatus(Constant.PICK_STATUS_UNDELIVERY);
                        mNormalReadyPickCount--;
                        refreshCountText();
                        if (mNormalReadyPickCount == 0) {
                            showContinuePickDialog();
                        } else {
                            mPickGoodsAdapter.notifyDataSetChanged();
                        }
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                    }
                });
    }

    //检查是否全部扫完，如果扫完显示捡货单所有信息；
    private boolean checkIsOver() {
        boolean isOver = true;
        if (mPickGoods != null) {
            List<PickGoods.PickGoodsDetail> details = mPickGoods.getDetails();
            if (details != null) {
                for (PickGoods.PickGoodsDetail detail : details) {
                    //正常数据且有待捡的
                    if (detail.getStatus() == 1 && detail.getPickStatus() == Constant.PICK_STATUS_UNPICK) {
                        isOver = false;
                        break;
                    }
                }
            }
        }
        return isOver;
    }

    //扫到货位条码
    private void handleStockBarcode(long barcodeId) {
        if (mHkCode == 0) {
            ToastUtils.showString("请先扫描货框！");
            SoundUtils.playError();
            return;
        }
        int stockIndex = queryStockIndex(barcodeId);
        if (stockIndex < 0) {
            ToastUtils.showString("扫描货柜错误！");
            SoundUtils.playError();
            return;
        }

        //当前货位
        mCurrentStockInfo = mStockInfos.get(stockIndex);
        textStockCurrent.setText(mCurrentStockInfo.getDescription());
        mPickGoodsAdapter.setNewData(queryNormalGoodsByStockId(mCurrentStockInfo.getId()));
        SoundUtils.playSuccess();

        //下一货位
        StockInfo nextStockInfo = queryNextStockIndexByStockIndex(stockIndex);
        if (nextStockInfo == null) {
            textStockNext.setText("");
        } else {
            textStockNext.setText(nextStockInfo.getDescription());
        }
    }

    //根据拣货单ID查找拣货信息
    private void getPickGoodsInfo(String pickId) {
        wrapHttp(apiService.getPickGoodsInfo(pickId))
                .compose(this.<PickGoods>bindToLifecycle())
                .subscribe(new RxObserver<PickGoods>(true, this) {
                    @Override
                    public void onSuccess(PickGoods value) {
                        if (value.getPickStatus() == Constant.PICK_STATUS_UNRECEIVE) {
                            ToastUtils.showString("拣货单未领取！");
                            finish();
                            return;
                        }
                        if (!TextUtils.equals(value.getPickDutyUserName(), SPUtils.getUser().getName())) {
                            ToastUtils.showString("负责人错误，该拣货单的负责人是：" + value.getPickDutyUserName());
                            finish();
                            return;
                        }
                        if (value.getDetails() == null || value.getDetails().size() == 0) {
                            ToastUtils.showString("拣货单物品明细为空！");
                            finish();
                            return;
                        }
                        handleData(value);
                        setViewData();
                        refreshCountText();
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        finish();
                    }
                });
    }

    //处理服务器返回的拣货单信息 1，获取所有货位去重排序 2，获取所有正常可扫的件数
    private void handleData(PickGoods value) {
        mPickGoods = value;
        mNormalReadyPickCount = 0;
        mNormalGoodsCount = 0;
        List<PickGoods.PickGoodsDetail> details = mPickGoods.details;
        List<Long> stockIds = new ArrayList<>();
        if (details == null) return;
        for (PickGoods.PickGoodsDetail detail : details) {
            StockInfo stockInfo = detail.getStockGrid();
            if (!stockIds.contains(stockInfo.getId())) {
                stockIds.add(stockInfo.getId());
                mStockInfos.add(stockInfo);
            }
            if (detail.getStatus() == 1) {
                mNormalGoodsCount++;
                if (detail.getPickStatus() == Constant.PICK_STATUS_UNPICK) {
                    mNormalReadyPickCount++;
                }
            }
        }
        Collections.sort(mStockInfos);
    }

    private void setViewData() {
        mPickGoodsAdapter.setNewData(mPickGoods.details);
        initButtonView();
        StockInfo stockInfo = null;
        for (StockInfo item : mStockInfos) {
            boolean existNotScanGoods = isExistNotScanGoods(item);
            if (existNotScanGoods) {
                stockInfo = item;
                break;
            }
        }
        if(mPickGoods.getContainer()!=null&&mPickGoods.getContainer().getContainerId()!=0){
            mHkCode = mPickGoods.getContainer().getContainerId();
            textHkNum.setText(BarcodeUtils.generateHKBarcode(mHkCode));
        }
        if (stockInfo != null) {
            textStockNext.setText(stockInfo.getDescription());
        }
    }

    private void refreshCountText() {
        if (mPickGoods == null || mPickGoods.details == null || mPickGoods.details.size() == 0) {
            textCount.setText("");
            textCount.setVisibility(View.GONE);
            return;
        }
        List<PickGoods.PickGoodsDetail> details = mPickGoods.details;
        textCount.setVisibility(View.VISIBLE);
        textCount.setText(String.format(Locale.CHINA, "总件数:%d 已扫:%d 未扫:%d  异常:%d",
                details.size(),
                mNormalGoodsCount - mNormalReadyPickCount,
                mNormalReadyPickCount,
                details.size() - mNormalGoodsCount));

    }

    //根据货位ID查找货位在当前货位集合中的位置
    private int queryStockIndex(long stockId) {
        int index = -1;
        for (int i = 0; i < mStockInfos.size(); i++) {
            if (mStockInfos.get(i).getId() == stockId) {
                index = i;
                break;
            }
        }
        return index;
    }

    //根据货位ID，查找正常的货物
    private List<PickGoods.PickGoodsDetail> queryNormalGoodsByStockId(long id) {
        List<PickGoods.PickGoodsDetail> list = new ArrayList<>();
        List<PickGoods.PickGoodsDetail> details = mPickGoods.getDetails();
        if (details != null) {
            for (PickGoods.PickGoodsDetail detail : details) {
                if (detail.getStockGrid().getId() == id && detail.getStatus() == 1) {
                    list.add(detail);
                }
            }
        }
        return list;
    }

    //判断该货位上是否存在正常状态的未拣货的货物
    private boolean isExistNotScanGoods(StockInfo stockInfo) {
        List<PickGoods.PickGoodsDetail> list = queryNormalGoodsByStockId(stockInfo.getId());
        if (list.isEmpty()) {
            return false;
        }

        boolean result = false;
        for (PickGoods.PickGoodsDetail pickGoodsDetail : list) {
            if (pickGoodsDetail.getPickStatus() == Constant.PICK_STATUS_UNPICK) {
                result = true;
                break;
            }
        }
        return result;
    }


    //根据当前货位位置，查找下一个最近的没扫货物所在的货位位置,先顺序查找如果有返回没有的话从头开始查找
    private StockInfo queryNextStockIndexByStockIndex(int currentStockIndex) {
        int beginIndex = currentStockIndex + 1;

        for (int i = beginIndex; i < mStockInfos.size(); i++) {
            StockInfo stockInfo = mStockInfos.get(i);
            if (isExistNotScanGoods(stockInfo)) {
                return stockInfo;
            }
        }
        for (int i = 0; i < currentStockIndex; i++) {
            StockInfo stockInfo = mStockInfos.get(i);
            if (isExistNotScanGoods(stockInfo)) {
                return stockInfo;
            }
        }
        return null;
    }

    private List<PickGoods.PickGoodsDetail> filterExceptionGoods(List<PickGoods.PickGoodsDetail> list) {
        if (list != null && list.size() > 0) {
            Iterator<PickGoods.PickGoodsDetail> iterator = list.iterator();
            while (iterator.hasNext()) {
                PickGoods.PickGoodsDetail goodsDetail = iterator.next();
                if (goodsDetail.getStatus() != 1) {
                    iterator.remove();
                }
            }
        }
        return list;
    }

    @OnClick({R.id.btn_over, R.id.btn_lose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_over:
                pickOver();
                break;
            case R.id.btn_lose:
                pickLose();
                break;
        }
    }

    private void pickLose() {
        if (mPickGoods == null || mNormalReadyPickCount == 0) {
            ToastUtils.showString("没有可以做丢失的物品");
            return;
        }
        LoseGoodsParam param = new LoseGoodsParam();
        param.setPickId(String.valueOf(mPickGoods.getId()));
        StringBuilder builder = new StringBuilder();
        for (PickGoods.PickGoodsDetail detail : mPickGoods.getDetails()) {
            if (detail.getStatus() == 1) {
                if (detail.getPickStatus() == Constant.PICK_STATUS_UNPICK) {
                    builder.append(detail.getGoodsId()).append(",");
                }
            }
        }
        param.setGoodsIds(builder.toString());

        wrapHttp(apiService.losePickGoods(param.getPickId(), param.getGoodsIds()))
                .compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(true, this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("成功！");
                        finish();
                    }
                });
    }

    private void pickOver() {
        if (mHkCode == 0) {
            ToastUtils.showString("请先扫描货框！");
            return;
        }

        if (mNormalReadyPickCount != 0) {
            showOverDialog();
        }
    }

    private void doPickOver() {
        wrapHttp(apiService.pickingComplete(mPickId)).compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(this) {
                    @Override
                    public void onSuccess(Object value) {
                        showContinuePickDialog();
                    }
                });
    }

    private void showContinuePickDialog() {
        final PickOverEvent pickEvent = new PickOverEvent();
        pickEvent.setPickId((int) mPickGoods.getId());
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("拣货完成，是否继续拣货?")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pickEvent.setContinuePick(true);
                        EventBusUtls.notifyPickChanged(pickEvent);
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pickEvent.setContinuePick(false);
                EventBusUtls.notifyPickChanged(pickEvent);
                finish();
            }
        })
                .show();
    }

    private void showOverDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(String.format(Locale.CHINA, "还有%d件货未拣，确认拣货完成？", mNormalReadyPickCount))
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doPickOver();
                    }
                }).setNegativeButton("取消", null)
                .show();
    }
}
