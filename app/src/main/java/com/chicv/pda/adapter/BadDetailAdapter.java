package com.chicv.pda.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.bean.StockAbnomalGoods;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.DateUtils;

/**
 * @author lilaoda
 * date: 2019/5/26 0026
 * email：liheyu999@163.com
 * description： 报损单列表
 */
public class BadDetailAdapter extends BaseQuickAdapter<StockAbnomalGoods, BaseViewHolder> {


    public BadDetailAdapter() {
        super(R.layout.item_bad_detail);
    }

    @Override
    protected void convert(final BaseViewHolder helper, StockAbnomalGoods item) {
        String wpCode = BarcodeUtils.generateWPBarcode(item.getGoodsId());
        helper.setText(R.id.text_product, TextUtils.isEmpty(item.getBatchCode()) ? wpCode : item.getBatchCode());
        helper.setText(R.id.text_stock, item.getDescription());
        helper.setText(R.id.text_time, DateUtils.getPdaDate(item.getCreateTime()));
        helper.setText(R.id.text_creator, item.getCreateUserName());
        View view = helper.getView(R.id.ll_root);
        if (item.getType() == 2) {
            view.setBackgroundResource(R.color.red_light);
        } else if (item.getType() == 1) {
            view.setBackgroundResource(R.color.blue_light);
        } else {
            view.setBackgroundResource(R.color.white);
        }
    }
}
