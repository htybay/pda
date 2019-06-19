package com.chicv.pda.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.bean.TransferIn;
import com.chicv.pda.utils.BarcodeUtils;

/**
 * @author lilaoda
 * date: 2019/5/26 0026
 * email：liheyu999@163.com
 * description： 内部捡货单适配器
 */
public class TransferInStockAdapter extends BaseQuickAdapter<TransferIn.DetailsBean, BaseViewHolder> {


    public TransferInStockAdapter() {
        super(R.layout.item_transfer_in_stock);
    }

    @Override
    protected void convert(BaseViewHolder helper, TransferIn.DetailsBean item) {
        String wpCode = BarcodeUtils.generateWPBarcode(item.getGoodsId());
        helper.setText(R.id.text_product, TextUtils.isEmpty(item.getBatchCode()) ? wpCode : item.getBatchCode());
        helper.setText(R.id.text_color_size, item.getSpecification());
        helper.setText(R.id.text_goods_id, wpCode);
        helper.setBackgroundRes(R.id.ll_root, item.isIsIn() ? R.color.blue_light : R.color.white);
    }
}
