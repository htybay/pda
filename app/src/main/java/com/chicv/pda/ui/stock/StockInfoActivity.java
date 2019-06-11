package com.chicv.pda.ui.stock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.StockInfo;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.DateUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-06-10
 * email: liheyu999@163.com
 * 货物信息查询
 */
public class StockInfoActivity extends BaseActivity {

    public static final String KEY_STOCK_ID = "key_stock_id";

    @BindView(R.id.text_stock_id)
    TextView textStockId;
    @BindView(R.id.text_stock_name)
    TextView textStockName;
    @BindView(R.id.text_position)
    TextView textPosition;
    @BindView(R.id.text_create_time)
    TextView textCreateTime;
    @BindView(R.id.text_update_time)
    TextView textUpdateTime;
    @BindView(R.id.text_create_person)
    TextView textCreatePerson;
    @BindView(R.id.text_update_person)
    TextView textUpdatePerson;
    @BindView(R.id.text_enable)
    TextView textEnable;
    @BindView(R.id.text_remark)
    TextView textRemark;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_info);
        ButterKnife.bind(this);
        initToolbar("货位信息查询");
        int stockId = getIntent().getIntExtra(KEY_STOCK_ID, 0);
        if (stockId != 0) {
            handleStockBarcode(stockId);
        }
    }


    @Override
    protected void onReceiveBarcode(String barcode) {
        if (BarcodeUtils.isContainerCode(barcode)) {
            //货位单号
            handleStockBarcode(BarcodeUtils.getBarcodeId(barcode));
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }

    private void handleStockBarcode(int stockId) {
        wrapHttp(apiService.getStockInfoByStockId(stockId))
                .compose(this.<StockInfo>bindToLifecycle())
                .subscribe(new RxObserver<StockInfo>(true, this) {
                    @Override
                    public void onSuccess(StockInfo value) {
                        setData(value);
                    }

                    @Override
                    public void onFailure(String msg) {
                        SoundUtils.playError();
                        clearData();
                    }
                });
    }

    private void clearData() {
        textStockId.setText("");
        textStockName.setText("");
        textPosition.setText("");
        textCreateTime.setText("");
        textUpdateTime.setText("");
        textCreatePerson.setText("");
        textUpdatePerson.setText("");
        textEnable.setText("");
        textRemark.setText("");
    }

    private void setData(StockInfo value) {
        textStockId.setText(BarcodeUtils.generateHWBarcode(value.getId()));
        textStockName.setText(value.getName());
        textPosition.setText(value.getPositionText());
        textCreateTime.setText(DateUtils.getPdaDate(value.getCreateTime()));
        textUpdateTime.setText(DateUtils.getPdaDate(value.getUpdateTime()));
        textCreatePerson.setText(value.getCreateUserName());
        textUpdatePerson.setText(value.getUpdateUserName());
        textEnable.setText(value.isEnable() ? "是" : "否");
//        textRemark.setText(value.getDescription());
    }
}
