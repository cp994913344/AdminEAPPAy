package com.cnpc.framework.util.wxpay;

import java.io.InputStream;

public class MyConfig extends WXPayConfig{


    public String getAppID() {
        return "wxd8ba58498aa5ee7a";
    }

    public String getMchID() {
        return "1344168901";
    }

    public String getKey() {
        return "f290ec7bdc38ed9a5d835db9a1809c6a";
    }
    
    public String getSecret() {
        return "18488321ebc275258503d2318b60a454";
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

	public InputStream getCertStream() {
		// TODO Auto-generated method stub
		return null;
	}

	IWXPayDomain getWXPayDomain() {
		
		return WXPayDomainSimpleImpl.instance();
	}


}
