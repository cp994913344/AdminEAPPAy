package com.cnpc.packmall.invoice.service;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.packmall.invoice.entity.InvoiceNormal;
import com.cnpc.packmall.invoice.entity.InvoiceWXPay;

import java.util.Map;

/**
* 发票微信服务接口
* @author cp
* 2018-08-16 14:37:42由代码生成器自动生成
*/
public interface InvoiceWXPayService extends BaseService {


    Map<String, String> doInvoicePay(String invoiceId, String invoiceType, String price, String openId);

    InvoiceWXPay getPrepayIdByInvoiceId(String invoiceId);
}
