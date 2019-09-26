package com.chicv.pda.bean.param;

import java.util.List;

/**
 * author: liheyu
 * date: 2019-09-17
 * email: liheyu999@163.com
 * <p>
 * 退货物品出库 提交数据
 */
public class GoodsOutParam {

    private String Operator;

    private List<GoodsParam> Parameter;

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String operator) {
        Operator = operator;
    }

    public List<GoodsParam> getParameter() {
        return Parameter;
    }

    public void setParameter(List<GoodsParam> parameter) {
        Parameter = parameter;
    }

    public static class GoodsParam {
        private int GoodsId;
        private int PickId;
        private String OperateUserId;

        public int getGoodsId() {
            return GoodsId;
        }

        public void setGoodsId(int goodsId) {
            GoodsId = goodsId;
        }

        public int getPickId() {
            return PickId;
        }

        public void setPickId(int pickId) {
            PickId = pickId;
        }

        public String getOperateUserId() {
            return OperateUserId;
        }

        public void setOperateUserId(String operateUserId) {
            OperateUserId = operateUserId;
        }
    }
}
