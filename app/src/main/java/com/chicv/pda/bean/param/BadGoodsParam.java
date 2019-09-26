package com.chicv.pda.bean.param;

import java.util.List;

/**
 * author: liheyu
 * date: 2019-09-23
 * email: liheyu999@163.com
 */
public class BadGoodsParam {

    private int SuccessCount;
    private int CancleCount;
    private int LossesId;
    private List<GoodsDetial> DetailList;

    public int getSuccessCount() {
        return SuccessCount;
    }

    public void setSuccessCount(int successCount) {
        SuccessCount = successCount;
    }

    public int getCancleCount() {
        return CancleCount;
    }

    public void setCancleCount(int cancleCount) {
        CancleCount = cancleCount;
    }

    public int getLossesId() {
        return LossesId;
    }

    public void setLossesId(int lossesId) {
        LossesId = lossesId;
    }

    public List<GoodsDetial> getDetailList() {
        return DetailList;
    }

    public void setDetailList(List<GoodsDetial> detailList) {
        DetailList = detailList;
    }

    public static class GoodsDetial{
        private int GoodsId;
        private int HandleType;

        public int getGoodsId() {
            return GoodsId;
        }

        public void setGoodsId(int goodsId) {
            GoodsId = goodsId;
        }

        public int getHandleType() {
            return HandleType;
        }

        public void setHandleType(int handleType) {
            HandleType = handleType;
        }
    }
}
