package com.cnpc.packmall.order.quartz;

import com.cnpc.framework.utils.DateUtil;
import com.cnpc.framework.utils.SpringContextUtil;
import com.cnpc.packmall.order.constant.OrderEnum;
import com.cnpc.packmall.order.entity.Order;
import com.cnpc.packmall.order.entity.OrderStateChange;
import com.cnpc.packmall.order.service.OrderService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CloseOrderJob implements Job {
	
	
	OrderService orderService=(OrderService)SpringContextUtil.getBean("orderService");
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//查询未支付订单超过1小时的
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);// 日期减1
		DateUtil.format(calendar.getTime(),DateUtil.YYYY_MM_DD_HH_MM_SS);
		String hql = "from Order where createDateTime <=:createDateTime and state=1";
		Map<String, Object> params = new HashMap<>();
		params.put("createDateTime", calendar.getTime());
		List<Order> orders = orderService.find(hql,params);
		//关闭订单
		//添加流转信息
		if(orders!=null&&orders.size()>0){
			
			List<OrderStateChange> orderStateChanges = new ArrayList<>();
			OrderStateChange orderStateChange = null;
			for (Order order : orders) {
				orderStateChange = new OrderStateChange();
				orderStateChange.setHistoryState(order.getState());
				order.setState("-1");
				order.setUpdateDateTime(new Date());
				orderStateChange.setOrderId(order.getId());
				orderStateChange.setState(order.getState());
				orderStateChange.setDescribe(OrderEnum.codeOf(order.getState()).getMsg());
				orderStateChanges.add(orderStateChange);
			}
			orderService.batchSaveOrUpdate(orders);
			orderService.batchSave(orderStateChanges);
		}
	}

}