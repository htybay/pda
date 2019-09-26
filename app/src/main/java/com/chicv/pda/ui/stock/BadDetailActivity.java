package com.chicv.pda.ui.stock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chicv.pda.R;
import com.chicv.pda.adapter.BadDetailAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.StockAbnomalGoods;
import com.chicv.pda.bean.param.BadGoodsParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;
import com.chicv.pda.widget.BadHandleDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-09-20
 * email: liheyu999@163.com
 * 报损详情页面
 */
public class BadDetailActivity extends BaseActivity {

    public static final String KEY_BAD_ID = "key_bad_id";
    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;
    @BindView(R.id.text_count)
    TextView textCount;

    private int badId;
    private BadDetailAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bad_detail);
        ButterKnife.bind(this);
        badId = getIntent().getIntExtra(KEY_BAD_ID, 0);
        if (badId == 0) finish();
        initToolbar("报损单审核");
        initView();
        initData();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new BadDetailAdapter();
        rlvGoods.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showHandleDialog(mAdapter.getData().get(position));
            }
        });
    }

    private void showHandleDialog(StockAbnomalGoods goods) {
        BadHandleDialog dialog = new BadHandleDialog(this, goods);
        dialog.show();
        dialog.setHandleClickListener(new BadHandleDialog.ClickListener() {
            @Override
            public void listener() {
                mAdapter.notifyDataSetChanged();
                refreshCountView();
            }
        });
    }

    private void initData() {
        wrapHttp(apiService.getAbnomalGoodsList(badId))
                .compose(this.<List<StockAbnomalGoods>>bindToLifecycle())
                .subscribe(new RxObserver<List<StockAbnomalGoods>>(this) {
                    @Override
                    public void onSuccess(List<StockAbnomalGoods> value) {
                        mAdapter.setNewData(value);
                        refreshCountView();
                    }

                    @Override
                    public void onFailure(String msg) {
                        finish();
                    }
                });
    }

    private void refreshCountView() {
        int totalCount = mAdapter.getData().size();
        int cancelCount = 0;
        int confirmCount = 0;
        for (StockAbnomalGoods item : mAdapter.getData()) {
            if (item.getType() == 1) {
                confirmCount++;
            } else if (item.getType() == 2) {
                cancelCount++;
            }
        }
        textCount.setText(String.format(Locale.CHINA, "待复核:%d,已复核:%d,撤回数:%d,确认数:%d,",
                totalCount - (cancelCount + confirmCount), cancelCount + confirmCount, cancelCount, confirmCount));
    }

    @OnClick({R.id.btn_clear, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_clear:
                clear();
                break;
            case R.id.btn_commit:
                commit();
                break;
        }
    }

    private void commit() {
        if (mAdapter.getData().size() == 0) {
            return;
        }
        if (getNotHandleCount() > 0) {
            ToastUtils.showString("还有待审核的数据");
            return;
        }

        BadGoodsParam param = new BadGoodsParam();
        List<BadGoodsParam.GoodsDetial> list = new ArrayList<>();
        int cancelCount = 0;
        int confirmCount = 0;
        for (StockAbnomalGoods item : mAdapter.getData()) {
            if (item.getType() == 1) {
                confirmCount++;
            } else if (item.getType() == 2) {
                cancelCount++;
            }
            BadGoodsParam.GoodsDetial goods = new BadGoodsParam.GoodsDetial();
            goods.setGoodsId(item.getGoodsId());
            goods.setHandleType(item.getType());
            list.add(goods);
        }
        param.setCancleCount(cancelCount);
        param.setSuccessCount(confirmCount);
        param.setLossesId(badId);
        param.setDetailList(list);
        wrapHttp(apiService.postLossesCheckData(param))
                .compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>() {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("操作成功");
                        SoundUtils.playSuccess();
                        finish();
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                    }
                });
    }

    private void clear() {
        for (StockAbnomalGoods item : mAdapter.getData()) {
            item.setType(0);
        }
        mAdapter.notifyDataSetChanged();
        refreshCountView();
    }

    private int getNotHandleCount() {
        int totalCount = mAdapter.getData().size();
        int cancelCount = 0;
        int confirmCount = 0;
        for (StockAbnomalGoods item : mAdapter.getData()) {
            if (item.getType() == 1) {
                confirmCount++;
            } else if (item.getType() == 2) {
                cancelCount++;
            }
        }
        return totalCount - (cancelCount + confirmCount);
    }
}
