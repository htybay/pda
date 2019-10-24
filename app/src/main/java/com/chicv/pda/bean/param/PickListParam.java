package com.chicv.pda.bean.param;

/**
 * author: liheyu
 * date: 2019-10-22
 * email: liheyu999@163.com
 *
 * 获取拣货单列表入参
 */
public class PickListParam {

    private String PickDutyUserName;
    private int ContainerId;

    public String getPickDutyUserName() {
        return PickDutyUserName;
    }

    public void setPickDutyUserName(String pickDutyUserName) {
        PickDutyUserName = pickDutyUserName;
    }

    public int getContainerId() {
        return ContainerId;
    }

    public void setContainerId(int containerId) {
        ContainerId = containerId;
    }
}
