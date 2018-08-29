package com.cnpc.packmall.order.pojo.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.cnpc.framework.base.pojo.BaseDTO;
import com.cnpc.packmall.order.entity.Order;

/**
 * 订单展示实体
 * @author 16692
 *
 */
public class OrderDTO extends BaseDTO {



	/**
     * 编号
     */
    private String code;

    
    /**
     * 用户id
     */
    private String openId;
    
    
    /**
     * 用户姓名
     */
    private String userName;
    
    
    /**
     * 收货地址id
     */
    private String addressId;
    
    /**
     * 联系方式
     */
    private String phone;
    
    /**
     * 收货人
     */
    private String contacts;

    /**
     * 送货时间
     */
    private String deliverytime;

    /**
     * 是否周末送货
     */
    private Boolean weekend;
    
    /**
     * SKU
     */
    private String sku;

    /**
     * 备注 留言
     */
    private String remarks;

    /**
     * 运费
     */
    private BigDecimal freight;

    /**
     * 总价格
     */
    private BigDecimal totalPrice;
    
    /**
     * 总价格
     */
    private BigDecimal payMethod;

    /**
     * 状态
     */
    private String state;
    
    
    private List<OrderDetailDTO> orderDetailDTOs;
    
    private Map<String,List<OrderDetailDTO>> orderDetailDTOMaps;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getDeliverytime() {
		return deliverytime;
	}

	public void setDeliverytime(String deliverytime) {
		this.deliverytime = deliverytime;
	}

	public Boolean getWeekend() {
		return weekend;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public void setWeekend(Boolean weekend) {
		this.weekend = weekend;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public BigDecimal getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(BigDecimal payMethod) {
		this.payMethod = payMethod;
	}

	public List<OrderDetailDTO> getOrderDetailDTOs() {
		return orderDetailDTOs;
	}

	public Map<String, List<OrderDetailDTO>> getOrderDetailDTOMaps() {
		return orderDetailDTOMaps;
	}

	public void setOrderDetailDTOMaps(Map<String, List<OrderDetailDTO>> orderDetailDTOMaps) {
		this.orderDetailDTOMaps = orderDetailDTOMaps;
	}

	public void setOrderDetailDTOs(List<OrderDetailDTO> orderDetailDTOs) {
		this.orderDetailDTOs = orderDetailDTOs;
	}

	public Order toEntity(OrderDTO orderDTO) {
		Order order = new Order();
		order.setAddressId(orderDTO.getAddressId());
		order.setPhone(orderDTO.getPhone());
		order.setContacts(orderDTO.getContacts());
		order.setDeliverytime(orderDTO.getDeliverytime());
		order.setWeekend(orderDTO.getWeekend());
		order.setRemarks(orderDTO.getRemarks());
		order.setFreight(orderDTO.getFreight());
		order.setPayMethod("微信支付");
		return order;
	}


}

