package com.cnpc.packmall.product.entity;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *  商品
 * @author cuipeng
 * @create 2018-08-15 19:25
 **/
@Entity
@Table(name="TB_PACKMAIL_PRODUCT")
public class Product extends BaseEntity{

    @Header(name="商品编码")
    @Column(name="product_code",length = 20)
    private String productCode;

    @Header(name="商品名称")
    @Column(name="product_name",length = 50)
    private String productName;

    @Header(name="banner图片id")
    @Column(name="banner_image_ids")
    private String bannerImageIds;

    @Header(name="detail图片id")
    @Column(name="detail_image_ids")
    private String detailImageIds;

    @Header(name="type图片id")
    @Column(name="type_image_ids")
    private String typeImageIds;

    @Header(name="颜色")
    @Column(name="product_color")
    private String productColor;

    @Header(name="规格")
    @Column(name="product_type")
    private String productType;

    @Header(name="质量")
    @Column(name="product_quality")
    private String productQuality;

    @Header(name="商品状态")
    @Column(name = "prodcut_status")
    private Integer prodcutStatus;

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setBannerImageIds(String bannerImageIds) {
        this.bannerImageIds = bannerImageIds;
    }

    public void setDetailImageIds(String detailImageIds) {
        this.detailImageIds = detailImageIds;
    }

    public void setTypeImageIds(String typeImageIds) {
        this.typeImageIds = typeImageIds;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setProductQuality(String productQuality) {
        this.productQuality = productQuality;
    }

    public void setProdcutStatus(Integer prodcutStatus) {
        this.prodcutStatus = prodcutStatus;
    }

    public String getProductName() {

        return productName;
    }

    public String getBannerImageIds() {
        return bannerImageIds;
    }

    public String getDetailImageIds() {
        return detailImageIds;
    }

    public String getTypeImageIds() {
        return typeImageIds;
    }

    public String getProductColor() {
        return productColor;
    }

    public String getProductType() {
        return productType;
    }

    public String getProductQuality() {
        return productQuality;
    }

    public Integer getProdcutStatus() {
        return prodcutStatus;
    }
}
