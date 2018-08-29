package com.cnpc.packmall.order.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.StrUtil;
import com.cnpc.packmall.order.entity.OrderDetail;
import com.cnpc.packmall.order.service.OrderDetailService;

/**
* 订单详情管理控制器
* @author jrn
* 2018-08-16 16:58:33由代码生成器自动生成
*/
@Controller
@RequestMapping("/orderdetail")
public class OrderDetailController {

    @Resource
    private OrderDetailService orderdetailService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "packmall/order/orderdetail_list";
    }

    @RefreshCSRFToken
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String edit(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "packmall/order/orderdetail_edit";
    }

    @RequestMapping(value="/detail",method = RequestMethod.GET)
    public String detail(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "packmall/order/orderdetail_detail";
    }

    @RequestMapping(value="/get/{id}",method = RequestMethod.POST)
    @ResponseBody
    public OrderDetail get(@PathVariable("id") String id){
        return orderdetailService.get(OrderDetail.class, id);
    }

    @VerifyCSRFToken
    @RequestMapping(value="/save")
    @ResponseBody
    public Result save(OrderDetail orderdetail){
        if(StrUtil.isEmpty(orderdetail.getId())){
            orderdetailService.save(orderdetail);
        }
        else{
            OrderDetail orderdetail_old=orderdetailService.get(OrderDetail.class,orderdetail.getId());
            BeanUtils.copyProperties(orderdetail,orderdetail_old, "productId","skuId");
            orderdetail_old.setUpdateDateTime(new Date());
            orderdetailService.update(orderdetail_old);
        }
        return new Result(true);
    }



    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id){
        OrderDetail orderdetail=this.get(id);
        try{
        	orderdetailService.delete(orderdetail);
            return new Result();
        }
        catch(Exception e){
            return new Result(false,"该数据已经被引用，不可删除");
        }
    }



}
