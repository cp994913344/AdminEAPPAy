package com.cnpc.framework.base.service.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cnpc.framework.base.pojo.Result;
import javassist.bytecode.stackmap.BasicBlock;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.pojo.TreeNode;
import com.cnpc.framework.base.service.DictService;
import com.cnpc.framework.constant.RedisConstant;
import com.cnpc.framework.utils.StrUtil;
import com.cnpc.framework.utils.TreeUtil;

@Service("dictService")
public class DictServiceImpl extends BaseServiceImpl implements DictService {

    @Override
    public List<TreeNode> getTreeData() {

        // 获取数据
        String key = RedisConstant.DICT_PRE+"tree";
        List<TreeNode> tnlist = null;
        String tnStr = redisDao.get(key);
        if(!StrUtil.isEmpty(key)) {
            tnlist = JSON.parseArray(tnStr,TreeNode.class);
        }
        if (tnlist != null) {
            return tnlist;
        } else {
            String hql = "from Dict order by levelCode asc";
            List<Dict> dicts = this.find(hql);
            Map<String, TreeNode> nodelist = new LinkedHashMap<String, TreeNode>();
            for (Dict dict : dicts) {
                TreeNode node = new TreeNode();
                node.setText(dict.getName());
                node.setId(dict.getId());
                node.setParentId(dict.getParentId());
                node.setLevelCode(dict.getLevelCode());
                nodelist.put(node.getId(), node);
            }
            // 构造树形结构
            tnlist = TreeUtil.getNodeList(nodelist);
            redisDao.save(key, tnlist);
            return tnlist;
        }
    }

    public List<Dict> getDictsByCode(String code) {
        String key = RedisConstant.DICT_PRE+ code;
        List dicts = redisDao.get(key, List.class);
        if (dicts == null) {
            String hql = "from Dict where code='" + code + "'";
            Dict dict = this.get(hql);
            dicts = this.find("from Dict where parentId='" + dict.getId() + "' order by levelCode");
            redisDao.add(key, dicts);
            return dicts;
        } else {
            return dicts;
        }

    }

    @Override
    public Result saveEntity(Dict dict) {
        String hql = "from Dict where code='" + dict.getCode() + "'";
        Dict parentDict = this.get(hql);
        if(parentDict!=null){
            //判断是否重名
            Dict dictOther = this.get("from Dict where parentId='" + parentDict.getId() + "' and name ='"+dict.getName()+"'");
            if(dictOther!=null){
                return new Result(false,"",null,"002");
            }
            //获取是否同等级最后添加字典纸
            // Dict dictLast = this.getBySql("select * from tbl_dict d where d.parent_Id='" + parentDict.getId() + "' order by d.levelCode desc limit 0,1");
            List<Dict> dictLastList = this.find("from Dict where parentId='" + parentDict.getId() + "' order by levelCode desc ",1,1);
            if(dictLastList!=null&&dictLastList.size()>0){
                Dict dictLast = dictLastList.get(0);
                if(dictLast!=null){
                    //处理 code
                    try {
                        String codeIndex = dictLast.getCode().replace(dict.getCode(), "");
                        int codeI = Integer.parseInt(codeIndex)+1;
                        if (codeI < 10) {
                            dict.setCode(parentDict.getCode() + "0" + codeI);
                        } else {
                            dict.setCode(parentDict.getCode() + codeI);
                        }
                        String levelCodeIndex = dictLast.getLevelCode().replace(parentDict.getLevelCode(), "");
                        String levelCodeI = String.valueOf(Integer.valueOf(levelCodeIndex) + 1)  ;
                        int zeroLenth = 6-levelCodeI.length();
                        String levelCode = parentDict.getLevelCode();
                        for(int i=0;i<zeroLenth;i++){
                            levelCode+="0";
                        }
                        dict.setLevelCode(levelCode+levelCodeI);
                    }catch (ClassCastException e){
                        return new Result(false);
                    }catch (NumberFormatException exception){
                        return new Result(false);
                    }
                }
            }else{
                dict.setCode(parentDict.getCode()+"01");
                dict.setLevelCode(parentDict.getLevelCode()+"000001");
            }
            dict.setParentId(parentDict.getId());
            dict.setCreateDateTime(new Date());
            dict.setUpdateDateTime(new Date());
            dict.setDeleted(0);
            dict.setVersion(0);
            this.save(dict);
            if (!StrUtil.isEmpty(dict.getParentId())) {
                Dict parent = this.get(Dict.class, dict.getParentId());
                this.deleteCacheByKey(RedisConstant.DICT_PRE + parent.getCode());
            }
            this.deleteCacheByKey(RedisConstant.DICT_PRE+"tree");
            return new Result(true,dict);
        }
        return new Result(false);
    }
}
