package com.chicv.pda.bean.param;

/**
 * author: liheyu
 * date: 2019-06-04
 * email: liheyu999@163.com
 */
public class RecommendStockParam {


    /**
     * GoodsId : 8029933
     * InStockType : 1
     */

    //   物品ID
    private long goodsId;

    //   入库类型
    private int inStockType;

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public int getInStockType() {
        return inStockType;
    }

    public void setInStockType(int inStockType) {
        this.inStockType = inStockType;
    }
}
