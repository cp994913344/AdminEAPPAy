package com.cnpc.framework.activiti.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;

/**
 * Created by billJiang on 2017/7/1.
 * e-mail:475572229@qq.com  qq:475572229
 * 任务完成后为后的审批人，发送消息通知,或者在办理时在业务的Service发送
 */
public class TaskCompletedListener implements EventHandler {


    @Override
    public void handle(ActivitiEvent event) {
        //TODO MESSAGE
    }
}
