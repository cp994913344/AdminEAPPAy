package com.cnpc.packmall.SKU.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.utils.StrUtil;
import com.cnpc.packmall.SKU.entity.SkuDetail;
import com.cnpc.packmall.product.entity.Product;
import com.cnpc.packmall.product.entity.ProductDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpc.packmall.SKU.service.SkuService;
import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.packmall.SKU.entity.Sku;

/**
* sku管理控制器
* @author jrn
* 2018-08-22 19:19:40由代码生成器自动生成
*/
@Controller
@RequestMapping("/packmall/sku")
public class SkuController {

    @Resource
    private SkuService skuService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "packmall/SKU/sku_list";
    }

    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(){
        return "packmall/SKU/sku_add";
    }

    @RefreshCSRFToken
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String edit(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "packmall/SKU/sku_edit";
    }

    @RequestMapping(value="/getbyId",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getbyId(String id){
        Map<String,Object> result = skuService.findDetailBySkuId(id);
        result.put("Sku",skuService.get(Sku.class, id));
        return result;
    }

    /**
     * 保存  数据  add
     * @param detailString
     * @param entityString
     * @return
     */
    @RequestMapping(value="/save",method = RequestMethod.POST)
    @ResponseBody
    public Result save(String detailString,String entityString ){
        if(StringUtils.isEmpty(detailString)||StringUtils.isEmpty(entityString)){
            return  new Result(false);
        }
        List<SkuDetail> skuDetailList = JSON.parseArray(detailString,SkuDetail.class);
        Sku sku =JSON.parseObject(entityString,Sku.class);
        return  skuService.savedata(skuDetailList, sku);
    }

    @RequestMapping(value="/editSku",method = RequestMethod.POST)
    @ResponseBody
    public Result editSku(String detailString,String entityString ){
        if(StringUtils.isEmpty(detailString)||StringUtils.isEmpty(entityString)){
            return  new Result(false);
        }
        List<SkuDetail> skuDetailList = JSON.parseArray(detailString,SkuDetail.class);
        Sku sku =JSON.parseObject(entityString,Sku.class);
        return  skuService.updatedata(skuDetailList, sku);
    }

    @RequestMapping(value="/delete" ,method = RequestMethod.POST)
    @ResponseBody
    public Result delete(String id){
        Sku sku= skuService.get(Sku.class,id);
        try{
            sku.setDeleted(1);
            sku.setUpdateDateTime(new Date());
        	skuService.update(sku);
            return new Result(true);
        }
        catch(Exception e){
            return new Result(false,"该数据已经被引用，不可删除");
        }
    }

    @RequestMapping(value="/updateStauts",method = RequestMethod.POST)
    @ResponseBody
    public boolean updateStauts(String id){
        if(StringUtils.isEmpty(id)){
            return false;
        }
        return skuService.updateStauts(id);
    }

    //————————————————————小程序接口start——————————————————————————

    /**
     * 根据商品 id 获取 sku尺寸信息
     * @param productId
     * @return
     */
    @RequestMapping(value="/pack_mall_api/getSkuListByProductId/${productId}")
    @ResponseBody
    public  Result getSkuListByProductId(@PathVariable("productId")String productId){
        List<Sku> skuList = skuService.findByProductId(productId);
        return new Result(true,skuList);
    }

    /**
     * 根据 skuId 获取 sku详情信息
     * @param skuId
     * @return
     */
    @RequestMapping(value="/pack_mall_api/getSkuDetailBySkuId")
    @ResponseBody
    public  Result getSkuDetailBySkuId(String skuId){
        Map<String, Object> result = skuService.findDetailBySkuId(skuId);
        return new Result(true,result);
    }

}
