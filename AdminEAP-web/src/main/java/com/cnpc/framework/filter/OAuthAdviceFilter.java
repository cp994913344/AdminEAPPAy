package com.cnpc.framework.filter;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.NboxResult;
import com.cnpc.framework.base.service.UserService;
import com.cnpc.framework.utils.HttpUtil;
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.StrUtil;

public class OAuthAdviceFilter extends AdviceFilter{
    @Resource
    private UserService userService;
    private MultipartHttpServletRequest multipartRequest = null; 
    @Override
    protected boolean preHandle(ServletRequest request,ServletResponse response) throws IOException{
        String userId = null;
        try {
            String contentType = request.getContentType();
            if (contentType!=null&&contentType.indexOf("multipart/form-data") >= 0) {
                ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;  
                CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();  
                multipartRequest = commonsMultipartResolver.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());
                userId = OAuthRequest(multipartRequest.getParameter("access_token"));
            }else{
                userId = OAuthRequest(request.getParameter("access_token"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().print(JSON.toJSON(new NboxResult(false, e.getMessage(), "1002")));
            return false;
        }
        //判断登陆
        Subject subject = SecurityUtils.getSubject();
        if(!subject.isAuthenticated()){
            User user = userService.get(User.class,userId);
            UsernamePasswordToken token = new UsernamePasswordToken(user.getLoginName(), user.getPassword());
            token.setRememberMe(true);
            subject.login(token);
        }
        return true;
       
    }
    
    @Override
    protected void executeChain(ServletRequest request, ServletResponse response, FilterChain chain) throws Exception {
        String contentType = request.getContentType();
        if (contentType!=null&&contentType.indexOf("multipart/form-data") >= 0) {
            chain.doFilter(multipartRequest, response);
        }else{
            chain.doFilter(request, response);
        }
    }
    
    private String OAuthRequest(String token) throws Exception {
        if(StrUtil.isBlankOrNull(token)){
            throw new Exception("TOKEN为空");
        }
        String url = PropertiesUtil.getValue("Oauth.checkToken.url");
        String result=HttpUtil.sendPostUrl(url, "token="+token,"utf-8");
        JSONObject jsonObject = JSONObject.parseObject(result);
        if(!Boolean.valueOf(jsonObject.getString("valid"))){
            throw new Exception("错误的TOKEN");
        }
        JSONObject tokenEntity = JSONObject.parseObject(jsonObject.getString("data"));
        return tokenEntity.getString("userId");
    }
}
