package com.cnpc.packmall.center.service.impl;

import com.cnpc.packmall.center.entity.ShippingAddress;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.packmall.center.service.ShippingAddressService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 收货地址服务实现
* @author WY
* 2018-08-16 10:50:26由代码生成器自动生成
*/
@Service("shippingaddressService")
public class ShippingAddressServiceImpl extends BaseServiceImpl implements ShippingAddressService {

    /**
     * 根据客户id 查询客户的收货地址
     * @param clientId
     * @return
     */
    @Override
    public List<ShippingAddress> findByClientId(String clientId){
        if(StringUtils.isEmpty(clientId)){
            return null;
        }
        String hql = "select sa.shippingDefault,sa.shippingName,sa.shippingAddress,sa.shippingPhone,sarea.mergerName as areaCode " +
                " from ShippingAddress as sa ,SysArea as sarea  where sa.areaCode = sarea.code and sa.clientId = :clientId";
        Map<String,Object> params = new HashMap<>(4);
        params.put("clientId",clientId);
        List<ShippingAddress> shippingAddressList =  this.baseDao.find(hql,params);
        return  shippingAddressList;
    }
}
