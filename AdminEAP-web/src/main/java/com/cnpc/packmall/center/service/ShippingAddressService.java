package com.cnpc.packmall.center.service;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.packmall.center.entity.ShippingAddress;

import java.util.List;

/**
* 收货地址服务接口
* @author WY
* 2018-08-16 10:50:26由代码生成器自动生成
*/
public interface ShippingAddressService extends BaseService {

    /**
     * 根据openid 查询客户的收货地址
     * @param openId
     * @return
     */
    Result findByOpenId(String openId);

    /**
     * 保存 更新 实体
     * @param shippingAddress
     * @return
     */
    ShippingAddress saveOrUpdateData(ShippingAddress shippingAddress);

    /**
     * 获取默认收货地址
     * @param openId
     * @return
     */
    ShippingAddress findDefaultByOpenId(String openId);

    /**
     * 修改默认地址
     * @param id
     * @param shippingDefault
     * @return
     */
    Result updateDefaultAddress(String id,Integer shippingDefault );
}
