package com.cnpc.packmall.order.service;

import java.util.List;
import java.util.Map;

import com.cnpc.framework.base.service.BaseService;
import com.cnpc.packmall.order.pojo.dto.OrderDetailDTO;
import com.cnpc.packmall.order.pojo.dto.OrderPurchaseDTO;

/**
* 订单详情服务接口
* @author WY
* 2018-08-16 16:58:33由代码生成器自动生成
*/
public interface OrderPurchaseService extends BaseService {

	Map<String, List<OrderPurchaseDTO>> findPackMallgetPurchaseList(String openId);

	Map<String, Object> savePackMallOrder(List<OrderPurchaseDTO> orderPurchaseDTOs,String openId);
	
	/**
	 * 根据id集合删除
	 * @param openId
	 * @param idStrings
	 */
	void deleteByIds(String openId, List<String> idStrings);

}
