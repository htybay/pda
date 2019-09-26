package com.chicv.pda.widget;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.bean.StockAbnomalGoods;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.PdaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 报损处理对话框
 */
public class BadHandleDialog extends Dialog {

    @BindView(R.id.text_batch_code)
    TextView textBatchCode;
    @BindView(R.id.text_goods_id)
    TextView textGoodsId;
    @BindView(R.id.text_status)
    TextView textStatus;

    private final StockAbnomalGoods mGoods;

    public BadHandleDialog(Activity context, StockAbnomalGoods goods) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mGoods = goods;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bad_handle);
        ButterKnife.bind(this);
        initConfig();
        initView();
    }

    private void initView() {
        textBatchCode.setText(mGoods.getBatchCode());
        textGoodsId.setText(BarcodeUtils.generateWPBarcode(mGoods.getGoodsId()));
        textStatus.setText(PdaUtils.getSourceTypeDes(mGoods.getSourceType()));
    }

    private void initConfig() {
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        Window window = getWindow();
        WindowManager windowManager = window.getWindowManager();
        WindowManager.LayoutParams attributes = window.getAttributes();
        Display defaultDisplay = windowManager.getDefaultDisplay();
        attributes.width = (int) (defaultDisplay.getWidth() * 0.8);
        window.setBackgroundDrawableResource(R.drawable.shape_corner_white);
        window.setAttributes(attributes);
    }


    @OnClick({R.id.btn_cancel, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                if (mGoods.getType() != 2) {
                    mGoods.setType(2);
                    if (mListener != null) {
                        mListener.listener();
                    }
                }
                break;
            case R.id.btn_confirm:
                dismiss();
                if (mGoods.getType() != 1) {
                    mGoods.setType(1);
                    if (mListener != null) {
                        mListener.listener();
                    }
                }
                break;
        }
    }

    public interface ClickListener {
        void listener();
    }

    private ClickListener mListener;

    public void setHandleClickListener(ClickListener listener) {
        mListener = listener;
    }
}
