package com.chicv.pda.bean.param;

/**
 * author: liheyu
 * date: 2019-06-14
 * email: liheyu999@163.com
 *
 * 调拨拣货 拣货入参
 */
public class TransferPickGoodsParam {

    private int GoodsId;
    private int TransferId;
    private int TransferDetailId;
    private String OperateUserId;
    private String OperateUserName;

    public int getGoodsId() {
        return GoodsId;
    }

    public void setGoodsId(int goodsId) {
        GoodsId = goodsId;
    }

    public int getTransferId() {
        return TransferId;
    }

    public void setTransferId(int transferId) {
        TransferId = transferId;
    }

    public int getTransferDetailId() {
        return TransferDetailId;
    }

    public void setTransferDetailId(int transferDetailId) {
        TransferDetailId = transferDetailId;
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
}
