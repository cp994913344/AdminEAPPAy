package com.cnpc.packmall.center.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.exceptions.ClientException;
import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.entity.SysFile;
import com.cnpc.framework.base.service.SysFileService;
import com.cnpc.framework.util.SendMsgUtil;
import com.cnpc.framework.utils.StrUtil;
import com.cnpc.packmall.center.entity.Client;
import com.cnpc.packmall.center.service.ClientService;
import com.cnpc.packmall.product.entity.Product;
import com.cnpc.packmall.product.entity.ProductDetail;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.Odd;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.Result;

/**
* 客户管理管理控制器
* @author jrn
* 2018-08-16 10:21:17由代码生成器自动生成
*/
@Controller
@RequestMapping("/packmall/client")
public class ClientController {

    @Resource
    private ClientService clientService;

    @Resource
    private SysFileService sysFileService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "packmall/center/client_list";
    }

    @RequestMapping(value="/updateDeleted",method = RequestMethod.POST)
    @ResponseBody
    public boolean updateDeleted(String id){
        if(StringUtils.isEmpty(id)){
            return false;
        }
        return clientService.updateDeleted(id);
    }

    @RequestMapping(value="/toHeadImgHtml",method = RequestMethod.GET)
    public String toHeadImgHtml(String clientId,HttpServletRequest request){
        request.setAttribute("clientId",clientId);
        return "packmall/center/client_headImg_view";
    }



    @RequestMapping(value="/detail",method = RequestMethod.GET)
    public String detail(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "packmall/center/client_detail";
    }

    @RequestMapping(value="/get/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Client get(@PathVariable("id") String id){
        return clientService.get(Client.class, id);
    }

    @VerifyCSRFToken
    @RequestMapping(value="/save")
    @ResponseBody
    public Result save(Client client){
        if(StrUtil.isEmpty(client.getId())){
            clientService.save(client);
        }
        else{
            client.setUpdateDateTime(new Date());
            clientService.update(client);
        }
        return new Result(true);
    }



    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id){
        Client client=this.get(id);
        try{
            client.setDeleted(1);
            clientService.update(client);
            return new Result();
        }
        catch(Exception e){
            return new Result(false,"该数据已经被引用，不可删除");
        }
    }

    //————————————————————小程序接口start——————————————————————————

    /**
     * 保存客户
     * @return
     */
    @RequestMapping(value="/pack_mall_api/saveClient",method = RequestMethod.POST)
    @ResponseBody
    public  Result saveClient(HttpServletRequest request,Client client){
        return clientService.saveClient(client);
    }

    /**
     * 修改姓名
     * @param clientName
     * @param openId
     * @return
     */
    @RequestMapping(value="/pack_mall_api/updateName",method = RequestMethod.POST)
    @ResponseBody
    public  Result updateName(String clientName,String enterpriseName,String  openId){
        if(StringUtils.isEmpty(openId)||StringUtils.isEmpty(clientName)){
            return new Result(false);
        }
        Client client =  clientService.getByOpenId(openId);
        if(StringUtils.isNotEmpty(clientName)){
            client.setClientName(clientName);
        }
        if(StringUtils.isNotEmpty(enterpriseName)){
            client.setEnterpriseName(enterpriseName);
        }
        clientService.update(client);
        return  new Result(true);
    }

    /**
     * 换绑手机
     * @param clientPhone
     * @param openId
     * @return
     */
    @RequestMapping(value="/pack_mall_api/updateClientPhone",method = RequestMethod.POST)
    @ResponseBody
    public  Result updateClientPhone(String clientPhone,String openId){
        if(StringUtils.isEmpty(openId)||StringUtils.isEmpty(clientPhone)){
            return new Result(false);
        }
        Client client =  clientService.getByOpenId(openId);
        client.setClientPhone(clientPhone);
        clientService.update(client);
        return  new Result(true);
    }

    //修改头像 姓名
    /**
     * 保存头像姓名 等信息
     * @param headImgId
     * @param openId
     * @param clientName
     * @return
     */
    @RequestMapping(value="/pack_mall_api/updateClientNameOrImg",method = RequestMethod.POST)
    @ResponseBody
    public  Result updateClientNameOrImg(String clientName,String enterpriseName,String headImgId,String openId,String clientType){
        if(StringUtils.isEmpty(openId)){
            return new Result(false);
        }
        Client client =  clientService.getByOpenId(openId);
        if(StringUtils.isNotEmpty(clientName)){
            if(client!=null){
                client.setClientName(clientName);
            }else{
                return new Result(false,"修改客户名称是失败");
            }
        }
        if(StringUtils.isNotEmpty(enterpriseName)){
            client.setEnterpriseName(enterpriseName);
        }
        if(StringUtils.isNotEmpty(clientType)){
            client.setClientType(clientType);
        }
        clientService.update(client);
        if(StringUtils.isNotEmpty(headImgId)&&client!=null&&!headImgId.equals(client.getHeadImgId())){
            //如果 修改头像 先查询原头像  有 删除图片
            SysFile oldFile = sysFileService.findByFormId(client.getId());
            SysFile sysFile = clientService.get(SysFile.class,headImgId);
            if(sysFile!=null){
                //保存新头像
                sysFile.setFormId(client.getId());
                sysFileService.save(sysFile);
                if(oldFile!=null&&oldFile.getFilePath()!=null){
                    //删除 老图片
                    File oldFileImg = new File(oldFile.getFilePath());
                    if(oldFileImg!=null&&oldFileImg.isFile()){
                        oldFileImg.delete();
                    }
                    sysFileService.delete(oldFile);
                }
            }else{
                return new Result(false, "修改头像失败");
            }
        }
        return  new Result(true);
    }



    /**
     *  获取验证码
     * @param clientPhone
     * @return
     */
    @RequestMapping(value="/pack_mall_api/getPhoneCode",method = RequestMethod.POST)
    @ResponseBody
    public  Result getPhoneCode(String clientPhone){
        if(StringUtils.isEmpty(clientPhone)){
            return new Result(false);
        }
        try{
            Map<String, Object> result = SendMsgUtil.sendSmsCode(clientPhone);
            return  new Result(true,result);
        }catch (InterruptedException e){
            return new Result(false, e.getMessage());
        }catch (ClientException e){
            return new Result(false, e.getMessage());
        }
    }

    /**
     *  判断是否已登录
     * @param openId
     * @return
     */
    @RequestMapping(value="/pack_mall_api/judgeRegistered",method = RequestMethod.POST)
    @ResponseBody
    public  Result judgeRegistered(String openId){
        if(StringUtils.isEmpty(openId)){
            return new Result(false,"openId为空");
        }
        Client client = clientService.getByOpenId(openId);
        if(client!=null){
            return new Result(true, client);
        }else{
            return new Result(false, "未注册");
        }
    }

}
