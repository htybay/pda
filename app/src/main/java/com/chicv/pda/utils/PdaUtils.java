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


}
