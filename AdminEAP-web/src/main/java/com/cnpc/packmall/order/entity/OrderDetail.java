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
 * 订单详情
 * @author 16692
 *
 */
@Entity
@Table(name = "tbl_packmall_order_order_detail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class OrderDetail extends BaseEntity {



	/**
	 * 
	 */
	private static final long serialVersionUID = -6800496706953132386L;


	@Header(name="订单id")
    @Column(name = "order_id", length = 100)
    private String orderId;
    
	@Header(name="商品id")
    @Column(name = "product_id", length = 100)
    private String productId;
    
	@Header(name="SKUid")
    @Column(name = "sku_id", length = 100)
    private String skuId;
    
	@Header(name="SKU信息")
    @Column(name = "sku_msg", length = 100)
    private String skumsg;
	
	@Header(name="尺寸")
	@Column(name = "size", length = 100)
	private String size;
	
	@Header(name="规格")
	@Column(name = "specification", length = 100)
	private String specification;
	
	@Header(name="颜色")
	@Column(name = "color", length = 100)
	private String color;
	
	@Header(name="质量")
	@Column(name = "quality", length = 100)
	private String quality;
    
    @Header(name="商品名称")
    @Column(name = "product_name", length = 100)
    private String productName;
    
    @Header(name="商品数量")
    @Column(name = "number", length = 100)
    private Integer number;

    @Header(name="商品单价")
    @Column(name = "price", length = 100)
    private BigDecimal price;
    
    @Header(name="总价格")
    @Column(name = "total_price", length = 100)
    private BigDecimal totalPrice;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getSkumsg() {
		return skumsg;
	}

	public void setSkumsg(String skumsg) {
		this.skumsg = skumsg;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}




}

