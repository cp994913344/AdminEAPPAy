package com.cnpc.packmall.center.service.impl;

import com.cnpc.packmall.center.entity.Client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.packmall.center.service.ClientService;

/**
* 客户管理服务实现
* @author WY
* 2018-08-16 10:21:17由代码生成器自动生成
*/
@Service("clientService")
public class ClientServiceImpl extends BaseServiceImpl implements ClientService {

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
		return this.get(hql, params);
	}
}
