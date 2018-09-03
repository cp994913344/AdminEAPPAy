package com.cnpc.packmall.order.constant;

import com.cnpc.packmall.order.entity.Order;

public enum OrderEnum {
	
	UNPAID("1","订单已提交，等待付款"),
	ALREADY_PAID("2","订单付款成功"),
	WAIT_FOR_DELIVERY("",""),
	UNSIGN("",""),
	COMPLETED("",""),
	ORDER_CLOSURE("","");
	/*两个属性*/
    private String code;
    private String msg;
   /*构造函数*/
    private OrderEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
	public static OrderEnum codeOf(String code) {
		if (code != null) {
			for (OrderEnum type : OrderEnum.values()) {
				if (type.getCode().equals(code)) {
					return type;
				}
			}
		}
		return null;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
