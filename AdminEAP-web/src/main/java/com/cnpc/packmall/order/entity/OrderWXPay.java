package com.cnpc.packmall.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 订单状态流转记录
 * @author 16692
 *
 */
@Entity
@Table(name = "tbl_packmall_order_order_wxpay")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class OrderWXPay extends BaseEntity {





	/**
	 * 
	 */
	private static final long serialVersionUID = 3694437632333636839L;

	@Header(name="订单id")
    @Column(name = "order_id", length = 100)
    private String orderId;
    
	@Header(name="微信预付费Id")
    @Column(name = "prepay_id", length = 100)
    private String prepayId;
    
	@Header(name="签名")
    @Column(name = "sign", length = 100)
    private String sign;
    
	@Header(name="描述")
    @Column(name = "remark", length = 100)
    private String remark;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


}

