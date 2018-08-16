package com.cnpc.packmall.invoice.entity;/**
 * @author cuipeng
 * @create 2018-08-15 19:25
 **/

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 *发票管理  增普
 * @author cuipeng
 * @create 2018-08-15 19:25
 **/
@Entity
@Table(name="TB_PACKMAIL_INVOICE_NORMAL")
public class InvoiceNormal extends BaseEntity{

    @Header(name="invoice编码")
    @Column(name="invoice_code",length = 20)
    private String invoiceCode;

    /**
     * 1企业单位 2 个人
     */
    @Header(name="抬头类型")
    @Column(name="taxpayer_type",length = 4)
    private Integer taxpayerType;

    @Header(name="发票抬头")
    @Column(name="invoice_taxpayer",length = 100)
    private String invoiceTaxpayer;

    @Header(name="纳税人识别号")
    @Column(name="invoice_taxpayer_num",length = 18)
    private String invoiceTaxpayerNum;

    @Header(name="发票金额")
    @Column(name = "invoice_price")
    private BigDecimal invoicePrice;

    @Header(name="开票地址")
    @Column(name="invoice_address",length = 100)
    private String invoiceAddress;

    @Header(name="开票电话")
    @Column(name="invoice_phone",length = 11)
    private String invoicePhone;

    @Header(name="收件人")
    @Column(name = "recipient",length = 16)
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

    /**
     *  1 商品明细 2 商品类别
     */
    @Header(name="发票内容")
    @Column(name="invoice_content",length = 4 )
    private Integer invoiceContent;

    @Header(name="备注")
    @Column(name="remark",length = 120)
    private String remark;

   @Header(name="发票代码")
   @Column(name="invoice_mark")
    private String invoice_mark;

    @Header(name="发票号")
    @Column(name="invoice_num")
    private String invoiceNum;

    @Header(name="快递单号")
    @Column(name="courier_no")
    private String courierNo;

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public void setTaxpayerType(Integer taxpayerType) {
        this.taxpayerType = taxpayerType;
    }

    public void setInvoiceTaxpayer(String invoiceTaxpayer) {
        this.invoiceTaxpayer = invoiceTaxpayer;
    }

    public void setInvoiceTaxpayerNum(String invoiceTaxpayerNum) {
        this.invoiceTaxpayerNum = invoiceTaxpayerNum;
    }

    public void setInvoicePrice(BigDecimal invoicePrice) {
        this.invoicePrice = invoicePrice;
    }

    public void setInvoiceAddress(String invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    public void setInvoicePhone(String invoicePhone) {
        this.invoicePhone = invoicePhone;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setInvoiceContent(Integer invoiceContent) {
        this.invoiceContent = invoiceContent;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setInvoice_mark(String invoice_mark) {
        this.invoice_mark = invoice_mark;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public void setCourierNo(String courierNo) {
        this.courierNo = courierNo;
    }

    public String getInvoiceCode() {

        return invoiceCode;
    }

    public Integer getTaxpayerType() {
        return taxpayerType;
    }

    public String getInvoiceTaxpayer() {
        return invoiceTaxpayer;
    }

    public String getInvoiceTaxpayerNum() {
        return invoiceTaxpayerNum;
    }

    public BigDecimal getInvoicePrice() {
        return invoicePrice;
    }

    public String getInvoiceAddress() {
        return invoiceAddress;
    }

    public String getInvoicePhone() {
        return invoicePhone;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public Integer getInvoiceContent() {
        return invoiceContent;
    }

    public String getRemark() {
        return remark;
    }

    public String getInvoice_mark() {
        return invoice_mark;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public String getCourierNo() {
        return courierNo;
    }
}
