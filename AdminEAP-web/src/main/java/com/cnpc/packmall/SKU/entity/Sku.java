package com.cnpc.packmall.SKU.entity;/**
 * @author cuipeng
 * @create 2018-08-15 19:03
 **/

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * sku
 * @author cuipeng
 * @create 2018-08-15 19:03
 **/
@Entity
@Table(name="TB_PACKMAIL_SKU")
public class Sku extends BaseEntity {

    @Header(name="sku编码")
    @Column(name="sku_code",length = 20)
    private String skuCode;

    @Header(name="sku型号")
    @Column(name="sku_model",length = 50)
    private String skuModel;

    @Header(name="sku尺寸")
    @Column(name="sku_size",length = 50)
    private String skuSize;

    @Header(name="颜色")
    @Column(name="sku_color")
    private String skuColor;

    @Header(name="规格")
    @Column(name="sku_type")
    private String skuType;

    @Header(name="质量")
    @Column(name="sku_quality")
    private String skuQuality;

    @Header(name="价格")
    @Column(name = "sku_price")
    private BigDecimal skuPrice;

    @Header(name="sku状态")
    @Column(name = "sku_status")
    private Integer skuStatus;

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public void setSkuType(String skuType) {
        this.skuType = skuType;
    }

    public void setSkuQuality(String skuQuality) {
        this.skuQuality = skuQuality;
    }

    public void setSkuPrice(BigDecimal skuPrice) {
        this.skuPrice = skuPrice;
    }

    public void setSkuStatus(Integer skuStatus) {
        this.skuStatus = skuStatus;
    }

    public void setSkuSize(String skuSize) {
        this.skuSize = skuSize;
    }

    public void setSkuColor(String skuColor) {
        this.skuColor = skuColor;
    }

    public String getSkuCode() {

        return skuCode;
    }

    public String getSkuType() {
        return skuType;
    }

    public String getSkuQuality() {
        return skuQuality;
    }

    public BigDecimal getSkuPrice() {
        return skuPrice;
    }

    public Integer getSkuStatus() {
        return skuStatus;
    }

    public String getSkuSize() {
        return skuSize;
    }

    public String getSkuColor() {
        return skuColor;
    }

    public void setSkuModel(String skuModel) {
        this.skuModel = skuModel;
    }

    public String getSkuModel() {

        return skuModel;
    }
}
