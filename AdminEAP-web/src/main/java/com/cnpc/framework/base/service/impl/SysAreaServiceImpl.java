package com.cnpc.framework.base.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.entity.SysArea;
import com.cnpc.framework.base.pojo.TreeNode;
import com.cnpc.framework.base.pojo.dto.SysAreaDTO;
import com.cnpc.framework.base.service.SysAreaService;
import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.framework.utils.StrUtil;
import com.cnpc.framework.utils.TreeUtil;

@Service("sysSysAreaService")
public class SysAreaServiceImpl extends BaseServiceImpl implements SysAreaService {
    private static final Logger logger= LoggerFactory.getLogger(SysAreaServiceImpl.class);

    /**
     * 编号查询地区全称
     * 
     * @return
     */
    @Override
    public String findNameByCode(String code) {

        Map<String, Object> param = new HashMap<String, Object>();
        String areaRegion = "from SysArea where code =:code and level =3";
        param.put("code", code);
        SysArea sysArea = this.get(areaRegion, param);

        return sysArea.getMergerName();
    }

    /**
     * 查询所有省
     * 
     * @return
     * 
     * @return
     */
    @Override
    public List<SysArea> findAllprovince() {

        Map<String, Object> param = new HashMap<String, Object>();
        String hql = "from SysArea where deleted=0 and  level=1 order by code";
        return this.find(hql, param);
    }

    /**
     * 省编号查询市
     * 
     * @return
     */
    @Override
    public List<SysArea> findCodeCity(String code) {

        if (code != null) {
            Map<String, Object> param = new HashMap<String, Object>();
            String str = code.substring(0, 2);
            String hql = "from SysArea where code like:str and code!=:str1 and deleted=0 and code like '%00'";
            param.put("str", str + "%00");
            param.put("str1", str + "0000");
            return this.find(hql, param);
        }
        return null;
    }

    /**
     * 查询区
     * 
     * @return
     */
    @Override
    public List<SysArea> findCodeArea(String code) {

        if (code != null) {
            Map<String, Object> param = new HashMap<String, Object>();
            String str = code.substring(0, 4);
            String hql = "from SysArea where code like:str and deleted=0 and code!=:str1";

            param.put("str", str + "%");
            param.put("str1", str + "00");

            return this.find(hql, param);
        }
        return null;
    }

    @Override
    public SysArea getAreaByCode(String code) {
        Map<String, Object> param = new HashMap<String, Object>();
        String hql = "from Area where deleted=0 and code=:code";
        param.put("code", code);
        return this.baseDao.get(hql, param);
    }

    @Override
    public List<SysAreaDTO> getTreeData() {

        String hql = "from SysArea order by code";
        List<SysArea> sysAreas = this.find(hql);
        Map<String, SysAreaDTO> nodelist = new LinkedHashMap<String, SysAreaDTO>();
        for (SysArea sysArea : sysAreas) {
            SysAreaDTO node = new SysAreaDTO();
            node.setName(sysArea.getName());
            node.setCode(sysArea.getCode());
            if(sysArea.getLevel().equals("2")){
                node.setParentCode(node.getCode().substring(0, 2)+"0000");
            }else{
                node.setParentCode(node.getCode().substring(0, 4)+"00");
            }
            //重庆有特殊区-特殊处理
            if(node.getParentCode().equals("500200")){
                node.setParentCode("500100");
            }
            node.setLevel(sysArea.getLevel());
            nodelist.put(node.getCode(), node);
        }
        // 构造树形结构
        return getNodeList(nodelist);
    }
    public static List<SysAreaDTO> getNodeList(Map<String, SysAreaDTO> nodelist) {
        List<SysAreaDTO> tnlist=new ArrayList<>();
        for (String id : nodelist.keySet()) {
            SysAreaDTO node = nodelist.get(id);
            if (node.getLevel().equals("1")) {
                tnlist.add(node);
            } else {
                if (nodelist.get(node.getParentCode()).getChildren() == null)
                    nodelist.get(node.getParentCode()).setChildren(new ArrayList<SysAreaDTO>());
                nodelist.get(node.getParentCode()).getChildren().add(node);
            }
        }
        return tnlist;
    }
}
