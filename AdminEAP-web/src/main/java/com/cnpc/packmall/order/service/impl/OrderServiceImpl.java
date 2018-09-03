package com.cnpc.packmall.order.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.persistence.Column;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.Dict;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.framework.util.wxpay.MyConfig;
import com.cnpc.framework.util.wxpay.WXPay;
import com.cnpc.packmall.center.service.ClientService;
import com.cnpc.packmall.order.entity.Order;
import com.cnpc.packmall.order.entity.OrderWXPay;
import com.cnpc.packmall.order.pojo.dto.OrderDTO;
import com.cnpc.packmall.order.pojo.dto.OrderDetailDTO;
import com.cnpc.packmall.order.service.OrderDetailService;
import com.cnpc.packmall.order.service.OrderService;

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

		String hql = "select o.id as id,o.code as code,o.openId as openId,o.userName as userName," +
				"o.addressId as addressId,o.phone as phone,o.contacts as contacts,o.deliverytime as deliverytime," +
				"o.weekend as weekend,o.sku as sku,o.remarks as remarks,o.freight as freight,o.totalPrice as totalPrice," +
				"o.payMethod as payMethod,o.state as state,o.productCategory as productCategory,o.productImgId as productImgId," +
				"o.productMsg as productMsg,o.whetherState as whetherState,o.whetherId as whetherId,o.createDateTime as createDateTime " +
				"from Order as o where 1=1 and openId=:openid";
		params.put("openid", openid);
		for (Map.Entry<String, String> entry : param.entrySet()) {
			params.put(entry.getKey(), entry.getValue());
			hql +=" and "+entry.getKey() +"=:"+entry.getKey();
		}
		List<OrderDTO> orderDTOs = this.find(hql, params,OrderDTO.class);
		writeOrderDetailDTO(orderDTOs);
		return orderDTOs;
	}

	public List<OrderDTO> writeOrderDetailDTO(List<OrderDTO> orderDTOs){
		if(orderDTOs==null||orderDTOs.size()==0){
			return null;
		}
		List<OrderDetailDTO> orderDetailDTOs = orderDetailService.findPackMallgetDetailList(orderDTOs.stream().map(OrderDTO::getId).collect(Collectors.toList()));
		//根据orderId分组
		Map<String, List<OrderDetailDTO>> orderDetailDTOMap = orderDetailDTOs.stream().collect(Collectors.groupingBy(OrderDetailDTO::getOrderId));
		Map<String, Map<String, List<OrderDetailDTO>>> orderDetailDTOMapMap = new HashMap<>();
		for (Map.Entry<String, List<OrderDetailDTO>> entry : orderDetailDTOMap.entrySet()) {
			orderDetailDTOMapMap.put(entry.getKey(), entry.getValue().stream().collect(Collectors.groupingBy(OrderDetailDTO::getProductId)));
		}
		orderDTOs.forEach(item ->{item.setOrderDetailDTOMaps(orderDetailDTOMapMap.get(item.getId()));});
		return orderDTOs;
	}

	@Override
	public Map<String, String> savePackMallOrder(String openid, OrderDTO orderDTO) {
		Order order = orderDTO.toEntity(orderDTO);
		order.setCode("PM"+new Date().getTime());
		order.setOpenId(openid);
		order.setUserName(clientService.getByOpenId(openid).getClientName());
		order.setState("1");//未支付
		order.setWhetherState("0");//未开票
		//保存详情
		this.save(order);
		Map<String, Object> detailResult= orderDetailService.savePackMallOrder(orderDTO.getOrderDetailDTOs(),order.getId());
		
		//填写所有sku 总价格
		order.setSku(detailResult.get("SKU").toString());
		order.setTotalPrice(new BigDecimal(detailResult.get("TOTAL").toString()).add(orderDTO.getFreight()));
		order.setProductCategory(Integer.valueOf(detailResult.get("productCategory").toString()));
		order.setProductImgId(detailResult.get("productImgId").toString());
		order.setProductMsg(detailResult.get("productMsg").toString());
		this.update(order);
		//TODO 微信统一下单  保存微信下单信息
//		Map<String, String> wxResult = doWXPay(order.getId(),openid,order.getProductMsg(),order.getTotalPrice().toString());
		Map<String, String> wxResult = new HashMap<>();
		Map<String, String> result = new HashMap<>();
		result.put("sign", wxResult.get("sign"));
		result.put("prepayId", wxResult.get("prepay_id"));
		result.put("orderId", order.getId());
		return result;
	}
	
	public Map<String, String> doWXPay(String orderId,String openId,String body,String price){
		MyConfig config = new MyConfig();
		try {
			WXPay wxpay = new WXPay(config);
	        Map<String, String> data = new HashMap<String, String>();
	        //商品描述
	        data.put("body", "一撕得纸箱订单");
	        //商户订单号
	        data.put("out_trade_no", orderId);
	//        data.put("device_info", "");
	        data.put("fee_type", "CNY");
	        data.put("total_fee", "1");
	        data.put("notify_url", "http://www.example.com/wxpay/notify");
	        data.put("trade_type", "JSAPI");  // 此处指定为小程序支付
	//        data.put("openid", "oOb_W5aVK-8PdOcg092PwnHoqos8");
	        data.put("openid", openId);
	        Map<String, String> resp = wxpay.unifiedOrder(data);
	        //保存签名等待回调验证
	        OrderWXPay orderWXPay = new OrderWXPay();
	        orderWXPay.setOrderId(orderId);
	        orderWXPay.setPrepayId(resp.get("prepay_id"));
	        orderWXPay.setSign(resp.get("sign"));
	        this.save(orderWXPay);
	        System.out.println(resp);
	        return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return new HashMap<>();
	}
	
}
