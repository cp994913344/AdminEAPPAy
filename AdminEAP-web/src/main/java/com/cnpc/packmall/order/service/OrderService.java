package com.cnpc.packmall.order.service;

import java.util.List;
import java.util.Map;

import com.cnpc.framework.base.service.BaseService;
import com.cnpc.packmall.order.entity.OrderStateChange;
import com.cnpc.packmall.order.pojo.dto.OrderDTO;

/**
* 订单管理服务接口
* @author WY
* 2018-08-16 16:52:48由代码生成器自动生成
*/
public interface OrderService extends BaseService {
	/**
	 * 订单集合
	 * @param openid
	 * @param param
	 * @return
	 */
	List<OrderDTO> packMallgetList(String openid, Map<String, String> param);
	/**
	 * 小程序下单
	 * @param openid
	 * @param orderDTO
	 * @return
	 */
	Map<String, String> savePackMallOrder(String openid, OrderDTO orderDTO);
	
	/**
	 * 更改订单状态
	 * @param openid
	 * @param orderId
	 * @param state
	 */
	void doConfirm(String openid, String orderId,String state);
	/**
	 * 根据订单编号查询订单流转信息
	 * @param orderId
	 * @return
	 */
	List<OrderStateChange> findOrderChangeByOrderId(String orderId);


}
