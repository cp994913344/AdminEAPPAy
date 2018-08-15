package com.cnpc.framework.base.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.alibaba.fastjson.JSONObject;
import com.cnpc.framework.base.entity.Function;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.ResultCode;
import com.cnpc.framework.base.service.FunctionService;
import com.cnpc.framework.base.service.RoleService;
import com.cnpc.framework.base.service.UserRoleService;
import com.cnpc.framework.base.service.UserService;
import com.cnpc.framework.exception.HttpUtilException;
import com.cnpc.framework.oauth.common.CustomOAuthService;
import com.cnpc.framework.oauth.entity.OAuthUser;
import com.cnpc.framework.oauth.service.OAuthServices;
import com.cnpc.framework.oauth.service.OAuthUserService;
import com.cnpc.framework.util.SecurityUtil;
import com.cnpc.framework.util.SendMsgUtil;
import com.cnpc.framework.utils.EncryptUtil;
import com.cnpc.framework.utils.HttpUtil;
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.StrUtil;

@Controller
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private RoleService roleService;

    @Resource
    private UserService userService;

    @Autowired
    OAuthServices oAuthServices;

    @Resource
    OAuthUserService oAuthUserService;

    @Resource
    private FunctionService functionService;

    @Resource
    private UserRoleService userRoleService;


    private final static String MAIN_PAGE = PropertiesUtil.getValue("page.main");
    private final static String LOGIN_PAGE = PropertiesUtil.getValue("page.login");
    private final static String REGISTER_PAGE = PropertiesUtil.getValue("page.register");

    @RequestMapping(value = "/login")
    private String doLogin(HttpServletRequest request, Model model) {
        model.addAttribute("oAuthServices", oAuthServices.getAllOAuthServices());
        //已经登录过，直接进入主页
        Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.isAuthenticated()) {
            Object authorized = subject.getSession().getAttribute("isAuthorized");
            //boolean isAuthorized = Boolean.valueOf(subject.getSession().getAttribute("isAuthorized").toString());
            if (authorized != null && Boolean.valueOf(authorized.toString()))
                return MAIN_PAGE;
        }
        String userName = request.getParameter("userName");
        //默认首页，第一次进来
        if (StrUtil.isEmpty(userName)) {
            return LOGIN_PAGE;
        }
        String password = request.getParameter("password");
        //密码加密+加盐
        password = EncryptUtil.getPassword(password, userName);
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        token.setRememberMe(true);
        subject = SecurityUtils.getSubject();
        String msg;
        try {
            subject.login(token);
            //通过认证
            if (subject.isAuthenticated()) {
                String userId = subject.getPrincipal().toString();
                Set<String> roles = roleService.getRoleCodeSet(userId);
                //Set<String> functions = functionService.getFunctionCodeSet(roles, userId);
                //---------调用realm doGetAuthorizationInfo----------
                boolean isPermitted = subject.isPermitted("user");
                if (!roles.isEmpty()) {
                    subject.getSession().setAttribute("isAuthorized", true);
                    return MAIN_PAGE;
                } else {//没有授权
                    msg = "您没有得到相应的授权！";
                    model.addAttribute("message", new ResultCode("1", msg));
                    subject.getSession().setAttribute("isAuthorized", false);
                    LOGGER.error(msg);
                    return LOGIN_PAGE;
                }

            } else {
                return LOGIN_PAGE;
            }
            //0 未授权 1 账号问题 2 密码错误  3 账号密码错误
        } catch (IncorrectCredentialsException e) {
            msg = "登录密码错误. Password for account " + token.getPrincipal() + " was incorrect";
            model.addAttribute("message", new ResultCode("2", msg));
            LOGGER.error(msg);
        } catch (ExcessiveAttemptsException e) {
            msg = "登录失败次数过多";
            model.addAttribute("message", new ResultCode("3", msg));
            LOGGER.error(msg);
        } catch (LockedAccountException e) {
            msg = "帐号已被锁定. The account for username " + token.getPrincipal() + " was locked.";
            model.addAttribute("message", new ResultCode("1", msg));
            LOGGER.error(msg);
        } catch (DisabledAccountException e) {
            msg = "帐号已被禁用. The account for username " + token.getPrincipal() + " was disabled.";
            model.addAttribute("message", new ResultCode("1", msg));
            LOGGER.error(msg);
        } catch (ExpiredCredentialsException e) {
            msg = "帐号已过期. the account for username " + token.getPrincipal() + "  was expired.";
            model.addAttribute("message", new ResultCode("1", msg));
            LOGGER.error(msg);
        } catch (UnknownAccountException e) {
            msg = "帐号不存在. There is no user with username of " + token.getPrincipal();
            model.addAttribute("message", new ResultCode("1", msg));
            LOGGER.error(msg);
        } catch (UnauthorizedException e) {
            msg = "您没有得到相应的授权！" + e.getMessage();
            model.addAttribute("message", new ResultCode("1", msg));
            LOGGER.error(msg);
        }
        return LOGIN_PAGE;
    }


    @RequestMapping(value = "/function/getlist", method = RequestMethod.POST)
    @ResponseBody
    public List<Function> getUserFunctions() {
        List<Function> functionList = new ArrayList<>();
        User user = SecurityUtil.getUser();
        Set<String> roles = roleService.getRoleCodeSet(user.getId());
        if ("1".equals(user.getIsSuperAdmin())) {
            functionList = functionService.getAll();
        } else {
            functionList = functionService.getFunctionList(roles, user.getId());
        }

        Map<String,Function> map=new HashMap<>();
        for (Function function : functionList) {
            if(StrUtil.isNotBlank(function.getQueryId())&&!map.containsKey(function.getQueryId())){
                map.put(function.getQueryId(),function);
            }
        }
        SecurityUtils.getSubject().getSession().setAttribute("functionMap", map);
        return functionList;
    }





 /*   @RequestMapping(value = "/logout")
    private String doLogout(HttpServletRequest request) {
        request.setAttribute("oAuthServices", oAuthServices.getAllOAuthServices());
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return LOGIN_PAGE;
    }*/

    /**
     * 注销调用此方法，需要注释request.setAttribute，因为会话删除后会出现问题，必须使用redirect:/login代替 LOGIN_PAGE
     * 还有可以使用SystemLogoutFilter进行重定向
     * 具体使用哪种方式，详见spring-shiro.xml的配置，本项目没使用SystemLogoutFilter
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout")
    private String doLogout(HttpServletRequest request) {
        //request.setAttribute("oAuthServices", oAuthServices.getAllOAuthServices());
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        model.addAttribute("oAuthInfo", new OAuthUser());
        return REGISTER_PAGE;
    }


    //----------------oauth 认证------------------
    @RequestMapping(value = "/oauth/{type}/callback", method = RequestMethod.GET)
    public String callback(@RequestParam(value = "code", required = true) String code, @PathVariable(value = "type") String type,
                           HttpServletRequest request, Model model) {
        model.addAttribute("oAuthServices", oAuthServices.getAllOAuthServices());
        try {
            CustomOAuthService oAuthService = oAuthServices.getOAuthService(type);
            Token accessToken = oAuthService.getAccessToken(null, new Verifier(code));
            //第三方授权返回的用户信息
            OAuthUser oAuthInfo = oAuthService.getOAuthUser(accessToken);
            //查询本地数据库中是否通过该方式登陆过
            OAuthUser oAuthUser = oAuthUserService.findByOAuthTypeAndOAuthId(oAuthInfo.getoAuthType(), oAuthInfo.getoAuthId());
            //未建立关联，转入用户注册界面
            if (oAuthUser == null) {
                model.addAttribute("oAuthInfo", oAuthInfo);
                return REGISTER_PAGE;
            }

            //如果已经过关联，直接登录
            User user = userService.get(User.class, oAuthUser.getUserId());
            return loginByAuth(user);
        } catch (Exception e) {
            String msg = "连接" + type + "服务器异常. 错误信息为：" + e.getMessage();
            model.addAttribute("message", new ResultCode("1", msg));
            LOGGER.error(msg);
            return LOGIN_PAGE;
        }

    }

    @RequestMapping(value = "/oauth/register", method = RequestMethod.POST)
    public String register_oauth(User user, @RequestParam(value = "oAuthType", required = false, defaultValue = "") String oAuthType,
                                 @RequestParam(value = "oAuthId", required = true, defaultValue = "") String oAuthId,
                                 HttpServletRequest request, Model model) {
        model.addAttribute("oAuthServices", oAuthServices.getAllOAuthServices());
        OAuthUser oAuthInfo = new OAuthUser();
        oAuthInfo.setoAuthId(oAuthId);
        oAuthInfo.setoAuthType(oAuthType);
        //保存用户
        user.setPassword(EncryptUtil.getPassword(user.getPassword(), user.getLoginName()));
        String userId = userService.save(user).toString();
        //建立第三方账号关联
        OAuthUser oAuthUser = oAuthUserService.findByOAuthTypeAndOAuthId(oAuthType, oAuthId);
        if (oAuthUser == null && !oAuthType.equals("-1")) {
            oAuthInfo.setUserId(userId);
            oAuthUserService.save(oAuthInfo);
        }
        //关联一般用户权限
        userRoleService.setRoleForRegisterUser(userId);
        //关联成功后登陆
        return loginByAuth(user);
    }


    public String loginByAuth(User user) {
        UsernamePasswordToken token = new UsernamePasswordToken(user.getLoginName(), user.getPassword());
        token.setRememberMe(true);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        //通过认证
        if (subject.isAuthenticated()) {
            return MAIN_PAGE;
        } else {
            return LOGIN_PAGE;
        }
    }


    /**
     * 校验当前登录名/邮箱的唯一性
     *
     * @param loginName 登录名
     * @param userId    用户ID（用户已经存在，改回原来的名字还是唯一的）
     * @return
     */
    @RequestMapping(value = "/oauth/checkUnique", method = RequestMethod.POST)
    @ResponseBody
    public Map checkExist(String loginName, String userId) {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        User user = userService.getUserByLoginName(loginName);
        //用户不存在，校验有效
        if (user == null) {
            map.put("valid", true);
        } else {
            if (!StrUtil.isEmpty(userId) && userId.equals(user.getLoginName())) {
                map.put("valid", true);
            } else {
                map.put("valid", false);
            }
        }
        return map;
    }
    
    /**
     * 校验当前登录名/邮箱的唯一性
     *
     * @param loginName 登录名
     * @param userId    用户ID（用户已经存在，改回原来的名字还是唯一的）
     * @return
     */
    @RequestMapping(value = "/oauth/nbox/checkUnique", method = RequestMethod.POST)
    @ResponseBody
    public Map nboxCheckExist(String loginName) {
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userService.getUserByLoginName(loginName);
        //用户不存在，校验有效
        if (user == null) {
            map.put("valid", true);
        } else {
            map.put("valid", false);
        }
        map.put("statusCode", "0000");
        return map;
    }
    
    /**
     * getVerificationCode
     * 获取验证码
     * @param phone
     * @return
     */
    @RequestMapping(value = "/oauth/getVerificationCode", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getVerificationCode(String phone,HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        //发送验证码
        try {
            result = SendMsgUtil.sendSmsCode(phone);
            Subject subject = SecurityUtils.getSubject();
            subject.getSession().setAttribute("verificationCodeTime", new Date().getTime()+5*60*1000);
            subject.getSession().setAttribute("verificationCode", result.get("code"));
            subject.getSession().setAttribute("phone", phone);
            result.put("statusCode", "0000");
//            String code = SendMsgUtil.getRandNum(6);
//            System.out.println(code);
//            Subject subject = SecurityUtils.getSubject();
//            subject.getSession().setAttribute("verificationCodeTime", new Date().getTime()+5*60*1000);
//            subject.getSession().setAttribute("verificationCode", code);
//            subject.getSession().setAttribute("phone", phone);
//            result.put("code", code);
//            result.put("statusCode", "0000");
        } catch (Exception e) {
            result.put("statusCode", "0005");
            result.put("msg", "服务器异常");
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * nbox_register_oauth
     * nbox注册并登陆
     * @param user
     * @param oAuthType
     * @param oAuthId
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/oauth/nbox/register", method = RequestMethod.POST)
    public String nbox_register_oauth(User user,String verificationCode,String clientId,String clientSecret,HttpServletRequest request,RedirectAttributes redirectAttributes) {
        Subject subject = SecurityUtils.getSubject();
        Long time = (Long)subject.getSession().getAttribute("verificationCodeTime");
        String code = subject.getSession().getAttribute("verificationCode")+"";
        String phone = subject.getSession().getAttribute("phone")+"";
        if(!StrUtil.isBlankOrNull(time+"")&&(time-new Date().getTime())>0&&!StrUtil.isBlankOrNull(verificationCode)&&verificationCode.equals(code)&&user.getLoginName().equals(phone)){
            User userCheck = userService.getUserByLoginName(user.getLoginName());
            //用户不存在，校验有效
            if (userCheck == null) {
                //保存用户
                user.setPassword(EncryptUtil.getPassword(user.getLoginName(), user.getLoginName()));
                user.setMobile(user.getLoginName());
                user.setDeleted(0);
                String userId = userService.save(user).toString();
                //关联一般用户权限
                userRoleService.setRoleForRegisterUser(userId);
            } 
        }else{
            redirectAttributes.addAttribute("msg", "验证码错误");
            return "redirect:/oauth/loginFail";
        }
        redirectAttributes.addAttribute("userName", user.getLoginName());
        redirectAttributes.addAttribute("verificationCode",code);
        redirectAttributes.addAttribute("clientId", clientId);
        redirectAttributes.addAttribute("clientSecret",clientSecret);
        //关联成功后登陆
        return "redirect:/oauth/login";
    }
    
    @RequestMapping(value = "/oauth/login")
    public String oauthLogin(String userName,String password123,String verificationCode,String clientId,String clientSecret,HttpServletRequest request,RedirectAttributes redirectAttributes) {
        Subject subject = SecurityUtils.getSubject();
        //TODO nbox 用户登陆默认密码和用户名相同 暂不启用验证码登陆 上线时启用
        Long time = (Long)subject.getSession().getAttribute("verificationCodeTime");
        String code = subject.getSession().getAttribute("verificationCode")+"";
        String phone = subject.getSession().getAttribute("phone")+"";
        if(!StrUtil.isBlankOrNull(time+"")&&(time-new Date().getTime())>0&&!StrUtil.isBlankOrNull(verificationCode)&&verificationCode.equals(code)&&userName.equals(phone)){
            password123 = userName;
        }else{
            redirectAttributes.addAttribute("msg", "验证码错误");
            return "redirect:/oauth/loginFail";
        }
        //密码加密+加盐
        password123 = EncryptUtil.getPassword(password123, userName);
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password123);
        token.setRememberMe(true);
        subject = SecurityUtils.getSubject();
        String msg="登陆失败";
        try {
            subject.login(token);
            //通过认证
            if (subject.isAuthenticated()) {
                String userId = subject.getPrincipal().toString();
                Set<String> roles = roleService.getRoleCodeSet(userId);
                if (!roles.isEmpty()) {
                    subject.getSession().setAttribute("isAuthorized", true);
                    redirectAttributes.addAttribute("userId",userId);
                    redirectAttributes.addAttribute("clientId",clientId);
                    redirectAttributes.addAttribute("clientSecret",clientSecret);
                    //登陆成功 重定向到授权服务器生成授权code
                    return "redirect:/oauth/code";
                } else {//没有授权
                    msg = "您没有得到相应的授权！";
                    redirectAttributes.addAttribute("msg",msg);
                    return "redirect:/oauth/loginFail";
                }

            }
        } catch (IncorrectCredentialsException e) {
            msg = "登录密码错误. Password for account " + token.getPrincipal() + " was incorrect";
        } catch (ExcessiveAttemptsException e) {
            msg = "登录失败次数过多";
        } catch (LockedAccountException e) {
            msg = "帐号已被锁定. The account for username " + token.getPrincipal() + " was locked.";
        } catch (DisabledAccountException e) {
            msg = "帐号已被禁用. The account for username " + token.getPrincipal() + " was disabled.";
        } catch (ExpiredCredentialsException e) {
            msg = "帐号已过期. the account for username " + token.getPrincipal() + "  was expired.";
        } catch (UnknownAccountException e) {
            msg = "帐号不存在. There is no user with username of " + token.getPrincipal();
        } catch (UnauthorizedException e) {
            msg = "您没有得到相应的授权！" + e.getMessage();
        }
        redirectAttributes.addAttribute("msg",msg);
        return "redirect:/oauth/loginFail";
    }
    
    /**
     * loginFail
     * Nbox登陆失败返回
     * @param msg
     * @return
     */
    @RequestMapping(value = "/oauth/loginFail")
    @ResponseBody
    public Object loginFail(String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("statusCode", "0004");
        jsonObject.put("successMsg", msg);
        return jsonObject;
    }
    
    /**
     * oauthCode
     * 用户授权获取Code
     * @param clientId
     * @param userId
     * @param request
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/oauth/code")
    public String oauthCode(String clientId,String userId,String clientSecret,HttpServletRequest request,RedirectAttributes redirectAttributes) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        JSONObject jsonObject = new JSONObject();
        if(subject.isAuthenticated()){
            String url = PropertiesUtil.getValue("Oauth.code.url");
            String param = PropertiesUtil.getValue("Oauth.code.param");
            
            param=param.replace("clientId", clientId);
            param=param.replace("user_id", userId);
            String result = HttpUtil.sendGet(url+"?"+param,"UTF-8" );
            jsonObject = JSONObject.parseObject(result);
            //获取code成功 重定向到获取token的url
            redirectAttributes.addAttribute("code",jsonObject.get("code"));
            redirectAttributes.addAttribute("clientId",clientId);
            redirectAttributes.addAttribute("clientSecret",clientSecret);
            return "redirect:/oauth/accessToken";
        }
        redirectAttributes.addAttribute("msg","登陆失败");
        return "redirect:/oauth/loginFail";
    }
    /**
     * oauthCode
     * 用户授权获取Code
     * @param clientId
     * @param userId
     * @param request
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/oauth/accessToken")
    @ResponseBody
    public Object accessToken(String clientId,String clientSecret,String code,HttpServletRequest request,RedirectAttributes redirectAttributes) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        JSONObject jsonObject = new JSONObject();
        if(subject.isAuthenticated()){
            String url = PropertiesUtil.getValue("Oauth.token.url");
            Map<String, String>  param = new HashMap<>();
            param.put("client_id", clientId);
            param.put("client_secret", clientSecret);
            param.put("code", code);
            param.put("grant_type", "authorization_code");
            param.put("redirect_uri", "redirect_uri");
            
            String result = HttpUtil.sendPost(url, param, "UTF-8");
            jsonObject = JSONObject.parseObject(result);
            jsonObject.put("statusCode", "0000");
            jsonObject.put("successMsg", "登陆成功");
            return jsonObject;
        }else{
            jsonObject.put("statusCode", "0004");
            jsonObject.put("successMsg", "登陆失败");
        }
        return jsonObject;
    }
}
