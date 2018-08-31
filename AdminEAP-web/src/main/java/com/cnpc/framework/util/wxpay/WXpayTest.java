package com.cnpc.framework.util.wxpay;

import java.util.Date;
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
	        data.put("out_trade_no", "2018082411595901111");
//	        data.put("device_info", "");
	        data.put("fee_type", "CNY");
	        data.put("total_fee", "1");
	        data.put("notify_url", "http://sit.yiside.cn:9000/auto/sewage/callback");
	        data.put("trade_type", "JSAPI");  // 此处指定为公众号支付
	        data.put("openid", "oOb_W5aVK-8PdOcg092PwnHoqos8");

	        try {
	            Map<String, String> resp = wxpay.unifiedOrder(data);
	            String paySign = "appId="+config.getAppID()+"&nonceStr="+ WXPayUtil.generateNonceStr() +"&package=prepay_id="+resp.get("prepay_id")+"&signType=HMAC-SHA256&timeStamp=" + new Date().getTime() + "&key="+config.getKey();
	            System.out.println(paySign);
	            System.out.println(WXPayUtil.HMACSHA256(paySign, config.getKey()));
	            System.out.println(resp);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
}
