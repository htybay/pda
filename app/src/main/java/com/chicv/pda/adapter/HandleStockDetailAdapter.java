package com.chicv.pda.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.bean.GoodsBatchCode;

/**
 * @author lilaoda
 * date: 2019/5/26 0026
 * email：liheyu999@163.com
 * description： 手动理库
 */
public class HandleStockDetailAdapter extends BaseQuickAdapter<GoodsBatchCode, BaseViewHolder> {


    public HandleStockDetailAdapter() {
        super(R.layout.item_handle_stock_detail);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsBatchCode item) {
        helper.setText(R.id.text_product, item.getBatchCode());
        helper.setText(R.id.text_scan, String.valueOf(item.getScanCount()));
        helper.setBackgroundRes(R.id.ll_root, item.getScanCount() == item.getTotalCount() ? R.color.blue_light : R.color.white);

    }

    public void setScanBatch(String batchCode) {
        for (GoodsBatchCode item : mData) {
            if (TextUtils.equals(item.getBatchCode().toLowerCase(), batchCode.toLowerCase())) {
                item.setScanCount(item.getScanCount() + 1);
                break;
            }
        }
    }

    public  int getScanCout(){
        int count = 0;
        for (GoodsBatchCode item : mData) {
           count+=item.getScanCount();
        }
        return count;
    }
}
