package com.chicv.pda.bean;

import android.support.annotation.NonNull;

/**
 * author: liheyu
 * date: 2019-06-14
 * email: liheyu999@163.com
 * <p>
 * 调拨拣货物品详情
 */
public class TransferPickGoods implements Comparable<TransferPickGoods> {
    /**
     * Id : 0
     * TransferId : 0
     * StockReceiveBatchId : 0
     * SpuId : 0
     * SkuId : 0
     * GoodsId : 0
     * BatchCode : string
     * Specification : string
     * IsPick : true
     * IsOut : true
     * IsIn : true
     * GridId : 0
     * GridName : string
     * StockGrid : {"Id":0,"Name":"string","RowsId":0,"RowName":"string","ShelfId":0,"ShelfName":"string","AreaId":0,"AreaName":"string","RoomId":0,"RoomName":"string","ColumnId":0,"FloorId":0,"ColumnName":"string","FloorName":"string","IsPickGrid":true,"AreaSort":0,"AreaType":0,"Description":"string","Sort":0,"IsEnable":true}
     */

    private int Id;
    private int TransferId;
    private int StockReceiveBatchId;
    private int SpuId;
    private int SkuId;
    private int GoodsId;
    private String BatchCode;
    private String Specification;
    private boolean IsPick;
    private boolean IsOut;
    private boolean IsIn;
    private int GridId;
    private String GridName;
    private StockInfo StockGrid;


    //本地使用，用于收货，记录相同规格下的总件数，已扫件数
    private int localTotalCount;
    private int localScanCount;
    private boolean IsSign;


    public boolean isIsSign() {
        return IsSign;
    }

    public void setIsSign(boolean sign) {
        IsSign = sign;
    }

    public int getLocalTotalCount() {
        return localTotalCount;
    }

    public void setLocalTotalCount(int localTotalCount) {
        this.localTotalCount = localTotalCount;
    }

    public int getLocalScanCount() {
        return localScanCount;
    }

    public void setLocalScanCount(int localScanCount) {
        this.localScanCount = localScanCount;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getTransferId() {
        return TransferId;
    }

    public void setTransferId(int TransferId) {
        this.TransferId = TransferId;
    }

    public int getStockReceiveBatchId() {
        return StockReceiveBatchId;
    }

    public void setStockReceiveBatchId(int StockReceiveBatchId) {
        this.StockReceiveBatchId = StockReceiveBatchId;
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

    public String getBatchCode() {
        return BatchCode;
    }

    public void setBatchCode(String BatchCode) {
        this.BatchCode = BatchCode;
    }

    public String getSpecification() {
        return Specification;
    }

    public void setSpecification(String Specification) {
        this.Specification = Specification;
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

    public boolean isIsIn() {
        return IsIn;
    }

    public void setIsIn(boolean IsIn) {
        this.IsIn = IsIn;
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

    public StockInfo getStockGrid() {
        return StockGrid;
    }

    public void setStockGrid(StockInfo StockGrid) {
        this.StockGrid = StockGrid;
    }

    @Override
    public int compareTo(@NonNull TransferPickGoods o) {
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
