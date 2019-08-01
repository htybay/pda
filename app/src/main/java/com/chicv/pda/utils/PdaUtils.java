package com.chicv.pda.utils;

import com.chicv.pda.base.Constant;

public class PdaUtils {


    //        拣货单状态
    //        未知数据 = 0,
    //        待打印 = 10,
    //        待领取 = 20,
    //        待拣货 = 30,
    //        待配货 = 40,
    //        待出库 = 50,
    //        已完成 = 90
    public static String getPickStatusDes(int status) {
        String value = "未知数据";
        switch (status) {
            case 10:
                value = "待打印";
                break;
            case 20:
                value = "待领取";
                break;
            case 30:
                value = "待拣货";
                break;
            case 40:
                value = "待配货";
                break;
            case 50:
                value = "待出库";
                break;
            case 90:
                value = "已完成";
                break;
        }
        return value;
    }

    /// 错误状态
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

    public static String getStatusDes(int status) {
        String value = "未知数据";
        switch (status) {
            case 0:
                value = "未知数据";
                break;
            case 1:
                value = "正常数据";
                break;
            case 2:
                value = "物品丢失";
                break;
            case 3:
                value = "订单取消";
                break;
            case 4:
                value = "订单暂停";
                break;
            case 5:
                value = "订单地址变更";
                break;
            case 6:
                value = "订单快递变更";
                break;
            case 7:
                value = "搬仓移库";
                break;
            case 8:
                value = "调拨移位丢失";
                break;
            case 90:
                value = "手动丢失";
                break;
        }
        return value;
    }


    //GoodsStatus  物品可用状态
    //
    //    正常 = 0,
    //    流程暂停 = 1,
    //    已删除 = 10
    public static String getGoodsStatusDes(int status) {
        String value;
        switch (status) {
            case Constant.GOODS_STATUS_PAUSE:
                value = "流程暂停";
                break;
            case Constant.GOODS_STATUS_DELETE:
                value = "已删除";
                break;
            default:
                value = "正常";
                break;
        }
        return value;
    }


//            待处理 = 10,
//            待拣货 = 20,
//            待出库 = 30,
//            待配送 = 40,
//            待签收 = 50,
//            待入库 = 60,
//            已完成 = 70

    public static String getTransferStatusDes(int status) {
        String value;
        switch (status) {
            case 10:
                value = "待处理";
                break;
            case 20:
                value = "待拣货";
                break;
            case 30:
                value = "待出库";
                break;
            case 40:
                value = "待配送";
                break;
            case 50:
                value = "待签收";
                break;
            case 60:
                value = "待入库";
                break;
            case 70:
                value = "已完成";
                break;
            default:
                value = "";
                break;
        }
        return value;
    }


//    public enum TransferType
//    {
//        自动出库调拨 = 1,
//        手动出库调拨 = 2,
//        手动收货调拨 = 3
//    }

    public static String getTransferTypeDes(int status) {
        String value;
        switch (status) {
            case 1:
                value = "自动出库调拨";
                break;
            case 2:
                value = "手动出库调拨";
                break;
            case 3:
                value = "手动收货调拨";
                break;
            default:
                value = "";
                break;
        }
        return value;
    }

    //        未知 = 0,
    //        移库中 = 10,
    //        上架中 = 20,
    //        移库完成 = 90
    public static String getMoveStatusDes(int status) {
        String value;
        switch (status) {
            case 0:
                value = "未知";
                break;
            case 10:
                value = "移库中";
                break;
            case 20:
                value = "上架中";
                break;
            case 90:
                value = "移库完成";
                break;
            default:
                value = "";
                break;
        }
        return value;
    }

    /// GoodsStockStatus 物品仓储状态
    //    待入库 = 0,
    //    已入库 = 1,
    //    已出库 = 2,
    //    不入库 = 3
    public static final int GOODS_STOCK_STATUS_READY_IN = 0;
    public static final int GOODS_STOCK_STATUS_IN = 1;
    public static final int GOODS_STOCK_STATUS_OUT = 2;
    public static final int GOODS_STOCK_STATUS_NOT_IN = 3;

    public static String getGoodsStockStatusDes(int status) {
        String value;
        switch (status) {
            case GOODS_STOCK_STATUS_READY_IN:
                value = "待入库";
                break;
            case GOODS_STOCK_STATUS_IN:
                value = "已入库";
                break;
            case GOODS_STOCK_STATUS_OUT:
                value = "已出库";
                break;
            case GOODS_STOCK_STATUS_NOT_IN:
                value = "不入库";
                break;
            default:
                value = "";
                break;
        }
        return value;
    }

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

    public static String getGoodsPurchaseStatusDes(int status) {
        String value;
        switch (status) {
            case GOODS_PUCHASE_STATUS_READY_BUY:
                value = "待采购";
                break;
            case GOODS_PUCHASE_STATUS_IN_PRICE:
                value = "询价中";
                break;
            case GOODS_PUCHASE_STATUS_READY_CONFIRM:
                value = "待确认";
                break;
            case GOODS_PUCHASE_STATUS_READY_SEND:
                value = "待发货";
                break;
            case GOODS_PUCHASE_STATUS_READY_RECEIVE:
                value = "待收货";
                break;
            case GOODS_PUCHASE_STATUS_READY_CHECK:
                value = "待质检";
                break;
            case GOODS_PUCHASE_STATUS_READY_WEIGHT:
                value = "不合格待处理";
                break;
            case GOODS_PUCHASE_STATUS_READY_HANDLE:
                value = "待称重";
                break;
            case GOODS_PUCHASE_STATUS_OVER:
                value = "已完成";
                break;
            default:
                value = "";
                break;
        }
        return value;
    }
//    public enum Room
//    {
//        广州菲宝仓 = 1,
//        美国库房 = 2,
//        亚马逊仓库 = 3,
//        泰国仓 = 4,
//        马来西亚仓 = 5,
//        新ERP仓 = 6,
//        广州唐阁仓 = 7,
//        广州金源仓 = 8,
//        调样库房 = 9
//    }
}
