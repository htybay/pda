package com.chicv.pda.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.bean.StockTaking;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.PdaUtils;

/**
 * @author lilaoda
 * date: 2019/5/26 0026
 * email：liheyu999@163.com
 * description： 报损单列表
 */
public class CheckListdapter extends BaseQuickAdapter<StockTaking, BaseViewHolder> {


    public CheckListdapter() {
        super(R.layout.item_check_list);
    }

    @Override
    protected void convert(final BaseViewHolder helper, StockTaking item) {
        helper.setText(R.id.text_product, BarcodeUtils.generatePDBarcode(item.getId()));
        helper.setText(R.id.text_status, PdaUtils.getTakingStatusDes(item.getStockTakingStatus()));
        helper.setText(R.id.text_type, PdaUtils.getTakingTypeDes(item.getStockTakingType()));
    }

}
