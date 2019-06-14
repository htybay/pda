package com.chicv.pda.adapter;

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
public class TransferPickAdapter extends BaseQuickAdapter<TransferPickGoods, BaseViewHolder> {


    public TransferPickAdapter() {
        super(R.layout.item_transfer_pick);
    }

    @Override
    protected void convert(BaseViewHolder helper, TransferPickGoods item) {
        String wpCode = BarcodeUtils.generateWPBarcode(item.getGoodsId());
        helper.setText(R.id.text_product_code, wpCode);
        helper.setText(R.id.text_rule, item.getBatchCode());
        helper.setText(R.id.text_stock, item.getGridName());
        helper.setText(R.id.text_color_size, item.getSpecification());
        helper.setBackgroundRes(R.id.ll_root, item.isIsPick() ? R.color.blue_light : R.color.white);
    }
}
