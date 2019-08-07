package com.chicv.pda.bean;

import android.support.annotation.NonNull;

import com.chicv.pda.utils.DateUtils;

/**
 * author: liheyu
 * date: 2019-06-06
 * email: liheyu999@163.com
 */
public class StockRecord implements Comparable<StockRecord> {

    /**
     * GoodsId : 8029933
     * SpuId : 239379
     * SkuId : 1134003
     * RoomId : 1
     * GridId : 781739
     * InTime : 2019-01-24T09:29:24.127
     * InStockType : 1
     * OrderGoodsId : 5560734
     * OutRoomId : 0
     * UserId : 1486
     * Id : 6874422
     * CreateUserName : 李普海
     * CreateTime : 2019-01-24T09:29:25.063
     * UpdateUserName : 李普海
     * UpdateTime : 2019-01-24T09:29:25.063
     */

    private int GoodsId;
    private int SpuId;
    private int SkuId;
    private int RoomId;
    private int GridId;
    private String InTime;
    private int InStockType;
    private int OrderGoodsId;
    private int OutRoomId;
    private int UserId;
    private int Id;
    private String CreateUserName;
    private String CreateTime;
    private String UpdateUserName;
    private String UpdateTime;


    //GetOutRecord

    private String OutTime;
    private int OutStockType;
    private int InRoomId;


    public int getGoodsId() {
        return GoodsId;
    }

    public void setGoodsId(int goodsId) {
        GoodsId = goodsId;
    }

    public int getSpuId() {
        return SpuId;
    }

    public void setSpuId(int spuId) {
        SpuId = spuId;
    }

    public int getSkuId() {
        return SkuId;
    }

    public void setSkuId(int skuId) {
        SkuId = skuId;
    }

    public int getRoomId() {
        return RoomId;
    }

    public void setRoomId(int roomId) {
        RoomId = roomId;
    }

    public int getGridId() {
        return GridId;
    }

    public void setGridId(int gridId) {
        GridId = gridId;
    }

    public String getInTime() {
        return InTime;
    }

    public void setInTime(String inTime) {
        InTime = inTime;
    }

    public int getInStockType() {
        return InStockType;
    }

    public void setInStockType(int inStockType) {
        InStockType = inStockType;
    }

    public int getOrderGoodsId() {
        return OrderGoodsId;
    }

    public void setOrderGoodsId(int orderGoodsId) {
        OrderGoodsId = orderGoodsId;
    }

    public int getOutRoomId() {
        return OutRoomId;
    }

    public void setOutRoomId(int outRoomId) {
        OutRoomId = outRoomId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

    public String getUpdateUserName() {
        return UpdateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        UpdateUserName = updateUserName;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    public String getOutTime() {
        return OutTime;
    }

    public void setOutTime(String outTime) {
        OutTime = outTime;
    }

    public int getOutStockType() {
        return OutStockType;
    }

    public void setOutStockType(int outStockType) {
        OutStockType = outStockType;
    }

    public int getInRoomId() {
        return InRoomId;
    }

    public void setInRoomId(int inRoomId) {
        InRoomId = inRoomId;
    }

    @Override
    public int compareTo(@NonNull StockRecord o) {
        //按时间降序排列
        if (getCreateTime() == null || o.getCreateTime() == null) {
            return 0;
        }
        return DateUtils.getDate(o.getCreateTime()).compareTo(DateUtils.getDate(this.getCreateTime()));
    }
}