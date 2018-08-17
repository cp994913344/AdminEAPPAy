package com.cnpc.framework.base.service;

import com.cnpc.framework.base.entity.Role;
import com.cnpc.framework.base.entity.SysFile;
import com.cnpc.framework.base.pojo.Result;

import java.util.List;
import java.util.Set;

public interface SysFileService extends BaseService {

    /**
     * 根据业务表id查询实体
     * @param formId
     * @return
     */
    SysFile findByFormId(String formId);
}
