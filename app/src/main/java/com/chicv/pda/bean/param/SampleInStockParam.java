package com.chicv.pda.bean.param;

/**
 * author: liheyu
 * date: 2019-07-29
 * email: liheyu999@163.com
 * <p>
 * 样品入库 入参
 */
public class SampleInStockParam {

    private int stockReceiveBatchId;
    private String batchCode;
    private int stockNums;
    private String operateUserId;
    private String gridId;

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

    public int getStockNums() {
        return stockNums;
    }

    public void setStockNums(int stockNums) {
        this.stockNums = stockNums;
    }

    public String getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(String operateUserId) {
        this.operateUserId = operateUserId;
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }
}


