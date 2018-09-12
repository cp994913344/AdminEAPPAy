package com.cnpc.packmall.product.service;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.packmall.SKU.entity.Sku;
import com.cnpc.packmall.product.entity.Product;
import com.cnpc.packmall.product.entity.ProductDetail;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* 商品服务接口
* @author WY
* 2018-08-20 16:10:15由代码生成器自动生成
*/
public interface ProductService extends BaseService {

    /**
     * 保存商品 和商品详情
     * @param list
     * @param product
     * @return
     */
    Result savedata(List<ProductDetail> list,Product product);

    /**
     * 修改商品 和商品详情
     * @param list
     * @param product
     * @return
     */
    Result updatedata(List<ProductDetail> list, Product product);

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

    /**
     * 小程序 获取商品列表
     * @return
     */
    List<Product> findList();

    /**
     *  获取商品列表 的展示图片list
     * @return
     */
    List<ProductDetail> getImagesListByProductIds(List<String> productIds);

    /**
     * 通过商品ids  获取sku最小价格list
     * @param productIds
     * @return
     */
    List<Sku> getSkuMinPriceByProductIds(List<String> productIds);
    
    /**
     * 通过商品id 和详情类型获取商品详情信息
     * @param productIds
     * @param type
     * @return
     */
    Map<String, List<ProductDetail>> findProductDetailByProductIdAndType(Set<String> productIds,String type);

    /**
     *  修改商品上下架
     * @param id
     * @return
     * @autor cp
     */
    public boolean updateStauts(String id);
}
