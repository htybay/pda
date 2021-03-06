package com.chicv.pda.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.bean.PurchaseGoods;
import com.chicv.pda.utils.BarcodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author lilaoda
 * date: 2019/5/26 0026
 * email：liheyu999@163.com
 * description： 配货丢失适配器
 */
public class InStockAdapter extends BaseQuickAdapter<PurchaseGoods, InStockAdapter.MyHolderHolder> {


    public InStockAdapter() {
        super(R.layout.item_in_stock);
    }

    @Override
    protected void convert(MyHolderHolder helper, PurchaseGoods item) {
        String wpCode = BarcodeUtils.generateWPBarcode(item.getId());
//        helper.textProductNum.setText(TextUtils.isEmpty(item.getBatchCode()) ? wpCode : item.getBatchCode());
        helper.textProductNum.setText(wpCode);
        helper.textSku.setText(String.valueOf(item.getSkuId()));
        helper.textSize.setText(item.getSpecification());
    }


    public static class MyHolderHolder extends BaseViewHolder {

        @BindView(R.id.text_sku)
        TextView textSku;
        @BindView(R.id.text_product_num)
        TextView textProductNum;
        @BindView(R.id.text_size)
        TextView textSize;

        public MyHolderHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
