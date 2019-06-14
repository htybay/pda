package com.chicv.pda.bean.param;

/**
 * author: liheyu
 * date: 2019-06-14
 * email: liheyu999@163.com
 *
 * 调拨单添加入参
 */
public class TransferGoodsAddParam {

    private String BatchCode;
    private int FromRoomId;
    private int ToRoomId;
    private int TransferQuantity;

//    <param name="flag">1 囤货规格 2 囤货发货批次</param>
    private int Flag;

    public String getBatchCode() {
        return BatchCode;
    }

    public void setBatchCode(String batchCode) {
        BatchCode = batchCode;
    }

    public int getFromRoomId() {
        return FromRoomId;
    }

    public void setFromRoomId(int fromRoomId) {
        FromRoomId = fromRoomId;
    }

    public int getToRoomId() {
        return ToRoomId;
    }

    public void setToRoomId(int toRoomId) {
        ToRoomId = toRoomId;
    }

    public int getTransferQuantity() {
        return TransferQuantity;
    }

    public void setTransferQuantity(int transferQuantity) {
        TransferQuantity = transferQuantity;
    }

    public int getFlag() {
        return Flag;
    }

    public void setFlag(int flag) {
        Flag = flag;
    }
}
