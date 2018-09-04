package com.cnpc.packmall.invoice.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.utils.StrUtil;
import com.cnpc.packmall.invoice.entity.InvoiceDedicated;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpc.packmall.invoice.service.InvoiceNormalService;
import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.packmall.invoice.entity.InvoiceNormal;

/**
* 发票管理（增普）管理控制器
* @author jrn
* 2018-08-16 14:37:42由代码生成器自动生成
*/
@Controller
    @RequestMapping("/packmall/invoicenormal")
public class InvoiceNormalController {

    @Resource
    private InvoiceNormalService invoicenormalService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "packmall/invoice/invoicenormal_list";
    }

    @RequestMapping(value="/toStatusChange",method = RequestMethod.GET)
    public String toStatusChange(String invoiceId,HttpServletRequest request){
        request.setAttribute("invoiceId",invoiceId);
        return "packmall/invoice/invoicenormal_status_change";
    }

    @RequestMapping(value="/saveChangeStatusData",method = RequestMethod.POST)
    @ResponseBody
    public boolean saveChangeStatusData(String invoiceId,String invoiceMark,String invoiceNo,String courierNo){
        if(StringUtils.isEmpty(invoiceId)||StringUtils.isEmpty(invoiceMark)||StringUtils.isEmpty(invoiceNo)||StringUtils.isEmpty(courierNo)){
            return false;
        }
        return invoicenormalService.saveInvoiceStatusChangeData( invoiceId, invoiceMark, invoiceNo, courierNo);
    }


    @RefreshCSRFToken
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String edit(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "packmall/invoice/invoicenormal_edit";
    }

    @RequestMapping(value="/detail",method = RequestMethod.GET)
    public String detail(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "packmall/invoice/invoicenormal_detail";
    }

    @RequestMapping(value="/get/{id}",method = RequestMethod.POST)
    @ResponseBody
    public InvoiceNormal get(@PathVariable("id") String id){
        return invoicenormalService.get(InvoiceNormal.class, id);
    }

    @VerifyCSRFToken
    @RequestMapping(value="/save")
    @ResponseBody
    public Result save(InvoiceNormal invoicenormal){
        if(StrUtil.isEmpty(invoicenormal.getId())){
            invoicenormalService.save(invoicenormal);
        }
        else{
            invoicenormal.setUpdateDateTime(new Date());
            invoicenormalService.update(invoicenormal);
        }
        return new Result(true);
    }



    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id){
        InvoiceNormal invoicenormal=this.get(id);
        try{
            invoicenormal.setDeleted(1);
        	invoicenormalService.update(invoicenormal);
            return new Result();
        }
        catch(Exception e){
            return new Result(false,"该数据已经被引用，不可删除");
        }
    }


    //————————————————————小程序接口start——————————————————————————


    /**
     * 保存发票信息
     * @param invoiceNormal
     * @return
     */
    @RequestMapping(value="/pack_mall_api/save",method = RequestMethod.POST)
    @ResponseBody
    public Result save(InvoiceNormal invoiceNormal, String orderIds){
        try{
            if(invoiceNormal!=null&&StringUtils.isNotEmpty(orderIds)){
                return invoicenormalService.insertData(invoiceNormal,orderIds);
            }
        }catch (Exception e){
            return  new Result(false,e.getMessage());
        }
        return  new Result(false);
    }

}
