package com.cnpc.packmall.invoice.entity;/**
 * @author cuipeng
 * @create 2018-08-15 19:24
 **/

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 发票管理  增专
 * @author cuipeng
 * @create 2018-08-15 19:24
 **/
@Entity
@Table(name="TB_PACKMAIL_INVOICE_DEDICATED")
public class InvoiceDedicated extends BaseEntity {

    @Header(name="invoice编码")
    @Column(name="invoice_code",length = 20)
    private String invoiceCode;

    @Header(name="抬头类型 纳税人")
    @Column(name="invoice_taxpayer")
    private String invoiceTaxpayer;

    @Header(name="纳税人识别号")
    @Column(name="invoice_taxpayer_num")
    private String invoiceTaxpayerNum;

    @Header(name="开户银行")
    @Column(name="deposit_bank",length = 50)
    private String depositBank;

    @Header(name="银行账户")
    @Column(name="bank_account",length = 50)
    private String bankAccount;

    @Header(name="开票地址")
    @Column(name="invoice_address")
    private String invoiceAddress;

    @Header(name="开票电话")
    @Column(name="invoice_phone",length = 11)
    private String invoicePhone;

    @Header(name="资质图片")
    @Column(name="qualification_images")
    private String qualificationImages;

    @Header(name="发票金额")
    @Column(name = "invoice_price")
    private BigDecimal invoicePrice;

    @Header(name="收件人")
    @Column(name = "recipient")
    private String recipient;

    @Header(name="联系人电话")
    @Column(name = "recipient_phone",length = 11)
    private String recipientPhone;

    @Header(name="城市编号")
    @Column(name = "AREACODE", length = 100)
    private String areaCode;

    @Header(name="收货地址")
    @Column(name="shipping_addresss")
    private String shippingAddress;

    @Header(name="发票内容")
    @Column(name="invoice_content")
    private String invoiceContent;

}
