package com.chicv.pda.bean;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * author: liheyu
 * date: 2019-06-01
 * email: liheyu999@163.com
 * 丢失物品
 */
public class LoseGoods implements Comparable<LoseGoods> {


    /**
     * PickId : 2081603
     * PickDetailId : 6918182
     * PackageId : 3315990
     * GoodsId : 7725055
     * BatchCode : 2SOY/002049171
     * GridId : 760089
     * GridName : Amazon01列/01-01-01
     */

    @JSONField(name = "PickId")
    private int pickId;
    @JSONField(name = "PickDetailId")
    private int pickDetailId;
    @JSONField(name = "PackageId")
    private int packageId;
    @JSONField(name = "GoodsId")
    private int goodsId;
    @JSONField(name = "BatchCode")
    private String batchCode;
    @JSONField(name = "GridId")
    private int gridId;
    @JSONField(name = "GridName")
    private String gridName;
    @JSONField(name = "IsLose")
    private boolean isLose;
    @JSONField(name = "IsMoveGrid")
    private boolean isMoveGrid;
    @JSONField(name = "IsNeedMoveGrid")
    private boolean isNeedMoveGrid;
    @JSONField(name = "StockGrid")
    private StockInfo stockGrid;

    //本地判断是否扫描
    private boolean isScan;

    public int getPickId() {
        return pickId;
    }

    public void setPickId(int pickId) {
        this.pickId = pickId;
    }

    public int getPickDetailId() {
        return pickDetailId;
    }

    public void setPickDetailId(int pickDetailId) {
        this.pickDetailId = pickDetailId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public int getGridId() {
        return gridId;
    }

    public void setGridId(int gridId) {
        this.gridId = gridId;
    }

    public String getGridName() {
        return gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
    }

    public boolean isLose() {
        return isLose;
    }

    public void setLose(boolean lose) {
        isLose = lose;
    }

    public boolean isMoveGrid() {
        return isMoveGrid;
    }

    public void setMoveGrid(boolean moveGrid) {
        isMoveGrid = moveGrid;
    }

    public boolean isNeedMoveGrid() {
        return isNeedMoveGrid;
    }

    public void setNeedMoveGrid(boolean needMoveGrid) {
        isNeedMoveGrid = needMoveGrid;
    }

    public StockInfo getStockGrid() {
        return stockGrid;
    }

    public void setStockGrid(StockInfo stockGrid) {
        this.stockGrid = stockGrid;
    }

    public boolean isScan() {
        return isScan;
    }

    public void setScan(boolean scan) {
        isScan = scan;
    }

    @Override
    public int compareTo(@NonNull LoseGoods o) {
        if (isScan != o.isScan) {
            if (isScan) {
                return 1;
            } else {
                return -1;
            }
        }
        return 0;
    }
}
