package com.cnpc.packmall.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.packmall.SKU.entity.Sku;
import com.cnpc.packmall.SKU.entity.SkuDetail;
import com.cnpc.packmall.SKU.service.SkuService;
import com.cnpc.packmall.order.entity.OrderDetail;
import com.cnpc.packmall.order.pojo.dto.OrderDetailDTO;
import com.cnpc.packmall.order.service.OrderDetailService;
import com.cnpc.packmall.product.entity.ProductDetail;
import com.cnpc.packmall.product.service.ProductService;

/**
* 订单详情服务实现
* @author WY
* 2018-08-16 16:58:33由代码生成器自动生成
*/
@Service("orderdetailService")
public class OrderDetailServiceImpl extends BaseServiceImpl implements OrderDetailService {

	@Resource
	SkuService skuService;
	
	@Resource
	ProductService productService;
	
	@Override
	public List<OrderDetailDTO> findPackMallgetDetailList(List<String> ids) {
		if(ids!=null&&ids.size()>0){
			Map<String, Object> params = new HashMap<>();
			String hql = "select od.orderId as orderId,od.productId as productId,od.productImgId as productImgId," +
					" od.skuId as skuId, od.skumsg as skumsg, od.size as size," +
					" od.specification as specification, od.specificationId as specificationId, od.color as color," +
					" od.colorId as colorId, od.quality as quality, od.qualityId as qualityId," +
					" od.productName as productName, od.number as number, od.price as price," +
					" od.priceId as priceId, od.totalPrice as totalPrice" +
					" from OrderDetail as od where 1=1 and od.id in (:ids)";
			params.put("ids",ids);
			List<OrderDetailDTO> orderDetailDTOs = this.find(hql, params,OrderDetailDTO.class);
			return orderDetailDTOs;
		}
		return null;
	}

	@Override
	public Map<String, Object> savePackMallOrder(List<OrderDetailDTO> orderDetailDTOs,String orderId) {
		List<OrderDetail> orderDetails = new ArrayList<>();
		Map<String, Object> result = new HashMap<>();
		Set<String> skuIds = new HashSet<>();
		Set<String> skuDetails = new HashSet<>();
		Set<String> productIds = new HashSet<>();
		orderDetailDTOs.forEach(orderDetailDTO ->{
			productIds.add(orderDetailDTO.getProductId());
			skuIds.add(orderDetailDTO.getSkuId());
			skuDetails.add(orderDetailDTO.getSpecificationId());
			skuDetails.add(orderDetailDTO.getColorId());
			skuDetails.add(orderDetailDTO.getQualityId());
			skuDetails.add(orderDetailDTO.getPriceId());
		});
		Map<String, Sku> skuMap = skuService.findSkuBySkuIds(skuIds);
		Map<String, Map<String, SkuDetail>> skuDetailMap = skuService.findSkuDetailBySkuDetailIds(skuDetails);
		Map<String, List<ProductDetail>> productDetailMap = productService.findProductDetailByProductIdAndType(productIds,"BANNERIMG");
		String orderSku = "";
		BigDecimal orderPrice = new BigDecimal(0);
		for (OrderDetailDTO orderDetailDTO : orderDetailDTOs) {
			OrderDetail orderDetail = new OrderDetail();
			Sku sku = skuMap.get(orderDetailDTO.getSkuId());
			ProductDetail productDetail = productDetailMap.get(orderDetailDTO.getProductId()).get(0);
			Map<String, SkuDetail> skuDetail = skuDetailMap.get(orderDetailDTO.getSkuId());
			orderDetail.setOrderId(orderId);
			orderDetail.setProductId(orderDetailDTO.getProductId());
			orderDetail.setProductImgId(productDetail.getDetailId());
			orderDetail.setSkuId(orderDetailDTO.getSkuId());
			orderDetail.setSize(sku.getSkuSizeLength()+"*"+sku.getSkuSizeWide()+"*"+sku.getSkuSizeHigh());
			orderDetail.setSpecification(skuDetail.get(orderDetailDTO.getSpecificationId()).getDetailName());
			orderDetail.setSpecificationId(orderDetailDTO.getSpecificationId());
			orderDetail.setColor(skuDetail.get(orderDetailDTO.getColorId()).getDetailName());
			orderDetail.setColorId(orderDetailDTO.getColorId());
			orderDetail.setQuality(skuDetail.get(orderDetailDTO.getQualityId()).getDetailName());
			orderDetail.setQualityId(orderDetailDTO.getQualityId());
			orderDetail.setProductName(sku.getProductName());
			orderDetail.setNumber(orderDetailDTO.getNumber());
			orderDetail.setPrice(skuDetail.get(orderDetailDTO.getPriceId()).getDetailVal());
			orderDetail.setPriceId(orderDetailDTO.getPriceId());
			orderDetail.setTotalPrice(new BigDecimal(orderDetail.getNumber()).multiply(orderDetail.getPrice()));
			orderDetail.setSkumsg(sku.getSkuCode()+"*"+orderDetail.getNumber()+"("+orderDetail.getSpecification()+";"+orderDetail.getColor()+";"+orderDetail.getQuality()+")");
			orderDetails.add(orderDetail);
			orderPrice = orderPrice.add(orderDetail.getTotalPrice());
			orderSku += orderDetail.getSkumsg()+"</br>"; 
		};
		this.batchSave(orderDetails);
		result.put("SKU", orderSku);
		result.put("TOTAL", orderPrice);
		result.put("productCategory", orderDetails.size());
		result.put("productImgId", orderDetails.get(0).getProductImgId());
		result.put("productMsg", orderDetails.size()>1?orderDetails.get(0).getProductName()+"等"+orderDetails.size()+"类商品":orderDetails.get(0).getProductName());
		
		return result;
	}

	/**
	 * 通过 order ids  查询  字表 的 map
	 * @param orderIds
	 * @return
	 * @autor cp
	 */
     @Override
	public Map<String,OrderDetailDTO> findMapByOrderIds(Set<String> orderIds){
		Map<String,Object> params = new HashMap<>(2);
		params.put("orderIds", orderIds);
		String hql = "select od.orderId as orderId,od.productId as productId ,od.productImgId as productImgId from OrderDetail od where od.deleted=0 and orderId in (:orderIds)";
		List<OrderDetailDTO> details = this.baseDao.find(hql,params,OrderDetailDTO.class);
		Map<String,OrderDetailDTO> result = details.stream().collect(Collectors.toMap(OrderDetailDTO::getProductId,Function.identity()));
		return result;
	}

}
