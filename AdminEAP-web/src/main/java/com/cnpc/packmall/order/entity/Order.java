package com.cnpc.packmall.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;
import com.cnpc.framework.base.entity.Dict;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 订单信息
 * @author 16692
 *
 */
@Entity
@Table(name = "tbl_packmall_order_order")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class Order extends BaseEntity {


    /**
	 * 
	 */
	private static final long serialVersionUID = -5251144036134846307L;

	/**
     * 编号
     */
	@Header(name="CODE")
    @Column(name = "CODE", length = 100)
    private String code;

    
    /**
     * 用户id
     */
	@Header(name="用户id")
    @Column(name = "open_id", length = 100)
    private String openId;
    
    
    /**
     * 用户姓名
     */
	@Header(name="用户姓名")
    @Column(name = "user_name", length = 100)
    private String userName;
    
    
    /**
     * 收货地址id
     */
	@Header(name="收货地址")
    @Column(name = "address_id", length = 100)
    private String addressId;
    
    @Header(name="联系方式")
    @Column(name = "phone", length = 100)
    private String phone;
    
    @Header(name="收货人")
    @Column(name = "contacts", length = 100)
    private String contacts;

    /**
     * 送货时间
     */
    @Header(name="送货时间",joinClass=Dict.class,dataSource="DELIVERYTIME")
    @Column(name = "delivery_time", length = 100)
    private String deliverytime;

    /**
     * 是否周末送货
     */
    @Header(name="是否周末送货")
    @Column(name = "weekend", length = 100)
    private Boolean weekend;
    
    @Header(name="SKU")
    @Column(name = "SKU", length = 100)
    private String sku;

    /**
     * 备注 留言
     */
    @Header(name="备注")
    @Column(name = "remarks", length = 100)
    private String remarks;

    /**
     * 运费
     */
    @Header(name="运费")
    @Column(name = "freight", length = 100)
    private BigDecimal freight;

    /**
     * 总价格
     */
    @Header(name="总价格")
    @Column(name = "total_price", length = 100)
    private BigDecimal totalPrice;
    

    @Header(name="商品种类")
    @Column(name = "product_category", length = 100)
    private Integer productCategory;
    

    @Header(name="商品缩略图")
    @Column(name = "product_img_id", length = 100)
    private String productImgId;
    

    @Header(name="商品信息概述")
    @Column(name = "product_msg", length = 100)
    private String productMsg;
    
    /**
     * 支付方式
     */
    @Header(name="支付方式")
    @Column(name = "pay_method", length = 100)
    private String payMethod;
    
    /**
     * 支付id
     */
    @Header(name="支付ID")
    @Column(name = "pay_id", length = 100)
    private String payId;

    /**
     * 状态1 未支付 2 待送货 3 待签收 4 已完结 -1 订单关闭
     */
    @Header(name="状态")
    @Column(name = "state", length = 100)
    private String state;
    
    @Header(name="开票状态")
    @Column(name = "whether_state", length = 100)
    private String whetherState;
    /**
     * 0未开票 1已开票
     */
    @Header(name="开票ID")
    @Column(name = "whether_id", length = 100)
    private String whetherId;
    
    

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getDeliverytime() {
		return deliverytime;
	}

	public void setDeliverytime(String deliverytime) {
		this.deliverytime = deliverytime;
	}

	public Boolean getWeekend() {
		return weekend;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public void setWeekend(Boolean weekend) {
		this.weekend = weekend;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}


	public String getProductImgId() {
		return productImgId;
	}

	public void setProductImgId(String productImgId) {
		this.productImgId = productImgId;
	}

	public String getProductMsg() {
		return productMsg;
	}

	public void setProductMsg(String productMsg) {
		this.productMsg = productMsg;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}


	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}


	public String getWhetherState() {
		return whetherState;
	}

	public void setWhetherState(String whetherState) {
		this.whetherState = whetherState;
	}

	public String getWhetherId() {
		return whetherId;
	}

	public void setWhetherId(String whetherId) {
		this.whetherId = whetherId;
	}

	public Integer getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(Integer productCategory) {
		this.productCategory = productCategory;
	}


}

