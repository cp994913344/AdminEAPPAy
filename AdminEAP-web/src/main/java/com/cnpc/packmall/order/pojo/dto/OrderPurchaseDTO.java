package com.cnpc.packmall.order.pojo.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;
import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.pojo.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 进货单
 * @author 16692
 *
 */
public class OrderPurchaseDTO extends BaseDTO {


	/**
	 * openId
	 */
    private String openId;
	/**
	 * 商品id
	 */
    private String productId;
    
    /**
     * 商品缩略图id
     */
    private String productImgId;
    
	/**
	 * SKUid
	 */
    private String skuId;
    
	/**
	 * 尺寸
	 */
	private String size;
	
	/**
	 * 规格
	 */
	private String specification;
	
	/**
	 * 规格ID
	 */
	private String specificationId;
	
	/**
	 * 颜色
	 */
	private String color;
	
	/**
	 * 颜色ID
	 */
	private String colorId;
	
	/**
	 * 质量
	 */
	private String quality;
	
	/**
	 * 质量ID
	 */
	private String qualityId;
    
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
     * 商品单价Id
     */
    private String priceId;
    

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

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getQuality() {
		return quality;
	}

	public String getProductImgId() {
		return productImgId;
	}

	public void setProductImgId(String productImgId) {
		this.productImgId = productImgId;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getSpecificationId() {
		return specificationId;
	}

	public void setSpecificationId(String specificationId) {
		this.specificationId = specificationId;
	}

	public String getColorId() {
		return colorId;
	}

	public void setColorId(String colorId) {
		this.colorId = colorId;
	}

	public String getQualityId() {
		return qualityId;
	}

	public void setQualityId(String qualityId) {
		this.qualityId = qualityId;
	}

	public String getPriceId() {
		return priceId;
	}

	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}

	public String getMapKey() {
		return productId+specificationId+colorId+qualityId+priceId;
	}

}

