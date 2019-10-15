package com.chicv.pda.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.bean.GoodsMoveBean;
import com.chicv.pda.utils.BarcodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author lilaoda
 * date: 2019/5/26 0026
 * email：liheyu999@163.com
 * description： 配货丢失适配器
 */
public class GoodsMoveStockAdapter extends BaseQuickAdapter<GoodsMoveBean, GoodsMoveStockAdapter.MyHolderHolder> {


    public GoodsMoveStockAdapter() {
        super(R.layout.item_goods_move_stock);
    }

    @Override
    protected void convert(final MyHolderHolder helper, final GoodsMoveBean item) {
        String wpCode = BarcodeUtils.generateWPBarcode(item.getGoodsId());
        helper.textProductNum.setText(wpCode);
        helper.textSku.setText(String.valueOf(item.getSkuId()));
        helper.textStatus.setText(item.getGoodsStatus());
        helper.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(helper.getLayoutPosition());
            }
        });
    }


    public static class MyHolderHolder extends BaseViewHolder {

        @BindView(R.id.text_sku)
        TextView textSku;
        @BindView(R.id.text_product_num)
        TextView textProductNum;
        @BindView(R.id.text_status)
        TextView textStatus;
        @BindView(R.id.btn_delete)
        Button btnDelete;
        @BindView(R.id.ll_root)
        LinearLayout llRoot;

        public MyHolderHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
