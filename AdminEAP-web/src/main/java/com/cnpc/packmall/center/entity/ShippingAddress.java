package com.cnpc.packmall.center.entity;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 收货地址
 * @author cuipeng
 * @create 2018-08-15 10:38
 **/
@Entity
@Table(name="TB_PACKMAIL_SHIPPING_ADDRESS")
public class ShippingAddress extends BaseEntity{

    @Header(name="客户id")
    @Column(name="client_id" ,length = 36)
    private String clientId;

    @Header(name="收货人")
    @Column(name="shipping_name")
    private String shippingName;

    @Header(name="城市编号")
    @Column(name = "AREACODE", length = 100)
    private String areaCode;

    @Header(name="收货地址")
    @Column(name="shipping_addresss")
    private String shippingAddress;

    @Header(name="联系方式")
    @Column(name="shipping_phone")
    private String shippingPhone;

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
    }

    public String getShippingName() {

        return shippingName;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {

        return clientId;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaCode() {

        return areaCode;
    }
}
