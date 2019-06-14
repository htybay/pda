package com.chicv.pda.bean;

import java.util.List;

/**
 * author: liheyu
 * date: 2019-06-14
 * email: liheyu999@163.com
 */
public class TransferPick {


    /**
     * Id : 0
     * FromRoomId : 0
     * ToRoomId : 0
     * TransferQuantity : 0
     * Type : 1
     * Status : 10
     * PickCount : 0
     * OutCount : 0
     * InCount : 0
     * Details : [{"Id":0,"TransferId":0,"StockReceiveBatchId":0,"SpuId":0,"SkuId":0,"GoodsId":0,"BatchCode":"string","Specification":"string","IsPick":true,"IsOut":true,"IsIn":true,"GridId":0,"GridName":"string","StockGrid":{"Id":0,"Name":"string","RowsId":0,"RowName":"string","ShelfId":0,"ShelfName":"string","AreaId":0,"AreaName":"string","RoomId":0,"RoomName":"string","ColumnId":0,"FloorId":0,"ColumnName":"string","FloorName":"string","IsPickGrid":true,"AreaSort":0,"AreaType":0,"Description":"string","Sort":0,"IsEnable":true}}]
     */

    private int Id;
    private int FromRoomId;
    private int ToRoomId;
    private int TransferQuantity;
    private int Type;
    private int Status;
    private int PickCount;
    private int OutCount;
    private int InCount;
    private List<TransferPickGoods> Details;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getFromRoomId() {
        return FromRoomId;
    }

    public void setFromRoomId(int FromRoomId) {
        this.FromRoomId = FromRoomId;
    }

    public int getToRoomId() {
        return ToRoomId;
    }

    public void setToRoomId(int ToRoomId) {
        this.ToRoomId = ToRoomId;
    }

    public int getTransferQuantity() {
        return TransferQuantity;
    }

    public void setTransferQuantity(int TransferQuantity) {
        this.TransferQuantity = TransferQuantity;
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public int getPickCount() {
        return PickCount;
    }

    public void setPickCount(int PickCount) {
        this.PickCount = PickCount;
    }

    public int getOutCount() {
        return OutCount;
    }

    public void setOutCount(int OutCount) {
        this.OutCount = OutCount;
    }

    public int getInCount() {
        return InCount;
    }

    public void setInCount(int InCount) {
        this.InCount = InCount;
    }

    public List<TransferPickGoods> getDetails() {
        return Details;
    }

    public void setDetails(List<TransferPickGoods> Details) {
        this.Details = Details;
    }
}
