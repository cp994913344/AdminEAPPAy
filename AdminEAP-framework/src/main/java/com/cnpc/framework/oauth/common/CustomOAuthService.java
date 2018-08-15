package com.cnpc.framework.oauth.common;

import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import com.cnpc.framework.oauth.entity.OAuthUser;

/**
 * Created by billJiang on 2017/1/15.
 * e-mail:jrn1012@petrochina.com.cn qq:475572229
 */
public interface CustomOAuthService extends OAuthService {
    String getoAuthType();
    String getAuthorizationUrl();
    OAuthUser getOAuthUser(Token accessToken);
    String getBtnClass();
}
