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
import com.chicv.pda.adapter.LosePickGoodsAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.LoseGoods;
import com.chicv.pda.bean.param.LoseGoodsParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.CommonUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Iterator;
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
public class LosePickGoodsActivity extends BaseActivity {

    @BindView(R.id.edit_barcode)
    EditText editBarcode;
    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;
    @BindView(R.id.text_pick_num)
    TextView textPickNum;

    private List<LoseGoods> mData;
    private LosePickGoodsAdapter mAdapter;
    private String mBarcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lose_pick_goods);
        ButterKnife.bind(this);
        initToolbar("拣货丢失");
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new LosePickGoodsAdapter();
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
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }

    //扫描到拣货单条码 ，根据拣货单ID查找拣货信息
    private void getPickGoodsInfo(String pickId) {
        wrapHttp(apiService.getLosePickGoods(pickId))
                .compose(this.<List<LoseGoods>>bindToLifecycle())
                .subscribe(new RxObserver<List<LoseGoods>>(true, this) {
                    @Override
                    public void onSuccess(List<LoseGoods> value) {
                        mData = filterGoods(value);
                        mAdapter.setNewData(mData);
                        textPickNum.setText(mBarcode);
                        SoundUtils.playSuccess();
                        if (mData.isEmpty()) {
                            ToastUtils.showString("没有丢失的数据！");
                            textPickNum.setText("");
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        mData = null;
                        mAdapter.setNewData(null);
                        textPickNum.setText("");
                        SoundUtils.playError();
                    }
                });
    }

    //只保留islose = true的数据
    private List<LoseGoods> filterGoods(List<LoseGoods> data) {
        if (data == null || data.size() == 0) {
            return new ArrayList<>();
        }

        Iterator<LoseGoods> iterator = data.iterator();
        while (iterator.hasNext()) {
            LoseGoods next = iterator.next();
            if (!next.isLose()) {
                iterator.remove();
            }
        }
        return data;
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
            builder.append(item.getGoodsId()).append(",");
        }
        param.setGoodsIds(builder.toString());

        wrapHttp(apiService.losePickGoods(param.getPickId(), param.getGoodsIds()))
                .compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(true, this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("成功！");
                        textPickNum.setText("");
                        mAdapter.setNewData(null);
                    }
                });
    }
}
