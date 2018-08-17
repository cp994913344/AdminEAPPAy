package com.cnpc.packmall.invoice.service.impl;

import com.cnpc.packmall.invoice.entity.InvoiceDedicated;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.packmall.invoice.service.InvoiceDedicatedService;

/**
* 发票管理（增专）服务实现
* @author WY
* 2018-08-16 14:37:56由代码生成器自动生成
*/
@Service("invoicededicatedService")
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
}
