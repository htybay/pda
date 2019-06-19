package com.chicv.pda.bean.param;

import java.util.List;

/**
 * author: liheyu
 * date: 2019-06-18
 * email: liheyu999@163.com
 * 移库下架，入参
 */
public class MoveRoomDownParam {


    /**
     * MoveId : 0
     * LowerShelfDetail : [{"BatchCode":"string","GoodsId":0}]
     */

    private int MoveId;
    private List<LowerShelfDetailBean> LowerShelfDetail;

    public int getMoveId() {
        return MoveId;
    }

    public void setMoveId(int MoveId) {
        this.MoveId = MoveId;
    }

    public List<LowerShelfDetailBean> getLowerShelfDetail() {
        return LowerShelfDetail;
    }

    public void setLowerShelfDetail(List<LowerShelfDetailBean> LowerShelfDetail) {
        this.LowerShelfDetail = LowerShelfDetail;
    }

    public static class LowerShelfDetailBean {
        /**
         * BatchCode : string
         * GoodsId : 0
         */

        private String BatchCode;
        private int GoodsId;

        public LowerShelfDetailBean() {
        }

        public LowerShelfDetailBean(String batchCode, int goodsId) {
            BatchCode = batchCode;
            GoodsId = goodsId;
        }

        public String getBatchCode() {
            return BatchCode;
        }

        public void setBatchCode(String BatchCode) {
            this.BatchCode = BatchCode;
        }

        public int getGoodsId() {
            return GoodsId;
        }

        public void setGoodsId(int GoodsId) {
            this.GoodsId = GoodsId;
        }
    }
}
