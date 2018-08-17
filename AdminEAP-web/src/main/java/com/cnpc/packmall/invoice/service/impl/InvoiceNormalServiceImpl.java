package com.cnpc.packmall.invoice.service.impl;

import com.cnpc.packmall.invoice.entity.InvoiceDedicated;
import com.cnpc.packmall.invoice.entity.InvoiceNormal;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.packmall.invoice.service.InvoiceNormalService;

/**
* 发票管理（增普）服务实现
* @author WY
* 2018-08-16 14:37:42由代码生成器自动生成
*/
@Service("invoicenormalService")
public class InvoiceNormalServiceImpl extends BaseServiceImpl implements InvoiceNormalService {

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
        InvoiceNormal invoiceNormal = this.baseDao.get(InvoiceNormal.class,invoiceId);
        if(invoiceNormal!=null){
            invoiceNormal.setInvoiceMark(invoiceMark);
            invoiceNormal.setInvoiceNo(invoiceNo);
            invoiceNormal.setCourierNo(courierNo);
            invoiceNormal.setInvoiceStatus(2);
            this.baseDao.saveOrUpdate(invoiceNormal);
            return true;
        }
        return false;
    }
}
