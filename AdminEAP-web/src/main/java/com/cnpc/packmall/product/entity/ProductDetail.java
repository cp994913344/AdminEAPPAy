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
@Table(name="TB_PACKMALL_PRODUCT_DETAIL")
public class ProductDetail extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = -1219521675403658571L;

	@Header(name="商品id")
    @Column(name="product_id",length = 36)
    private String productId;

    /**
     * BANNERIMG banner图片
     * DETAILIMG 详情图片
     * TYPEIMG  规格图片
     * COLOR 颜色
     *  TYPE 规格
     * QUALITY 质量
     */
    @Header(name="详情类型")
    @Column(name="detail_type",length = 20)
    private String detailType;

    @Header(name="详情表id")
    @Column(name="detail_id")
    private String detailId;

    @Header(name="详情表value")
    @Column(name="detail_val")
    private String detailVal;

    @Header(name="顺序")
    @Column(name="detail_seq")
    private Integer detailSeq;

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public void setDetailVal(String detailVal) {
        this.detailVal = detailVal;
    }

    public void setDetailSeq(Integer detailSeq) {
        this.detailSeq = detailSeq;
    }

    public String getProductId() {

        return productId;
    }

    public String getDetailType() {
        return detailType;
    }

    public String getDetailId() {
        return detailId;
    }

    public String getDetailVal() {
        return detailVal;
    }

    public Integer getDetailSeq() {
        return detailSeq;
    }
}
