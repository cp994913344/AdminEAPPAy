package com.cnpc.framework.activiti.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;

import com.cnpc.framework.activiti.service.IdentityPageService;

/**
 * Created by billJiang on 2017/6/18.
 * e-mail:475572229@qq.com  qq:475572229
 * 自定义用户组查询
 */
public class CustomGroupEntityManager extends GroupEntityManager {

    @Resource
    private IdentityPageService identityPageService;

    //@Override
    public Group findGroupById(String groupId) {
        return identityPageService.findGroupById(groupId);
    }

    @Override
    public List<Group> findGroupsByUser(String userId) {
        return identityPageService.findGroupsByUser(userId);
    }
}