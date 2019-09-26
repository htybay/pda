package com.chicv.pda.ui.stock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chicv.pda.R;
import com.chicv.pda.adapter.BadListAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.StockLoss;
import com.chicv.pda.bean.User;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.SPUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-09-20
 * email: liheyu999@163.com
 */
public class BadListActivity extends BaseActivity {

    public static final int CODE_REQUEST = 999;

    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;
    @BindView(R.id.text_count)
    TextView textCount;

    private BadListAdapter mAdapter;
    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bad_list);
        ButterKnife.bind(this);
        initToolbar("报损单列表");
        mUser = SPUtils.getUser();
        initView();
        initData();
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        if (BarcodeUtils.isBadCode(barcode)) {
            if (mAdapter.existBadId(BarcodeUtils.getBarcodeId(barcode))) {
                gotoDetail(BarcodeUtils.getBarcodeId(barcode));
            } else {
                ToastUtils.showString("请输入正确的报损单号！");
                SoundUtils.playError();
            }
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }

    private void gotoDetail(int barcodeId) {
        Intent intent = new Intent(BadListActivity.this, BadDetailActivity.class).putExtra(BadDetailActivity.KEY_BAD_ID, barcodeId);
        startActivityForResult(intent, CODE_REQUEST);
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new BadListAdapter();
        rlvGoods.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                StockLoss item = mAdapter.getItem(position);
                gotoDetail(item.getId());
            }
        });
    }

    private void initData() {
        wrapHttp(apiService.getStockLosses(mUser.getRoomId()))
                .compose(this.<List<StockLoss>>bindToLifecycle())
                .subscribe(new RxObserver<List<StockLoss>>(this) {
                    @Override
                    public void onSuccess(List<StockLoss> value) {
                        mAdapter.setNewData(value);
                        textCount.setText(String.valueOf(value.size()));
                        textCount.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mAdapter.setNewData(null);
                        textCount.setText("");
                        textCount.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_REQUEST && resultCode == RESULT_OK) {
            initData();
        }
    }
}
