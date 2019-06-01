package com.chicv.pda.bean.param;

import com.chicv.pda.bean.PickGoods;

import java.util.List;

/**
 * author: liheyu
 * date: 2019-05-31
 * email: liheyu999@163.com
 *
 * 出库参数
 */
public class OutStockParam {

    private long pickId;
    private List<PickGoods.PickGoodsDetail> details;

    public long getPickId() {
        return pickId;
    }

    public void setPickId(long pickId) {
        this.pickId = pickId;
    }

    public List<PickGoods.PickGoodsDetail> getDetails() {
        return details;
    }

    public void setDetails(List<PickGoods.PickGoodsDetail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "OutStockParam{" +
                "pickId=" + pickId +
                ", details=" + details +
                '}';
    }
}
