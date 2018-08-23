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
@Table(name="TB_PACKMAlL_SKU")
public class Sku extends BaseEntity {

    @Header(name="商品id")
    @Column(name="product_id",length = 36)
    private String productId;

    @Header(name="商品名称")
    @Column(name="product_name",length = 36)
    private String productName;

    @Header(name="sku编码")
    @Column(name="sku_code",length = 20)
    private String skuCode;

    @Header(name="sku型号")
    @Column(name="sku_model",length = 50)
    private String skuModel;

    @Header(name="sku尺寸长")
    @Column(name="sku_size_length",length = 50)
    private String skuSizeLength;

    @Header(name="sku尺寸宽")
    @Column(name="sku_size_wide",length = 50)
    private String skuSizeWide;

    @Header(name="sku尺寸高")
    @Column(name="sku_size_high",length = 50)
    private String skuSizeHigh;

    /**
     *  1 上架  2 下架
     */
    @Header(name="sku状态")
    @Column(name = "sku_status")
    private Integer skuStatus;

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public void setSkuStatus(Integer skuStatus) {
        this.skuStatus = skuStatus;
    }


    public String getSkuCode() {

        return skuCode;
    }

    public Integer getSkuStatus() {
        return skuStatus;
    }


    public void setSkuModel(String skuModel) {
        this.skuModel = skuModel;
    }

    public String getSkuModel() {

        return skuModel;
    }

    public void setSkuSizeLength(String skuSizeLength) {
        this.skuSizeLength = skuSizeLength;
    }

    public void setSkuSizeWide(String skuSizeWide) {
        this.skuSizeWide = skuSizeWide;
    }

    public void setSkuSizeHigh(String skuSizeHigh) {
        this.skuSizeHigh = skuSizeHigh;
    }

    public String getSkuSizeLength() {

        return skuSizeLength;
    }

    public String getSkuSizeWide() {
        return skuSizeWide;
    }

    public String getSkuSizeHigh() {
        return skuSizeHigh;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {

        return productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {

        return productName;
    }
}
