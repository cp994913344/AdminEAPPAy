package com.cnpc.packmall.SKU.dto;/**
 * @author cuipeng
 * @create 2018-08-31 11:46
 **/

/**
 *
 * @author cuipeng
 * @create 2018-08-31 11:46
 **/
public class SkuIdDTO {

    /**
     * skuId
     */
    private String skuId;

    /**
     * QUALITY
     */
    private String qualityId;
    /**
     * COLOR
     */
    private String colorId;
    /**
     * TYPE
     */
    private String typeId;

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public void setQualityId(String qualityId) {
        this.qualityId = qualityId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getSkuId() {

        return skuId;
    }

    public String getQualityId() {
        return qualityId;
    }

    public String getColorId() {
        return colorId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeId() {

        return typeId;
    }
}
