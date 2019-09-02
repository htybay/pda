package com.chicv.pda.bean.param;

/**
 * author: liheyu
 * date: 2019-08-28
 * email: liheyu999@163.com
 */
public class GoodsBatchParam {

    private int goodsId;
    private String batchCode;
    private boolean isMoveDown;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public boolean isMoveDown() {
        return isMoveDown;
    }

    public void setIsMoveDown(boolean moveDown) {
        isMoveDown = moveDown;
    }
}
