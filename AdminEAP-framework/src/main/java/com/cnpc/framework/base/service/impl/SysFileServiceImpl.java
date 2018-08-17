package com.cnpc.framework.base.service.impl;

import com.cnpc.framework.base.entity.Role;
import com.cnpc.framework.base.entity.SysFile;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.RoleService;
import com.cnpc.framework.base.service.SysFileService;
import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.framework.constant.RedisConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("sysFileService")
public class SysFileServiceImpl extends BaseServiceImpl implements SysFileService {

    /**
     * 根据业务表id查询实体
     * @param formId
     * @return
     */
    @Override
    public SysFile findByFormId(String formId) {
        if(StringUtils.isEmpty(formId)){
            return null;
        }
        String hql = "from SysFile where formId = :formId";
        Map<String,Object> params = new HashMap<>(4);
        params.put("formId",formId);
        return  this.baseDao.get(hql,params);
    }
}
