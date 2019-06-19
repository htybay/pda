package com.chicv.pda.bean.param;

import java.util.List;

/**
 * author: liheyu
 * date: 2019-06-18
 * email: liheyu999@163.com
 * 移库上架 上架入参
 */
public class MoveRoomUpParam {

    /**
     * MoveId : 0
     * GridId : 0
     * UpperShelfDetail : [{"DetailId":0,"BatchCode":"string","GoodsId":0}]
     */

    private int MoveId;
    private int GridId;
    private List<UpperShelfDetailBean> UpperShelfDetail;

    public int getMoveId() {
        return MoveId;
    }

    public void setMoveId(int MoveId) {
        this.MoveId = MoveId;
    }

    public int getGridId() {
        return GridId;
    }

    public void setGridId(int GridId) {
        this.GridId = GridId;
    }

    public List<UpperShelfDetailBean> getUpperShelfDetail() {
        return UpperShelfDetail;
    }

    public void setUpperShelfDetail(List<UpperShelfDetailBean> UpperShelfDetail) {
        this.UpperShelfDetail = UpperShelfDetail;
    }

    public static class UpperShelfDetailBean {
        /**
         * DetailId : 0
         * BatchCode : string
         * GoodsId : 0
         */

        private int DetailId;
        private String BatchCode;
        private int GoodsId;

        public int getDetailId() {
            return DetailId;
        }

        public void setDetailId(int DetailId) {
            this.DetailId = DetailId;
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
