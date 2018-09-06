package com.cnpc.packmall.invoice.service.impl;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.framework.util.wxpay.MyConfig;
import com.cnpc.framework.util.wxpay.WXPay;
import com.cnpc.framework.util.wxpay.WXPayUtil;
import com.cnpc.packmall.invoice.entity.InvoiceDedicated;
import com.cnpc.packmall.invoice.entity.InvoiceNormal;
import com.cnpc.packmall.invoice.entity.InvoiceWXPay;
import com.cnpc.packmall.invoice.service.InvoiceNormalService;
import com.cnpc.packmall.invoice.service.InvoiceWXPayService;
import com.cnpc.packmall.order.entity.Order;
import com.cnpc.packmall.order.entity.OrderWXPay;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
* 发票微信服务实现
* @author WY
* 2018-08-16 14:37:42由代码生成器自动生成
*/
@Service("invoiceWXPayService")
public class InvoiceWXPayServiceImpl extends BaseServiceImpl implements InvoiceWXPayService {


    @Override
    public Map<String, String> doInvoicePay(String invoiceId, String invoiceType, String price, String openId){
        //查询是否存在支付id
        InvoiceWXPay invoiceWXPay = getPrepayIdByInvoiceId(invoiceId);
        if(invoiceWXPay!=null&&StringUtils.isNotEmpty(invoiceWXPay.getPrepayId())){
            return paySign(invoiceWXPay.getPrepayId());
        }
        String message = "";
        if("1".equals(invoiceType)){
            InvoiceDedicated invoiceDedicated = this.get(InvoiceDedicated.class,invoiceId);
            message = invoiceDedicated.getInvoiceTaxpayer();
        }else if("2".equals(invoiceType)){
            InvoiceNormal invoiceNormal= this.get(InvoiceNormal.class,invoiceId);
            message = invoiceNormal.getInvoiceTaxpayer();
        }
        Map<String, String> wxResult = doWXPay(invoiceId,openId,message,price,invoiceType);
        return paySign(wxResult.get("prepay_id"));
    }

    @Override
    public InvoiceWXPay getPrepayIdByInvoiceId(String invoiceId){
        Map<String,Object> params = new HashMap<>();
        String hql = "from InvoiceWXPay where invoiceId =:invoiceId";
        params.put("invoiceId", invoiceId);
        InvoiceWXPay invoiceWXPay = this.get(hql,params);
        if(invoiceWXPay!=null){
            return invoiceWXPay;
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

    public Map<String, String> doWXPay(String invoiceId,String openId,String body,String price,String invoiceType){
        if(StringUtils.isEmpty(invoiceId)||StringUtils.isEmpty(openId)||StringUtils.isEmpty(price)||StringUtils.isEmpty(invoiceType)){
            return null;
        }
        MyConfig config = new MyConfig();
        try {
            WXPay wxpay = new WXPay(config);
            Map<String, String> data = new HashMap<String, String>();
            //商品描述
            data.put("body", body);
            //商户订单号
            data.put("out_trade_no", invoiceId);
            data.put("fee_type", "CNY");
            //TODO 上线修改为正常金额 以及回调地址修改
            data.put("total_fee", "1");
            data.put("notify_url", "https://weixin.yiside.cn/wxpay/pack_mall_api/invoiceNotify");
            data.put("trade_type", "JSAPI");  // 此处指定为小程序支付
            data.put("openid", openId);
            Map<String, String> resp = wxpay.unifiedOrder(data);
            //保存签名等待回调验证
            InvoiceWXPay invoiceWXPay = new InvoiceWXPay();
            invoiceWXPay.setInvoiceId(invoiceId);
            invoiceWXPay.setInvoiceType(Integer.parseInt(invoiceType));
            invoiceWXPay.setPrepayId(resp.get("prepay_id"));
            invoiceWXPay.setSign(resp.get("sign"));
            invoiceWXPay.setRemark(body);
            invoiceWXPay.setCreateDateTime(new Date());
            invoiceWXPay.setDeleted(0);
            invoiceWXPay.setVersion(0);
            this.save(invoiceWXPay);
            System.out.println(resp);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

}
