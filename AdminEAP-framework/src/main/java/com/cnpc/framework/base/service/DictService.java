package com.cnpc.framework.base.service;

import java.util.List;

import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.pojo.TreeNode;

public interface DictService extends BaseService {

    List<TreeNode> getTreeData();

    List<Dict> getDictsByCode(String code);

    /**
     * 商品新增保存字典
     * @param dict
     * @return
     */
    Result saveEntity(Dict dict);
}
