package com.cnpc.packmall.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.batik.util.HaltingThread;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.ast.expr.SQLCaseExpr.Item;
import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.packmall.center.entity.Client;
import com.cnpc.packmall.center.service.ClientService;
import com.cnpc.packmall.order.entity.Order;
import com.cnpc.packmall.order.pojo.dto.OrderDTO;
import com.cnpc.packmall.order.pojo.dto.OrderDetailDTO;
import com.cnpc.packmall.order.service.OrderDetailService;
import com.cnpc.packmall.order.service.OrderService;
import com.mysql.fabric.xmlrpc.base.Array;

/**
* 订单管理服务实现
* @author WY
* 2018-08-16 16:52:48由代码生成器自动生成
*/
@Service("orderService")
public class OrderServiceImpl extends BaseServiceImpl implements OrderService {

	@Resource
	OrderDetailService orderDetailService;
	
	@Resource
	ClientService clientService;
	
	@Override
	public List<OrderDTO> packMallgetList(String openid, Map<String, String> param) {
		Map<String, Object> params = new HashMap<>();
		String hql = "select code from Order where 1=1 ";
		param.forEach((k,v) ->{params.put(k, v);});
		List<OrderDTO> orderDTOs = this.find(hql, params,OrderDTO.class);
		writeOrderDetailDTO(orderDTOs);
		return orderDTOs;
	}

	public List<OrderDTO> writeOrderDetailDTO(List<OrderDTO> orderDTOs){
		List<OrderDetailDTO> orderDetailDTOs = orderDetailService.findPackMallgetDetailList(orderDTOs.stream().map(OrderDTO::getId).collect(Collectors.toList()));
		Map<String, List<OrderDetailDTO>> orderDetailDTOMap = orderDetailDTOs.stream().collect(Collectors.groupingBy(OrderDetailDTO::getOrderId));
		orderDTOs.forEach(item ->{item.setOrderDetailDTOs(orderDetailDTOMap.get(item.getId()));});
		return orderDTOs;
	}

	@Override
	public String savePackMallOrder(String openid, OrderDTO orderDTO) {
		Order order = orderDTO.toEntity(orderDTO);
		order.setCode("PM"+new Date().getTime());
		order.setOpenId(openid);
		order.setUserName(clientService.getByOpenId(openid).getClientName());
		order.setState("1");//未支付
		order.setWhetherState("0");//未开票
		//保存详情
		this.save(order);
		Map<String, Object> result= orderDetailService.savePackMallOrder(orderDTO.getOrderDetailDTOs(),order.getId());
		
		//填写所有sku 总价格
		order.setSku(result.get("SKU").toString());
		order.setTotalPrice(new BigDecimal(result.get("TOTAL").toString()).add(orderDTO.getFreight()));
		return order.getId();
	}
	
}
