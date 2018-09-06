package com.cnpc.packmall.order.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.persistence.Column;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.Dict;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.framework.util.wxpay.MyConfig;
import com.cnpc.framework.util.wxpay.WXPay;
import com.cnpc.framework.util.wxpay.WXPayUtil;
import com.cnpc.framework.utils.DateUtil;
import com.cnpc.packmall.center.entity.ShippingAddress;
import com.cnpc.packmall.center.service.ClientService;
import com.cnpc.packmall.center.service.ShippingAddressService;
import com.cnpc.packmall.order.constant.OrderEnum;
import com.cnpc.packmall.order.entity.Order;
import com.cnpc.packmall.order.entity.OrderStateChange;
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
				"o.addressId as addressId,o.areaAddress as areaAddress,o.shippingAddress as shippingAddress,o.phone as phone,o.contacts as contacts,o.deliverytime as deliverytime," +
				"o.weekend as weekend,o.sku as sku,o.remarks as remarks,o.freight as freight,o.totalPrice as totalPrice," +
				"o.payMethod as payMethod,o.state as state,o.productCategory as productCategory,o.productImgId as productImgId," +
				"o.productMsg as productMsg,o.whetherState as whetherState,o.whetherId as whetherId,o.createDateTime as createDateTime " +
				"from Order as o where 1=1 and o.openId=:openid";
		params.put("openid", openid);
		for (Map.Entry<String, String> entry : param.entrySet()) {
			if(entry.getKey().equals("today")){//今日订单
				hql +=" and o.createDateTime >=:"+entry.getKey();
				params.put(entry.getKey(), DateUtil.getNextDay(new Date(), -1));
			}else if(entry.getKey().equals("history")){//历史订单
				hql +=" and o.createDateTime <:"+entry.getKey();
				params.put(entry.getKey(), new Date(entry.getValue()));
			}else{
				params.put(entry.getKey(), entry.getValue());
				hql +=" and o."+entry.getKey() +"=:"+entry.getKey();
			}
		}
		hql+=" order by o.createDateTime desc";
		List<OrderDTO> orderDTOs = this.find(hql, params,OrderDTO.class);
		if(orderDTOs.size()>0){
			writeOrderDetailDTO(orderDTOs);
		}
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
		ShippingAddress shippingAddress = this.get(ShippingAddress.class, orderDTO.getAddressId());
		order.setAreaAddress(shippingAddress.getAreaAddress());
		order.setShippingAddress(shippingAddress.getShippingAddress());
		order.setPhone(shippingAddress.getShippingPhone());
		order.setContacts(shippingAddress.getShippingName());
		order.setDeleted(0);
		order.setWhetherState("0");
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
		//保存订单流转状态
		saveOrderChange(order.getId(), null, order.getState(),null);

		Map<String, String> result = new HashMap<>();
		result.put("orderId", order.getId());
		return result;
	}
	
	@Override
	public Map<String, String> doOrderPay(String orderId,String openId){
		//查询是否存在支付id
		String prepayId = getPrepayIdByOrderId(orderId);
		if(prepayId!=null){
			return paySign(prepayId);
		}
		Order order = this.get(Order.class, orderId);
		Map<String, String> wxResult = doWXPay(orderId,openId,order.getProductMsg(),order.getTotalPrice().toString());
		return paySign(wxResult.get("prepay_id"));
	}
	
	@Override
	public String getPrepayIdByOrderId(String orderId){
		Map<String,Object> params = new HashMap<>();
		String hql = "from OrderWXPay where orderId =:orderId";
		params.put("orderId", orderId);
		OrderWXPay orderWXPay = this.get(hql,params);
		if(orderWXPay!=null){
			return orderWXPay.getPrepayId();
		}
		return null;
	}
	public Map<String, String> paySign(String prepayId){
		MyConfig config = new MyConfig();
    	String timeStamp = new Date().getTime()+"";
    	String nonceStr = WXPayUtil.generateNonceStr();
    	String paySignString = "appId="+config.getAppID()+"&nonceStr="+ nonceStr +"&package=prepay_id="+prepayId+"&signType=HMAC-SHA256&timeStamp=" + timeStamp + "&key="+config.getKey();
        String paySign = null;
    	try {
    		paySign=WXPayUtil.HMACSHA256(paySignString, config.getKey());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Map<String, String> result = new HashMap<>();
        result.put("timeStamp", timeStamp);
        result.put("nonceStr", nonceStr);
        result.put("signType", "HMAC-SHA256");
        result.put("package", "prepay_id="+prepayId);
        result.put("paySign", paySign);
        return result;
	}
	public Map<String, String> doWXPay(String orderId,String openId,String body,String price){
		MyConfig config = new MyConfig();
		try {
			WXPay wxpay = new WXPay(config);
	        Map<String, String> data = new HashMap<String, String>();
	        //商品描述
	        data.put("body", body);
	        //商户订单号
	        data.put("out_trade_no", orderId);
	        data.put("fee_type", "CNY");
	        //TODO 上线修改为正常金额 以及回调地址修改
	        data.put("total_fee", "1");
	        data.put("notify_url", "https://weixin.yiside.cn/wxpay/notify");
	        data.put("trade_type", "JSAPI");  // 此处指定为小程序支付
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

	@Override
	public void doConfirm(String openId, String orderId,String state,String remark) {
		
        Order order =  this.get(Order.class, orderId);
        String oldState = order.getState();
        order.setState(state);
        order.setUpdateDateTime(new Date());
        this.update(order);
        saveOrderChange(orderId, oldState, state,remark);
	}
	
	public void saveOrderChange(String orderId,String oldState,String state,String remark) {
		//保存订单流转状态
		OrderStateChange orderStateChange = new OrderStateChange();
		orderStateChange.setOrderId(orderId);
		orderStateChange.setHistoryState(oldState);
		orderStateChange.setState(state);
		orderStateChange.setDescribe(OrderEnum.codeOf(state).getMsg());
        orderStateChange.setRemark(remark);
        this.save(orderStateChange);
	}
	
	@Override
	public List<OrderStateChange> findOrderChangeByOrderId(String orderId) {
		Map<String,Object> params = new HashMap<>();
		String hql = "from OrderStateChange where orderId =:orderId order by createDateTime desc";
		params.put("orderId", orderId);
		return this.find(hql, params);
	}

	/**
	 * 发票使用 openId查询订单中商品的图片  小程序 展示
	 * @param openId
	 * @param param
	 * @return
	 */
	@Override
	public List<OrderDTO> findByParams(String openId, Map<String, String> param){
		Map<String,Object> params = new HashMap<>();
		params.put("openId",openId);
		if(StringUtils.isEmpty(openId)){
			return null;
		}
		String hql = "select o.id as id,o.code as code,o.totalPrice as totalPrice," +
				"o.whetherState as whetherState,o.whetherId as whetherId,o.createDateTime as createDateTime " +
				"from Order as o where deleted = 0 and openId = :openId";
		for (Map.Entry<String, String> entry : param.entrySet()) {
			params.put(entry.getKey(), entry.getValue());
			hql +=" and "+entry.getKey() +"=:"+entry.getKey();
		}
		List<OrderDTO> orderDTOs = this.find(hql, params,OrderDTO.class);
		//获取详情表信息
		if(orderDTOs!=null&&orderDTOs.size()>0){
			List<String> orderIds = new ArrayList<>(orderDTOs.size());
			orderDTOs.forEach(orderDto->{
				orderIds.add(orderDto.getId());
			});
			Map<String, Object> detailParams = new HashMap<>();
			String detailHql = "select od.orderId as orderId,od.productId as productId,od.productImgId as productImgId," +
					" od.priceId as priceId, od.totalPrice as totalPrice" +
					" from OrderDetail as od where 1=1 and od.orderId in (:orderIds)";
			detailParams.put("orderIds",orderIds);
			List<OrderDetailDTO> orderDetailDTOs = this.find(detailHql, detailParams,OrderDetailDTO.class);
			Map<String,List<OrderDetailDTO>> detailMap= orderDetailDTOs.stream().collect(Collectors.groupingBy(OrderDetailDTO::getOrderId));
			orderDTOs.forEach(orderDto->{
				for(Map.Entry<String,List<OrderDetailDTO>> mp:detailMap.entrySet()){
					if(orderDto.getId().equals(mp.getKey())){
						orderDto.setOrderDetailDTOs(mp.getValue());
					}
				}
			});
		}
		//处理重复商品
		for(OrderDTO dto:orderDTOs){
			if(dto.getOrderDetailDTOs()!=null&&dto.getOrderDetailDTOs().size()>0){
				List<OrderDetailDTO> dtoDtoList=dto.getOrderDetailDTOs();
				List<OrderDetailDTO> newList = new ArrayList<>(dtoDtoList.size());
				Set<String> productSet = dtoDtoList.stream().map(OrderDetailDTO::getProductId).collect(Collectors.toSet());
				for(OrderDetailDTO ddto :dtoDtoList){
					if(productSet.size()>0){
						for(String s:productSet){
							if(ddto.getProductId().equals(s)){
								newList.add(ddto);
								productSet.remove(s);
								break;
							}
						}
					}
				}
				dto.setOrderDetailDTOs(newList);
				//dtoDtoList.stream().collect(Collectors.toSet())
			}
		}
		return orderDTOs;
	}

	/**
	 * 发票 详情 展示商品信息
	 * @param id
	 * @return
	 */
	@Override
	public Set<String> findOrderProductById(String id){
		//先获取所有的 订单id
		String orderHql = "select o.id as id from Order as o where o.whetherId='"+id+"'";
		List<OrderDTO> orderDTOs = this.find(orderHql,OrderDTO.class);
		//获取详情表信息
		if(orderDTOs!=null&&orderDTOs.size()>0){
			List<String> orderIds= orderDTOs.stream().map(OrderDTO::getId).collect(Collectors.toList());
			Map<String, Object> detailParams = new HashMap<>(2);
			String detailHql = "select od.productId as productId,od.productImgId as productImgId " +
					" from OrderDetail as od where 1=1 and od.orderId in (:orderIds)";
			detailParams.put("orderIds",orderIds);
			List<OrderDetailDTO> orderDetailDTOs = this.find(detailHql, detailParams,OrderDetailDTO.class);
			//处理重复商品图片   合成一个set  存放图片id
			Set<String> orderDtailImgIds = orderDetailDTOs.stream().map(OrderDetailDTO::getProductImgId).collect(Collectors.toSet());
			//Map<String,List<OrderDetailDTO>> result = orderDetailDTOs.stream().collect(Collectors.groupingBy(OrderDetailDTO::getProductId));
			return orderDtailImgIds;
		}
		return null;
	}

	@Override
	public Map<String, String>  findStatisticsByOpenId(String openId) {
		String sql = "select count(id) as total from tbl_packmall_order_order where open_id =:openId";
		Map<String, Object> params = new HashMap<>();
		params.put("openId", openId);
		Map<String, Object> map= this.findMapBySql(sql,params).get(0);
		String hql = "select o.id as id, o.state as state, o.createDateTime as createDateTime from Order as o where o.state in ('1','2','3') and o.openId=:openId";
		List<Order> orders = this.find(hql,params,Order.class);
		Integer today = 0;
		Integer unPay = 0;
		Integer alreadyPaid = 0;
		Integer unSign = 0;
		for (Order order : orders) {
			if(DateUtil.getCurrDateStr().equals(DateUtil.getyyyyMMddDateStr(order.getCreateDateTime()))){
				today++;
			}
			switch (order.getState()) {
			case "1":
				unPay++;
				break;

			case "2":
				alreadyPaid++;
				break;
			case "3":
				unSign++;
				break;
			}
		}
		Map<String, String> result = new HashMap<>();
		result.put("today",today.toString());
		Integer history = Integer.valueOf(map.get("total").toString())-today;
		result.put("history",history.toString());
		result.put("unPay",unPay.toString());
		result.put("alreadyPaid",alreadyPaid.toString());
		result.put("unSign",unSign.toString());
		return result;
	}
	/**
	 * 
	 */
}
