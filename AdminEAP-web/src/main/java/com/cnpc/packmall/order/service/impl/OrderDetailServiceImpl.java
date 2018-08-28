package com.cnpc.packmall.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Resource;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.packmall.SKU.entity.Sku;
import com.cnpc.packmall.SKU.service.SkuService;
import com.cnpc.packmall.order.entity.OrderDetail;
import com.cnpc.packmall.order.pojo.dto.OrderDetailDTO;
import com.cnpc.packmall.order.service.OrderDetailService;

/**
* 订单详情服务实现
* @author WY
* 2018-08-16 16:58:33由代码生成器自动生成
*/
@Service("orderdetailService")
public class OrderDetailServiceImpl extends BaseServiceImpl implements OrderDetailService {

	@Resource
	SkuService skuService;
	
	@Override
	public List<OrderDetailDTO> findPackMallgetDetailList(List<String> ids) {
		Map<String, Object> params = new HashMap<>();
		String hql = "select orderId from OrderDetail where 1=1 and ids in :ids";
		params.put("ids",ids);
		List<OrderDetailDTO> orderDetailDTOs = this.find(hql, params,OrderDetailDTO.class);
		return orderDetailDTOs;
	}

	@Override
	public Map<String, Object> savePackMallOrder(List<OrderDetailDTO> orderDetailDTOs,String orderId) {
		List<OrderDetail> orderDetails = new ArrayList<>();
		Map<String, Object> result = new HashMap<>();
		Set<String> skuIds = new HashSet<>();
		Set<String> skuDetails = new HashSet<>();
		orderDetailDTOs.forEach(orderDetailDTO ->{
			skuIds.add(orderDetailDTO.getSkuId());
			skuDetails.add(orderDetailDTO.getSpecification());
			skuDetails.add(orderDetailDTO.getColor());
			skuDetails.add(orderDetailDTO.getQuality());
			skuDetails.add(orderDetailDTO.getPriceId());
		});
		Map<String, Sku> skuMap = skuService.findSkuBySkuIds(skuIds);
		Map<String, Map<String, String>> skuDetailMap = skuService.findSkuDetailBySkuDetailIds(skuDetails);
		String orderSku = "";
		BigDecimal orderPrice = new BigDecimal(0);
		for (OrderDetailDTO orderDetailDTO : orderDetailDTOs) {
			OrderDetail orderDetail = new OrderDetail();
			Sku sku = skuMap.get(orderDetailDTO.getSkuId());
			Map<String, String> skuDetail = skuDetailMap.get(orderDetailDTO.getSkuId());
			orderDetail.setOrderId(orderId);
			orderDetail.setProductId(orderDetailDTO.getProductId());
			orderDetail.setSkuId(orderDetailDTO.getSkuId());
			orderDetail.setSize(sku.getSkuSizeLength()+"*"+sku.getSkuSizeWide()+"*"+sku.getSkuSizeHigh());
			orderDetail.setSpecification(skuDetail.get(orderDetail.getSpecification()));
			orderDetail.setColor(skuDetail.get(orderDetail.getColor()));
			orderDetail.setQuality(skuDetail.get(orderDetail.getQuality()));
			orderDetail.setProductName(sku.getProductName());
			orderDetail.setNumber(orderDetailDTO.getNumber());
			orderDetail.setPrice(new BigDecimal(skuDetail.get(orderDetail.getPrice())));
			orderDetail.setTotalPrice(new BigDecimal(orderDetail.getNumber()).multiply(orderDetail.getPrice()));
			orderDetail.setSkumsg(sku.getSkuCode()+"*"+orderDetail.getNumber()+"("+orderDetail.getSpecification()+";"+orderDetail.getColor()+";"+orderDetail.getQuality()+")");
			orderDetails.add(orderDetail);
			orderPrice = orderPrice.add(orderDetail.getTotalPrice());
			orderSku += orderDetail.getSkumsg(); 
		};
		result.put("SKU", orderSku);
		result.put("TOTAL", orderPrice);
		return result;
	}


}
