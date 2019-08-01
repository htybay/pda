package com.chicv.pda.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.bean.StockReceiveBatch;

/**
 * @author lilaoda
 * date: 2019/5/26 0026
 * email：liheyu999@163.com
 * description： 样品入库
 */
public class SampleInStockAdapter extends BaseQuickAdapter<StockReceiveBatch, BaseViewHolder> {

    public SampleInStockAdapter() {
        super(R.layout.item_sample_in);
    }

    @Override
    protected void convert(BaseViewHolder helper, StockReceiveBatch item) {
        helper.setText(R.id.text_product, item.getBatchCode());
        String sampleNo = TextUtils.isEmpty(item.getSkuAttribute()) ? "" : item.getSkuAttribute().replace("颜色", "").replace("尺码", "").replace(":", "").replace(";", "");
        helper.setText(R.id.text_size, sampleNo);
        if (item.getWaitReceiveCount() == item.getScanCount()) {
            //扫完
            helper.setBackgroundRes(R.id.ll_root, R.color.green_light);
        } else {
            helper.setBackgroundRes(R.id.ll_root, R.color.white);
        }
    }

    public int findPositionByBatchNo(String barcode) {
        int position = -1;
        for (int i = 0; i < mData.size(); i++) {
            StockReceiveBatch item = mData.get(i);
            if (TextUtils.equals(barcode, item.getBatchCode())) {
                position = i;
                break;
            }
        }
        return position;
    }

    public int getTotalScanCount() {
        int count = 0;
        for (StockReceiveBatch item : mData) {
            count += item.getScanCount();
        }
        return count;
    }
}
