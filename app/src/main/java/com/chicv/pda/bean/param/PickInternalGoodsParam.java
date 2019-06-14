package com.chicv.pda.bean.param;

/**
 * 内部捡货参数
 */
public class PickInternalGoodsParam {

    //拣货单ID
    private int pickId;
    //货物ID
    private int goodsId;
    //用户ID
    private String operateUserId;
    //用户名
    private String operateUserName;

    public int getPickId() {
        return pickId;
    }

    public void setPickId(int pickId) {
        this.pickId = pickId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
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
