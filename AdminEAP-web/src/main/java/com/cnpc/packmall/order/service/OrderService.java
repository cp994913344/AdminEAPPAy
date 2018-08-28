package com.cnpc.packmall.order.service;

import java.util.List;
import java.util.Map;

import com.cnpc.framework.base.service.BaseService;
import com.cnpc.packmall.order.entity.Order;
import com.cnpc.packmall.order.pojo.dto.OrderDTO;

/**
* 订单管理服务接口
* @author WY
* 2018-08-16 16:52:48由代码生成器自动生成
*/
public interface OrderService extends BaseService {

	List<OrderDTO> packMallgetList(String openid, Map<String, String> param);

	String savePackMallOrder(String openid, OrderDTO orderDTO);

}
