package com.chicv.pda.bean;

import java.util.List;

/**
 * author: liheyu
 * date: 2019-06-12
 * email: liheyu999@163.com
 *
 * 内部拣货单
 */
public class InternalPick {


    /**
     * Id : 1
     * StartTime : 2018-11-24T11:13:24.193
     * StopTime : 2018-11-24T11:13:24.193
     * TotalCount : 1
     * PickCount : 1
     * OutCount : 1
     * RoomId : 1
     * Type : 3
     * TypeName : 物品重新质检
     * Details : [{"Id":1,"PickId":1,"TypeId":77328,"SpuId":0,"SkuId":0,"GoodsId":7003502,"GoodsType":0,"BatchCode":"","GridId":760101,"GridName":"不01列/02-02-01","IsPick":true,"IsOut":true,"PickUserName":"刘柏晨","OutUserName":"","PickTime":null,"OutTime":null}]
     */

    private int Id;
    private String StartTime;
    private String StopTime;
    private int TotalCount;
    private int PickCount;
    private int OutCount;
    private int RoomId;
    private int Type;
    private String TypeName;
    private List<InternalPickGoods> Details;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }

    public String getStopTime() {
        return StopTime;
    }

    public void setStopTime(String StopTime) {
        this.StopTime = StopTime;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int TotalCount) {
        this.TotalCount = TotalCount;
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

    public int getRoomId() {
        return RoomId;
    }

    public void setRoomId(int RoomId) {
        this.RoomId = RoomId;
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String TypeName) {
        this.TypeName = TypeName;
    }

    public List<InternalPickGoods> getDetails() {
        return Details;
    }

    public void setDetails(List<InternalPickGoods> Details) {
        this.Details = Details;
    }

}
