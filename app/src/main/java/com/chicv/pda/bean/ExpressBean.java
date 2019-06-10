package com.chicv.pda.bean;

/**
 * author: liheyu
 * date: 2019-06-06
 * email: liheyu999@163.com
 */
public class ExpressBean {


    /**
     * ReceiveId : 0
     * Type : string
     * SupplierId : 0
     * SupplierName : string
     * SingleUserName : string
     * DeliveryQuantity : 0
     * DeliveryWeight : 0
     * DeliveryTime : 2019-06-06T07:34:08.726Z
     * ExpressName : string
     * ExpressNo : string
     */

    //收货单Id 或者 发货批次Id
    private int ReceiveId;
    //类型 PurchaseReceive表示收货单，StockReceive表示发货批次
    private String Type;
    //供应商Id
    private int SupplierId;
    //供应商
    private String SupplierName;
    //跟单人
    private String SingleUserName;
    //发货数量
    private int DeliveryQuantity;
    //发货重量
    private int DeliveryWeight;
    //发货时间
    private String DeliveryTime;
    //快递名称
    private String ExpressName;
    //快递单号
    private String ExpressNo;

    // 签收人
    private String SignUserName ;
    /// 签收时间
    private String SignTime ;
    /// 包裹数量
    private int PackageCount ;

    public String getSignUserName() {
        return SignUserName;
    }

    public void setSignUserName(String signUserName) {
        SignUserName = signUserName;
    }

    public String getSignTime() {
        return SignTime;
    }

    public void setSignTime(String signTime) {
        SignTime = signTime;
    }

    public int getPackageCount() {
        return PackageCount;
    }

    public void setPackageCount(int packageCount) {
        PackageCount = packageCount;
    }

    public int getReceiveId() {
        return ReceiveId;
    }

    public void setReceiveId(int ReceiveId) {
        this.ReceiveId = ReceiveId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public int getSupplierId() {
        return SupplierId;
    }

    public void setSupplierId(int SupplierId) {
        this.SupplierId = SupplierId;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String SupplierName) {
        this.SupplierName = SupplierName;
    }

    public String getSingleUserName() {
        return SingleUserName;
    }

    public void setSingleUserName(String SingleUserName) {
        this.SingleUserName = SingleUserName;
    }

    public int getDeliveryQuantity() {
        return DeliveryQuantity;
    }

    public void setDeliveryQuantity(int DeliveryQuantity) {
        this.DeliveryQuantity = DeliveryQuantity;
    }

    public int getDeliveryWeight() {
        return DeliveryWeight;
    }

    public void setDeliveryWeight(int DeliveryWeight) {
        this.DeliveryWeight = DeliveryWeight;
    }

    public String getDeliveryTime() {
        return DeliveryTime;
    }

    public void setDeliveryTime(String DeliveryTime) {
        this.DeliveryTime = DeliveryTime;
    }

    public String getExpressName() {
        return ExpressName;
    }

    public void setExpressName(String ExpressName) {
        this.ExpressName = ExpressName;
    }

    public String getExpressNo() {
        return ExpressNo;
    }

    public void setExpressNo(String ExpressNo) {
        this.ExpressNo = ExpressNo;
    }
}
