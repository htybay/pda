package com.chicv.pda.ui.stock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chicv.pda.R;
import com.chicv.pda.adapter.CheckListdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.StockTaking;
import com.chicv.pda.bean.User;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.SPUtils;
import com.chicv.pda.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-09-23
 * email: liheyu999@163.com
 */
public class CheckListActivity extends BaseActivity {

    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;

    private CheckListdapter mAdapter;
    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        ButterKnife.bind(this);
        initToolbar("物品盘点");
        mUser = SPUtils.getUser();
        initView();
        initData();
    }

    private void initData() {
        wrapHttp(apiService.getCheckTask(mUser.getId()))
                .compose(this.<List<StockTaking>>bindToLifecycle())
                .subscribe(new RxObserver<List<StockTaking>>(this) {
                    @Override
                    public void onSuccess(List<StockTaking> value) {
                        mAdapter.setNewData(value);
                        if (value.size() == 0) {
                            ToastUtils.showString("没有盘点任务");
                        }
                    }
                });
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new CheckListdapter();
        rlvGoods.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                StockTaking taking = mAdapter.getData().get(position);
                startActivity(new Intent(CheckListActivity.this, CheckDetailActivity.class)
                        .putExtra(CheckDetailActivity.KEY_CHECK_ID, taking.getId()));

            }
        });
    }
}
