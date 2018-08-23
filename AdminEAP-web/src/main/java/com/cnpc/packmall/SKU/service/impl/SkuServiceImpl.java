package com.cnpc.packmall.SKU.service.impl;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.packmall.SKU.entity.Sku;
import com.cnpc.packmall.SKU.entity.SkuDetail;
import com.cnpc.packmall.center.entity.Client;
import com.cnpc.packmall.product.entity.Product;
import com.cnpc.packmall.product.entity.ProductDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.packmall.SKU.service.SkuService;

import java.util.Date;
import java.util.List;

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
}
