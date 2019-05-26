package com.chicv.pda.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.bean.PickGoods;

/**
 * @author lilaoda
 * date: 2019/5/26 0026
 * email：liheyu999@163.com
 * description：
 */
public class PickGoodsAdapter extends BaseQuickAdapter<PickGoods,PickGoodsAdapter.PickGoodsHolder> {

    public PickGoodsAdapter() {
        super(R.layout.item_pick_goods);
    }

    @Override
    protected void convert(PickGoodsHolder helper, PickGoods item) {

    }

    public static class PickGoodsHolder extends BaseViewHolder {

        public PickGoodsHolder(View view) {
            super(view);
        }
    }
}
