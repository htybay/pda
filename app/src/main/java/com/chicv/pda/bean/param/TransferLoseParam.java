package com.chicv.pda.bean.param;

import java.util.List;

/**
 * author: liheyu
 * date: 2019-05-31
 * email: liheyu999@163.com
 *
 * 调拨丢失入参
 */
public class TransferLoseParam {

    private int TransferId;
    private int LoseType;

    private List<TransferLoseParam.DetailBean> Details;

    public static class DetailBean {
        private int GoodsId;
        private int TransferDetailId;

        public int getGoodsId() {
            return GoodsId;
        }

        public void setGoodsId(int goodsId) {
            GoodsId = goodsId;
        }

        public int getTransferDetailId() {
            return TransferDetailId;
        }

        public void setTransferDetailId(int transferDetailId) {
            TransferDetailId = transferDetailId;
        }
    }

    public int getTransferId() {
        return TransferId;
    }

    public void setTransferId(int transferId) {
        TransferId = transferId;
    }

    public int getLoseType() {
        return LoseType;
    }

    public void setLoseType(int loseType) {
        LoseType = loseType;
    }

    public List<TransferLoseParam.DetailBean> getDetails() {
        return Details;
    }

    public void setDetails(List<TransferLoseParam.DetailBean> details) {
        Details = details;
    }
}
