package com.cnpc.packmall.center.service.impl;

import com.cnpc.framework.base.pojo.Result;
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
@Service("shippingAddressService")
public class ShippingAddressServiceImpl extends BaseServiceImpl implements ShippingAddressService {


    /**
     * 根据openid 查询客户的收货地址
     * @param openId
     * @return
     */
    @Override
    public Result findByOpenId(String openId) {
        if(StringUtils.isEmpty(openId)){
            return new Result(false);
        }
        String hql = "select sa.shippingDefault as shippingDefault,sa.shippingName as shippingName," +
                "sa.shippingAddress as shippingAddress,sa.shippingPhone as shippingPhone,sarea.mergerName as areaCode " +
                " from ShippingAddress as sa ,SysArea as sarea  where sa.areaCode = sarea.code and sa.openId = :openId and sa.deleted=0";
        Map<String,Object> params = new HashMap<>(4);
        params.put("openId",openId);
        List<ShippingAddress> list = this.baseDao.find(hql,params,ShippingAddress.class);
        return new Result(true,list);
    }
}
