package com.chicv.pda.bean.param;

import java.util.List;

/**
 * author: liheyu
 * date: 2019-06-04
 * email: liheyu999@163.com
 * 入库参数
 */
public class InStockParam {


    /**
     * StockInStockType : 0
     * OperateUserId : 0
     * GoodsType : 0
     * Details : [{"GridId":0,"GoodsId":0}]
     */

    private int stockInStockType;
    private String operateUserId;
    private int goodsType;
    private List<DetailsBean> details;

    public int getStockInStockType() {
        return stockInStockType;
    }

    public void setStockInStockType(int stockInStockType) {
        this.stockInStockType = stockInStockType;
    }

    public String getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(String operateUserId) {
        this.operateUserId = operateUserId;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public List<DetailsBean> getDetails() {
        return details;
    }

    public void setDetails(List<DetailsBean> details) {
        this.details = details;
    }

    public static class DetailsBean {
        /**
         * GridId : 0
         * GoodsId : 0
         */

        private long gridId;
        private long goodsId;

        public long getGridId() {
            return gridId;
        }

        public void setGridId(long gridId) {
            this.gridId = gridId;
        }

        public long getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }
    }
}
