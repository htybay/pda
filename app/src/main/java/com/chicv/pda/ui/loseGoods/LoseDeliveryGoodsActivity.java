package com.chicv.pda.ui.loseGoods;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.adapter.LoseDeliveryGoodsAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.LoseGoods;
import com.chicv.pda.bean.param.LoseGoodsParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.CommonUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-05-31
 * email: liheyu999@163.com
 */
public class LoseDeliveryGoodsActivity extends BaseActivity {

    @BindView(R.id.edit_barcode)
    EditText editBarcode;
    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;
    @BindView(R.id.text_pick_num)
    TextView textPickNum;
    @BindView(R.id.text_un_out)
    TextView textUnOut;
    @BindView(R.id.text_exist)
    TextView textExist;
    @BindView(R.id.text_lose)
    TextView textLose;

    private List<LoseGoods> mData;
    private LoseDeliveryGoodsAdapter mAdapter;
    private String mBarcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lose_delivery_goods);
        ButterKnife.bind(this);
        initToolbar("配货丢失");
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new LoseDeliveryGoodsAdapter();
        rlvGoods.setAdapter(mAdapter);
        editBarcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String content = CommonUtils.getString(editBarcode);
                    if (TextUtils.isEmpty(content)) {
                        ToastUtils.showString("条码不能为空");
                    } else {
                        showKeyboard(false);
                        handleBarcode(content);
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        handleBarcode(barcode);
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

    //扫描到拣货单条码 ，根据拣货单ID查找拣货信息
    private void getPickGoodsInfo(String pickId) {
        wrapHttp(apiService.getLoseDeliveryGoods(pickId))
                .compose(this.<List<LoseGoods>>bindToLifecycle())
                .subscribe(new RxObserver<List<LoseGoods>>(true, this) {
                    @Override
                    public void onSuccess(List<LoseGoods> value) {
                        mData = value;
                        mAdapter.setNewData(mData);
                        textPickNum.setText(mBarcode);
                        textUnOut.setText(String.valueOf(mData.size()));
                        refreshView();
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
        mData = null;
        mAdapter.setNewData(null);
        textPickNum.setText("");
        textUnOut.setText("");
        textExist.setText("");
        textLose.setText("");
    }

    //扫描到物品条码
    private void handleGoodsBarcode(long goodsId) {
        if (mData == null) {
            ToastUtils.showString("请先扫描拣货单！");
            SoundUtils.playError();
            return;
        }

        List<LoseGoods> data = mAdapter.getData();
        LoseGoods scanGoods = null;
        for (LoseGoods goodsDetail : data) {
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
        if (scanGoods.isScan()) {
            ToastUtils.showString("该物品已扫描!");
            SoundUtils.playError();
            return;
        }
        SoundUtils.playSuccess();
        scanGoods.setScan(true);
        mAdapter.notifyDataSetChanged();
        refreshView();
    }

    // 扫描到囤货规格条码
    private void handleGoodsRuleBarcode(String barcode) {
        if (mData == null) {
            ToastUtils.showString("请先扫描拣货单！");
            SoundUtils.playError();
            return;
        }

        boolean isExist = false;
        List<LoseGoods> data = mAdapter.getData();
        LoseGoods scanGoods = null;
        for (LoseGoods goodsDetail : data) {
            if (TextUtils.equals(goodsDetail.getBatchCode().toLowerCase(), barcode.toLowerCase())) {
                isExist = true;
                if (!goodsDetail.isScan()) {
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
        SoundUtils.playSuccess();
        scanGoods.setScan(true);
        mAdapter.notifyDataSetChanged();
        refreshView();
    }

    @OnClick(R.id.btn_lose)
    public void onViewClicked() {
        if (mData == null || mData.size() == 0) {
            ToastUtils.showString("请先扫描拣货单！");
            return;
        }
        showAlertDialog();
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("是否确认丢失？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        commit();
                    }
                }).show();
    }

    private void commit() {
        LoseGoodsParam param = new LoseGoodsParam();
        param.setPickId(String.valueOf(mData.get(0).getPickId()));
        StringBuilder builder = new StringBuilder();
        for (LoseGoods item : mData) {
            if (item.isScan()) {
                builder.append(item.getGoodsId()).append(",");
            }
        }
        param.setGoodsIds(builder.toString());

        wrapHttp(apiService.loseDeliveryGoods(param.getPickId(), param.getGoodsIds()))
                .compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(true, this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("成功！");
                        clearData();
                    }
                });
    }

    private void refreshView() {
        List<LoseGoods> data = mAdapter.getData();
        int scanCount = 0;
        for (LoseGoods item : data) {
            if (item.isScan()) {
                scanCount++;
            }
        }
        textExist.setText(String.valueOf(scanCount));
        textLose.setText(String.valueOf(data.size() - scanCount));
    }
}
