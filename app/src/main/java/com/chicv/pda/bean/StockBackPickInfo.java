package com.chicv.pda.bean;

/**
 * author: liheyu
 * date: 2019-09-18
 * email: liheyu999@163.com
 */
public class StockBackPickInfo {

    private boolean IsPickingComplete;
    private String NextGridName;

    public boolean isPickingComplete() {
        return IsPickingComplete;
    }

    public void setIsPickingComplete(boolean pickingComplete) {
        IsPickingComplete = pickingComplete;
    }

    public String getNextGridName() {
        return NextGridName;
    }

    public void setNextGridName(String nextGridName) {
        NextGridName = nextGridName;
    }
}
