package com.chicv.pda.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * author: liheyu
 * date: 2019-08-28
 * email: liheyu999@163.com
 */
public class PositionGoods {

    @JSONField(name = "GoodsId")
    private int goodsId;
    @JSONField(name = "Position")
    private String position;
    @JSONField(name = "BatchCode")
    private String batchCode;
    @JSONField(name = "Specification")
    private String specification;
    @JSONField(name = "IsReturn")
    private boolean isReturn;
    @JSONField(name = "IsLock")
    private boolean isLock;

    //本地使用
    private int scanCount;
    private int totalCount;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public boolean isReturn() {
        return isReturn;
    }

    public void setIsReturn(boolean aReturn) {
        isReturn = aReturn;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setIsLock(boolean lock) {
        isLock = lock;
    }

    public int getScanCount() {
        return scanCount;
    }

    public void setScanCount(int scanCount) {
        this.scanCount = scanCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
