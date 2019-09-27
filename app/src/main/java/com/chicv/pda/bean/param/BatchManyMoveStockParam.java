package com.chicv.pda.bean.param;

import java.util.List;

/**
 * author: liheyu
 * date: 2019-09-27
 * email: liheyu999@163.com
 * <p>
 * 囤货批量移位 入参
 */
public class BatchManyMoveStockParam {

    private int OldGridId;
    private int NewGridId;
    private String OperateUserId;
    private List<MoveDetail> Details;

    public int getOldGridId() {
        return OldGridId;
    }

    public void setOldGridId(int oldGridId) {
        OldGridId = oldGridId;
    }

    public int getNewGridId() {
        return NewGridId;
    }

    public void setNewGridId(int newGridId) {
        NewGridId = newGridId;
    }

    public String getOperateUserId() {
        return OperateUserId;
    }

    public void setOperateUserId(String operateUserId) {
        OperateUserId = operateUserId;
    }

    public List<MoveDetail> getDetails() {
        return Details;
    }

    public void setDetails(List<MoveDetail> details) {
        Details = details;
    }

    public static class MoveDetail {
        private String BatchCode;
        private int Quantity;

        public String getBatchCode() {
            return BatchCode;
        }

        public void setBatchCode(String batchCode) {
            BatchCode = batchCode;
        }

        public int getQuantity() {
            return Quantity;
        }

        public void setQuantity(int quantity) {
            Quantity = quantity;
        }
    }
}
