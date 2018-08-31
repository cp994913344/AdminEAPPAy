package com.cnpc.packmall.SKU.service;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.packmall.SKU.dto.SkuIdDTO;
import com.cnpc.packmall.SKU.entity.Sku;
import com.cnpc.packmall.SKU.entity.SkuDetail;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* sku服务接口
* @author WY
* 2018-08-22 19:19:40由代码生成器自动生成
*/
public interface SkuService extends BaseService {

    /**
     * 保存sku 和 sku详情
     * @param list
     * @param sku
     * @return
     */
     Result savedata(List<SkuDetail> list, Sku sku);

    /**
     *  修改sku上下架
     * @param id
     * @return
     */
    boolean updateStauts(String id);

    /**
     * 通过 skuId 查询 详情
     * @param skuId
     * @return
     */
    Map<String, Object> findDetailBySkuId(String skuId);

    /**
     * 修改sku 和sku详情
     * @param list
     * @param sku
     * @return
     */
    Result updatedata(List<SkuDetail> list, Sku sku);


    /**
     * 根据商品id 获取 尺寸信息
     * @param productId
     * @return
     */
    List<Sku> findByProductId(String productId);

    /**
     * 根据 ids  查询skuDetailmap
     * @param skuDetailIdList
     * @return
     */
    Map<String,Map<String, SkuDetail>> findSkuDetailBySkuDetailIds(Set<String> skuDetailIdList);
    
    /**
     * 根据 ids  查询skumap
     * @param skuIdList
     * @return
     */
    Map<String,Sku> findSkuBySkuIds(Set<String> skuIdList);

    Integer findSkuNumByProductId(String productId);

    /**
     * 查询具体sku  是否可用 若不可用则返回 不可用的sku信息
     */
    public List<SkuIdDTO> findNotEffectiveBySkuIdDTOList(List<SkuIdDTO> dtoList );
}
