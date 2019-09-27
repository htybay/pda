package com.chicv.pda.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.bean.BatchMoveBean;

/**
 * @author lilaoda
 * date: 2019/5/26 0026
 * email：liheyu999@163.com
 * description： 囤货规格批量移位
 */
public class BatchManyMovelAdapter extends BaseQuickAdapter<BatchMoveBean, BaseViewHolder> {


    public BatchManyMovelAdapter() {
        super(R.layout.item_batch_many_move);
    }

    @Override
    protected void convert(BaseViewHolder helper, BatchMoveBean item) {
        helper.setText(R.id.text_product, item.getBatchCode());
        helper.setText(R.id.text_total, String.valueOf(item.getWaitMoveCount()));
        helper.setText(R.id.text_scan, String.valueOf(item.getScanCount()));
        helper.setBackgroundRes(R.id.ll_root, item.getScanCount() == item.getWaitMoveCount() ? R.color.blue_light : R.color.white);

    }
}
