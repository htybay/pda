package com.chicv.pda.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.bean.StockMoveRoom;
import com.chicv.pda.utils.BarcodeUtils;

/**
 * @author lilaoda
 * date: 2019/5/26 0026
 * email：liheyu999@163.com
 * description： 移库上架
 */
public class MoveRoomUpAdapter extends BaseQuickAdapter<StockMoveRoom.DetailsBean, BaseViewHolder> {


    private StockMoveRoom.DetailsBean mSelectedItem;

    public MoveRoomUpAdapter() {
        super(R.layout.item_move_room);
    }

    @Override
    protected void convert(BaseViewHolder helper, StockMoveRoom.DetailsBean item) {
        String wpCode = BarcodeUtils.generateWPBarcode(item.getGoodsId());
        if (TextUtils.isEmpty(item.getBatchCode())) {
            helper.setText(R.id.text_product, wpCode);
            helper.setText(R.id.text_num, item.isMatch() ? String.valueOf(0) : String.valueOf(1));
            helper.setBackgroundRes(R.id.ll_root, item.isMatch() ? R.color.green_light : R.color.white);
        } else {
            helper.setText(R.id.text_product, item.getBatchCode());
            helper.setText(R.id.text_num, String.valueOf(item.getLocalTotalCount()-item.getLocalScanCount()));
            if (item == mSelectedItem) {
                //选 中
                helper.setBackgroundRes(R.id.ll_root, R.color.blue_light);
            } else if (item.getLocalTotalCount() == item.getLocalScanCount()) {
                //扫完
                helper.setBackgroundRes(R.id.ll_root, R.color.green_light);
            } else if (item.getLocalScanCount() > 0) {
                //没扫完
                helper.setBackgroundRes(R.id.ll_root, R.color.red_light);
            } else {
                //一个没扫
                helper.setBackgroundRes(R.id.ll_root, R.color.white);
            }
        }
    }

    public void setSelected(StockMoveRoom.DetailsBean batchCode) {
        this.mSelectedItem = batchCode;
        notifyDataSetChanged();
    }

    public StockMoveRoom.DetailsBean getmSelectedItem() {
        return mSelectedItem;
    }
}
