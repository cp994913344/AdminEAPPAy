package com.cnpc.packmall.SKU.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.utils.StrUtil;
import com.cnpc.packmall.SKU.entity.SkuDetail;
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

    @RequestMapping(value="/detail",method = RequestMethod.GET)
    public String detail(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "packmall/SKU/sku_detail";
    }

    @RequestMapping(value="/get/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Sku get(@PathVariable("id") String id){
        return skuService.get(Sku.class, id);
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



    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id){
        Sku sku=this.get(id);
        try{
        	skuService.delete(sku);
            return new Result();
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

}
