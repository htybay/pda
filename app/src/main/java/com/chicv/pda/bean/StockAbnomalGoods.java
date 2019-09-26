package com.chicv.pda.bean;

/**
 * author: liheyu
 * date: 2019-09-23
 * email: liheyu999@163.com
 */
public class StockAbnomalGoods {
    private int Id;
    private int GoodsId;
    private int SourceType;
    private int GridId;
    private String CreateUserName;
    private String CreateTime;
    private String BatchCode;
    private String Description;

    //处理状态 本地使用 未处理 0 确认 1 撤销  2
    private int type;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getGoodsId() {
        return GoodsId;
    }

    public void setGoodsId(int goodsId) {
        GoodsId = goodsId;
    }

    public int getSourceType() {
        return SourceType;
    }

    public void setSourceType(int sourceType) {
        SourceType = sourceType;
    }

    public int getGridId() {
        return GridId;
    }

    public void setGridId(int gridId) {
        GridId = gridId;
    }

    public String getCreateUserName() {
        return CreateUserName;
    }

    public void setCreateUserName(String createUserName) {
        CreateUserName = createUserName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getBatchCode() {
        return BatchCode;
    }

    public void setBatchCode(String batchCode) {
        BatchCode = batchCode;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
