package com.chicv.pda.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.bean.GoodsStock;
import com.chicv.pda.utils.BarcodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author lilaoda
 * date: 2019/5/26 0026
 * email：liheyu999@163.com
 * description： 物品货位
 */
public class GoodsStockAdapter extends BaseQuickAdapter<GoodsStock, GoodsStockAdapter.MyHolderHolder> {


    public GoodsStockAdapter() {
        super(R.layout.item_goods_stock);
    }

    @Override
    protected void convert(MyHolderHolder helper, GoodsStock item) {
        String text = "";
        if (item.isGoodsRule()) {
            //扫描囤货规格
            text = item.getPosition() + "  " + item.getNotOutNum();
        } else {
            //扫描的货位
            if (TextUtils.isEmpty(item.getBatchCode()) || !TextUtils.isEmpty(item.getBatchCode()) && item.isReturn()) {
                text = BarcodeUtils.generateWPBarcode(item.getGoodsId()) + "  1  " + item.getSpecification();
            } else {
                text = item.getBatchCode() + "  " + getSampileGoodsNum(item.getBatchCode()) + "  " + item.getSpecification();
            }
        }
        helper.textDes.setText(text);
    }

    //查询相同批次号的物品数量
    private int getSampileGoodsNum(String batchCode) {
        int count = 0;
        for (GoodsStock item : mData) {
            if (TextUtils.equals(item.getBatchCode(), batchCode) && !item.isReturn()) {
                count++;
            }
        }
        return count;
    }

    public static class MyHolderHolder extends BaseViewHolder {

        @BindView(R.id.text_des)
        TextView textDes;

        public MyHolderHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
