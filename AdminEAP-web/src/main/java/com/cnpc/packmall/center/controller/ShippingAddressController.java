package com.cnpc.packmall.center.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.utils.StrUtil;
import com.cnpc.packmall.center.entity.ShippingAddress;
import com.cnpc.packmall.center.service.ShippingAddressService;
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
* 收货地址管理控制器
* @author jrn
* 2018-08-16 10:50:26由代码生成器自动生成
*/
@Controller
@RequestMapping("/packmall/shippingaddress")
public class ShippingAddressController {

    @Resource
    private ShippingAddressService shippingaddressService;

    @RequestMapping(value="/clientSAList",method = RequestMethod.GET)
    public String list(String openId,HttpServletRequest request){
        request.setAttribute("openId",openId);
        return "packmall/center/shippingaddress_client_list";
    }

    /**
     * 根据客户openid查询收货地址列表
     * @param openId
     * @return
     */
    @RequestMapping(value="/getByClientOpenId",method = RequestMethod.POST)
    @ResponseBody
    public Result getByClientOpenId(String openId){
        return  shippingaddressService.findByOpenId(openId);
    }

    @RefreshCSRFToken
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String edit(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "packmall/center/shippingaddress_edit";
    }

    @RequestMapping(value="/detail",method = RequestMethod.GET)
    public String detail(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "packmall/center/shippingaddress_detail";
    }

    @RequestMapping(value="/get/{id}",method = RequestMethod.POST)
    @ResponseBody
    public ShippingAddress get(@PathVariable("id") String id){
        return shippingaddressService.get(ShippingAddress.class, id);
    }

    @VerifyCSRFToken
    @RequestMapping(value="/save")
    @ResponseBody
    public Result save(ShippingAddress shippingaddress){
        if(StrUtil.isEmpty(shippingaddress.getId())){
            shippingaddressService.save(shippingaddress);
        }
        else{
            shippingaddress.setUpdateDateTime(new Date());
            shippingaddressService.update(shippingaddress);
        }
        return new Result(true);
    }



    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id){
        ShippingAddress shippingaddress=this.get(id);
        try{
            shippingaddressService.delete(shippingaddress);
            return new Result();
        }
        catch(Exception e){
            return new Result(false,"该数据已经被引用，不可删除");
        }
    }

    //————————————————————小程序接口start——————————————————————————

    /**
     * 获取收货地址列表
     * @param clientId
     * @return
     */
    @RequestMapping(value="/pack_mall_api/getList/${clientId}",method = RequestMethod.POST)
    @ResponseBody
    public  Result getList(@PathVariable("clientId")String clientId){
        List<ShippingAddress> list =  shippingaddressService.findByClientId(clientId);
        return new Result(true,list);
    }

    /**
     * 获取编辑收货地址数据
     * @param id
     * @return
     */
    @RequestMapping(value="/pack_mall_api/getEdit/${id}",method = RequestMethod.POST)
    @ResponseBody
    public  Result getEdit(@PathVariable("id")String id){
        ShippingAddress shippingAddress =  shippingaddressService.get(ShippingAddress.class,id);
        return new Result(true,shippingAddress);
    }

    /**
     * 保存修改 实体
     * @param conData
     * @return
     */
    @RequestMapping(value="/pack_mall_api/saveOrUpdate/${conData}",method = RequestMethod.POST)
    @ResponseBody
    public  Result saveOrUpdate(@PathVariable("conData")String conData){
        try{
            ShippingAddress shippingAddress =  JSON.parseObject(conData,ShippingAddress.class);
            shippingaddressService.saveOrUpdate(shippingAddress);
        }catch (Exception e){
            return new Result(false,"错误："+e.getMessage());
        }
        return new Result(true);
    }


    /**
     * 根据客户id查询收货地址列表
     * @param openId
     * @return
     */
    @RequestMapping(value="/pack_mall_api/getByOpenId/${openId}",method = RequestMethod.POST)
    @ResponseBody
    public Result getByOpenId(@PathVariable("openId")  String openId){
        return  shippingaddressService.findByOpenId(openId);
    }




}
