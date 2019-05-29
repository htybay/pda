package com.chicv.pda.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.bean.PickGoods;
import com.chicv.pda.bean.User;
import com.chicv.pda.bean.param.PickGoodsParam;
import com.chicv.pda.repository.HttpManager;
import com.chicv.pda.repository.remote.ApiService;
import com.chicv.pda.repository.remote.RxObserver;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.CommonUtils;
import com.chicv.pda.utils.DateUtils;
import com.chicv.pda.utils.SPUtils;
import com.chicv.pda.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chicv.pda.utils.RxUtils.wrapHttp;

public class UpdateDialog extends Dialog {

    @BindView(R.id.edit_code)
    EditText editCode;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_time)
    TextView textTime;

    private BaseActivity mActivity;
    private ApiService apiService;
    private PickGoods mPickGoods;
    private User user;

    public UpdateDialog(BaseActivity context) {
        super(context);
        mActivity = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pick_goods);
        ButterKnife.bind(this);
        apiService = HttpManager.getInstance().getApiService();
        user = SPUtils.getUser();
        initConfig();
        initView();
    }

    private void initView() {
        editCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    handleBarcode(CommonUtils.getString(editCode));
                }
                return false;
            }
        });
        editCode.setText("jh-002169765");
    }

    private void initConfig() {
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        WindowManager windowManager = window.getWindowManager();
        WindowManager.LayoutParams attributes = window.getAttributes();
        Display defaultDisplay = windowManager.getDefaultDisplay();
        attributes.width = (int) (defaultDisplay.getWidth() * 0.9);
        window.setBackgroundDrawableResource(R.drawable.shape_circle_corner_white);
        window.setAttributes(attributes);
    }

    @OnClick({R.id.btn_cancel, R.id.btn_change_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_change_name:
                if (mPickGoods == null) {
                    ToastUtils.showString("请先扫描拣货单");
                    return;
                }
                if (TextUtils.equals(mPickGoods.getPickDutyUserName(), user.getName())) {
                    ToastUtils.showString("当前拣货单的负责人已是：" + user.getName());
                    return;
                }
                showChangePersonDialog();
                break;
        }
    }

    //收到扫描数据
    private void handleBarcode(String barcode) {
        if (TextUtils.isEmpty(barcode)) {
            ToastUtils.showString("条码为空！");
            return;
        }
        if (!BarcodeUtils.isPickCode(barcode)) {
            ToastUtils.showString("无效的条码！");
            return;
        }

        //捡货单号
        getPickGoodsInfo(String.valueOf(BarcodeUtils.getBarcodeId(barcode)));
    }

    //扫描到拣货单条码 ，根据拣货单ID查找拣货信息
    private void getPickGoodsInfo(String barcodeId) {
        wrapHttp(apiService.getPickGoodsInfo(barcodeId))
                .compose(mActivity.<PickGoods>bindToLifecycle())
                .subscribe(new RxObserver<PickGoods>(true, mActivity) {
                    @Override
                    public void onSuccess(PickGoods value) {
                        if (value.getPickStatus() == 20) {
                            //拣货单未领取是主动调用领取接口领取该捡货单
                            receivePickGoods(value.getId());
                            return;
                        }
                        mPickGoods = value;
                        setViewData();
                    }

                    @Override
                    public void onFailure(String msg) {
                        clearViewData();
                    }
                });
    }

    // 领取捡货单
    private void receivePickGoods(final long id) {
        PickGoodsParam param = new PickGoodsParam();
        param.setPickId(String.valueOf(id));
        wrapHttp(apiService.receivePickGoods(param))
                .subscribe(new RxObserver<Object>(true, mActivity) {
                    @Override
                    public void onSuccess(Object value) {
                        changeViewData();
                    }
                });
    }

    private void showChangePersonDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage("当前负责人为：" + mPickGoods.getPickDutyUserName()+" 确认更改为："+user.getName())
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changePerson();
                    }
                }).show();
    }

    //更改捡货单负责人
    private void changePerson() {
        PickGoodsParam param = new PickGoodsParam();
        param.setPickId(String.valueOf(mPickGoods.getId()));
        wrapHttp(apiService.changePickGoods(param)).compose(mActivity.bindToLifecycle())
                .subscribe(new RxObserver<Object>(true,mActivity) {
                    @Override
                    public void onSuccess(Object value) {
                        ToastUtils.showString("更改成功！");
                        changeViewData();
                    }
                });
    }

    private void changeViewData() {
        textName.setText(user.getName());
        textTime.setText(DateUtils.getTime());
    }

    private void setViewData() {
        textName.setText(mPickGoods.getPickDutyUserName());
        textTime.setText(DateUtils.getPdaDate(mPickGoods.getPickReceiveTime()));
    }

    private void clearViewData() {
        textTime.setText("");
        textTime.setText("");

    }


}
