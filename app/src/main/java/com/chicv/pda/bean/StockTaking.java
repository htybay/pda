package com.chicv.pda.bean;

/**
 * author: liheyu
 * date: 2019-09-23
 * email: liheyu999@163.com
 */
public class StockTaking {

    private int Id;
    private int GoodsType;
    private int PlanQuantity;
    private int HandleQuantity;
    private int StockTakingStatus;
    private int StockTakingType;
    private String RoomName;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getGoodsType() {
        return GoodsType;
    }

    public void setGoodsType(int goodsType) {
        GoodsType = goodsType;
    }

    public int getPlanQuantity() {
        return PlanQuantity;
    }

    public void setPlanQuantity(int planQuantity) {
        PlanQuantity = planQuantity;
    }

    public int getHandleQuantity() {
        return HandleQuantity;
    }

    public void setHandleQuantity(int handleQuantity) {
        HandleQuantity = handleQuantity;
    }

    public int getStockTakingStatus() {
        return StockTakingStatus;
    }

    public void setStockTakingStatus(int stockTakingStatus) {
        StockTakingStatus = stockTakingStatus;
    }

    public int getStockTakingType() {
        return StockTakingType;
    }

    public void setStockTakingType(int stockTakingType) {
        StockTakingType = stockTakingType;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }
}
