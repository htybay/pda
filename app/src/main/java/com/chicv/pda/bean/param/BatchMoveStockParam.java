package com.chicv.pda.bean.param;

/**
 * author: liheyu
 * date: 2019-06-12
 * email: liheyu999@163.com
 * <p>
 * 囤货移库入参
 */
public class BatchMoveStockParam {

    private int oldGridId;
    private int newGridId;
    private String batchCode;
    private int quantity;
    private String operateUserId;

    public int getOldGridId() {
        return oldGridId;
    }

    public void setOldGridId(int oldGridId) {
        this.oldGridId = oldGridId;
    }

    public int getNewGridId() {
        return newGridId;
    }

    public void setNewGridId(int newGridId) {
        this.newGridId = newGridId;
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
}
