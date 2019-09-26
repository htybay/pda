package com.chicv.pda.bean.param;

import java.util.List;

/**
 * author: liheyu
 * date: 2019-09-26
 * email: liheyu999@163.com
 */
public class MoveRoomLoseParam {

    private int MoveId;
    private List<LoseGoods> LoseDetail;

    public int getMoveId() {
        return MoveId;
    }

    public void setMoveId(int moveId) {
        MoveId = moveId;
    }

    public List<LoseGoods> getLoseDetail() {
        return LoseDetail;
    }

    public void setLoseDetail(List<LoseGoods> loseDetail) {
        LoseDetail = loseDetail;
    }

    public static class LoseGoods {
        private int DetailId;
        private int GoodsId;
        private String BatchCode;

        public int getDetailId() {
            return DetailId;
        }

        public void setDetailId(int detailId) {
            DetailId = detailId;
        }

        public int getGoodsId() {
            return GoodsId;
        }

        public void setGoodsId(int goodsId) {
            GoodsId = goodsId;
        }

        public String getBatchCode() {
            return BatchCode;
        }

        public void setBatchCode(String batchCode) {
            BatchCode = batchCode;
        }
    }
}
