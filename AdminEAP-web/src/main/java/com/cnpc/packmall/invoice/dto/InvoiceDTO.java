package com.cnpc.packmall.invoice.dto;/**
 * @author cuipeng
 * @create 2018-09-03 19:51
 **/

import com.cnpc.framework.base.pojo.BaseDTO;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author cuipeng
 * @create 2018-09-03 19:51
 **/
public class InvoiceDTO extends BaseDTO {

    private String  type;
    private Integer invoiceStatus;

    private BigDecimal invoicePrice;
    public void setType(String type) {
        this.type = type;
    }

    public void setInvoiceStatus(Integer invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getType() {

        return type;
    }

    public Integer getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoicePrice(BigDecimal invoicePrice) {
        this.invoicePrice = invoicePrice;
    }

    public BigDecimal getInvoicePrice() {

        return invoicePrice;
    }
}
