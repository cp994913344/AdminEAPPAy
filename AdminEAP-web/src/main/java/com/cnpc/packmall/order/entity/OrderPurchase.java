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
 * 进货单
 * @author 16692
 *
 */
@Entity
@Table(name = "tbl_packmall_order_order_purchase")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class OrderPurchase extends BaseEntity {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7583483357919817032L;

	@Header(name="用户openId")
    @Column(name = "openId", length = 100)
    private String openId;
	
	@Header(name="商品id")
    @Column(name = "product_id", length = 100)
    private String productId;
	
	@Header(name="商品缩略图id")
    @Column(name = "product_img_id", length = 100)
    private String productImgId;
    
	@Header(name="SKUid")
    @Column(name = "sku_id", length = 100)
    private String skuId;
    
	@Header(name="尺寸")
	@Column(name = "size", length = 100)
	private String size;
	
	@Header(name="规格")
	@Column(name = "specification", length = 100)
	private String specification;
	
	@Header(name="规格ID")
	@Column(name = "specification_id", length = 100)
	private String specificationId;
	
	@Header(name="颜色")
	@Column(name = "color", length = 100)
	private String color;
	
	@Header(name="颜色ID")
	@Column(name = "color_id", length = 100)
	private String colorId;
	
	@Header(name="质量")
	@Column(name = "quality", length = 100)
	private String quality;
	
	@Header(name="质量ID")
	@Column(name = "quality_id", length = 100)
	private String qualityId;
    
    @Header(name="商品名称")
    @Column(name = "product_name", length = 100)
    private String productName;
    
    @Header(name="商品数量")
    @Column(name = "number", length = 100)
    private Integer number;

    @Header(name="商品单价")
    @Column(name = "price", length = 100)
    private BigDecimal price;
    
    @Header(name="商品单价Id")
    @Column(name = "price_id", length = 100)
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

	public void setColor(String color) {
		this.color = color;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getProductImgId() {
		return productImgId;
	}

	public void setProductImgId(String productImgId) {
		this.productImgId = productImgId;
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

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}




}

