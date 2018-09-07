package com.cnpc.packmall.invoice.quartz;

import com.cnpc.framework.utils.DateUtil;
import com.cnpc.framework.utils.SpringContextUtil;
import com.cnpc.packmall.invoice.entity.InvoiceDedicated;
import com.cnpc.packmall.invoice.entity.InvoiceNormal;
import com.cnpc.packmall.invoice.entity.InvoiceWXPay;
import com.cnpc.packmall.invoice.service.InvoiceWXPayService;
import com.cnpc.packmall.order.constant.OrderEnum;
import com.cnpc.packmall.order.entity.Order;
import com.cnpc.packmall.order.entity.OrderStateChange;
import com.cnpc.packmall.order.service.OrderService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.*;
import java.util.stream.Collectors;

public class CloseInvoiceJob implements Job {


	InvoiceWXPayService invoiceWXPayService=(InvoiceWXPayService)SpringContextUtil.getBean("invoiceWXPayService");
	
	@Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
		//查询未支付订单超过1小时的
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);// 日期减1
		DateUtil.format(calendar.getTime(),DateUtil.YYYY_MM_DD_HH_MM_SS);
		String hql = "from InvoiceDedicated where createDateTime <=:createDateTime and payStatus='1'";
		String hql2 = "from InvoiceNormal where createDateTime <=:createDateTime and payStatus='1'";
		Map<String, Object> params = new HashMap<>();
		params.put("createDateTime", calendar.getTime());
		//查询要 修改的发票信息
		List<InvoiceDedicated> list = invoiceWXPayService.find(hql,params,InvoiceDedicated.class);
		List<InvoiceNormal> list2 = invoiceWXPayService.find(hql2,params,InvoiceNormal.class);
        list.forEach(w->{w.setDeleted(1);});
        list2.forEach(w->{w.setDeleted(1);});
        List<Order>  orders = new ArrayList<>();
        List<Order>  orders2 = new ArrayList<>();
        List<InvoiceWXPay> wxList = new ArrayList<>();
        List<InvoiceWXPay> wxList2 = new ArrayList<>();
        //查询 要修改的订单信息
        if(list!=null&&list.size()>0){
            //专用发票 订单
            List <String> dedicatedIds = list.stream().map(InvoiceDedicated::getId).collect(Collectors.toList());
            Map<String,Object> dedicatedMap = new HashMap<>(2);
            dedicatedMap.put("dedicatedIds",dedicatedIds);
            String orderHql = "from Order where whetherId in (:dedicatedIds)";
            orders= invoiceWXPayService.find(orderHql,dedicatedMap,Order.class);
            orders.forEach(o->{o.setWhetherState("0");o.setWhetherId("");});

            String wxHql = "from InvoiceWXPay where invoiceId in (:dedicatedIds)";
            wxList = invoiceWXPayService.find(wxHql,dedicatedMap,InvoiceWXPay.class);
            wxList.forEach(w->{w.setDeleted(1);});
        }
        if(list2!=null&&list2.size()>0){
            //普通发票订单
            List <String> normalIds = list2.stream().map(InvoiceNormal::getId).collect(Collectors.toList());
            Map<String,Object> normalMap = new HashMap<>(2);
            normalMap.put("normalIds",normalIds);
            String orderHql = "from Order where whetherId in (:normalIds)";
            orders2= invoiceWXPayService.find(orderHql,normalMap,Order.class);
            orders2.forEach(o->{o.setWhetherState("0");o.setWhetherId("");});

            String wxHql2 = "from InvoiceWXPay where invoiceId in (:dedicatedIds)";
            wxList2 = invoiceWXPayService.find(wxHql2,normalMap,InvoiceWXPay.class);
            wxList2.forEach(w->{w.setDeleted(1);});
        }
        if(list!=null&&list.size()>0){
            invoiceWXPayService.batchSaveOrUpdate(list);
        }
        if(list2!=null&&list2.size()>0){
            invoiceWXPayService.batchSaveOrUpdate(list2);
        }
        if(orders!=null&&orders.size()>0){
            invoiceWXPayService.batchSaveOrUpdate(orders);
        }
        if(orders2!=null&&orders2.size()>0){
            invoiceWXPayService.batchSaveOrUpdate(orders2);
        }
        if(wxList!=null&&wxList.size()>0){
            invoiceWXPayService.batchSaveOrUpdate(wxList);
        }
        if(wxList2!=null&&wxList2.size()>0){
            invoiceWXPayService.batchSaveOrUpdate(wxList2);
        }
	}

}