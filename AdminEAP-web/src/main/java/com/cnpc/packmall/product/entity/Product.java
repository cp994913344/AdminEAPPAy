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
@Table(name="TB_PACKMALL_PRODUCT")
public class Product extends BaseEntity {

    @Header(name = "商品编码")
    @Column(name = "product_code", length = 20)
    private String productCode;

    @Header(name = "商品名称")
    @Column(name = "product_name", length = 50)
    private String productName;

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {

        return productCode;
    }

    public String getProductName() {
        return productName;
    }
}