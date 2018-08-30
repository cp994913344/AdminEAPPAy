package com.cnpc.framework.util.wxpay.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.http.HttpRequest;
import com.cnpc.framework.util.wxpay.MyConfig;
import com.cnpc.framework.utils.HttpUtil;

@Controller
@RequestMapping("/wxpay")
public class WXPayController {
	
    @RequestMapping(value="/pack_mall_api/getOpenId/{code}")
    @ResponseBody
    public String get(@PathVariable("code") String code){
    	MyConfig config = new MyConfig();
    	String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+config.getAppID()+"&secret="+config.getSecret()+"&js_code="+code+"&grant_type=authorization_code";
        return JSONObject.parseObject(HttpUtil.sendGet(url,"utf-8")).getString("openid");
    }
    
    @RequestMapping(value="/pack_mall_api/notify")
    @ResponseBody
    public Map<String, String> notify(HttpServletResponse response,HttpServletRequest request){
    	
    	// 将返回的输入流转换成字符串
		InputStream is = null;
		try {
			is = request.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // 使用dom4j解析xml字符串  
        SAXReader reader = new SAXReader();  
        Document document = null;
		try {
			document = reader.read(is);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        // 得到xml根元素  
        Element root = document.getRootElement();  
        
        String out_trade_no = root.element("out_trade_no").getText();    //订单号
        String count2 = root.element("total_fee").getText();    //金额
        String count3 = root.element("result_code").getText();  //支付状态
        if("SUCCESS".equals(count3)){
        	System.out.println("订单处理成功");
//        	if (StringUtils.isNotBlank(out_trade_no)) {
//			}
        }
        Map<String, String> result = new HashMap<>();
        result.put("return_code", "SUCCESS");
        result.put("return_msg", "");
        return result;
    }
}
