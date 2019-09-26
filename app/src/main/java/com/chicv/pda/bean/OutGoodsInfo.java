package com.chicv.pda.bean;

/**
 * author: liheyu
 * date: 2019-09-17
 * email: liheyu999@163.com
 *
 * 退货物品出库 获取物品退货信息
 */
public class OutGoodsInfo {

    private int pickId;
    private int goodsId;
    private int groupNo;
    private int gridId;
    private int goodsType;
    private String gridName;

    public int getPickId() {
        return pickId;
    }

    public void setPickId(int pickId) {
        this.pickId = pickId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(int groupNo) {
        this.groupNo = groupNo;
    }

    public int getGridId() {
        return gridId;
    }

    public void setGridId(int gridId) {
        this.gridId = gridId;
    }

    public String getGridName() {
        return gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }
}
