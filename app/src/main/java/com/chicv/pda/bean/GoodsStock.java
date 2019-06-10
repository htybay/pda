package com.chicv.pda.bean;

/**
 * author: liheyu
 * date: 2019-06-10
 * email: liheyu999@163.com
 * 物品货位 实体类
 */
public class GoodsStock {

    private int goodsId;
    private int batchCode;
    private int position;
    private int isReturn;
    private int specification;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(int batchCode) {
        this.batchCode = batchCode;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(int isReturn) {
        this.isReturn = isReturn;
    }

    public int getSpecification() {
        return specification;
    }

    public void setSpecification(int specification) {
        this.specification = specification;
    }
}
