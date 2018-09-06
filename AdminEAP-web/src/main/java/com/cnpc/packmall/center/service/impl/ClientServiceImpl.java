package com.cnpc.packmall.center.service.impl;

import bsh.StringUtil;
import com.cnpc.framework.base.entity.SysFile;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.SysFileService;
import com.cnpc.packmall.center.entity.Client;

import java.util.*;

import com.cnpc.packmall.product.entity.Product;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.packmall.center.service.ClientService;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;

/**
* 客户管理服务实现
* @author WY
* 2018-08-16 10:21:17由代码生成器自动生成
*/
@Service("clientService")
public class ClientServiceImpl extends BaseServiceImpl implements ClientService {

    @Resource
    private  SysFileService sysFileService;

    /**
     *  修改客户禁用
     * @param id
     * @return
     */
    @Override
    public boolean updateDeleted(String id) {
        Client client = this.baseDao.get(Client.class,id);
        if(client!=null){
            if(client.getDeleted()==1){
                client.setDeleted(0);
            }else{
                client.setDeleted(1);
            }
            this.baseDao.update(client);
            return true;
        }
        return false;
    }

	@Override
	public Client getByOpenId(String openId) {
		Map<String, Object> params = new HashMap<>();
		String hql = "from Client where openId =:openId";
		params.put("openId", openId);
		Client client =  this.get(hql, params);
		if(client!=null){
		    SysFile sysFile = sysFileService.findByFormId(client.getId());
		    if(sysFile!=null){
                client.setHeadImgId(sysFile.getId());
            }

        }
        return client;
	}

    /**
     *
     *
     * 新增客户信息
     * @param client
     * @return
     */
    @Override
    public Result saveClient(Client client) {
        if(client==null||StringUtils.isEmpty(client.getOpenId())){
            return new Result(false, "请先用微信登陆");
        }
        if(client!=null&&StringUtils.isNotEmpty(client.getClientName())
                &&StringUtils.isNotEmpty(client.getClientPhone())&&StringUtils.isNotEmpty(client.getOpenId())&&StringUtils.isNotEmpty(client.getClientType())){
            Client hasClient =  getByOpenId(client.getOpenId());
            if(hasClient!=null){
                return new Result(false, "已注册");
            }
            Calendar calendar = Calendar.getInstance();
            String clientCode =String.valueOf(calendar.get(Calendar.YEAR)).substring(2,4);
            clientCode = "C"+clientCode+(calendar.get(Calendar.MONTH)+1)+calendar.get(Calendar.DATE);
            String hql = "from Client where clientCode like  '"+ clientCode+"%' order by clientCode desc";
            List<Client> lastClientList = this.baseDao.find(hql,1,1);
            if(lastClientList!=null&&lastClientList.size()>0){
                Client lastClient = lastClientList.get(0);
                String lastClientCode = lastClient.getClientCode();
                Integer lastN = Integer.parseInt(lastClientCode.replace(clientCode,"0"))+1;
                try{
                    if(lastN>0&&lastN<10){
                        clientCode=clientCode+"00"+lastN;
                    }else if(lastN<100&&lastN>=10){
                        clientCode=clientCode+"0"+lastN;
                    }else if(lastN>=100){
                        clientCode=clientCode+lastN;
                    }
                }catch (ClassCastException e){
                    return new Result(false);
                }catch (NumberFormatException exception){
                    return new Result(false);
                }
            }else{
                clientCode = clientCode+"001";
            }
            if(StringUtils.isEmpty(clientCode)){
                return new Result(false);
            }
            client.setClientCode(clientCode);
            client.setDeleted(0);
            client.setCreateDateTime(new Date());
            client.setVersion(0);
            this.baseDao.save(client);
            if(StringUtils.isNotEmpty(client.getId())&&StringUtils.isNotEmpty(client.getHeadImgId())) {
                SysFile sysFile =  this.baseDao.get(SysFile.class,client.getHeadImgId());
                if(sysFile!=null){
                    sysFile.setFormId(client.getId());
                    sysFile.setCreateUserId(client.getId());
                    this.baseDao.save(sysFile);
                }
            }
        }
        return new Result(true);
    }
}
