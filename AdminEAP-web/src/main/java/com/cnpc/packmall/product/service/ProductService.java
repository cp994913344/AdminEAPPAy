package com.cnpc.packmall.product.service;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.packmall.product.entity.Product;
import com.cnpc.packmall.product.entity.ProductDetail;

import java.util.List;
import java.util.Map;

/**
* 商品服务接口
* @author WY
* 2018-08-20 16:10:15由代码生成器自动生成
*/
public interface ProductService extends BaseService {

    /**
     * 保存商品 和商品详情
     * @param list
     * @param productName
     * @return
     */
    Result savedata(List<ProductDetail> list, String productName);

    /**
     * 修改商品 和商品详情
     * @param list
     * @param productName
     * @return
     */
    Result updatedata(List<ProductDetail> list, String productName,String id);

    /**
     * 通过 productId 查询 详情
     * @param productId
     * @return
     */
    Map<String, Object> findDetailByProducid(String productId);

    /**
     * 获取商品列表
     * @return
     */
    List<Product> findProductList();
}
