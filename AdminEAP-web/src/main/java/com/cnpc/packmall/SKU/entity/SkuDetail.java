package com.cnpc.packmall.SKU.entity;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 *  商品
 * @author cuipeng
 * @create 2018-08-15 19:25
 **/
@Entity
@Table(name="TB_PACKMALL_SKU_DETAIL")
public class SkuDetail extends BaseEntity{

    @Header(name="sku id")
    @Column(name="sku_id",length = 36)
    private String skuId;

    /**
     * COLOR 颜色
     *  TYPE 规格
     * QUALITY 质量
     *  PRICE 价格
     */
    @Header(name="详情类型")
    @Column(name="detail_type",length = 20)
    private String detailType;

    @Header(name="详情表id")
    @Column(name="detail_id")
    private String detailId;

    @Header(name="详情name")
    @Column(name="detail_name")
    private String detailName;

    @Header(name="详情表value")
    @Column(name="detail_val")
    private BigDecimal detailVal;

    @Header(name="顺序")
    @Column(name="detail_seq")
    private Integer detailSeq;


    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }


    public void setDetailSeq(Integer detailSeq) {
        this.detailSeq = detailSeq;
    }

    public String getDetailType() {
        return detailType;
    }

    public String getDetailId() {
        return detailId;
    }


    public Integer getDetailSeq() {
        return detailSeq;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuId() {

        return skuId;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public String getDetailName() {

        return detailName;
    }

    public void setDetailVal(BigDecimal detailVal) {
        this.detailVal = detailVal;
    }

    public BigDecimal getDetailVal() {

        return detailVal;
    }
}
