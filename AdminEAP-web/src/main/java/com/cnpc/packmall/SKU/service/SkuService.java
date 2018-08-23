package com.cnpc.packmall.SKU.service;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.packmall.SKU.entity.Sku;
import com.cnpc.packmall.SKU.entity.SkuDetail;

import java.util.List;

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
}
