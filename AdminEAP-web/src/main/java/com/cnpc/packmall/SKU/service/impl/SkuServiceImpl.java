package com.cnpc.packmall.SKU.service.impl;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.packmall.SKU.entity.Sku;
import com.cnpc.packmall.SKU.entity.SkuDetail;
import com.cnpc.packmall.center.entity.Client;
import com.cnpc.packmall.center.entity.ShippingAddress;
import com.cnpc.packmall.product.entity.Product;
import com.cnpc.packmall.product.entity.ProductDetail;
import com.cnpc.packmall.util.SortUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.packmall.SKU.service.SkuService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* sku服务实现
* @author WY
* 2018-08-22 19:19:40由代码生成器自动生成
*/
@Service("skuService")
public class SkuServiceImpl extends BaseServiceImpl implements SkuService {


    /**
     * 保存商品 和商品详情
     * @param list
     * @param sku
     * @return
     */
    @Override
    public Result savedata(List<SkuDetail> list, Sku sku) {
        if(sku==null||list==null||list.size()==0||StringUtils.isEmpty(sku.getProductId())){
            return new Result(false);
        }
        String skuCode;
        Product product = this.baseDao.get(Product.class,sku.getProductId());
        if(product!=null&&StringUtils.isNotEmpty(product.getProductCode())){
            sku.setProductName(product.getProductName());
            skuCode = product.getProductCode().replace("M","MS");
            //获取最后一个  此商品的 sku
            String hql = "from Sku s where s.skuCode like '"+ skuCode+"%'  order by s.skuCode desc";
            List<Sku> lastSkuList =  this.baseDao.find(hql,1,1);
            if(lastSkuList!=null&&lastSkuList.size()>0){
                Sku lastSku = lastSkuList.get(0);
                String lastSkuCode = lastSku.getSkuCode();
                //默认  MS00100001 为九位  超过 九位
                if(StringUtils.isNotEmpty(lastSkuCode)&&lastSkuCode.length()>10){
                    skuCode +=  (Integer.parseInt(lastSkuCode.replace(skuCode,"0"))+1);
                }else{
                    try{
                        int skuNum =  Integer.parseInt(lastSkuCode.replace(skuCode,"0"))+1;
                        if(skuNum>0&&skuNum<10){
                            skuCode=skuCode+"0000"+skuNum;
                        }else if(skuNum<100&&skuNum>=10){
                            skuCode=skuCode+"000"+skuNum;
                        }else if(1000>skuNum&&skuNum>=100){
                            skuCode=skuCode+"00"+skuNum;
                        }else if(10000>skuNum&&skuNum>=1000){
                            skuCode=skuCode+"0"+skuNum;
                        }else if(skuNum>=10000){
                            skuCode=skuCode+skuNum;
                        }
                    }catch (ClassCastException e){
                        return new Result(false);
                    }catch (NumberFormatException exception){
                        return new Result(false);
                    }
                }
            }else{
                skuCode += "00001";
            }
            if(StringUtils.isNotEmpty(skuCode)){
                 sku.setSkuCode(skuCode);
                 sku.setCreateDateTime(new Date());
                 sku.setDeleted(0);
                 sku.setVersion(0);
                 this.baseDao.save(sku);
                 if(StringUtils.isNotEmpty(sku.getId())){
                    for(SkuDetail pd: list){
                        pd.setSkuId(sku.getId());
                    }
                    this.batchSave(list);
                    return new Result(true);
                }
            }
        }
        return new Result(false);
    }

    /**
     *  修改客户禁用
     * @param id
     * @return
     */
    @Override
    public boolean updateStauts(String id) {
        Sku sku = this.baseDao.get(Sku.class,id);
        if(sku!=null){
            if(sku.getSkuStatus()==1){
                sku.setSkuStatus(2);
            }else{
                sku.setSkuStatus(1);
            }
            this.baseDao.update(sku);
            return true;
        }
        return false;
    }
    /**
     * 通过 skuId 查询 详情
     * @param skuId
     * @return
     */
    @Override
    public Map<String, Object> findDetailBySkuId(String skuId) {
        Map<String, Object> result = new HashMap<>(16);
        String hql = "from SkuDetail where skuId='"+skuId+"'";
        List<SkuDetail> detailsList = this.baseDao.find(hql);
        if(detailsList!=null&&detailsList.size()>0){
            // PRICE 价格
            List<SkuDetail> priceList = SortUtil.sortSkuDetailByType("PRICE",detailsList);
            result.put("priceList",priceList);
            // COLOR 颜色
            List<SkuDetail> colorList = SortUtil.sortSkuDetailByType("COLOR",detailsList);
            result.put("colorList",colorList);
            //  TYPE 规格
            List<SkuDetail> typeList = SortUtil.sortSkuDetailByType("TYPE",detailsList);
            result.put("typeList",typeList);
            // QUALITY 质量
            List<SkuDetail> qualityList = SortUtil.sortSkuDetailByType("QUALITY",detailsList);
            result.put("qualityList",qualityList);
        }
        return result;
    }

    /**
     * 修改sku 和sku详情
     * @param list
     * @param sku
     * @return
     */
    @Override
    public Result updatedata(List<SkuDetail> list, Sku sku){
        Sku oldSku = this.baseDao.get(Sku.class,sku.getId());
        //判断是否是同一个商品 如果是同一个商品那就修改一些信息  删除 字表  重新提交
        // 如果不是 那就是删除整个商品 和详情表新增
        if(oldSku.getProductId().equals(sku.getProductId())){
            oldSku.setSkuModel(sku.getSkuModel());
            oldSku.setUpdateDateTime(new Date());
            oldSku.setSkuSizeHigh(sku.getSkuSizeHigh());
            oldSku.setSkuSizeWide(sku.getSkuSizeWide());
            oldSku.setSkuSizeHigh(sku.getSkuSizeHigh());
            this.baseDao.update(oldSku);
            String hql = "delete SkuDetail sd where sd.skuId='"+oldSku.getId()+"'";
            this.baseDao.executeHql(hql);
            for(SkuDetail pd: list){
                pd.setSkuId(sku.getId());
            }
            this.batchSave(list);
        }else{
            String hql = "delete Sku s where s.id='"+oldSku.getId()+"'";
            this.baseDao.executeHql(hql);
            String Detailhql = "delete SkuDetail sd where sd.skuId='"+oldSku.getId()+"'";
            this.baseDao.executeHql(Detailhql);
            savedata(list,sku);
        }
        return new Result(true);
    }


    /**
     * 根据商品id 获取 尺寸信息
     * @param productId
     * @return
     */
    @Override
    public List<Sku> findByProductId(String productId){
        if(StringUtils.isEmpty(productId)){
            return null;
        }
        String hql = "select s.id as id,s.skuSizeLength as skuSizeLength,s.skuSizeWide as skuSizeWide,s.skuSizeHigh as skuSizeHigh " +
                " from Sku s where  s.productId = :productId";
        Map<String,Object> params = new HashMap<>(4);
        params.put("productId",productId);
        List<Sku> list =  this.baseDao.find(hql,params,Sku.class);
        if(list!=null&&list.size()>0){
            for(Sku s:list){
                s.setSkuSize(s.getSkuSizeLength()+"*"+s.getSkuSizeWide()+"*"+s.getSkuSizeHigh()+"cm");
            }
            if(list!=null&&list.size()>0){
                List<String> skuIds = new ArrayList<>(list.size());
                for(Sku pd :list){
                    skuIds.add(pd.getId());
                }
                Map<String,Object> params2 = new HashMap<>(2);
                //获取所有sku 的价格  并选择每个sku的 最低价格
                params2.put("skuIds", skuIds);
                String hql2 = "select sd.skuId as skuId,sd.detailVal as detailVal " +
                        " from SkuDetail as sd" +
                        " where  sd.detailType='PRICE' and sd.skuId in (:skuIds)";
                List<SkuDetail> skuDetails = this.baseDao.find(hql2,params2,SkuDetail.class);
                for(Sku s :list){
                    for(SkuDetail sd :skuDetails){
                        if(s.getId().equals(sd.getSkuId())){
                            if(s.getMixPrice()==null||s.getMixPrice().equals(0)){
                                s.setMixPrice(sd.getDetailVal());
                            }else{
                                if(s.getMixPrice().compareTo(sd.getDetailVal())== 1){
                                    s.setMixPrice(sd.getDetailVal());
                                }
                            }
                        }
                    }
                }
            }
        }
        return  list;
    }

    /**
     * 根据 skuid  查询skumap
     * @param skuDetailIds
     * @return
     */
    @Override
    public Map<String,Map<String, String>> findSkuDetailBySkuDetailIds(Set<String> skuDetailIds) {
        if(skuDetailIds!=null&skuDetailIds.size()>0){
            Map<String,Object> params = new HashMap<>();
            params.put("skuDetailIds",skuDetailIds);
            String hql = "from SkuDetail where id in(:skuDetailIds)";
            List<SkuDetail> skuDetailList = this.baseDao.find(hql, params);
            Map<String,List<SkuDetail>> skuDetailMap = skuDetailList.stream().collect(Collectors.groupingBy(SkuDetail::getSkuId));
            Map<String,Map<String, String>> skuDMap = new HashMap<>();
            skuDetailMap.forEach((k,v) ->{
            	Map<String, String> map = v.stream().collect(Collectors.toMap(SkuDetail::getId, SkuDetail::getDetailName));
            	skuDMap.put(k, map);
            });
            return skuDMap;
        }
        return null;
    }

	@Override
	public Map<String, Sku> findSkuBySkuIds(Set<String> skuIdList) {
		Map<String,Object> params = new HashMap<>();
		String hql = "from Sku where id in(:skuIdList)";
        params.put("skuIdList",skuIdList);
        List<Sku> skus = this.find(hql, params);
        Map<String,Sku> skuMap = skus.stream().collect(Collectors.toMap(Sku::getId,Function.identity()));
		return skuMap;
	}
}
