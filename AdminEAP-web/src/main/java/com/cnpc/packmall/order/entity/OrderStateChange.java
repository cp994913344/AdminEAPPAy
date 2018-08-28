package com.cnpc.packmall.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;
import com.cnpc.framework.base.entity.Dict;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 订单状态流转记录
 * @author 16692
 *
 */
@Entity
@Table(name = "tbl_packmall_order_order_state_change")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class OrderStateChange extends BaseEntity {




	/**
	 * 
	 */
	private static final long serialVersionUID = 2892185927627465086L;

	@Header(name="订单id")
    @Column(name = "order_id", length = 100)
    private String orderId;
    
	@Header(name="state")
    @Column(name = "state", length = 100)
    private String state;
    
	@Header(name="历史状态")
    @Column(name = "history_state", length = 100)
    private String historyState;
    
	@Header(name="描述")
    @Column(name = "remark", length = 100)
    private String remark;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getHistoryState() {
		return historyState;
	}

	public void setHistoryState(String historyState) {
		this.historyState = historyState;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    

}

