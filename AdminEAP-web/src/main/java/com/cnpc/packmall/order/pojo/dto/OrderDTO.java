package com.cnpc.packmall.order.pojo.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.pojo.BaseDTO;
import com.cnpc.packmall.order.entity.Order;

/**
 * 订单展示实体
 * @author 16692
 *
 */
public class OrderDTO extends BaseDTO {


   private String id;

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
     * 支付方式
     */
    private String payMethod;

    /**
     * 状态
     */
    private String state;
    
    
    /**
     * 商品种类
     */
    private Integer productCategory;
    
    /**
     * 商品缩略图
     */
    private String productImgId;
    
    /**
     * 商品信息概述
     */
    private String productMsg;


	private String whetherState;
	/**
	 * 0未开票 1已开票
	 */
	private String whetherId;
    
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

	public void setProductCategory(Integer productCategory) {
		this.productCategory = productCategory;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public String getProductImgId() {
		return productImgId;
	}

	public void setProductImgId(String productImgId) {
		this.productImgId = productImgId;
	}

	public String getProductMsg() {
		return productMsg;
	}

	public void setProductMsg(String productMsg) {
		this.productMsg = productMsg;
	}

	public void setPayMethod(String payMethod) {
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
		order.setWhetherState(orderDTO.getWhetherState());
		order.setWhetherId(orderDTO.getWhetherId());
		order.setId(orderDTO.getId());
		order.setPayMethod("微信支付");
		return order;
	}

	public Integer getProductCategory() {
		return productCategory;
	}


	public void setWhetherState(String whetherState) {
		this.whetherState = whetherState;
	}

	public void setWhetherId(String whetherId) {
		this.whetherId = whetherId;
	}

	public String getWhetherState() {

		return whetherState;
	}

	public String getWhetherId() {
		return whetherId;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {

		return id;
	}
}

