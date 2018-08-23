package com.cnpc.packmall.product.service.impl;

import bsh.StringUtil;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.packmall.product.entity.Product;
import com.cnpc.packmall.product.entity.ProductDetail;
import com.cnpc.packmall.util.SortUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.packmall.product.service.ProductService;

import java.util.*;

/**
* 商品服务实现
* @author WY
* 2018-08-20 16:10:15由代码生成器自动生成
*/
@Service("productService")
public class ProductServiceImpl extends BaseServiceImpl implements ProductService {

    /**
     * 保存商品 和商品详情
     * @param list
     * @param productName
     * @return
     */
    @Override
    public Result savedata(List<ProductDetail> list, String productName) {
        //Calendar calendar = Calendar.getInstance();
        //String productCode = ""+calendar.get(Calendar.YEAR)+calendar.get(Calendar.MONTH)+calendar.get(Calendar.DATE);
        String productCode = "";
        //设置商品编号  查询今日商品 最大编号
        String hql = "from Product p order by productCode desc";
        List<Product> lastProductList =  this.baseDao.find(hql,1,1); 
        if(lastProductList!=null&&lastProductList.size()>0){
             Product lastProduct = lastProductList.get(0);
             String lastProductnCode = lastProduct.getProductCode();
            if(StringUtils.isNotEmpty(lastProductnCode)&&lastProductnCode.length()>4){
                productCode = "M"+ (Integer.parseInt(lastProductnCode.substring(1,lastProductnCode.length()))+1);
            }else{
                try{
                    int productNum =  Integer.parseInt(lastProductnCode.substring(1,lastProductnCode.length()))+1;
                    if(productNum>0&&productNum<10){
                        productCode="M"+"00"+productNum;
                    }else if(productNum<100&&productNum>=10){
                        productCode="M"+"0"+productNum;
                    }else if(productNum>=100){
                        productCode="M"+productNum;
                    }
                }catch (ClassCastException e){
                    return new Result(false);
                }catch (NumberFormatException exception){
                    return new Result(false);
                }
            }
          }else{
            productCode = "M001";
        }
             if(StringUtils.isNotEmpty(productCode)){
            Product product = new Product();
            product.setProductName(productName);
            product.setProductCode(productCode);
            product.setCreateDateTime(new Date());
            product.setDeleted(0);
            product.setVersion(0);
            this.baseDao.save(product);
            if(StringUtils.isNotEmpty(product.getId())){
                for(ProductDetail pd: list){
                    pd.setProductId(product.getId());
                }
                this.batchSave(list);
                return new Result(true);
            }
        }
        return new Result(false );
    }

    /**
     * 修改商品 和商品详情
     * @param list
     * @param productName
     * @return
     */
    @Override
    public Result updatedata(List<ProductDetail> list, String productName, String id) {
        Product product = this.baseDao.get(Product.class,id);
        product.setProductName(productName);
        product.setUpdateDateTime(new Date());
        this.baseDao.update(product);
        String hql = "delete ProductDetail pd where pd.productId='"+id+"'";
        this.baseDao.executeHql(hql);
        for(ProductDetail pd: list){
            pd.setProductId(product.getId());
        }
        this.batchSave(list);
        return new Result(true);
    }

    /**
     * 通过 productId 查询 详情
     * @param productId
     * @return
     */
    @Override
    public Map<String, Object> findDetailByProducid(String productId) {
        Map<String, Object> result = new HashMap<>(16);
        String hql = "from ProductDetail where productId='"+productId+"'";
        List<ProductDetail> detailsList = this.baseDao.find(hql);
        if(detailsList!=null&&detailsList.size()>0){
            // BANNERIMG banner图片
            List<ProductDetail> bannerImgList = SortUtil.sortByType("BANNERIMG",detailsList);
            result.put("bannerImgList",bannerImgList);
            // DETAILIMG 详情图片
            List<ProductDetail> detailImgList = SortUtil.sortByType("DETAILIMG",detailsList);
            result.put("detailImgList",detailImgList);
            // TYPEIMG  规格图片
            List<ProductDetail> typeImgList = SortUtil.sortByType("TYPEIMG",detailsList);
            result.put("typeImgList",typeImgList);
            // COLOR 颜色
            List<ProductDetail> colorList = SortUtil.sortByType("COLOR",detailsList);
            result.put("colorList",colorList);
            //  TYPE 规格
            List<ProductDetail> typeList = SortUtil.sortByType("TYPE",detailsList);
            result.put("typeList",typeList);
            // QUALITY 质量
            List<ProductDetail> qualityList = SortUtil.sortByType("QUALITY",detailsList);
            result.put("qualityList",qualityList);
        }
        return result;
    }

    /**
     * 获取商品列表
     * @return
     */
    @Override
    public List<Product> findProductList() {
        String hql = "from Product where deleted = 0";
        return this.baseDao.find(hql);
    }

}
