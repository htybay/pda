package com.chicv.pda.bean;

import java.util.List;

/**
 * author: liheyu
 * date: 2019-09-25
 * email: liheyu999@163.com
 */
public class TakingSubTask {
    private int gridId;
    private int RemainderCount;
    private int SubtaskId;
    private String GridDescription;
    private List<StockTakingRecord> Details;

    public int getGridId() {
        return gridId;
    }

    public void setGridId(int gridId) {
        this.gridId = gridId;
    }

    public int getRemainderCount() {
        return RemainderCount;
    }

    public void setRemainderCount(int remainderCount) {
        RemainderCount = remainderCount;
    }

    public int getSubtaskId() {
        return SubtaskId;
    }

    public void setSubtaskId(int subtaskId) {
        SubtaskId = subtaskId;
    }

    public String getGridDescription() {
        return GridDescription;
    }

    public void setGridDescription(String gridDescription) {
        GridDescription = gridDescription;
    }

    public List<StockTakingRecord> getDetails() {
        return Details;
    }

    public void setDetails(List<StockTakingRecord> details) {
        Details = details;
    }

    public static class StockTakingRecord {

        private int SubTaskId;
        private double Cost;
        private int GoodsType;
        private int GoodsId;
        private String BatchCode;
        private int GridId;
        private int RecordType;
        private int CheckStatus;

        public int getSubTaskId() {
            return SubTaskId;
        }

        public void setSubTaskId(int subTaskId) {
            SubTaskId = subTaskId;
        }

        public double getCost() {
            return Cost;
        }

        public void setCost(double cost) {
            Cost = cost;
        }

        public int getGoodsType() {
            return GoodsType;
        }

        public void setGoodsType(int goodsType) {
            GoodsType = goodsType;
        }

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

        public int getGridId() {
            return GridId;
        }

        public void setGridId(int gridId) {
            GridId = gridId;
        }

        public int getRecordType() {
            return RecordType;
        }

        public void setRecordType(int recordType) {
            RecordType = recordType;
        }

        public int getCheckStatus() {
            return CheckStatus;
        }

        public void setCheckStatus(int checkStatus) {
            CheckStatus = checkStatus;
        }
    }
}
