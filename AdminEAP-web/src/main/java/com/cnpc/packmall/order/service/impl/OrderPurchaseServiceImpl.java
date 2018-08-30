package com.cnpc.packmall.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.packmall.SKU.entity.Sku;
import com.cnpc.packmall.SKU.entity.SkuDetail;
import com.cnpc.packmall.SKU.service.SkuService;
import com.cnpc.packmall.order.entity.OrderPurchase;
import com.cnpc.packmall.order.pojo.dto.OrderPurchaseDTO;
import com.cnpc.packmall.order.service.OrderPurchaseService;
import com.cnpc.packmall.product.entity.ProductDetail;
import com.cnpc.packmall.product.service.ProductService;

/**
* 订单详情服务实现
* @author WY
* 2018-08-16 16:58:33由代码生成器自动生成
*/
@Service("orderpurchaseService")
public class OrderPurchaseServiceImpl extends BaseServiceImpl implements OrderPurchaseService {

	@Resource
	SkuService skuService;
	
	@Resource
	ProductService productService;
	
	@Override
	public Map<String, List<OrderPurchaseDTO>> findPackMallgetPurchaseList(String openId) {
		Map<String, Object> params = new HashMap<>();
		String hql = "from OrderPurchase where 1=1 and openId = :openId";
		params.put("openId",openId);
		List<OrderPurchaseDTO> orderPurchaseDTOs = this.find(hql, params,OrderPurchaseDTO.class);
		if(orderPurchaseDTOs!=null){
			return orderPurchaseDTOs.stream().collect(Collectors.groupingBy(OrderPurchaseDTO::getProductId));
		}
		return null;
	}

	@Override
	public Map<String, Object> savePackMallOrder(List<OrderPurchaseDTO> orderPurchaseDTOs,String openId) {
		List<OrderPurchase> orderPurchases = new ArrayList<>();
		Map<String, Object> result = new HashMap<>();
		Set<String> skuIds = new HashSet<>();
		Set<String> skuDetails = new HashSet<>();
		Set<String> productId = new HashSet<>();
		Set<String> specificationId = new HashSet<>();
		Set<String> colorId = new HashSet<>();
		Set<String> qualityId = new HashSet<>();
		Set<String> priceId = new HashSet<>();
		orderPurchaseDTOs.forEach(orderPurchaseDTO ->{
			productId.add(orderPurchaseDTO.getProductId());
			skuIds.add(orderPurchaseDTO.getSkuId());
			skuDetails.add(orderPurchaseDTO.getSpecificationId());
			skuDetails.add(orderPurchaseDTO.getColorId());
			skuDetails.add(orderPurchaseDTO.getQualityId());
			skuDetails.add(orderPurchaseDTO.getPriceId());
			specificationId.add(orderPurchaseDTO.getSpecificationId());
			colorId.add(orderPurchaseDTO.getColorId());
			qualityId.add(orderPurchaseDTO.getQualityId());
			priceId.add(orderPurchaseDTO.getPriceId());
			
		});
		//查询是否有已加入的商品
		String hql = "from OrderPurchase where openId =:opendId and productId in(:productId) and specificationId in(:specificationId) and colorId in(:colorId) and qualityId in(:qualityId) and priceId in(:priceId)"; 
		Map<String, Object> params = new HashMap<>();
		params.put("productId", productId);
		params.put("opendId", openId);
		params.put("specificationId", specificationId);
		params.put("colorId", colorId);
		params.put("qualityId", qualityId);
		params.put("priceId", priceId);
		List<OrderPurchase> orderPurchases2 = this.find(hql, params);
		Map<String,OrderPurchaseDTO> orderPurchaseDTOMap = orderPurchaseDTOs.stream().collect(Collectors.toMap(OrderPurchaseDTO::getMapKey,Function.identity()));
		if(orderPurchases2!=null&&orderPurchases2.size()>0){
			Map<String,OrderPurchase> orderPurchaseMap = orderPurchases2.stream().collect(Collectors.toMap(OrderPurchase::getMapKey,Function.identity()));
			
			orderPurchaseMap.forEach((k,v) ->{
				v.setNumber(v.getNumber()+orderPurchaseDTOMap.get(k).getNumber());
				v.setUpdateDateTime(new Date());
				orderPurchases.add(v);
				orderPurchaseDTOMap.remove(k);
			});
		}
		
		Map<String, Sku> skuMap = skuService.findSkuBySkuIds(skuIds);
		Map<String,Map<String, SkuDetail>> skuDetailMap = skuService.findSkuDetailBySkuDetailIds(skuDetails);
		Map<String, List<ProductDetail>> productDetailMap = productService.findProductDetailByProductIdAndType(productId,"BANNERIMG");
		orderPurchaseDTOMap.forEach((k,orderPurchaseDTO) ->{
			OrderPurchase orderPurchase = new OrderPurchase();
			Sku sku = skuMap.get(orderPurchaseDTO.getSkuId());
			ProductDetail productDetail = productDetailMap.get(orderPurchaseDTO.getProductId()).get(0);
			Map<String, SkuDetail> skuDetail = skuDetailMap.get(orderPurchaseDTO.getSkuId());
			orderPurchase.setOpenId(openId);
			orderPurchase.setProductId(orderPurchaseDTO.getProductId());
			orderPurchase.setProductImgId(productDetail.getDetailId());
			orderPurchase.setSkuId(orderPurchaseDTO.getSkuId());
			orderPurchase.setSize(sku.getSkuSizeLength()+"*"+sku.getSkuSizeWide()+"*"+sku.getSkuSizeHigh());
			orderPurchase.setSpecification(skuDetail.get(orderPurchaseDTO.getSpecificationId()).getDetailName());
			orderPurchase.setSpecificationId(orderPurchaseDTO.getSpecificationId());
			orderPurchase.setColor(skuDetail.get(orderPurchaseDTO.getColorId()).getDetailName());
			orderPurchase.setColorId(orderPurchaseDTO.getColorId());
			orderPurchase.setQuality(skuDetail.get(orderPurchaseDTO.getQualityId()).getDetailName());
			orderPurchase.setQualityId(orderPurchaseDTO.getQualityId());
			orderPurchase.setPrice(skuDetail.get(orderPurchaseDTO.getPriceId()).getDetailVal());
			orderPurchase.setPriceId(orderPurchaseDTO.getPriceId());
			orderPurchase.setProductName(sku.getProductName());
			orderPurchase.setNumber(orderPurchaseDTO.getNumber());
			orderPurchases.add(orderPurchase);
		});
		this.batchSaveOrUpdate(orderPurchases);
		result.put("success", true);
		return result;
	}


}
