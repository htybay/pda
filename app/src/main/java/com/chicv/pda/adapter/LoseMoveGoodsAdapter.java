package com.chicv.pda.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.bean.StockMoveRoom;
import com.chicv.pda.utils.BarcodeUtils;

/**
 * @author lilaoda
 * date: 2019/5/26 0026
 * email：liheyu999@163.com
 * description： 移库丢失适配器
 */
public class LoseMoveGoodsAdapter extends BaseQuickAdapter<StockMoveRoom.DetailsBean, BaseViewHolder> {

    public LoseMoveGoodsAdapter() {
        super(R.layout.item_lose_move_goods);
    }

    @Override
    protected void convert(BaseViewHolder helper, StockMoveRoom.DetailsBean item) {
        String wpCode = BarcodeUtils.generateWPBarcode(item.getGoodsId());
//        helper.textProductNum.setText(TextUtils.isEmpty(item.getBatchCode()) ? wpCode : item.getBatchCode());
        helper.setText(R.id.text_product_num,wpCode);
        helper.setText(R.id.text_rule,item.getBatchCode());
        helper.setText(R.id.text_count,"1");
    }
}
