package com.chicv.pda.bean.param;

import java.util.List;

/**
 * author: liheyu
 * date: 2019-08-28
 * email: liheyu999@163.com
 */
public class HandleStockParam {

    private int cardingId;
    private int oldGrid;
    private int newGrid;
    private boolean isFinish;
    private List<GoodsBatchParam> goodsBatchList;


    public int getCardingId() {
        return cardingId;
    }

    public void setCardingId(int cardingId) {
        this.cardingId = cardingId;
    }

    public int getOldGrid() {
        return oldGrid;
    }

    public void setOldGrid(int oldGrid) {
        this.oldGrid = oldGrid;
    }

    public int getNewGrid() {
        return newGrid;
    }

    public void setNewGrid(int newGrid) {
        this.newGrid = newGrid;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public List<GoodsBatchParam> getGoodsBatchList() {
        return goodsBatchList;
    }

    public void setGoodsBatchList(List<GoodsBatchParam> goodsBatchList) {
        this.goodsBatchList = goodsBatchList;
    }
}
