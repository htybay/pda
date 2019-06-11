package com.chicv.pda.bean;

/**
 * author: liheyu
 * date: 2019-06-11
 * email: liheyu999@163.com
 *
 * 囤货发货批次
 */
public class PurchaseBatch {

    /// 发货批次编号
    private int stockReceiveBatchId;

    /// 囤货规格下待入库物品数量
    private int waitReceiveCount ;


    public int getStockReceiveBatchId() {
        return stockReceiveBatchId;
    }

    public void setStockReceiveBatchId(int stockReceiveBatchId) {
        this.stockReceiveBatchId = stockReceiveBatchId;
    }

    public int getWaitReceiveCount() {
        return waitReceiveCount;
    }

    public void setWaitReceiveCount(int waitReceiveCount) {
        this.waitReceiveCount = waitReceiveCount;
    }
}
