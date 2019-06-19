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
 * description： 移库下架
 */
public class MoveRoomDownAdapter extends BaseQuickAdapter<StockMoveRoom.DetailsBean, BaseViewHolder> {


    private StockMoveRoom.DetailsBean mSelectedItem;

    public MoveRoomDownAdapter() {
        super(R.layout.item_move_room);
    }

    @Override
    protected void convert(BaseViewHolder helper, StockMoveRoom.DetailsBean item) {
        String wpCode = BarcodeUtils.generateWPBarcode(item.getGoodsId());
        if (TextUtils.isEmpty(item.getBatchCode())) {
            helper.setText(R.id.text_product, wpCode);
            helper.setText(R.id.text_num, "1");
            helper.setBackgroundRes(R.id.ll_root, item.isScan() ? R.color.blue_dark : R.color.white);
        } else {
            helper.setText(R.id.text_product, item.getBatchCode());
            helper.setText(R.id.text_num, String.valueOf(item.getLocalTotalCount()));
            if (item == mSelectedItem) {
                //选 中
                helper.setBackgroundRes(R.id.ll_root, R.color.blue_light);
            } else if (item.isScan()) {
                //扫过
                helper.setBackgroundRes(R.id.ll_root, R.color.blue_dark);
            } else {
                //一个没扫
                helper.setBackgroundRes(R.id.ll_root, R.color.white);
            }
        }
    }

    //存在，直接选中，不存在添加一条数量为0的数据并选中
    public void setSelected(String batchCode) {
        if (TextUtils.isEmpty(batchCode)) {
            mSelectedItem = null;
            notifyDataSetChanged();
            return;
        }
        boolean exist = false;
        for (StockMoveRoom.DetailsBean item : mData) {
            if (TextUtils.equals(item.getBatchCode(), batchCode)) {
                exist = true;
                mSelectedItem = item;
                break;
            }
        }
        if (exist) {
            mData.remove(mSelectedItem);
            notifyDataSetChanged();
        } else {
            mSelectedItem = new StockMoveRoom.DetailsBean();
            mSelectedItem.setBatchCode(batchCode);
        }
        addData(0, mSelectedItem);
    }

    public StockMoveRoom.DetailsBean getmSelectedItem() {
        return mSelectedItem;
    }
}
