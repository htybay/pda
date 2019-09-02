package com.chicv.pda.bean;

/**
 * author: liheyu
 * date: 2019-08-30
 * email: liheyu999@163.com
 *
 * 理库操作
 */
public class GoodsBatchCode {

    private int GoodsId;
    private String BatchCode;
    private boolean IsMoveDown;
    //本地使用
    private int scanCount;
    private int totalCount;

    public int getGoodsId() {
        return GoodsId;
    }

    public void setGoodsId(int goodsId) {
        GoodsId = goodsId;
    }

    public String getBatchCode() {
        return BatchCode;
    }

    public void setBatchCode(String batchCode) {
        BatchCode = batchCode;
    }

    public boolean isMoveDown() {
        return IsMoveDown;
    }

    public void setIsMoveDown(boolean moveDown) {
        IsMoveDown = moveDown;
    }

    public void setMoveDown(boolean moveDown) {
        IsMoveDown = moveDown;
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
