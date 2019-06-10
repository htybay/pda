package com.chicv.pda.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * author: liheyu
 * date: 2019-06-04
 * email: liheyu999@163.com
 *
 * 入库结果
 */
public class PurchaseGoods {


    /**
     * OrderId : 2979572
     * OrderGoodsId : 5560734
     * SiteId : 601
     * SpuId : 239379
     * SkuId : 1134003
     * Status : 0
     * PurchaseStatus : 10
     * QCStatus : 1
     * StockStatus : 2
     * LogisticsStatus : 5
     * Cost : 3.9
     * SupplierId : 1176
     * IsPublic : false
     * PreparedDate : 0
     * AllocationTime : 2019-01-08T06:42:51.203
     * ConfirmTime : null
     * SupplierDeliveryTime : null
     * ReceiveTime : 2019-01-20T16:46:41.3
     * QCTime : 2019-01-23T17:44:51.61
     * StockInTime : 2019-01-24T09:29:25.063
     * StockOutTime : 2019-01-25T11:36:57.737
     * LogisticsDeliveryTime : 2019-01-25T11:52:26.05
     * SourceUrl : https://detail.1688.com/offer/1256952663.html?spm=b26110380.8880418.csimg003.1.e428adccS3jjHj
     * SupplierGoodsNo : 7953
     * Specification : 颜色:卡其色;尺码:One-size
     * SupplierName : 网拍供应商-囤货
     * CategoryId : 10127
     * Weight : 112
     * ThumbnailsUrl : /image/catalog/2018Q1/07/88f16341-ae28-4951-b7ff-14175f1e842c.jpg
     * SingleId : 1027
     * NewQuote : false
     * SupplyStatus : 0
     * GoodsType : 0
     * GoodsSource : 0
     * IsInBatch : false
     * Remark :
     * SupplierRemark :
     * NoGoodsReason :
     * isPumpQc : false
     * IsNoHeavy : false
     * Currency : CNY
     * LocalCost : 3.9
     * IsReturn : false
     * IsCreateVoucher : false
     * QCSource : 0
     * IsSystemPush : false
     * IsHeavy : false
     * CostType : 0
     * ReturnTimes : 0
     * IsReturnBySku : false
     * SystemArrivalDate : 2019-01-12T06:42:51.203
     * LatestArrivalDate : 2019-01-12T23:59:59
     * WeightTime : 2019-01-23T20:42:52.907
     * ReceiveUserName : 龙惠霞
     * QCUserName : 林丽莎
     * NormalCost : 3.9
     * US_Size : One-size
     * Id : 8029933
     * CreateUserName : System
     * CreateTime : 2019-01-08T06:42:51.203
     * UpdateUserName : 梁锋
     * UpdateTime : 2019-01-25T11:52:26
     */

    @JSONField(name = "OrderId")
    private int orderId;
    @JSONField(name = "OrderGoodsId")
    private int orderGoodsId;
    @JSONField(name = "SiteId")
    private int siteId;
    //产品ID
    @JSONField(name = "SpuId")
    private int spuId;
    @JSONField(name = "SkuId")
    private int skuId;
    @JSONField(name = "Status")
    private int status;
    @JSONField(name = "PurchaseStatus")
    private int purchaseStatus;
    @JSONField(name = "QCStatus")
    private int qCStatus;
    @JSONField(name = "StockStatus")
    private int stockStatus;
    @JSONField(name = "LogisticsStatus")
    private int logisticsStatus;
    @JSONField(name = "Cost")
    private double cost;
    @JSONField(name = "SupplierId")
    private int supplierId;
    @JSONField(name = "IsPublic")
    private boolean isPublic;
    @JSONField(name = "PreparedDate")
    private int preparedDate;
    @JSONField(name = "AllocationTime")
    private String allocationTime;
    @JSONField(name = "ConfirmTime")
    private Object confirmTime;
    @JSONField(name = "SupplierDeliveryTime")
    private Object supplierDeliveryTime;
    @JSONField(name = "ReceiveTime")
    private String receiveTime;
    @JSONField(name = "QCTime")
    private String qCTime;
    @JSONField(name = "StockInTime")
    private String stockInTime;
    @JSONField(name = "StockOutTime")
    private String stockOutTime;
    @JSONField(name = "LogisticsDeliveryTime")
    private String logisticsDeliveryTime;
    @JSONField(name = "SourceUrl")
    private String sourceUrl;
    @JSONField(name = "SupplierGoodsNo")
    private String supplierGoodsNo;
    @JSONField(name = "Specification")
    private String specification;
    @JSONField(name = "SupplierName")
    private String supplierName;
    @JSONField(name = "CategoryId")
    private int categoryId;
    @JSONField(name = "Weight")
    private int weight;
    @JSONField(name = "ThumbnailsUrl")
    private String thumbnailsUrl;
    @JSONField(name = "SingleId")
    private int singleId;
    @JSONField(name = "NewQuote")
    private boolean newQuote;
    @JSONField(name = "SupplyStatus")
    private int supplyStatus;
    @JSONField(name = "GoodsType")
    private int goodsType;
    @JSONField(name = "GoodsSource")
    private int goodsSource;
    @JSONField(name = "IsInBatch")
    private boolean isInBatch;
    @JSONField(name = "Remark")
    private String remark;
    @JSONField(name = "SupplierRemark")
    private String supplierRemark;
    @JSONField(name = "NoGoodsReason")
    private String noGoodsReason;
    @JSONField(name = "isPumpQc")
    private boolean isPumpQc;
    @JSONField(name = "IsNoHeavy")
    private boolean isNoHeavy;
    @JSONField(name = "Currency")
    private String currency;
    @JSONField(name = "LocalCost")
    private double localCost;
    @JSONField(name = "IsReturn")
    private boolean isReturn;
    @JSONField(name = "IsCreateVoucher")
    private boolean isCreateVoucher;
    @JSONField(name = "QCSource")
    private int qCSource;
    @JSONField(name = "IsSystemPush")
    private boolean isSystemPush;
    @JSONField(name = "IsHeavy")
    private boolean isHeavy;
    @JSONField(name = "CostType")
    private int costType;
    @JSONField(name = "ReturnTimes")
    private int returnTimes;
    @JSONField(name = "IsReturnBySku")
    private boolean isReturnBySku;
    @JSONField(name = "SystemArrivalDate")
    private String systemArrivalDate;
    @JSONField(name = "LatestArrivalDate")
    private String latestArrivalDate;
    @JSONField(name = "WeightTime")
    private String weightTime;
    @JSONField(name = "ReceiveUserName")
    private String receiveUserName;
    @JSONField(name = "QCUserName")
    private String qCUserName;
    @JSONField(name = "NormalCost")
    private double normalCost;
    @JSONField(name = "US_Size")
    private String uSSize;
    @JSONField(name = "Id")
    private int id;
    @JSONField(name = "CreateUserName")
    private String createUserName;
    @JSONField(name = "CreateTime")
    private String createTime;
    @JSONField(name = "UpdateUserName")
    private String updateUserName;
    @JSONField(name = "UpdateTime")
    private String updateTime;




    private int GoodsId;
    //仓库ID
    private int RoomId;
    //出库类型
    private int OutStockType;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderGoodsId() {
        return orderGoodsId;
    }

    public void setOrderGoodsId(int orderGoodsId) {
        this.orderGoodsId = orderGoodsId;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public int getSpuId() {
        return spuId;
    }

    public void setSpuId(int spuId) {
        this.spuId = spuId;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPurchaseStatus() {
        return purchaseStatus;
    }

    public void setPurchaseStatus(int purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }

    public int getQCStatus() {
        return qCStatus;
    }

    public void setQCStatus(int qCStatus) {
        this.qCStatus = qCStatus;
    }

    public int getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(int stockStatus) {
        this.stockStatus = stockStatus;
    }

    public int getLogisticsStatus() {
        return logisticsStatus;
    }

    public void setLogisticsStatus(int logisticsStatus) {
        this.logisticsStatus = logisticsStatus;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public int getPreparedDate() {
        return preparedDate;
    }

    public void setPreparedDate(int preparedDate) {
        this.preparedDate = preparedDate;
    }

    public String getAllocationTime() {
        return allocationTime;
    }

    public void setAllocationTime(String allocationTime) {
        this.allocationTime = allocationTime;
    }

    public Object getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Object confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Object getSupplierDeliveryTime() {
        return supplierDeliveryTime;
    }

    public void setSupplierDeliveryTime(Object supplierDeliveryTime) {
        this.supplierDeliveryTime = supplierDeliveryTime;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getQCTime() {
        return qCTime;
    }

    public void setQCTime(String qCTime) {
        this.qCTime = qCTime;
    }

    public String getStockInTime() {
        return stockInTime;
    }

    public void setStockInTime(String stockInTime) {
        this.stockInTime = stockInTime;
    }

    public String getStockOutTime() {
        return stockOutTime;
    }

    public void setStockOutTime(String stockOutTime) {
        this.stockOutTime = stockOutTime;
    }

    public String getLogisticsDeliveryTime() {
        return logisticsDeliveryTime;
    }

    public void setLogisticsDeliveryTime(String logisticsDeliveryTime) {
        this.logisticsDeliveryTime = logisticsDeliveryTime;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSupplierGoodsNo() {
        return supplierGoodsNo;
    }

    public void setSupplierGoodsNo(String supplierGoodsNo) {
        this.supplierGoodsNo = supplierGoodsNo;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getThumbnailsUrl() {
        return thumbnailsUrl;
    }

    public void setThumbnailsUrl(String thumbnailsUrl) {
        this.thumbnailsUrl = thumbnailsUrl;
    }

    public int getSingleId() {
        return singleId;
    }

    public void setSingleId(int singleId) {
        this.singleId = singleId;
    }

    public boolean isNewQuote() {
        return newQuote;
    }

    public void setNewQuote(boolean newQuote) {
        this.newQuote = newQuote;
    }

    public int getSupplyStatus() {
        return supplyStatus;
    }

    public void setSupplyStatus(int supplyStatus) {
        this.supplyStatus = supplyStatus;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public int getGoodsSource() {
        return goodsSource;
    }

    public void setGoodsSource(int goodsSource) {
        this.goodsSource = goodsSource;
    }

    public boolean isIsInBatch() {
        return isInBatch;
    }

    public void setIsInBatch(boolean isInBatch) {
        this.isInBatch = isInBatch;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSupplierRemark() {
        return supplierRemark;
    }

    public void setSupplierRemark(String supplierRemark) {
        this.supplierRemark = supplierRemark;
    }

    public String getNoGoodsReason() {
        return noGoodsReason;
    }

    public void setNoGoodsReason(String noGoodsReason) {
        this.noGoodsReason = noGoodsReason;
    }

    public boolean isIsPumpQc() {
        return isPumpQc;
    }

    public void setIsPumpQc(boolean isPumpQc) {
        this.isPumpQc = isPumpQc;
    }

    public boolean isIsNoHeavy() {
        return isNoHeavy;
    }

    public void setIsNoHeavy(boolean isNoHeavy) {
        this.isNoHeavy = isNoHeavy;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getLocalCost() {
        return localCost;
    }

    public void setLocalCost(double localCost) {
        this.localCost = localCost;
    }

    public boolean isIsReturn() {
        return isReturn;
    }

    public void setIsReturn(boolean isReturn) {
        this.isReturn = isReturn;
    }

    public boolean isIsCreateVoucher() {
        return isCreateVoucher;
    }

    public void setIsCreateVoucher(boolean isCreateVoucher) {
        this.isCreateVoucher = isCreateVoucher;
    }

    public int getQCSource() {
        return qCSource;
    }

    public void setQCSource(int qCSource) {
        this.qCSource = qCSource;
    }

    public boolean isIsSystemPush() {
        return isSystemPush;
    }

    public void setIsSystemPush(boolean isSystemPush) {
        this.isSystemPush = isSystemPush;
    }

    public boolean isIsHeavy() {
        return isHeavy;
    }

    public void setIsHeavy(boolean isHeavy) {
        this.isHeavy = isHeavy;
    }

    public int getCostType() {
        return costType;
    }

    public void setCostType(int costType) {
        this.costType = costType;
    }

    public int getReturnTimes() {
        return returnTimes;
    }

    public void setReturnTimes(int returnTimes) {
        this.returnTimes = returnTimes;
    }

    public boolean isIsReturnBySku() {
        return isReturnBySku;
    }

    public void setIsReturnBySku(boolean isReturnBySku) {
        this.isReturnBySku = isReturnBySku;
    }

    public String getSystemArrivalDate() {
        return systemArrivalDate;
    }

    public void setSystemArrivalDate(String systemArrivalDate) {
        this.systemArrivalDate = systemArrivalDate;
    }

    public String getLatestArrivalDate() {
        return latestArrivalDate;
    }

    public void setLatestArrivalDate(String latestArrivalDate) {
        this.latestArrivalDate = latestArrivalDate;
    }

    public String getWeightTime() {
        return weightTime;
    }

    public void setWeightTime(String weightTime) {
        this.weightTime = weightTime;
    }

    public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    public String getQCUserName() {
        return qCUserName;
    }

    public void setQCUserName(String qCUserName) {
        this.qCUserName = qCUserName;
    }

    public double getNormalCost() {
        return normalCost;
    }

    public void setNormalCost(double normalCost) {
        this.normalCost = normalCost;
    }

    public String getUSSize() {
        return uSSize;
    }

    public void setUSSize(String uSSize) {
        this.uSSize = uSSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
