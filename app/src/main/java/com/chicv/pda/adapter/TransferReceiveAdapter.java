package com.chicv.pda.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.bean.TransferPickGoods;
import com.chicv.pda.utils.BarcodeUtils;

/**
 * @author lilaoda
 * date: 2019/5/26 0026
 * email：liheyu999@163.com
 * description： 内部捡货单适配器
 */
public class TransferReceiveAdapter extends BaseQuickAdapter<TransferPickGoods, BaseViewHolder> {


    public TransferReceiveAdapter() {
        super(R.layout.item_transfer_receive);
    }

    @Override
    protected void convert(BaseViewHolder helper, TransferPickGoods item) {
        String wpCode = BarcodeUtils.generateWPBarcode(item.getGoodsId());
        if (TextUtils.isEmpty(item.getBatchCode())) {
            helper.setText(R.id.text_product, wpCode);
            helper.setText(R.id.text_total, String.valueOf(1));
            helper.setText(R.id.text_scan, item.isIsSign() ? String.valueOf(1) : String.valueOf(0));
            helper.setBackgroundRes(R.id.ll_root, item.isIsSign() ? R.color.blue_light : R.color.white);

        } else {
            helper.setText(R.id.text_product, item.getBatchCode());
            helper.setText(R.id.text_total, String.valueOf(item.getLocalTotalCount()));
            helper.setText(R.id.text_scan, String.valueOf(item.getLocalScanCount()));
            helper.setBackgroundRes(R.id.ll_root, item.getLocalTotalCount() == item.getLocalScanCount() ? R.color.blue_light : R.color.white);

        }
    }
}
