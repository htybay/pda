package com.chicv.pda.ui.goods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.ExpressBean;
import com.chicv.pda.bean.User;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.DateUtils;
import com.chicv.pda.utils.SPUtils;
import com.chicv.pda.utils.SoundUtils;
import com.chicv.pda.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

/**
 * author: liheyu
 * date: 2019-06-04
 * email: liheyu999@163.com
 * 收货 扫描单号成功后立即调用签收接口，失败则不调
 */
public class ReceiveGoodsActivity extends BaseActivity {

    @BindView(R.id.edit_barcode)
    EditText editBarcode;
    @BindView(R.id.text_send_num)
    TextView textSendNum;
    @BindView(R.id.text_weight)
    TextView textWeight;
    @BindView(R.id.text_time)
    TextView textTime;
    @BindView(R.id.text_person)
    TextView textPerson;
    @BindView(R.id.text_company)
    TextView textCompany;
    @BindView(R.id.text_express)
    TextView textExpress;
    @BindView(R.id.text_type)
    TextView textType;
    @BindView(R.id.text_sign_count)
    TextView textSignCount;
    @BindView(R.id.text_packet_count)
    TextView textPacketCount;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_goods);
        user = SPUtils.getUser();
        ButterKnife.bind(this);
        initToolbar("收货");
    }

    @Override
    protected void onReceiveBarcode(String barcode) {
        //物流单号，扫到什么就是什么
        if (BarcodeUtils.isExpressCode(barcode)) {
            getExpressInfo(barcode);
        } else {
            ToastUtils.showString("无效的条码！");
            SoundUtils.playError();
        }
    }

    private void getExpressInfo(String barcode) {
        wrapHttp(apiService.getReceiveByExpressNo(barcode))
                .compose(this.<List<ExpressBean>>bindToLifecycle())
                .subscribe(new RxObserver<List<ExpressBean>>(true, this) {
                    @Override
                    public void onSuccess(List<ExpressBean> value) {
                        if (value.size() != 1) {
                            ToastUtils.showString("数据异常！");
                            SoundUtils.playError();
                            clearViewData();
                            return;
                        }
                        //收到数据后立即调用接货接口，成功后清空页面，失败不清空
                        setViewData(value.get(0));
                        receiveGoods(value.get(0));
                    }

                    @Override
                    public void onFailure(String msg) {
                        clearViewData();
                        SoundUtils.playError();
                    }
                });
    }

    private void receiveGoods(ExpressBean bean) {
        bean.setSignUserName(user.getId());
        bean.setSignTime(DateUtils.getPdaDate());

        wrapHttp(apiService.expressSign(bean))
                .compose(bindToLifecycle())
                .subscribe(new RxObserver<Object>(false, this) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("收货成功");
                        clearViewData();
                        SoundUtils.playSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        ToastUtils.showString("收货异常：" + msg);
                        SoundUtils.playError();
                    }
                });
    }

    private void clearViewData() {
        textSendNum.setText("");
        textWeight.setText("");
        textTime.setText("");
        textPerson.setText("");
        textCompany.setText("");
        textExpress.setText("");
        textType.setText("");
        textPacketCount.setText("");
    }

    private void setViewData(ExpressBean bean) {
        textSendNum.setText(bean.getDeliveryQuantity() + "件");
        textWeight.setText(bean.getDeliveryWeight() + "g");
        textTime.setText(TextUtils.isEmpty(bean.getDeliveryTime()) ? "" : DateUtils.getPdaDate(bean.getDeliveryTime()));
        textPerson.setText(bean.getSingleUserName());
        textCompany.setText(bean.getSupplierName());
        textExpress.setText(bean.getExpressName() + "\n" + bean.getExpressNo());
        textType.setText(getTypeDes(bean.getType()));
        textPacketCount.setText(bean.getPackageCount() + "件");
    }

    private String getTypeDes(String type) {
        String result;
        if (TextUtils.equals("PurchaseReceive", type)) {
            result = "采购包裹";
        } else if (TextUtils.equals("StockReceive", type)) {
            result = "囤货包裹";
        } else {
            result = "未知包裹";
        }
        return result;
    }
}
