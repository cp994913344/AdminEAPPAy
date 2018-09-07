package com.cnpc.framework.util.wxpay.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cnpc.packmall.invoice.entity.InvoiceDedicated;
import com.cnpc.packmall.invoice.entity.InvoiceNormal;
import com.cnpc.packmall.invoice.service.InvoiceDedicatedService;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.http.HttpRequest;
import com.cnpc.framework.util.wxpay.MyConfig;
import com.cnpc.framework.util.wxpay.WXPayConstants;
import com.cnpc.framework.util.wxpay.WXPayConstants.SignType;
import com.cnpc.framework.util.wxpay.WXPayUtil;
import com.cnpc.framework.utils.HttpUtil;
import com.cnpc.packmall.order.entity.Order;
import com.cnpc.packmall.order.service.OrderService;

@Controller
@RequestMapping("/wxpay")
public class WXPayController {
	
	@Resource
	OrderService orderService;

	@Resource
	private InvoiceDedicatedService invoiceDedicatedService;
	
    @RequestMapping(value="/pack_mall_api/getOpenId/{code}")
    @ResponseBody
    public String get(@PathVariable("code") String code){
    	MyConfig config = new MyConfig();
    	String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+config.getAppID()+"&secret="+config.getSecret()+"&js_code="+code+"&grant_type=authorization_code";
        return JSONObject.parseObject(HttpUtil.sendGet(url,"utf-8")).getString("openid");
    }
    
    @RequestMapping(value="/pack_mall_api/notify")
    @ResponseBody
    public String notify(HttpServletResponse response,HttpServletRequest request){
    	// 将返回的输入流转换成字符串
		InputStream is = null;
		try {
			is = request.getInputStream();
	        // 使用dom4j解析xml字符串  
	        SAXReader reader = new SAXReader();  
	        Document document = null;
			document = reader.read(is);
			// 得到xml根元素  
	        Element root = document.getRootElement();  
	        //xml 转map
			Map<String, String> data = WXPayUtil.xmlToMap(root.asXML());
			//签名
			MyConfig config = new MyConfig();
			String sign = WXPayUtil.generateSignature(data, config.getKey(), SignType.HMACSHA256);
	        if(data.get(WXPayConstants.FIELD_SIGN).equals(sign)&&"SUCCESS".equals(data.get("result_code"))){
	        	Order order = orderService.get(Order.class,data.get("out_trade_no"));
	        	//判断订单 状态 及金额
	        	if(order!=null&&order.getState().equals("1")){
	        		//改变订单状态 并保存流转信息
	        		orderService.doConfirm(null, order.getId(), "2", null);
	        		System.out.println("订单处理成功");
	        	}
	        	//判断订单 状态 及金额
//	        	if(order!=null&&order.getState().equals("1")&&(order.getTotalPrice().multiply(new BigDecimal(100))).compareTo(new BigDecimal(data.get("total_fee")))==0){
//	        		//改变订单状态 并保存流转信息
//	        		orderService.doConfirm(null, order.getId(), "2", null);
//	        		System.out.println("订单处理成功");
//	        	}
	        }
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return setXml("SUCCESS", "Y");

    }
    @RequestMapping(value="/pack_mall_api/invoiceNotify")
    @ResponseBody
    public String invoiceNotify(HttpServletResponse response,HttpServletRequest request){
		// 将返回的输入流转换成字符串
		InputStream is = null;
		try {
			is = request.getInputStream();
			// 使用dom4j解析xml字符串
			SAXReader reader = new SAXReader();
			Document document = null;
			document = reader.read(is);
			// 得到xml根元素
			Element root = document.getRootElement();
			//xml 转map
			Map<String, String> data = WXPayUtil.xmlToMap(root.asXML());
			//签名
			MyConfig config = new MyConfig();
			String sign = WXPayUtil.generateSignature(data, config.getKey(), SignType.HMACSHA256);
			if(data.get(WXPayConstants.FIELD_SIGN).equals(sign)&&"SUCCESS".equals(data.get("result_code"))){
				InvoiceDedicated invoiceDedicated = invoiceDedicatedService.get(InvoiceDedicated.class, data.get("out_trade_no"));
				if(invoiceDedicated!=null){
					invoiceDedicated.setPayStatus("2");
					invoiceDedicated.setUpdateDateTime(new Date());
					invoiceDedicatedService.update(invoiceDedicated);
				}else{
					InvoiceNormal invoiceNormal = invoiceDedicatedService.get(InvoiceNormal.class, data.get("out_trade_no"));
					if(invoiceNormal!=null){
						invoiceNormal.setPayStatus("2");
						invoiceNormal.setUpdateDateTime(new Date());
						invoiceDedicatedService.update(invoiceNormal);
					}
				}
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return setXml("SUCCESS", "Y");
    }
    //通过xml 发给微信消息
  	public static String setXml(String return_code, String return_msg) {
  		SortedMap<String, String> parameters = new TreeMap<String, String>();
  		parameters.put("return_code", return_code);
  		parameters.put("return_msg", return_msg);
  		return "<xml><return_code><![CDATA[" + return_code + "]]>" + 
  				"</return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
  	}
  	public static void main(String[] args) {
		String xml =
				"<xml><appid><![CDATA[wxd8ba58498aa5ee7a]]></appid>"+
						"<bank_type><![CDATA[CFT]]></bank_type>"+
						"<cash_fee><![CDATA[1]]></cash_fee>"+
						"<fee_type><![CDATA[CNY]]></fee_type>"+
						"<is_subscribe><![CDATA[N]]></is_subscribe>"+
						"<mch_id><![CDATA[1344168901]]></mch_id>"+
						"<nonce_str><![CDATA[QnJJBd4gFFwzuBIUk8c7ytHDTXmVQE3E]]></nonce_str>"+
						"<openid><![CDATA[oOb_W5aVK-8PdOcg092PwnHoqos8]]></openid>"+
						"<out_trade_no><![CDATA[FrkOjXuxJZDI97iZHc3gKFETQ6HvvQEv]]></out_trade_no>"+
						"<result_code><![CDATA[SUCCESS]]></result_code>"+
						"<return_code><![CDATA[SUCCESS]]></return_code>"+
						"<sign><![CDATA[ADD64D975B67022CD1374F521B50CBB7E0ED2D48BBFBC63B590B6C27379275DB]]></sign>"+
						"<time_end><![CDATA[20180906184958]]></time_end>"+
						"<total_fee>1</total_fee>"+
						"<trade_type><![CDATA[JSAPI]]></trade_type>"+
						"<transaction_id><![CDATA[4200000170201809067138794039]]></transaction_id>"+
						"</xml>";
		
		SortedMap<String, String> parameters = new TreeMap<String, String>();
  		parameters.put("return_code", "1111");
  		parameters.put("return_msg", "2222");
  		try {
			System.out.println(WXPayUtil.mapToXml(parameters));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
