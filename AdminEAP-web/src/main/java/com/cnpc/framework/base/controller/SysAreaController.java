package com.cnpc.framework.base.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpc.framework.base.entity.SysArea;
import com.cnpc.framework.base.entity.UserRole;
import com.cnpc.framework.base.pojo.NboxResult;
import com.cnpc.framework.base.pojo.dto.SysAreaDTO;
import com.cnpc.framework.base.service.SysAreaService;

@Controller
@RequestMapping(value = "/sysArea")
public class SysAreaController {


    @Resource
    private SysAreaService sysAreaService;

    @RequestMapping(value = "/getAreaMergerName", method = RequestMethod.POST)
    @ResponseBody
    public String findNameByCode(String code) {

        return sysAreaService.findNameByCode(code);
    }

    @RequestMapping(value = "/allprovince", method = RequestMethod.POST)
    @ResponseBody
    public List<SysArea> findAllprovince(String code) {

        return sysAreaService.findAllprovince();
    }
    
    @RequestMapping(value = "/cityByProvince", method = RequestMethod.POST)
    @ResponseBody
    public List<SysArea> findCodeCity(String code) {

        return sysAreaService.findCodeCity(code);
    }
    
    @RequestMapping(value = "/areaByCity", method = RequestMethod.POST)
    @ResponseBody
    public List<SysArea> findCodeArea(String code) {

        return sysAreaService.findCodeArea(code);
    }

    @RequestMapping(value = "/getArea", method = RequestMethod.POST)
    @ResponseBody
    public SysArea getAreaByCode(String code) {

        return sysAreaService.getAreaByCode(code);
    }
    
    @RequestMapping(value = "/nbox/getAreaTreaDate", method = RequestMethod.POST)
    @ResponseBody
    public NboxResult getAreaTreaDate() {
        NboxResult nboxResult = new NboxResult();
        nboxResult.setData(sysAreaService.getTreeData());
        return nboxResult;
    }
}
