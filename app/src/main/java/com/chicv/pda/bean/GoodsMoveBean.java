package com.chicv.pda.bean;

/**
 * author: liheyu
 * date: 2019-06-11
 * email: liheyu999@163.com
 *
 * 物品移位 bean
 */
public class GoodsMoveBean {

    private int SkuId;
    private String GoodsStatus;
    private int GoodsId;
    private int GridId;

    public int getSkuId() {
        return SkuId;
    }

    public void setSkuId(int skuId) {
        SkuId = skuId;
    }

    public String getGoodsStatus() {
        return GoodsStatus;
    }

    public void setGoodsStatus(String goodsStatus) {
        GoodsStatus = goodsStatus;
    }

    public int getGoodsId() {
        return GoodsId;
    }

    public void setGoodsId(int goodsId) {
        GoodsId = goodsId;
    }

    public int getGridId() {
        return GridId;
    }

    public void setGridId(int gridId) {
        GridId = gridId;
    }
}
