package com.cnpc.packmall.order.pojo.dto;

import java.math.BigDecimal;

import com.cnpc.framework.base.pojo.BaseDTO;

/**
 * 订单详情展示实体
 * @author 16692
 *
 */
public class OrderDetailDTO extends BaseDTO {


	/**
	 * 订单id
	 */
    private String orderId;
    
    /*
     * 商品id
     */
    private String productId;
    
    /**
     * KUid
     */
    private String skuId;
    
    /**
     * SKU信息
     */
    private String skumsg;
	/**
	 * 尺寸
	 */
	private String size;
	/**
	 * 规格
	 */
	private String specification;
	/**
	 * 颜色
	 */
	private String color;
	/**
	 * 质量
	 */
	private String quality;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品数量
     */
    private Integer number;
	/**
	 * 商品单价
	 */
    private BigDecimal price;
    
	/**
	 * 商品单价
	 */
    private String priceId;
    /**
     * 总价格
     */
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

	public String getPriceId() {
		return priceId;
	}

	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}




}

