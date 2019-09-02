package com.chicv.pda.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.bean.PositionGoods;

/**
 * @author lilaoda
 * date: 2019/5/26 0026
 * email：liheyu999@163.com
 * description： 理库点数
 */
public class HandleStockCountAdapter extends BaseQuickAdapter<PositionGoods, BaseViewHolder> {


    public HandleStockCountAdapter() {
        super(R.layout.item_handle_stock_count);
    }

    @Override
    protected void convert(BaseViewHolder helper, PositionGoods item) {
        helper.setText(R.id.text_product, item.getBatchCode());
        helper.setText(R.id.text_total, String.valueOf(item.getTotalCount()));
        helper.setText(R.id.text_scan, String.valueOf(item.getScanCount()));
        helper.setBackgroundRes(R.id.ll_root, item.getScanCount() == item.getTotalCount() ? R.color.blue_light : R.color.white);

    }

    public void setScanBatch(String batchCode) {
        for (PositionGoods item : mData) {
            if (TextUtils.equals(item.getBatchCode().toLowerCase(), batchCode.toLowerCase())) {
                item.setScanCount(item.getScanCount() + 1);
                break;
            }
        }
    }
}
