package com.chicv.pda.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.bean.LoseGoods;
import com.chicv.pda.utils.BarcodeUtils;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author lilaoda
 * date: 2019/5/26 0026
 * email：liheyu999@163.com
 * description： 配货丢失适配器
 */
public class LoseDeliveryGoodsAdapter extends BaseQuickAdapter<LoseGoods, LoseDeliveryGoodsAdapter.MyHolderHolder> {

    public LoseDeliveryGoodsAdapter() {
        super(R.layout.item_lose_delivery_goods);
    }

    @Override
    protected void convert(MyHolderHolder helper, LoseGoods item) {
        String wpCode = BarcodeUtils.generateWPBarcode(item.getGoodsId());
//        helper.textProductNum.setText(TextUtils.isEmpty(item.getBatchCode()) ? wpCode : item.getBatchCode());
        helper.textProductNum.setText(wpCode);
        helper.textRule.setText(item.getBatchCode());
        helper.textStock.setText(item.getGridName());
        if (item.isScan()) {
            helper.llRoot.setBackgroundResource(R.color.blue_light);
        } else {
            helper.llRoot.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public void setNewData(@Nullable List<LoseGoods> data) {
        if (data != null) {
            Collections.sort(data);
        }
        super.setNewData(data);
    }

    public static class MyHolderHolder extends BaseViewHolder {


        @BindView(R.id.ll_root)
        LinearLayout llRoot;
        //产品编号
        @BindView(R.id.text_product_num)
        TextView textProductNum;
        //囤货规格
        @BindView(R.id.text_rule)
        TextView textRule;
        //货位信息
        @BindView(R.id.text_stock)
        TextView textStock;

        public MyHolderHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
