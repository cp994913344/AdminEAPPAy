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
@Table(name="TB_PACKMALL_CLIENT")
public class Client extends BaseEntity{

    @Header(name="openID")
    @Column(name="open_id",length = 60)
    private String openId;

    @Header(name="编码")
    @Column(name="CLIENT_CODE" ,length = 20)
    private String clientCode;

    /**
     *  1 个人 2 企业
     */
    @Header(name="姓名/企业名称")
    @Column(name="CLIENT_NAME" ,length = 100)
    private String clientName;

    @Header(name="性质")
    @Column(name="CLIENT_TYPE" ,length = 20)
    private String clientType;

    @Header(name="联系方式")
    @Column(name="CLIENT_PHONE" ,length = 11)
    private String clientPhone;

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
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

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientType() {

        return clientType;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
