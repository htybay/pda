package com.chicv.pda.base;

public class Constant {


//    public static final String API_ADDRESS = "http://erp.chicv.com:8081/";
    public static final String API_ADDRESS = "http://beta.erp.chicv.com:8081/";
//    public static final String API_ADDRESS = "http://192.168.191.177:8832/";

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
}
