package com.cnpc.packmall.invoice.service;

import com.cnpc.framework.base.service.BaseService;
import com.cnpc.packmall.invoice.entity.InvoiceDedicated;

/**
* 发票管理（增专）服务接口
* @author WY
* 2018-08-16 14:37:56由代码生成器自动生成
*/
public interface InvoiceDedicatedService extends BaseService {

    /**
     *  保存状态变更数据
     * @param invoiceId
     * @param invoiceMark
     * @param invoiceNo
     * @param courierNo
     * @return
     */
    boolean saveInvoiceStatusChangeData(String invoiceId,String invoiceMark,String invoiceNo,String courierNo);

    void insertData(InvoiceDedicated invoiceDedicated);
}
