package com.chicv.pda.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * author: liheyu
 * date: 2019-06-04
 * email: liheyu999@163.com
 */
public class LocationGoods {


    private StockInfo StockGrid;
    private List<LocationGoodsDetail> LocationGoodsList;

    public StockInfo getStockGrid() {
        return StockGrid;
    }

    public void setStockGrid(StockInfo stockGrid) {
        StockGrid = stockGrid;
    }

    public List<LocationGoodsDetail> getLocationGoodsList() {
        return LocationGoodsList;
    }

    public void setLocationGoodsList(List<LocationGoodsDetail> locationGoodsList) {
        LocationGoodsList = locationGoodsList;
    }

    public static class LocationGoodsDetail {

        /**
         * GoodsId : 8786638
         * SkuId : 1424046
         * BatchCode : 370I/001424046
         * Specification : 颜色:棕色;尺码:One-size
         */

        @JSONField(name = "GoodsId")
        private int goodsId;
        @JSONField(name = "SkuId")
        private int skuId;
        @JSONField(name = "BatchCode")
        private String batchCode;
        @JSONField(name = "Specification")
        private String specification;

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public int getSkuId() {
            return skuId;
        }

        public void setSkuId(int skuId) {
            this.skuId = skuId;
        }

        public String getBatchCode() {
            return batchCode;
        }

        public void setBatchCode(String batchCode) {
            this.batchCode = batchCode;
        }

        public String getSpecification() {
            return specification;
        }

        public void setSpecification(String specification) {
            this.specification = specification;
        }
    }


}
