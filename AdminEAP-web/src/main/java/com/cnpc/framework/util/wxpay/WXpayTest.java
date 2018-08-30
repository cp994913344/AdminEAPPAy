package com.cnpc.framework.util.wxpay;

import java.util.HashMap;
import java.util.Map;

public class WXpayTest {
	
		public static void main(String[] args) throws Exception {
			MyConfig config = new MyConfig();
	        WXPay wxpay = new WXPay(config);

	        Map<String, String> data = new HashMap<String, String>();
	        //商品描述
	        data.put("body", "一撕得纸箱订单");
	        //商户订单号
	        data.put("out_trade_no", "2018082410595900000011");
//	        data.put("device_info", "");
	        data.put("fee_type", "CNY");
	        data.put("total_fee", "1");
	        data.put("notify_url", "http://www.example.com/wxpay/notify");
	        data.put("trade_type", "JSAPI");  // 此处指定为公众号支付
	        data.put("openid", "oOb_W5aVK-8PdOcg092PwnHoqos8");

	        try {
	            Map<String, String> resp = wxpay.unifiedOrder(data);
	            System.out.println(resp);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
}
