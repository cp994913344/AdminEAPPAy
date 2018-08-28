package com.cnpc.framework.util.wxpay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpc.framework.exception.HttpUtilException;
import com.cnpc.framework.util.wxpay.MyConfig;
import com.cnpc.framework.utils.HttpUtil;

@Controller
@RequestMapping("/wxpay")
public class WXPayController {
    @RequestMapping(value="/pack_mall_api/getOpenId/{code}",method = RequestMethod.POST)
    @ResponseBody
    public String get(@PathVariable("code") String code){
    	MyConfig config = new MyConfig();
    	String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+config.getAppID()+"&secret="+config.getSecret()+"&js_code="+code+"&grant_type=authorization_code";
        try {
			return HttpUtil.httpGet(url).getString("openid");
		} catch (HttpUtilException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
}
