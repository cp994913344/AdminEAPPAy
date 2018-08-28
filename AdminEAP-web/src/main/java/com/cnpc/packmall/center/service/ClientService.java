package com.cnpc.packmall.center.service;

import com.cnpc.framework.base.service.BaseService;
import com.cnpc.packmall.center.entity.Client;

/**
* 客户管理服务接口
* @author WY
* 2018-08-16 10:21:17由代码生成器自动生成
*/
public interface ClientService extends BaseService {

    /**
     *  修改客户禁用
     * @param id
     * @return
     */
    boolean updateDeleted(String id);
    /**
     * 根据openId 查询
     * @param openId
     * @return
     */
    Client getByOpenId(String openId);
}
