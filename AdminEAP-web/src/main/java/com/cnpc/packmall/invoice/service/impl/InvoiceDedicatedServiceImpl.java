package com.cnpc.packmall.invoice.service.impl;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.packmall.center.entity.ShippingAddress;
import com.cnpc.packmall.invoice.entity.InvoiceDedicated;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.packmall.invoice.service.InvoiceDedicatedService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 发票管理（增专）服务实现
* @author WY
* 2018-08-16 14:37:56由代码生成器自动生成
*/
@Service("invoiceDedicatedService")
public class InvoiceDedicatedServiceImpl extends BaseServiceImpl implements InvoiceDedicatedService {

    /**
     *  保存状态变更数据
     * @param invoiceId
     * @param invoiceMark
     * @param invoiceNo
     * @param courierNo
     * @return
     */
    @Override
    public boolean saveInvoiceStatusChangeData(String invoiceId, String invoiceMark, String invoiceNo, String courierNo) {
        InvoiceDedicated invoiceDedicated = this.baseDao.get(InvoiceDedicated.class,invoiceId);
        if(invoiceDedicated!=null){
            invoiceDedicated.setInvoiceMark(invoiceMark);
            invoiceDedicated.setInvoiceNo(invoiceNo);
            invoiceDedicated.setCourierNo(courierNo);
            invoiceDedicated.setInvoiceStatus(2);
            this.baseDao.saveOrUpdate(invoiceDedicated);
            return true;
        }
        return false;
    }

    @Override
    public void insertData(InvoiceDedicated invoiceDedicated) {
        this.baseDao.save(invoiceDedicated);
    }

    /**
     * 根据openid 查询客户的发票
     * @param openId
     * @return
     */
    @Override
    public Result findByOpenId(String openId) {
        if(StringUtils.isEmpty(openId)){
            return new Result(false);
        }
        String hql = " from InvoiceDedicated  where openId = '"+openId+"' and deleted=0";
        List<InvoiceDedicated> list = this.baseDao.find(hql);
        return new Result(true,list);
    }

    /**
     * 通过id 查询详情 及商品信息
     * @param id
     * @return
     */
    @Override
    public Result getDetailById(String id) {
        if(StringUtils.isEmpty(id)){
            return new Result(false);
        }
        Map<String, Object> result = new HashMap<>(4);
        InvoiceDedicated invoiceDedicated = this.baseDao.get(InvoiceDedicated.class, id);
        result.put("invoiceDedicated", invoiceDedicated);
        return new Result(true,result);
    }
}
