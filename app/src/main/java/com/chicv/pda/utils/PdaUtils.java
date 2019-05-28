package com.chicv.pda.utils;

public class PdaUtils {


    //        拣货单状态
    //        未知数据 = 0,
    //        待打印 = 10,
    //        待领取 = 20,
    //        待拣货 = 30,
    //        待配货 = 40,
    //        待出库 = 50,
    //        已完成 = 90
    public static String getPickStatusDesc(int status) {
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
}
