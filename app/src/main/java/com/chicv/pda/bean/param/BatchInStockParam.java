package com.chicv.pda.bean.param;

/**
 * author: liheyu
 * date: 2019-06-12
 * email: liheyu999@163.com
 * <p>
 * 囤货入库入参
 */
public class BatchInStockParam {

    private int gridId;
    private int stockNums;
    private int stockReceiveBatchId;
    private String operateUserId;

    public int getGridId() {
        return gridId;
    }

    public void setGridId(int gridId) {
        this.gridId = gridId;
    }

    public int getStockNums() {
        return stockNums;
    }

    public void setStockNums(int stockNums) {
        this.stockNums = stockNums;
    }

    public int getStockReceiveBatchId() {
        return stockReceiveBatchId;
    }

    public void setStockReceiveBatchId(int stockReceiveBatchId) {
        this.stockReceiveBatchId = stockReceiveBatchId;
    }

    public String getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(String operateUserId) {
        this.operateUserId = operateUserId;
    }
}
