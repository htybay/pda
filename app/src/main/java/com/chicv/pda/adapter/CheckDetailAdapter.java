package com.chicv.pda.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.bean.TakingSubTask;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.PdaUtils;

/**
 * @author lilaoda
 * date: 2019/5/26 0026
 * email：liheyu999@163.com
 * description： 报损单列表
 */
public class CheckDetailAdapter extends BaseQuickAdapter<TakingSubTask.StockTakingRecord, BaseViewHolder> {


    public CheckDetailAdapter() {
        super(R.layout.item_check_detail);
    }

    @Override
    protected void convert(final BaseViewHolder helper, TakingSubTask.StockTakingRecord item) {
        helper.setText(R.id.text_product, BarcodeUtils.generateWPBarcode(item.getGoodsId()));
        helper.setText(R.id.text_batch, item.getBatchCode());
        helper.setText(R.id.text_status, PdaUtils.getCheckStatusDes(item.getCheckStatus()));
        View view = helper.getView(R.id.ll_root);
        if (item.getCheckStatus() == PdaUtils.CHECK_STATUS_EXTRA) {
            view.setBackgroundResource(R.color.red_light);
        } else if (item.getCheckStatus() == PdaUtils.CHECK_STATUS_CHECKED) {
            view.setBackgroundResource(R.color.blue_light);
        } else {
            view.setBackgroundResource(R.color.white);
        }
    }

    public TakingSubTask.StockTakingRecord queryReadyCheckGoods(int goodsId) {
        for (TakingSubTask.StockTakingRecord item : mData) {
            if (item.getCheckStatus() == PdaUtils.CHECK_STATUS_READY) {
                return item;
            }
        }
        return null;
    }
}
