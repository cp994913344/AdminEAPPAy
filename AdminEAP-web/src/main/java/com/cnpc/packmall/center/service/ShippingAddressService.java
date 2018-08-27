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
     * 根据客户id 查询客户的收货地址
     * @param clientId
     * @return
     */
    public List<ShippingAddress> findByClientId(String clientId);


    /**
     * 根据openid 查询客户的收货地址
     * @param openId
     * @return
     */
    public Result findByOpenId(String openId);
}
