package com.chicv.pda.bean.param;

/**
 * author: liheyu
 * date: 2019-09-20
 * email: liheyu999@163.com
 *
 * 囤货出库 入参
 */
public class StockBackOutParam {

    private int stockPickId;
    private int gridId;
    private String batchCode;
    private int backQuantity;
    private String operateUserId;


    public int getStockPickId() {
        return stockPickId;
    }

    public void setStockPickId(int stockPickId) {
        this.stockPickId = stockPickId;
    }

    public int getGridId() {
        return gridId;
    }

    public void setGridId(int gridId) {
        this.gridId = gridId;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public int getBackQuantity() {
        return backQuantity;
    }

    public void setBackQuantity(int backQuantity) {
        this.backQuantity = backQuantity;
    }

    public String getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(String operateUserId) {
        this.operateUserId = operateUserId;
    }
}
