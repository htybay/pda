package com.chicv.pda.utils;

import com.chicv.pda.bean.event.PickOverEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Lilaoda on 2017/11/10.
 * Email:749948218@qq.com
 */

public class EventBusUtls {

    /**
     * 通知拣货列表有改变 在拣货列表接受通知{@link com.chicv.pda.ui.pickGoods.PickManagerActivity}
     *
     * @param pickEvent 事件信息
     */
    public static void notifyPickChanged(PickOverEvent pickEvent) {
        EventBus.getDefault().post(pickEvent);
    }

}
