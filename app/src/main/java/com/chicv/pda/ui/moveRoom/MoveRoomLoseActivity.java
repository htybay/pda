package com.chicv.pda.ui.moveRoom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.adapter.LoseMoveGoodsAdapter;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.StockMoveRoom;
import com.chicv.pda.bean.param.MoveRoomLoseParam;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.PdaUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-09-26
 * email: liheyu999@163.com
 * <p>
 * 移库丢失
 */
public class MoveRoomLoseActivity extends BaseActivity {

    @BindView(R.id.text_pick_num)
    TextView textPickNum;
    @BindView(R.id.text_total)
    TextView textTotal;
    @BindView(R.id.text_diff)
    TextView textDiff;
    @BindView(R.id.rlv_goods)
    RecyclerView rlvGoods;

    private LoseMoveGoodsAdapter mAdapter;
    private String mBarcode;
    private List<StockMoveRoom.DetailsBean> mMovingList;
    private StockMoveRoom mStockMoveRoom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_lose);
        ButterKnife.bind(this);
        initToolbar("移库丢失");
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvGoods.setLayoutManager(layoutManager);
        mAdapter = new LoseMoveGoodsAdapter();
        rlvGoods.setAdapter(mAdapter);
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        mBarcode = barcode;
        if (BarcodeUtils.isMoveCode(barcode)) {
            //移库单号
            getStockMoveRoom(BarcodeUtils.getBarcodeId(barcode));
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }

    private void getStockMoveRoom(int barcodeId) {
        wrapHttp(apiService.getStockMoveRoom(barcodeId)).compose(this.<StockMoveRoom>bindToLifecycle())
                .subscribe(new RxObserver<StockMoveRoom>(this) {
                    @Override
                    public void onSuccess(StockMoveRoom value) {
                        if (value.getMoveStatus() != PdaUtils.MOVE_STATUS_OVER) {
                            ToastUtils.showString(String.format(Locale.CHINA, "已完成状态才可以操作丢失 当前状态:%s", PdaUtils.getMoveStatusDes(value.getMoveStatus())));
                            SoundUtils.playError();
                            return;
                        }
                        mStockMoveRoom = value;
                        mMovingList = getMovingGoods(value);
                        textPickNum.setText(mBarcode);
                        textTotal.setText(String.valueOf(value.getDetails().size()));
                        textDiff.setText(String.valueOf(mMovingList.size()));
                        showData(mMovingList);
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                        clearData();
                    }
                });
    }

    private void showData(List<StockMoveRoom.DetailsBean> movingList) {
        List<StockMoveRoom.DetailsBean> result = new ArrayList<>();
        Map<String, StockMoveRoom.DetailsBean> map = new HashMap<>();
        for (StockMoveRoom.DetailsBean item : movingList) {
            if (TextUtils.isEmpty(item.getBatchCode())) {
                item.setLocalTotalCount(1);
                result.add(item);
            } else {
                if (map.containsKey(item.getBatchCode())) {
                    StockMoveRoom.DetailsBean detailsBean = map.get(item.getBatchCode());
                    detailsBean.setLocalTotalCount(detailsBean.getLocalTotalCount() + 1);
                } else {
                    item.setLocalTotalCount(1);
                    map.put(item.getBatchCode(), item);
                    result.add(item);
                }
            }
        }
        mAdapter.setNewData(result);
    }

    private List<StockMoveRoom.DetailsBean> getMovingGoods(StockMoveRoom value) {
        List<StockMoveRoom.DetailsBean> list = new ArrayList<>();
        if (value != null && value.getDetails() != null) {
            for (StockMoveRoom.DetailsBean detail : value.getDetails()) {
                if (detail.getStatus() == 1 && detail.getMoveStatus() == PdaUtils.MOVE_STATUS_MOVING) {
                    list.add(detail);
                }
            }
        }
        return list;
    }


    @OnClick(R.id.btn_lose)
    public void onViewClicked() {
        if (mStockMoveRoom == null) {
            ToastUtils.showString("请扫描移库单");
            SoundUtils.playError();
            return;
        }
        if (mMovingList == null || mMovingList.size() == 0) {
            ToastUtils.showString("移库单异常数为0");
            SoundUtils.playError();
            return;
        }

        MoveRoomLoseParam param = new MoveRoomLoseParam();
        param.setMoveId(mStockMoveRoom.getId());
        List<MoveRoomLoseParam.LoseGoods> list = new ArrayList<>();
        param.setLoseDetail(list);
        for (StockMoveRoom.DetailsBean item : mMovingList) {
            MoveRoomLoseParam.LoseGoods loseGoods = new MoveRoomLoseParam.LoseGoods();
            loseGoods.setDetailId(item.getId());
            loseGoods.setGoodsId(item.getGoodsId());
            loseGoods.setBatchCode(item.getBatchCode());
            list.add(loseGoods);
        }
        wrapHttp(apiService.moveRoomLose(param))
                .compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("操作成功!");
                        clearData();
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                    }
                });
    }

    private void clearData() {
        mStockMoveRoom = null;
        mMovingList = null;
        mAdapter.setNewData(null);
        textPickNum.setText("");
        textTotal.setText("");
        textDiff.setText("");
    }
}
