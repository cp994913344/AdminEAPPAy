package com.cnpc.packmall.order.service;

import java.util.List;
import java.util.Map;

import com.cnpc.framework.base.service.BaseService;
import com.cnpc.packmall.order.pojo.dto.OrderDetailDTO;

/**
* 订单详情服务接口
* @author WY
* 2018-08-16 16:58:33由代码生成器自动生成
*/
public interface OrderDetailService extends BaseService {

	List<OrderDetailDTO> findPackMallgetDetailList(List<String> ids);

	Map<String, Object> savePackMallOrder(List<OrderDetailDTO> orderDetailDTOs,String orderId);

}
