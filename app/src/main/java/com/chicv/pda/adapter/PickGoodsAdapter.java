package com.chicv.pda.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chicv.pda.R;
import com.chicv.pda.bean.PickGoods;
import com.chicv.pda.utils.PdaUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author lilaoda
 * date: 2019/5/26 0026
 * email：liheyu999@163.com
 * description：
 */
public class PickGoodsAdapter extends BaseQuickAdapter<PickGoods.PickGoodsDetail, PickGoodsAdapter.PickGoodsHolder> {

    private final MyComparator comparator;

    public PickGoodsAdapter() {
        super(R.layout.item_pick_goods);
        comparator = new MyComparator();
    }

    @Override
    protected void convert(PickGoodsHolder helper, PickGoods.PickGoodsDetail item) {
        helper.textProductNum.setText(String.valueOf(item.getGoodsId()));
        helper.textColorSize.setText(item.getSpecification());
        helper.textStock.setText(item.getStockGrid().getDescription());
        helper.textDeliveryNum.setText(String.valueOf(item.getGroupNo()));
        helper.textStatus.setText(PdaUtils.getPickStatusDesc(item.getPickStatus()));
        helper.textPacket.setText(String.valueOf(item.getPackageId()));


        if (item.getStatus() != 1) {
            //非正常数据标红
            helper.llRoot.setBackgroundResource(R.color.red_light);
        } else {
            if (item.getPickStatus() != 30) {
                //非待捡货标蓝
                helper.llRoot.setBackgroundResource(R.color.blue_light);
            } else {
                //待捡也就是正常数据为白色
                helper.llRoot.setBackgroundResource(R.color.white);
            }
        }
    }

    //隐藏异常元素 非正常数据不显示
    public void setNewData(@Nullable List<PickGoods.PickGoodsDetail> data, boolean hideExceptionGoods) {
        if (data == null || !hideExceptionGoods) {
            setNewData(data);
        } else {
            Iterator<PickGoods.PickGoodsDetail> iterator = data.iterator();
            while (iterator.hasNext()) {
                PickGoods.PickGoodsDetail goodsDetail = iterator.next();
                if (goodsDetail.getStatus() != 1) {
                    iterator.remove();
                }
            }
            setNewData(data);
        }
    }

    @Override
    public void setNewData(@Nullable List<PickGoods.PickGoodsDetail> data) {
        if (data != null) {
            Collections.sort(data, comparator);
        }
        super.setNewData(data);
    }

    public static class MyComparator implements Comparator<PickGoods.PickGoodsDetail> {


        @Override
        public int compare(PickGoods.PickGoodsDetail o1, PickGoods.PickGoodsDetail o2) {
            int i = o1.status - o2.status;
            if (i != 0) {
                return i;
            }
            i = o1.pickStatus - o2.pickStatus;
            if (i != 0) {
                return i;
            }
            return 0;
        }
    }

    public static class PickGoodsHolder extends BaseViewHolder {


        @BindView(R.id.ll_root)
        LinearLayout llRoot;
        //产品编号
        @BindView(R.id.text_product_num)
        TextView textProductNum;
        @BindView(R.id.text_color_size)
        TextView textColorSize;
        //货位信息
        @BindView(R.id.text_stock)
        TextView textStock;
        //配货位
        @BindView(R.id.text_delivery_num)
        TextView textDeliveryNum;
        //状态
        @BindView(R.id.text_status)
        TextView textStatus;
        //包裹号
        @BindView(R.id.text_packet)
        TextView textPacket;


        public PickGoodsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
