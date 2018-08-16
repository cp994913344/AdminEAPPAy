package com.cnpc.packmall.center.entity;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;
import com.cnpc.framework.base.entity.SysFile;

import javax.persistence.*;

/**
 * 客户
 * @author cuipeng
 * @create 2018-08-15 10:38
 **/
@Entity
@Table(name="TB_PACKMAIL_CLIENT")
public class Client extends BaseEntity{
    @Header(name="编码")
    @Column(name="CLIENT_CODE" ,length = 20)
    private String clientCode;

    @Header(name="姓名/企业名称")
    @Column(name="CLIENT_NAME" ,length = 50)
    private String clientName;

    @Header(name="性质")
    @Column(name="CLIENT_TYPE" ,length = 20)
    private String clientType;

    @Header(name="联系方式")
    @Column(name="CLIENT_PHONE" ,length = 11)
    private String clientPhone;

    @Header(name="状态")
    @Column(name="ClIENT_STATUS",length = 4)
    private Integer clientStatus;

    @Header(name="头像url")
    @Column(name="HEAD_IMAGE_id" ,length =36)
    private  String headImageId;

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public void setClientStatus(Integer clientStatus) {
        this.clientStatus = clientStatus;
    }


    public String getClientCode() {

        return clientCode;
    }

    public String getClientName() {
        return clientName;
    }


    public String getClientPhone() {
        return clientPhone;
    }

    public Integer getClientStatus() {
        return clientStatus;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public void setHeadImageId(String headImageId) {
        this.headImageId = headImageId;
    }

    public String getClientType() {

        return clientType;
    }

    public String getHeadImageId() {
        return headImageId;
    }
}
