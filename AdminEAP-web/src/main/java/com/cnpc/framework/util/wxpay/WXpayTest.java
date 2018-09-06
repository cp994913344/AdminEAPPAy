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
	        data.put("out_trade_no", WXPayUtil.generateNonceStr());
//	        data.put("device_info", "");
	        data.put("fee_type", "CNY");
	        data.put("total_fee", "1");
	        data.put("notify_url", "https://weixin.yiside.cn/wxpay/pack_mall_api/notify");
	        data.put("trade_type", "JSAPI");  // 此处指定为公众号支付
	        data.put("openid", "oOb_W5aVK-8PdOcg092PwnHoqos8");

	        try {
	            Map<String, String> resp = wxpay.unifiedOrder(data);
	            System.out.println(resp);
	            //支付签名
				String payId= resp.get("prepay_id");
		    	String timeStamp = new Date().getTime()+"";
		    	String nonceStr = WXPayUtil.generateNonceStr();
		    	String paySign = "appId="+config.getAppID()+"&nonceStr="+ nonceStr +"&package=prepay_id="+payId+"&signType=HMAC-SHA256&timeStamp=" + timeStamp + "&key="+config.getKey();
		        Map<String, String> result = new HashMap<>();
		        result.put("prepay_id", payId);
		        result.put("timeStamp", timeStamp);
		        result.put("nonceStr", nonceStr);
		        result.put("paySign", WXPayUtil.HMACSHA256(paySign, config.getKey()));
		        System.out.println(result);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		
}
