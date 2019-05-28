package com.chicv.pda.bean.param;

/**
 * 捡货参数
 */
public class PickGoodsParam {

    //拣货单ID
    private String pickId;
    //货物ID
    private String goodsId;
    //用户ID
    private String operateUserId;
    //用户名
    private String operateUserName;

    public String getPickId() {
        return pickId;
    }

    public void setPickId(String pickId) {
        this.pickId = pickId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(String operateUserId) {
        this.operateUserId = operateUserId;
    }

    public String getOperateUserName() {
        return operateUserName;
    }

    public void setOperateUserName(String operateUserName) {
        this.operateUserName = operateUserName;
    }
}
