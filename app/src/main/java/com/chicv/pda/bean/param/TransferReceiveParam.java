package com.chicv.pda.bean.param;

/**
 * author: liheyu
 * date: 2019-06-14
 * email: liheyu999@163.com
 * 调拨收货入参
 */
public class TransferReceiveParam {

    private int transferId;
    private String operateUserName;

    public TransferReceiveParam(int transferId, String operateUserName) {
        this.transferId = transferId;
        this.operateUserName = operateUserName;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public String getOperateUserName() {
        return operateUserName;
    }

    public void setOperateUserName(String operateUserName) {
        this.operateUserName = operateUserName;
    }
}
