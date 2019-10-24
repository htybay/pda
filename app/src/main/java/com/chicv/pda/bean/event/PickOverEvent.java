package com.chicv.pda.bean.event;

/**
 * author: liheyu
 * date: 2019-10-23
 * email: liheyu999@163.com
 */
public class PickOverEvent {

    private boolean continuePick;
    private int pickId;

    public PickOverEvent() {
    }

    public int getPickId() {
        return pickId;
    }

    public void setPickId(int pickId) {
        this.pickId = pickId;
    }

    public boolean isContinuePick() {
        return continuePick;
    }

    public void setContinuePick(boolean continuePick) {
        this.continuePick = continuePick;
    }
}
