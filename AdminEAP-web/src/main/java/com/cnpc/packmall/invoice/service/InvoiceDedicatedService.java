package com.cnpc.packmall.invoice.service;

import com.cnpc.framework.base.pojo.Result;
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


    /**
     * 根据openid 查询客户的发票
     * @param openId
     * @return
     */
    Result findByOpenId(String openId);

    /**
     * 通过id 查询详情 及商品信息
     * @param id
     * @return
     */
    Result getDetailById(String id);
}
