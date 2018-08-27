package com.cnpc.packmall.product.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.service.DictService;
import com.cnpc.framework.constant.RedisConstant;
import com.cnpc.framework.utils.StrUtil;
import com.cnpc.packmall.SKU.entity.Sku;
import com.cnpc.packmall.SKU.service.SkuService;
import com.cnpc.packmall.center.entity.ShippingAddress;
import com.cnpc.packmall.product.entity.ProductDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpc.packmall.product.service.ProductService;
import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.packmall.product.entity.Product;

/**
* 商品管理控制器
* @author jrn
* 2018-08-20 16:10:15由代码生成器自动生成
*/
@Controller
@RequestMapping("/packmall/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @Resource
    private DictService dictService;

    @Resource
    private SkuService skuService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "packmall/product/product_list";
    }

    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(){
        return "packmall/product/product_add";
    }

    @RequestMapping(value="/getProductDict",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getProductDict(){
        Map<String,Object> result = new HashMap<>(8);
        List<Dict> colorDictList = dictService.getDictsByCode("PACKMALL_PRODUCT_COLOR");
        List<Dict> typeDictList = dictService.getDictsByCode("PACKMALL_PRODUCT_TYPE");
        List<Dict> qualityDictList = dictService.getDictsByCode("PACKMALL_PRODUCT_QUALITY");
        result.put("colorDictList",colorDictList);
        result.put("typeDictList",typeDictList);
        result.put("qualityDictList",qualityDictList);
         return result;
    }

    /**
     * 保存 字典值
     * @param dict
     * @return
     */
    @RequestMapping(value = "/saveDict", method = RequestMethod.POST)
    @ResponseBody
    public Result saveDict(Dict dict) {
        if(dict!=null&& StringUtils.isNotEmpty(dict.getName())&&StringUtils.isNotEmpty(dict.getCode())){
            return dictService.saveEntity(dict );
        }else{
            return new Result(false);
        }
    }

    @RefreshCSRFToken
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String edit(String id,HttpServletRequest request){
        request.setAttribute("productId", id);
        return "packmall/product/product_edit";
    }

    @RequestMapping(value="/getbyId",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getbyId(String id){
        Map<String,Object> result = productService.findDetailByProducid(id);
        result.put("product",productService.get(Product.class, id));
        return result;
    }

    @RequestMapping(value="/save",method = RequestMethod.POST)
    @ResponseBody
    public Result save(String con,String productName ){
        if(StringUtils.isEmpty(con)||StringUtils.isEmpty(productName)){
            return  new Result(false);
        }
        List<ProductDetail> productDetailList = JSON.parseArray(con,ProductDetail.class);
        return  productService.savedata(productDetailList, productName);
    }

    @RequestMapping(value="/editProdcut",method = RequestMethod.POST)
    @ResponseBody
    public Result editProdcut(String con,String productName,String id ){
        if(StringUtils.isEmpty(con)||StringUtils.isEmpty(productName)){
            return  new Result(false);
        }
        List<ProductDetail> productDetailList = JSON.parseArray(con,ProductDetail.class);
        return  productService.updatedata(productDetailList, productName,id);
    }

    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id){
        Product product=productService.get(Product.class,id);
        try{
            product.setDeleted(1);
        	productService.update(product);
            return new Result();
        }
        catch(Exception e){
            return new Result(false,"该数据已经被引用，不可删除");
        }
    }

    /**
     * sku获取商品列表
     * @return
     */
    @RequestMapping(value="/getProductList",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getProductList(){
        Map<String,Object> result = new HashMap<>(2);
        List<Product> productList = productService.findProductList();
        result.put("productList",productList);
        return result;
    }

     //获取商品 对应质量等信息
    @RequestMapping(value="/getDetailbyProductId",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getDetailbyProductId(String id){
        return productService.findDetailByProducid(id);
    }



    //————————————————————小程序接口start——————————————————————————


    //获取所有商品   list【
    /**
     * 获取 商品页信息
     * @param id
     * @return
     */
    @RequestMapping(value="/pack_mall_api/getProductById/${id}",method = RequestMethod.POST)
    @ResponseBody
    public  Result saveOrUpdate(@PathVariable("id")String id){
        Map<String,Object> result = new HashMap<>(8);
        List<Sku> skuList = skuService.findByProductId(id);
        result.put("skuList", skuList);
        return new Result(true,result);
    }
    //规格选择

    //规格选择  选择尺寸后
}
