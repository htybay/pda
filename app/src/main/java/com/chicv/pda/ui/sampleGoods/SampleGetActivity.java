package com.chicv.pda.ui.sampleGoods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.GoodsStock;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-10-10
 * email: liheyu999@163.com
 */
public class SampleGetActivity extends BaseActivity {

    @BindView(R.id.text_stock_id)
    TextView textStockId;
    @BindView(R.id.text_count)
    TextView textCount;
    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;

    private String mBarcode;
    private BaseQuickAdapter<GoodsStock, BaseViewHolder> mAdapter;
    private int mStockId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_get);
        ButterKnife.bind(this);
        initToolbar("取样");
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new BaseQuickAdapter<GoodsStock, BaseViewHolder>(R.layout.item_sample_get) {
            @Override
            protected void convert(BaseViewHolder helper, GoodsStock item) {
                helper.setText(R.id.text_product, item.getBatchCode());
                helper.setText(R.id.text_size, item.getSpecification());
//                        .replace("颜色", "").replace("尺码", "").toLowerCase().replace("color", "").replace("size", ""));
            }
        };
        rlvGoods.setAdapter(mAdapter);
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        mBarcode = barcode;
        if (BarcodeUtils.isStockCode(barcode)) {
            //货位单号
            handleStockBarcode(BarcodeUtils.getBarcodeId(barcode));
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }

    private void handleStockBarcode(final int gridId) {
        wrapHttp(apiService.getGoodsStockByGridId((gridId)))
                .compose(this.<List<GoodsStock>>bindToLifecycle())
                .subscribe(new RxObserver<List<GoodsStock>>(this) {
                    @Override
                    public void onSuccess(List<GoodsStock> value) {
                        if (value.size() == 0) {
                            ToastUtils.showString("没有数据！");
                            SoundUtils.playError();
                            clearViewData();
                            mStockId = 0;
                            return;
                        }
                        mAdapter.setNewData(value);
                        textStockId.setText(value.get(0).getPosition());
                        textCount.setText(String.valueOf(value.size()));
                        mStockId = gridId;
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        clearViewData();
                        mStockId = 0;
                        SoundUtils.playError();
                    }
                });
    }

    private void clearViewData() {
        textStockId.setText("");
        textCount.setText("");
        mAdapter.setNewData(null);
        mStockId = 0;
    }

    @OnClick({R.id.btn_clear, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_clear:
                clearViewData();
                break;
            case R.id.btn_commit:
                commit();
                break;
        }
    }

    private void commit() {
        if (mStockId == 0 || mAdapter.getData().size() == 0) {
            ToastUtils.showString("请扫描货位");
            return;
        }
        wrapHttp(apiService.commitSample(mStockId))
                .compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("操作成功");
                        clearViewData();
                    }
                });
    }
}
