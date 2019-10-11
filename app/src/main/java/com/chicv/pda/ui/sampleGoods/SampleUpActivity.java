package com.chicv.pda.ui.sampleGoods;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.ApiResult;
import com.chicv.pda.bean.SampleInverfyInfo;
import com.chicv.pda.bean.StockInfo;
import com.chicv.pda.bean.User;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.repository.remote.exception.ApiException;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.SPUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-10-10
 * email: liheyu999@163.com
 * <p>
 * 此页面很多判断没有判断（比如中途再扫货位，货位为空时就扫物品），按WINCE的来的
 */
public class SampleUpActivity extends BaseActivity {

    @BindView(R.id.text_stock_id)
    TextView textStockId;
    @BindView(R.id.text_type)
    TextView textType;
    @BindView(R.id.text_count)
    TextView textCount;
    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;

    private BaseQuickAdapter<SampleInverfyInfo, BaseViewHolder> mAdapter;
    private String mBarcode;
    private StockInfo mStockInfo;
    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_up);
        ButterKnife.bind(this);
        initToolbar("调样上架");
        initView();
        mUser = SPUtils.getUser();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new BaseQuickAdapter<SampleInverfyInfo, BaseViewHolder>(R.layout.item_sample_get) {
            @Override
            protected void convert(BaseViewHolder helper, SampleInverfyInfo item) {
                helper.setText(R.id.text_product, item.getBatchCode());
                helper.setText(R.id.text_size, item.getSkuAttribute());
//                        .replace("颜色", "").replace("尺码", "").toLowerCase().replace("color", "").replace("size", ""));
            }
        };
        rlvGoods.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showDelDialog(position);
            }
        });
    }

    private void showDelDialog(final int position) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确认要删除此条数据？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAdapter.remove(position);
                        textCount.setText(String.valueOf(mAdapter.getData().size()));
                    }
                }).setNegativeButton("取消", null)
                .show();
    }


    @Override
    protected void onReceiveBarcode(String barcode) {
        mBarcode = barcode;
        if (BarcodeUtils.isStockCode(barcode)) {
            //货位单号
            if (mStockInfo != null) {
                showHwDialog(BarcodeUtils.getBarcodeId(barcode));
            } else {
                handleStockBarcode(BarcodeUtils.getBarcodeId(barcode));
            }
        } else if (BarcodeUtils.isGoodsRuleCode(barcode)) {
            //囤货规格 扫到什么就是什么忽略大小写
            handleGoodsRuleBarcode(barcode);
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }

    private void handleGoodsRuleBarcode(String barcode) {
        if (mStockInfo == null) {
            ToastUtils.showString("请扫描货位！");
            SoundUtils.playError();
            return;
        }
        final SampleInverfyInfo info = new SampleInverfyInfo();
        info.setBatchCode(barcode);
        info.setGridId((int) mStockInfo.getId());
        info.setOperatorId(mUser.getId());

        apiService.sampleInVerify(info).map(new Function<ApiResult<String>, String>() {
            @Override
            public String apply(ApiResult<String> stringApiResult) throws Exception {
                if (stringApiResult.isSuccess()) {
                    return stringApiResult.getMessage();
                } else {
                    throw new ApiException(stringApiResult.getMessage());
                }
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<String>bindToLifecycle())
                .subscribe(new RxObserver<String>(this) {
                    @Override
                    public void onSuccess(String value) {
                        String[] split = value.split("\\|");
                        if (split.length > 1) {
                            textType.setText(split[1]);
                        }
                        info.setSkuAttribute(split[0]);
                        mAdapter.addData(0, info);
                        textCount.setText(String.valueOf(mAdapter.getData().size()));
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                    }
                });
    }

    private void handleStockBarcode(int barcodeId) {
        wrapHttp(apiService.getViewGrid(barcodeId))
                .compose(this.<StockInfo>bindToLifecycle())
                .subscribe(new RxObserver<StockInfo>(this) {
                    @Override
                    public void onSuccess(StockInfo value) {
                        textStockId.setText(value.getDescription());
                        mStockInfo = value;
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        super.onFailure(msg);
                        textStockId.setText("");
                        mStockInfo = null;
                        SoundUtils.playError();
                    }
                });
    }

    private void showHwDialog(final int barcodeId) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("当前已扫描货位，重新扫描会清空已扫数据，请确认？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearData();
                        handleStockBarcode(barcodeId);
                    }
                }).setNegativeButton("取消", null)
                .show();
    }


    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        if (mAdapter.getData().size() == 0) {
            ToastUtils.showString("没有要提交的数据");
            return;
        }
        wrapHttp(apiService.sampleIn(mAdapter.getData()))
                .compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("操作成功");
                        clearData();
                    }
                });
    }

    private void clearData() {
        mStockInfo = null;
        mAdapter.setNewData(null);
        textStockId.setText("");
        textType.setText("");
        textCount.setText("");
    }


}
