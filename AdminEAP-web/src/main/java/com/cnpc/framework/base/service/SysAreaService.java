package com.cnpc.framework.base.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cnpc.framework.base.entity.SysArea;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.pojo.dto.SysAreaDTO;
import com.cnpc.framework.base.service.BaseService;

public interface SysAreaService extends BaseService {

    String findNameByCode(String code);
    
    List<SysArea> findAllprovince();
    
    List<SysArea> findCodeCity(String code);
    
    List<SysArea> findCodeArea(String code);
    
    SysArea getAreaByCode(String code);

    SysArea getByCode(String code);

    List<SysAreaDTO> getTreeData();

    /**
     * 根据code list 查询 城市名称
     * @param codes
     * @return
     */
    List<SysArea> findNameByCodeList(List<String> codes);
}
