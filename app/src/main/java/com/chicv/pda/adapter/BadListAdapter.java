package com.chicv.pda.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.bean.StockLoss;
import com.chicv.pda.utils.BarcodeUtils;

/**
 * @author lilaoda
 * date: 2019/5/26 0026
 * email：liheyu999@163.com
 * description： 报损单列表
 */
public class BadListAdapter extends BaseQuickAdapter<StockLoss, BaseViewHolder> {


    public BadListAdapter() {
        super(R.layout.item_bad_list);
    }

    @Override
    protected void convert(final BaseViewHolder helper, StockLoss item) {
        helper.setText(R.id.text_product, BarcodeUtils.generateBSBarcode(item.getId()));
        helper.setText(R.id.text_total, String.valueOf(item.getTotalCount()));
        helper.setText(R.id.text_status, "待审核");
        helper.setText(R.id.text_creator, item.getCreateUserName());
    }

    public boolean existBadId(int bsId) {
        for (StockLoss item : getData()) {
            if (item.getId() == bsId)
                return true;
        }
        return false;
    }
}
