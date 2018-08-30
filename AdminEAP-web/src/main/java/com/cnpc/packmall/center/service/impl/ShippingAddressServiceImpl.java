package com.cnpc.packmall.center.service.impl;

import com.cnpc.framework.base.entity.SysArea;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.SysAreaService;
import com.cnpc.packmall.center.entity.ShippingAddress;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.packmall.center.service.ShippingAddressService;

import javax.annotation.Resource;
import java.util.*;

/**
* 收货地址服务实现
* @author WY
* 2018-08-16 10:50:26由代码生成器自动生成
*/
@Service("shippingAddressService")
public class ShippingAddressServiceImpl extends BaseServiceImpl implements ShippingAddressService {

    @Resource
    private SysAreaService sysAreaService;

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
        String hql = "select sa.id as id , sa.openId as openId, sa.shippingDefault as shippingDefault,sa.shippingName as shippingName," +
                "sa.shippingAddress as shippingAddress,sa.shippingPhone as shippingPhone,sa.areaCode as areaCode,sa.areaAddress as areaAddress " +
                " from ShippingAddress as sa where sa.openId = :openId and sa.deleted=0 order by createDateTime desc";
        Map<String,Object> params = new HashMap<>(4);
        params.put("openId",openId);
        List<ShippingAddress> list = this.baseDao.find(hql,params,ShippingAddress.class);
        return new Result(true,list);
    }

    /**
     * 保存 更新 实体
     * @param shippingAddress
     * @return
     */
    @Override
    public ShippingAddress saveOrUpdateData(ShippingAddress shippingAddress) {
        if(shippingAddress!=null){
            shippingAddress.setDeleted(0);
            //如果是默认收货 地址 把其他的 该客户地址 改成 非默认
            String updateHql = "";
            if(shippingAddress.getShippingDefault().equals(1)&&StringUtils.isEmpty(shippingAddress.getId())){
                updateHql = "update ShippingAddress set shippingDefault = 2 where openId = '"+shippingAddress.getOpenId()+"'";
            }
            //如果是默认收货 地址 把其他的 该客户地址 改成 非默认
            if(shippingAddress.getShippingDefault().equals(1)&&StringUtils.isNotEmpty(shippingAddress.getId())){
                 updateHql = "update ShippingAddress set shippingDefault = 2 where openId = '"+shippingAddress.getOpenId()+"' and id != '"+shippingAddress.getId()+"'";
            }
            if(StringUtils.isNotEmpty(updateHql)){
                this.baseDao.executeHql(updateHql);
            }
            if(StringUtils.isEmpty(shippingAddress.getId())){
                shippingAddress.setDeleted(0);
                shippingAddress.setCreateDateTime(new Date());
                shippingAddress.setVersion(0);
                this.baseDao.save(shippingAddress);
                return shippingAddress;
            }else{
                ShippingAddress oldShipping = this.baseDao.get(ShippingAddress.class,shippingAddress.getId());
                oldShipping.setAreaCode(shippingAddress.getAreaCode());
                oldShipping.setShippingDefault(shippingAddress.getShippingDefault());
                oldShipping.setShippingAddress(shippingAddress.getShippingAddress());
                oldShipping.setShippingName(shippingAddress.getShippingName());
                oldShipping.setShippingPhone(shippingAddress.getShippingPhone());
                oldShipping.setUpdateDateTime(new Date());
                oldShipping.setAreaAddress(shippingAddress.getAreaAddress());
                this.baseDao.update(oldShipping);
                return  oldShipping;
            }
        }
        return null;
    }

    /**
     * 获取默认收货地址
     * @param openId
     * @return
     */
    @Override
    public ShippingAddress findDefaultByOpenId(String openId) {
        String hql = "from ShippingAddress where shippingDefault=1 and openId = '"+openId+"'";
        return this.baseDao.get(hql);
    }

    /**
     * 修改默认地址
     * @param id
     * @param shippingDefault
     * @return
     */
    @Override
    public Result updateDefaultAddress(String id, Integer shippingDefault) {
        if(StringUtils.isEmpty(id)||shippingDefault==null||shippingDefault<=0){
            return new Result(false);
        }
        ShippingAddress shippingAddress = this.baseDao.get(ShippingAddress.class,id);
        if(shippingAddress!=null){
            shippingAddress.setShippingDefault(shippingDefault);
            if(shippingDefault.equals(1)){
              String  updateHql = "update ShippingAddress set shippingDefault = 2 where openId = '"+shippingAddress.getOpenId()+"' and id != '"+shippingAddress.getId()+"'";
                this.baseDao.executeHql(updateHql);
            }
            this.baseDao.update(shippingAddress);
        }
        return new Result(false);
    }


}
