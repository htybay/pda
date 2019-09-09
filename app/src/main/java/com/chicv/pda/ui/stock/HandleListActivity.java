package com.chicv.pda.ui.stock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.StockCardingBean;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.chicv.pda.ui.stock.HandleStockDetailActivity.KEY_LK;
import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-08-28
 * email: liheyu999@163.com
 * 理库任务
 */
public class HandleListActivity extends BaseActivity {

    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;

    private BaseQuickAdapter<StockCardingBean, BaseViewHolder> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_list);
        ButterKnife.bind(this);
        initToolbar("理库任务");
        initView();
        getGoodsList();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new BaseQuickAdapter<StockCardingBean, BaseViewHolder>(R.layout.item_handle_list) {
            @Override
            protected void convert(BaseViewHolder helper, StockCardingBean item) {
                helper.setText(R.id.text_product, BarcodeUtils.generateLKBarcode(item.getId()));
                helper.setText(R.id.text_stock, item.getLocation());
                helper.setText(R.id.text_status, "理库中");
            }
        };
        rlvGoods.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                StockCardingBean item = (StockCardingBean) adapter.getItem(position);
                startActivity(new Intent(HandleListActivity.this, HandleStockDetailActivity.class).putExtra(KEY_LK, item));
            }
        });
    }

    private void getGoodsList() {
        wrapHttp(apiService.getCardingList()).compose(this.<List<StockCardingBean>>bindToLifecycle())
                .subscribe(new RxObserver<List<StockCardingBean>>(this) {
                    @Override
                    public void onSuccess(List<StockCardingBean> value) {
                        mAdapter.setNewData(value);
                        if (mAdapter.getData().size() == 0) {
                            ToastUtils.showString("没有理库任务");
                        }
                    }
                });
    }
}
