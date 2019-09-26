package com.chicv.pda.bean;

/**
 * author: liheyu
 * date: 2019-09-20
 * email: liheyu999@163.com
 * 报损列表
 */
public class StockLoss {

    /// 报损单ID
    private  int id;
    /// 囤货数
    private  int BatchCount;
    /// 订单数
    private  int GoodsCount;
    /// 物品总数
    private  int TotalCount;
    private  String CreateUserName;
    /// 进入列表的都是待审核的
    private  String Status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBatchCount() {
        return BatchCount;
    }

    public void setBatchCount(int batchCount) {
        BatchCount = batchCount;
    }

    public int getGoodsCount() {
        return GoodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        GoodsCount = goodsCount;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }

    public String getCreateUserName() {
        return CreateUserName;
    }

    public void setCreateUserName(String createUserName) {
        CreateUserName = createUserName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
