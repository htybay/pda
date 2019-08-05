package com.chicv.pda.base;

public class Constant {


    public static final String API_ADDRESS = "http://erp.chicv.com:8081/";
//    public static final String API_ADDRESS = "http://beta.erp.chicv.com:8081/";
//    public static final String API_ADDRESS = "http://192.168.191.177:8832/";

    /**
     * areaId = 152就是快发区,只能装12件货
     */
    public static final int FAST_STOCK_TAG = 152;

    public static final String TOAST_PREFIX = "服务器：";


    //------------------------------------------SP KEY--------------------------------------------------

    /**
     * KEY 记住密码
     */
    public static final String KEY_REMEMBER_PWD = "key_remember_pwd";
    /**
     * KEY 记录拣货单ID
     */
    public static final String KEY_PICK_GOODS_ID = "key_pick_goods_id";
    /**
     * KEY 记录用户ID
     */
    public static final String KEY_PICK_GOODS_USER_ID = "key_pick_goods_user_id";

    //------------------------------------------拣货单状态---------------------------------------------------

    //pickStatus
    /**
     * 拣货单状态 未知数据
     */
    public static final int PICK_STATUS_UNKNOW = 0;
    /**
     * 拣货单状态 待打印
     */
    public static final int PICK_STATUS_UNPRINT = 10;
    /**
     * 拣货单状态 待领取
     */
    public static final int PICK_STATUS_UNRECEIVE = 20;
    /**
     * 拣货单状态 待拣货
     */
    public static final int PICK_STATUS_UNPICK = 30;
    /**
     * 拣货单状态 待配货
     */
    public static final int PICK_STATUS_UNDELIVERY = 40;
    /**
     * 拣货单状态 待出库
     */
    public static final int PICK_STATUS_UNOUT = 50;
    /**
     * 拣货单状态 已完成
     */
    public static final int PICK_STATUS_OVER = 90;


    //------------------------------------------物品状态---------------------------------------------------
    //        未知数据 = 0,
    //        正常数据 = 1,
    //        物品丢失 = 2,
    //        订单取消 = 3,
    //        订单暂停 = 4,
    //        订单地址变更 = 5,
    //        订单快递变更 = 6,
    //        搬仓移库 = 7,
    //        调拨移位丢失 = 8,
    //        手动丢失 = 99
    //Status
    /**
     * 物品状态 未知数据
     */
    public static final int STATUS_UNKNOW = 0;
    /**
     * 物品状态 正常数据
     */
    public static final int STATUS_NORMAL = 1;
    /**
     * 物品状态 物品丢失
     */
    public static final int STATUS_LOSE = 2;
    /**
     * 物品状态 订单取消
     */
    public static final int STATUS_CANCEL = 3;
    /**
     * 物品状态 订单暂停
     */
    public static final int STATUS_PAUSE = 4;
    /**
     * 物品状态 订单地址变更
     */
    public static final int STATUS_CHANGE_ADDRESS = 5;
    /**
     * 物品状态 订单快递变更
     */
    public static final int STATUS_CHANGE_DELIVERY = 6;
    /**
     * 物品状态 搬仓移库
     */
    public static final int STATUS_REMOVE_STOCK = 7;
    /**
     * 物品状态 调拨移位丢失
     */
    public static final int STATUS_REMOVE_LOSE = 8;
    /**
     * 物品状态 手动丢失
     */
    public static final int STATUS_MANUAL_LOSE = 9;


    //------------------------------------------入库类型---------------------------------------------------

    // stockInStockType

    /**
     * 入库类型 未知
     */
    public static final int STOCK_TYPE_IN_UNKNOW = 0;
    /**
     * 入库类型 采购入库
     */
    public static final int STOCK_TYPE_IN_BUY = 1;
    /**
     * 入库类型 客户退货
     */
    public static final int STOCK_TYPE_IN_BACK_GOODS = 2;
    /**
     * 入库类型 调拨入库
     */
    public static final int STOCK_TYPE_IN_TRANFER = 3;
    /**
     * 入库类型 盘盈入库
     */
    public static final int STOCK_TYPE_IN_CHECK_EXTRA = 4;
    /**
     * 入库类型 包裹取消
     */
    public static final int STOCK_TYPE_IN_CANCEL = 5;
    /**
     * 入库类型 换款入库
     */
    public static final int STOCK_TYPE_IN_CHANGE_GOODS = 6;
    /**
     * 入库类型 调样入库
     */
    public static final int STOCK_TYPE_IN_SAMPLE = 7;
    /**
     * 入库类型 重新质检
     */
    public static final int STOCK_TYPE_IN_RETRY_CHECK = 8;
    /**
     * 入库类型 不合格入库
     */
    public static final int STOCK_TYPE_IN_NOT_STANTARD = 99;

    //    GoodsQCStatus
    //    未质检 = 0,
    //    合格 = 1,
    //    不合格 = 2,
    //    报废不质检 = 3
    public static final int QC_STATUS_NO_CHECK = 0;
    public static final int QC_STATUS_OK = 1;
    public static final int QC_STATUS_FAIL = 2;
    public static final int QC_STATUS_BAD_NO_CHECK = 3;

    //    GoodsSource
    //    采购物品 = 0,
    //    换款物品 = 1,
    //    盘盈物品 = 2
    public static final int GOODS_SOURCE_BUY = 0;
    public static final int GOODS_SOURCE_EXCHANGE = 1;
    public static final int GOODS_SOURCE_CHECK = 2;

    //GoodsStatus  物品可用状态
    //
    //    正常 = 0,
    //    流程暂停 = 1,
    //    已删除 = 10
    public static final int GOODS_STATUS_NORMAL = 0;
    public static final int GOODS_STATUS_PAUSE = 1;
    public static final int GOODS_STATUS_DELETE = 10;

    /// GoodsStockStatus 物品仓储状态
    //    待入库 = 0,
    //    已入库 = 1,
    //    已出库 = 2,
    //    不入库 = 3
    public static final int GOODS_STOCK_STATUS_READY_IN = 0;
    public static final int GOODS_STOCK_STATUS_IN = 1;
    public static final int GOODS_STOCK_STATUS_OUT = 2;
    public static final int GOODS_STOCK_STATUS_NOT_IN = 3;

    /// 物品采购状态
    //GoodsPurchaseStatus
    //    待采购 = 0,
    //    询价中 = 1,
    //    待确认 = 2,
    //    待发货 = 3,
    //    待收货 = 4,
    //    待质检 = 5,
    //    不合格待处理 = 7,
    //    待称重 = 6,
    //    已完成 = 10

    public static final int GOODS_PUCHASE_STATUS_READY_BUY = 0;
    public static final int GOODS_PUCHASE_STATUS_IN_PRICE = 1;
    public static final int GOODS_PUCHASE_STATUS_READY_CONFIRM = 2;
    public static final int GOODS_PUCHASE_STATUS_READY_SEND = 3;
    public static final int GOODS_PUCHASE_STATUS_READY_RECEIVE = 4;
    public static final int GOODS_PUCHASE_STATUS_READY_CHECK = 5;
    public static final int GOODS_PUCHASE_STATUS_READY_WEIGHT = 6;
    public static final int GOODS_PUCHASE_STATUS_READY_HANDLE = 7;
    public static final int GOODS_PUCHASE_STATUS_OVER = 10;


    //    StockOutStockType
    //    未知 = 0,
    //    销售出库 = 1,
    //    调拨出库 = 2,
    //    采购退货 = 3,
    //    盘亏出库 = 4,
    //    报损出库 = 5,
    //    质检出库 = 6,
    //    换款出库 = 7
    public static final int STOCK_OUT_TYPE_UNKNOW = 0;
    public static final int STOCK_OUT_TYPE_XIAOSOU = 1;
    public static final int STOCK_OUT_TYPE_DIAOBO = 2;
    public static final int STOCK_OUT_TYPE_CAIGOU = 3;
    public static final int STOCK_OUT_TYPE_PANKUI = 4;
    public static final int STOCK_OUT_TYPE_BAOSUN = 5;
    public static final int STOCK_OUT_TYPE_ZHIJIAN = 6;
    public static final int STOCK_OUT_TYPE_HUANKUAN = 7;

    //            待处理 = 10,
    //            待拣货 = 20,
    //            待出库 = 30,
    //            待配送 = 40,
    //            待签收 = 50,
    //            待入库 = 60,
    //            已完成 = 70
    public static final int TRANSFER_STATUS_UN_HANDLE = 10;
    public static final int TRANSFER_STATUS_UN_PICK = 20;
    public static final int TRANSFER_STATUS_UN_OUT = 30;
    public static final int TRANSFER_STATUS_UN_DELIVERY = 40;
    public static final int TRANSFER_STATUS_UN_SIGN = 50;
    public static final int TRANSFER_STATUS_UN_IN = 60;
    public static final int TRANSFER_STATUS_OVER = 70;

}
