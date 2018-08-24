package com.cnpc.packmall.center.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.utils.StrUtil;
import com.cnpc.packmall.center.entity.Client;
import com.cnpc.packmall.center.service.ClientService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.Result;

/**
* 客户管理管理控制器
* @author jrn
* 2018-08-16 10:21:17由代码生成器自动生成
*/
@Controller
@RequestMapping("/packmall/client")
public class ClientController {

    @Resource
    private ClientService clientService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "packmall/center/client_list";
    }

    @RequestMapping(value="/updateDeleted",method = RequestMethod.POST)
    @ResponseBody
    public boolean updateDeleted(String id){
        if(StringUtils.isEmpty(id)){
            return false;
        }
        return clientService.updateDeleted(id);
    }

    @RequestMapping(value="/toHeadImgHtml",method = RequestMethod.GET)
    public String toHeadImgHtml(String clientId,HttpServletRequest request){
        request.setAttribute("clientId",clientId);
        return "packmall/center/client_headImg_view";
    }



    @RequestMapping(value="/detail",method = RequestMethod.GET)
    public String detail(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "packmall/center/client_detail";
    }

    @RequestMapping(value="/get/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Client get(@PathVariable("id") String id){
        return clientService.get(Client.class, id);
    }

    @VerifyCSRFToken
    @RequestMapping(value="/save")
    @ResponseBody
    public Result save(Client client){
        if(StrUtil.isEmpty(client.getId())){
            clientService.save(client);
        }
        else{
            client.setUpdateDateTime(new Date());
            clientService.update(client);
        }
        return new Result(true);
    }



    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id){
        Client client=this.get(id);
        try{
            client.setDeleted(1);
            clientService.update(client);
            return new Result();
        }
        catch(Exception e){
            return new Result(false,"该数据已经被引用，不可删除");
        }
    }

    //————————————————————小程序接口start——————————————————————————

    // 修改姓名

    //修改企业名称

    //换绑手机

    //保存头像 姓名


    //修改头像




}
