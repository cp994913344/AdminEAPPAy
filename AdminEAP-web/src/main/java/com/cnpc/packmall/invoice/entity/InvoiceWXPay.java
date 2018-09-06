package com.cnpc.packmall.invoice.entity;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 订单支付信息表
 * @author 16692
 *
 */
@Entity
@Table(name = "TB_PACKMALL_INVOICE_WXPAY")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class InvoiceWXPay extends BaseEntity {

	private static final long serialVersionUID = 3694437632333636839L;

	@Header(name="发票id")
    @Column(name = "invoice_id", length = 100)
    private String invoiceId;
	/**
	 *1 专用 2 普通
	 */
	@Header(name="发票类型")
	@Column(name = "invoice_type")
	private Integer  invoiceType;

	@Header(name="微信预付费Id")
    @Column(name = "prepay_id", length = 100)
    private String prepayId;
    
	@Header(name="签名")
    @Column(name = "sign", length = 100)
    private String sign;
    
	@Header(name="描述")
    @Column(name = "remark", length = 100)
    private String remark;

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getInvoiceId() {

		return invoiceId;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public String getSign() {
		return sign;
	}

	public String getRemark() {
		return remark;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public Integer getInvoiceType() {

		return invoiceType;
	}
}

