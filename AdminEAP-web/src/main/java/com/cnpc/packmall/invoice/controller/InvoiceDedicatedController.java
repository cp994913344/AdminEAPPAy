package com.cnpc.packmall.invoice.controller;

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

import com.cnpc.packmall.invoice.service.InvoiceDedicatedService;
import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.packmall.invoice.entity.InvoiceDedicated;

/**
* 发票管理（增专）管理控制器
* @author jrn
* 2018-08-16 14:37:56由代码生成器自动生成
*/
@Controller
@RequestMapping("/packmail/invoicededicated")
public class InvoiceDedicatedController {

    @Resource
    private InvoiceDedicatedService invoicededicatedService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "packmail/invoice/invoicededicated_list";
    }

    @RefreshCSRFToken
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String edit(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "packmail/invoice/invoicededicated_edit";
    }

    @RequestMapping(value="/detail",method = RequestMethod.GET)
    public String detail(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "packmail/invoice/invoicededicated_detail";
    }

    @RequestMapping(value="/get/{id}",method = RequestMethod.POST)
    @ResponseBody
    public InvoiceDedicated get(@PathVariable("id") String id){
        return invoicededicatedService.get(InvoiceDedicated.class, id);
    }

    @VerifyCSRFToken
    @RequestMapping(value="/save")
    @ResponseBody
    public Result save(InvoiceDedicated invoicededicated){
        if(StrUtil.isEmpty(invoicededicated.getId())){
            invoicededicatedService.save(invoicededicated);
        }
        else{
            invoicededicated.setUpdateDateTime(new Date());
            invoicededicatedService.update(invoicededicated);
        }
        return new Result(true);
    }



    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id){
        InvoiceDedicated invoicededicated=this.get(id);
        try{
        	invoicededicatedService.delete(invoicededicated);
            return new Result();
        }
        catch(Exception e){
            return new Result(false,"该数据已经被引用，不可删除");
        }
    }



}
