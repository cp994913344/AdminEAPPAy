package com.cnpc.packmall.invoice.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.cnpc.framework.utils.StrUtil;
import com.cnpc.packmall.invoice.entity.InvoiceNormal;
import com.cnpc.packmall.order.pojo.dto.OrderDTO;
import com.cnpc.packmall.order.service.OrderService;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/packmall/invoicededicated")
public class InvoiceDedicatedController {

    @Resource
    private InvoiceDedicatedService invoicededicatedService;

    @Resource
    private OrderService orderService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "packmall/invoice/invoicededicated_list";
    }

    @RequestMapping(value="/toStatusChange",method = RequestMethod.GET)
    public String toStatusChange(String invoiceId,HttpServletRequest request){
        request.setAttribute("invoiceId",invoiceId);
        return "packmall/invoice/invoicededicated_status_change";
    }

    @RequestMapping(value="/saveChangeStatusData",method = RequestMethod.POST)
    @ResponseBody
    public boolean saveChangeStatusData(String invoiceId,String invoiceMark,String invoiceNo,String courierNo){
        if(StringUtils.isEmpty(invoiceId)||StringUtils.isEmpty(invoiceMark)||StringUtils.isEmpty(invoiceNo)||StringUtils.isEmpty(courierNo)){
            return false;
        }
        return invoicededicatedService.saveInvoiceStatusChangeData( invoiceId, invoiceMark, invoiceNo, courierNo);
    }

    @RefreshCSRFToken
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String edit(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "packmall/invoice/invoicededicated_edit";
    }

    @RequestMapping(value="/detail",method = RequestMethod.GET)
    public String detail(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "packmall/invoice/invoicededicated_detail";
    }

    @RequestMapping(value="/get/{id}",method = RequestMethod.POST)
    @ResponseBody
    public InvoiceDedicated get(@PathVariable("id") String id){
        return invoicededicatedService.get(InvoiceDedicated.class, id);
    }

    @VerifyCSRFToken
    @RequestMapping(value="/save")
    @ResponseBody
    public Result save(InvoiceNormal invoicenormal){
        if(StrUtil.isEmpty(invoicenormal.getId())){
            invoicededicatedService.save(invoicenormal);
        }
        else{
            invoicenormal.setUpdateDateTime(new Date());
            invoicededicatedService.update(invoicenormal);
        }
        return new Result(true);
    }


    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id){
        InvoiceDedicated invoicededicated=this.get(id);
        try{
            invoicededicated.setDeleted(1);
        	invoicededicatedService.update(invoicededicated);
            return new Result();
        }
        catch(Exception e){
            return new Result(false,"该数据已经被引用，不可删除");
        }
    }

    //————————————————————小程序接口start——————————————————————————

    /**
     * 保存发票信息
     * @param openId
     * @return
     */
    @RequestMapping(value="/pack_mall_api/getOrderNotInvoice/",method = RequestMethod.POST)
    @ResponseBody
    public Result getOrderNotInvoice(String openId){
        Map<String,String> params = new HashMap<>(2);
        params.put("whetherState","0");
        List<OrderDTO> orderDTOList=orderService.packMallgetList(openId,params);
        return  new Result(true);
    }

    /**
     * 保存发票信息
     * @param invoiceDedicated
     * @return
     */
    @RequestMapping(value="/pack_mall_api/save/${invoiceDedicated}",method = RequestMethod.POST)
    @ResponseBody
    public Result save(@PathVariable("invoiceDedicated")  String invoiceDedicated){
        try{
            InvoiceDedicated invoiceDedicated1 = JSON.parseObject(invoiceDedicated,InvoiceDedicated.class);
            if(invoiceDedicated!=null){
                invoicededicatedService.save(invoiceDedicated);
            }
        }catch (Exception e){
            return  new Result(false,e.getMessage());
        }
        return  new Result(true);
    }

    /**
     * 根据客户openId查询收货地址列表
     * @param openId
     * @return
     */
    @RequestMapping(value="/pack_mall_api/getByOpenId/${openId}",method = RequestMethod.POST)
    @ResponseBody
    public Result getByOpenId(@PathVariable("openId")  String openId){
        return  invoicededicatedService.findByOpenId(openId);
    }

    /**
     * 根据客户openId查询收货地址列表
     * @param id
     * @return
     */
    @RequestMapping(value="/pack_mall_api/getDetailById/${id}",method = RequestMethod.POST)
    @ResponseBody
    public Result getDetailById(@PathVariable("id")  String id){
        return  invoicededicatedService.getDetailById(id);
    }

}
