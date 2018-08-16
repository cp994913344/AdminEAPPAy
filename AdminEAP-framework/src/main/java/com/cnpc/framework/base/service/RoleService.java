package com.cnpc.framework.base.service;

import java.util.List;
import java.util.Set;

import com.cnpc.framework.base.entity.Role;
import com.cnpc.framework.base.pojo.Result;

public interface RoleService extends BaseService {

    Result delete(String id);

    /**
     * 根据登录名，获取角色集合
     * @param userId 用户id
     * @return 角色编码集合
     */
    Set<String> getRoleCodeSet(String userId);

    List<Role> getRoleList(String userId);
}
