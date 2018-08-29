package com.cnpc.framework.util.wxpay.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.util.SystemOutLogger;
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
    @RequestMapping(value="/pack_mall_api/getOpenId/{code}")
    @ResponseBody
    public String get(@PathVariable("code") String code){
    	MyConfig config = new MyConfig();
    	String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+config.getAppID()+"&secret="+config.getSecret()+"&js_code="+code+"&grant_type=authorization_code";
        return JSONObject.parseObject(HttpUtil.sendGet(url,"utf-8")).getString("openid");
    }
}
