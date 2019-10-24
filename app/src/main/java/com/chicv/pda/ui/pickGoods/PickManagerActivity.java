package com.chicv.pda.ui.pickGoods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.base.Constant;
import com.chicv.pda.bean.PickGoods;
import com.chicv.pda.bean.User;
import com.chicv.pda.bean.event.PickOverEvent;
import com.chicv.pda.bean.param.PickGoodsParam;
import com.chicv.pda.bean.param.PickListParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.DateUtils;
import com.chicv.pda.utils.PdaUtils;
import com.chicv.pda.utils.SPUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-10-21
 * email: liheyu999@163.com
 */
public class PickManagerActivity extends BaseActivity {

    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    Button btnReceive;

    BaseQuickAdapter<PickGoods, BaseViewHolder> mAdapter;
    private User mUser;
    private View mEmptyView;
    private Disposable mTimeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_manager);
        ButterKnife.bind(this);
        mUser = SPUtils.getUser();
        EventBus.getDefault().register(this);
        initToolbar("单件拣货");
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getPickList() {
        getPickList(0);
    }

    private void getPickList(final int hkId) {
        PickListParam param = new PickListParam();
        param.setPickDutyUserName(mUser.getName());
        param.setContainerId(hkId);
        wrapHttp(apiService.getPickList(param))
                .compose(this.<List<PickGoods>>bindToLifecycle())
                .subscribe(new RxObserver<List<PickGoods>>() {
                    @Override
                    public void onSuccess(List<PickGoods> value) {
                        mAdapter.setNewData(value);
                        refreshLayout.finishRefresh(true);
                    }

                    @Override
                    public void onFailure(String msg) {
                        super.onFailure(msg);
                        refreshLayout.finishRefresh(false);
                    }
                });
    }

    private void initView() {
        mAdapter = new BaseQuickAdapter<PickGoods, BaseViewHolder>(R.layout.item_hk_list) {
            @Override
            protected void convert(BaseViewHolder helper, PickGoods item) {
                helper.setText(R.id.text_product_num, BarcodeUtils.generateJHBarcode(item.getId()));
                if (item.getContainer() == null || item.getContainer().getContainerId() == 0) {
                    helper.setGone(R.id.ll_hk, false);
                } else {
                    helper.setGone(R.id.ll_hk, true);
                    helper.setText(R.id.text_hk_num, BarcodeUtils.generateHKBarcode(item.getContainer().getContainerId()));
                }
                helper.setText(R.id.text_name, item.getPickDutyUserName());
                helper.setText(R.id.text_time, DateUtils.getPdaDate(item.getPickReceiveTime()));
                helper.setText(R.id.text_floor, item.getFloorNames());
                helper.setText(R.id.text_size, PdaUtils.getLocationTypeDes(item.getLocationType()));
            }
        };
        rlvGoods.setLayoutManager(new LinearLayoutManager(this));
        rlvGoods.setAdapter(mAdapter);
        mEmptyView = View.inflate(getApplicationContext(), R.layout.view_empty_pick_manager, null);
        btnReceive = mEmptyView.findViewById(R.id.btn_receive);
        btnReceive.setVisibility(mUser.containPermission(PdaUtils.PERMISSION_PICK_RECEIVE) ? View.VISIBLE : View.GONE);
        btnReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiveOrder();
            }
        });
        mAdapter.setEmptyView(mEmptyView);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PickGoods item = mAdapter.getItem(position);
                if (item == null) {
                    return;
                }
                //是否领取 未领导自动领取后跳到拣货页面 若领取失败刷新页面
                if (item.getPickStatus() == Constant.PICK_STATUS_UNRECEIVE) {
                    receivePickGoods(item.getId());
                } else {
                    PickHkGoodsActivity.start(PickManagerActivity.this, (int) item.getId());
                }
            }
        });

        refreshLayout.autoRefresh();
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getPickList();
            }
        });
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        if (BarcodeUtils.isHKCode(barcode)) {
            //扫描的货柜 根据货框ID去加载列表
            getPickList(BarcodeUtils.getBarcodeId(barcode));
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }

    // 领取捡货单
    private void receivePickGoods(final long pickId) {
        PickGoodsParam param = new PickGoodsParam();
        param.setPickId(String.valueOf(pickId));
        wrapHttp(apiService.receivePickGoods(param))
                .subscribe(new RxObserver<Object>(this) {
                    @Override
                    public void onSuccess(Object value) {
                        PickHkGoodsActivity.start(PickManagerActivity.this, (int) pickId);
                    }

                    @Override
                    public void onFailure(String msg) {
                        //刷新页面
                        refreshLayout.autoRefresh();
                    }
                });
    }

    //接单
    private void receiveOrder() {
        if (mAdapter.getData().size() > 0) {
            ToastUtils.showString("您有拣货中的数据，请先拣货完成或刷新页面");
            return;
        }
        wrapHttp(apiService.distributeOrder())
                .compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(this) {
                    @Override
                    public void onSuccess(Object value) {
                        statTimeTask();
                    }
                });
    }

    private void statTimeTask() {
        mTimeDisposable = Flowable.interval(0, 1, TimeUnit.SECONDS)
                .take(11)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return 10 - aLong;
                    }
                })
                .onBackpressureLatest()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindToLifecycle())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        btnReceive.setEnabled(false);
                        refreshLayout.setEnableRefresh(false);
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        btnReceive.setEnabled(true);
                        btnReceive.setText("接单");
                        refreshLayout.setEnableRefresh(true);
                        refreshLayout.autoRefresh();
                    }
                })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        btnReceive.setText(String.format(Locale.CHINA, "正在派单中(%d S)", aLong));
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PickOverEvent event) {
        removeByPickId(event.getPickId());
        if (event.isContinuePick()) {
            receiveOrder();
        }
    }

    private void removeByPickId(int pickId) {
        int position = -1;
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            PickGoods item = mAdapter.getData().get(i);
            if (item.getId() == pickId) {
                position = i;
                break;
            }
        }
        if (position != -1) {
            mAdapter.remove(position);
        }
    }
}
