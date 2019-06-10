package com.chicv.pda.bean;

/**
 * author: liheyu
 * date: 2019-06-04
 * email: liheyu999@163.com
 * 根据物品ID获取货位信息
 */
public class RecommendStock {

    private PurchaseGoods purchaseGoods;

    private LocationRecommendBean locationRecommend;

    public PurchaseGoods getPurchaseGoods() {
        return purchaseGoods;
    }

    public void setPurchaseGoods(PurchaseGoods purchaseGoods) {
        this.purchaseGoods = purchaseGoods;
    }

    public LocationRecommendBean getLocationRecommend() {
        return locationRecommend;
    }

    public void setLocationRecommend(LocationRecommendBean locationRecommend) {
        this.locationRecommend = locationRecommend;
    }

    public static class LocationRecommendBean {

        private int goodsId;
        private int roomId;
        private int areaId;
        private String areaName;
        private int grid;

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }

        public int getAreaId() {
            return areaId;
        }

        public void setAreaId(int areaId) {
            this.areaId = areaId;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public int getGrid() {
            return grid;
        }

        public void setGrid(int grid) {
            this.grid = grid;
        }
    }
}
