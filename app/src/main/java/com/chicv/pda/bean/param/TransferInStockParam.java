package com.chicv.pda.bean.param;

import java.util.List;

/**
 * author: liheyu
 * date: 2019-06-14
 * email: liheyu999@163.com
 * <p>
 * 调拨入库 入参
 */
public class TransferInStockParam {

    private int TransferId;
    private int GridId;
    private String OperateUserId;
    private String OperateUserName;
    private List<DetailBean> InDetails;

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

    public int getGridId() {
        return GridId;
    }

    public void setGridId(int gridId) {
        GridId = gridId;
    }

    public String getOperateUserId() {
        return OperateUserId;
    }

    public void setOperateUserId(String operateUserId) {
        OperateUserId = operateUserId;
    }

    public String getOperateUserName() {
        return OperateUserName;
    }

    public void setOperateUserName(String operateUserName) {
        OperateUserName = operateUserName;
    }

    public List<DetailBean> getInDetails() {
        return InDetails;
    }

    public void setInDetails(List<DetailBean> inDetails) {
        InDetails = inDetails;
    }
}
