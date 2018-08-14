package com.cnpc.demos.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.utils.StrUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpc.demos.service.UserTService;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.demos.entity.UserT;

/**
* 用户测试管理控制器
* @author jrn
* 2017-10-26 19:21:56由代码生成器自动生成
*/
@Controller
@RequestMapping("/usert")
public class UserTController {

    @Resource
    private UserTService usertService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "demos/usert_list";
    }

    @RefreshCSRFToken
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String edit(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "demos/usert_edit";
    }

    @RequestMapping(value="/detail",method = RequestMethod.GET)
    public String detail(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "demos/usert_detail";
    }

    @RequestMapping(value="/get/{id}",method = RequestMethod.POST)
    @ResponseBody
    public UserT get(@PathVariable("id") String id){
        return usertService.get(UserT.class, id);
    }

    @VerifyCSRFToken
    @RequestMapping(value="/save")
    @ResponseBody
    public Result save(String obj){
        UserT usert= JSON.parseObject(obj,UserT.class);
        usert.setNation(usertService.get(Dict.class,usert.getNation().getId()));
        if(StrUtil.isEmpty(usert.getId())){
            usertService.save(usert);
        }
        else{
            usert.setUpdateDateTime(new Date());
            usertService.update(usert);
        }
        return new Result(true);
    }



    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id){
        UserT usert=this.get(id);
        try{
            usertService.delete(usert);
            return new Result();
        }
        catch(Exception e){
            return new Result(false,"该数据已经被引用，不可删除");
        }
    }



}
