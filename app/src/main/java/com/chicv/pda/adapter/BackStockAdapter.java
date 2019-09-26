package com.chicv.pda.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.bean.OutGoodsInfo;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.PdaUtils;

/**
 * @author lilaoda
 * date: 2019/5/26 0026
 * email：liheyu999@163.com
 * description： 退货物品出库
 */
public class BackStockAdapter extends BaseQuickAdapter<OutGoodsInfo, BaseViewHolder> {


    public BackStockAdapter() {
        super(R.layout.item_back_stock);
    }

    @Override
    protected void convert(final BaseViewHolder helper, OutGoodsInfo item) {
        String wpCode = BarcodeUtils.generateWPBarcode(item.getGoodsId());
        helper.setText(R.id.text_product,wpCode);
        helper.setText(R.id.text_stock,item.getGridName());
        helper.setText(R.id.text_type, PdaUtils.getGoodsTypeDes(item.getGoodsType()));
        helper.addOnClickListener(R.id.btn_delete);
    }

    public boolean existGoods(int goodsId) {
        for (OutGoodsInfo goods : getData()) {
            if (goods.getGoodsId() == goodsId)
                return true;
        }
        return false;
    }
}
