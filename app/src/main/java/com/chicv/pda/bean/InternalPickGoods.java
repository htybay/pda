package com.chicv.pda.bean;

import android.support.annotation.NonNull;

/**
 * author: liheyu
 * date: 2019-06-12
 * email: liheyu999@163.com
 * 内部拣货单物品详情
 */
public class InternalPickGoods implements Comparable<InternalPickGoods> {

    /**
     * Id : 1
     * PickId : 1
     * TypeId : 77328
     * SpuId : 0
     * SkuId : 0
     * GoodsId : 7003502
     * GoodsType : 0
     * BatchCode :
     * GridId : 760101
     * GridName : 不01列/02-02-01
     * IsPick : true
     * IsOut : true
     * PickUserName : 刘柏晨
     * OutUserName :
     * PickTime : null
     * OutTime : null
     */

    private int Id;
    private int PickId;
    private int TypeId;
    private int SpuId;
    private int SkuId;
    private int GoodsId;
    private int GoodsType;
    private String BatchCode;
    private int GridId;
    private String GridName;
    private boolean IsPick;
    private boolean IsOut;
    private String PickUserName;
    private String OutUserName;
    private String PickTime;
    private String OutTime;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getPickId() {
        return PickId;
    }

    public void setPickId(int PickId) {
        this.PickId = PickId;
    }

    public int getTypeId() {
        return TypeId;
    }

    public void setTypeId(int TypeId) {
        this.TypeId = TypeId;
    }

    public int getSpuId() {
        return SpuId;
    }

    public void setSpuId(int SpuId) {
        this.SpuId = SpuId;
    }

    public int getSkuId() {
        return SkuId;
    }

    public void setSkuId(int SkuId) {
        this.SkuId = SkuId;
    }

    public int getGoodsId() {
        return GoodsId;
    }

    public void setGoodsId(int GoodsId) {
        this.GoodsId = GoodsId;
    }

    public int getGoodsType() {
        return GoodsType;
    }

    public void setGoodsType(int GoodsType) {
        this.GoodsType = GoodsType;
    }

    public String getBatchCode() {
        return BatchCode;
    }

    public void setBatchCode(String BatchCode) {
        this.BatchCode = BatchCode;
    }

    public int getGridId() {
        return GridId;
    }

    public void setGridId(int GridId) {
        this.GridId = GridId;
    }

    public String getGridName() {
        return GridName;
    }

    public void setGridName(String GridName) {
        this.GridName = GridName;
    }

    public boolean isIsPick() {
        return IsPick;
    }

    public void setIsPick(boolean IsPick) {
        this.IsPick = IsPick;
    }

    public boolean isIsOut() {
        return IsOut;
    }

    public void setIsOut(boolean IsOut) {
        this.IsOut = IsOut;
    }

    public String getPickUserName() {
        return PickUserName;
    }

    public void setPickUserName(String PickUserName) {
        this.PickUserName = PickUserName;
    }

    public String getOutUserName() {
        return OutUserName;
    }

    public void setOutUserName(String OutUserName) {
        this.OutUserName = OutUserName;
    }

    public String getPickTime() {
        return PickTime;
    }

    public void setPickTime(String PickTime) {
        this.PickTime = PickTime;
    }

    public String getOutTime() {
        return OutTime;
    }

    public void setOutTime(String OutTime) {
        this.OutTime = OutTime;
    }

    @Override
    public int compareTo(@NonNull InternalPickGoods o) {
        if (isIsPick() != o.isIsPick()) {
            if (isIsPick()) {
                return 1;
            } else {
                return -1;
            }
        }
        return 0;
    }
}
