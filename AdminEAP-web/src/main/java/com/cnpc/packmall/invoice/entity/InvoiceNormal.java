package com.cnpc.packmall.invoice.entity;/**
 * @author cuipeng
 * @create 2018-08-15 19:25
 **/

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;
import com.cnpc.framework.base.entity.SysArea;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 *发票管理  增普
 * @author cuipeng
 * @create 2018-08-15 19:25
 **/
@Entity
@Table(name="TB_PACKMALL_INVOICE_NORMAL")
public class InvoiceNormal extends BaseEntity{

    @Header(name="openID")
    @Column(name="open_id",length = 60)
    private String openId;

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

    @Header(name="收货地址id")
    @Column(name = "shipping_address_id",length = 36)
    private String shippingAddressId;

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
    private String invoiceMark;

    @Header(name="发票号")
    @Column(name="invoice_no")
    private String invoiceNo;

    @Header(name="快递单号")
    @Column(name="courier_no")
    private String courierNo;

    /**
     *  1 未邮寄 2 已邮寄
     */
    @Header(name="发票状态")
    @Column(name="invoice_status",length = 4 )
    private Integer invoiceStatus;

    /**
     *  1 未付款 2 已付款
     */
    @Header(name="付款状态")
    @Column(name="pay_status",length = 4 )
    private String  payStatus;

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


    public void setInvoiceContent(Integer invoiceContent) {
        this.invoiceContent = invoiceContent;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Integer getInvoiceContent() {
        return invoiceContent;
    }

    public String getRemark() {
        return remark;
    }

    public String getCourierNo() {
        return courierNo;
    }

    public void setInvoiceStatus(Integer invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public Integer getInvoiceStatus() {

        return invoiceStatus;
    }

    public void setInvoiceMark(String invoiceMark) {
        this.invoiceMark = invoiceMark;
    }

    public String getInvoiceMark() {

        return invoiceMark;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceNo() {

        return invoiceNo;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public void setShippingAddressId(String shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public String getShippingAddressId() {

        return shippingAddressId;
    }

    public String getOpenId() {

        return openId;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }
}
