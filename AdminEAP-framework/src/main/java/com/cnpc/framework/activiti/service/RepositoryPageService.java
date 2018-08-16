package com.cnpc.framework.activiti.service;

import java.util.List;

import org.activiti.engine.repository.Model;

import com.cnpc.framework.activiti.pojo.ProcessDefVo;
import com.cnpc.framework.base.pojo.PageInfo;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.query.entity.QueryCondition;

/**
 * Created by billJiang on 2017/6/8.
 * e-mail:475572229@qq.com  qq:475572229
 * 流程定义服务接口
 */
public interface RepositoryPageService extends BaseService {
    List<ProcessDefVo> getProcessDefList(QueryCondition condition, PageInfo pageInfo);

    /**
     * 根据分页获取模型列表
     * @param condition
     * @param pageInfo
     * @return
     */
    List<Model> getModelList(QueryCondition condition, PageInfo pageInfo);
}
