package com.cnpc.packmall.order.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.packmall.order.pojo.dto.OrderPurchaseDTO;
import com.cnpc.packmall.order.service.OrderPurchaseService;

@Controller
@RequestMapping("/orderpurchase")
public class OrderPurchaseController {
	@Resource
	OrderPurchaseService OrderPurchaseService;
	
    @RequestMapping(value="/pack_mall_api/getlist/{openid}",method = RequestMethod.POST)
    @ResponseBody
    public Result packMallgetList(@PathVariable("openid") String openid,@RequestParam Map<String, String> param){
    	
    	Map<String, List<OrderPurchaseDTO>> orderPurchaseDTOMap = OrderPurchaseService.findPackMallgetPurchaseList(openid);
        Result result = new Result();
        result.setData(orderPurchaseDTOMap);
        return result;
    }
    
    @RequestMapping(value="/pack_mall_api/save/{openid}",method = RequestMethod.POST)
    @ResponseBody
    public Result packMallgetSave(@PathVariable("openid") String openid,String con){
    	List<OrderPurchaseDTO> orderPurchaseDTOs = JSONArray.parseArray(con,OrderPurchaseDTO.class);
        Map<String, Object> re=OrderPurchaseService.savePackMallOrder(orderPurchaseDTOs,openid);
        Result result = new Result();
        result.setData(re);
        return result;
    }
}
