package com.chicv.pda.adapter;

import android.support.annotation.Nullable;
import android.view.View;
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
 * description：
 */
public class LosePickGoodsAdapter extends BaseQuickAdapter<LoseGoods, LosePickGoodsAdapter.MyHolderHolder> {

    public LosePickGoodsAdapter() {
        super(R.layout.item_lose_pick_goods);
    }

    @Override
    protected void convert(MyHolderHolder helper, LoseGoods item) {
        String wpCode = BarcodeUtils.generateWPBarcode(item.getGoodsId());
        helper.textProductNum.setText(wpCode);
        helper.textRule.setText(item.getBatchCode());
        helper.textStock.setText(item.getGridName());
    }

    @Override
    public void setNewData(@Nullable List<LoseGoods> data) {
        if (data != null) {
            Collections.sort(data);
        }
        super.setNewData(data);
    }

    public static class MyHolderHolder extends BaseViewHolder {

        //产品编号
        @BindView(R.id.text_product_num)
        TextView textProductNum;
        //囤货规格
        @BindView(R.id.text_rule)
        TextView textRule;
        //状态
        @BindView(R.id.text_stock)
        TextView textStock;


        public MyHolderHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
