package com.chicv.pda.bean;

/**
 * author: liheyu
 * date: 2019-09-27
 * email: liheyu999@163.com
 *
 * 囤货规格批量移位
 */
public class BatchMoveBean {

    private String batchCode;
    private int scanCount;
    private int waitMoveCount;

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public int getScanCount() {
        return scanCount;
    }

    public void setScanCount(int scanCount) {
        this.scanCount = scanCount;
    }

    public int getWaitMoveCount() {
        return waitMoveCount;
    }

    public void setWaitMoveCount(int waitMoveCount) {
        this.waitMoveCount = waitMoveCount;
    }
}
