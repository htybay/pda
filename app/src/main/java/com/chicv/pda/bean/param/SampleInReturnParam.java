package com.chicv.pda.bean.param;

/**
 * author: liheyu
 * date: 2019-07-29
 * email: liheyu999@163.com
 * <p>
 * 归还上架 入参
 */
public class SampleInReturnParam {

    private int stockReceiveBatchId;
    private String batchCode;
    private int quantity;
    private String operateUserId;
    private String newGridId;


    public int getStockReceiveBatchId() {
        return stockReceiveBatchId;
    }

    public void setStockReceiveBatchId(int stockReceiveBatchId) {
        this.stockReceiveBatchId = stockReceiveBatchId;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(String operateUserId) {
        this.operateUserId = operateUserId;
    }

    public String getNewGridId() {
        return newGridId;
    }

    public void setNewGridId(String newGridId) {
        this.newGridId = newGridId;
    }
}


