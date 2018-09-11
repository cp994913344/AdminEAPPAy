package com.cnpc.packmall.product.service.impl;

import bsh.StringUtil;
import com.cnpc.framework.base.entity.SysFile;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.packmall.SKU.entity.Sku;
import com.cnpc.packmall.SKU.entity.SkuDetail;
import com.cnpc.packmall.product.entity.Product;
import com.cnpc.packmall.product.entity.ProductDetail;
import com.cnpc.packmall.util.SortUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.packmall.product.service.ProductService;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    public Result savedata(List<ProductDetail> list, String productName,String headImgId) {
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
            product.setHeadImgId(headImgId);
            product.setProductCode(productCode);
            product.setCreateDateTime(new Date());
            product.setDeleted(0);
            product.setVersion(0);
            product.setProductStatus(1);
            this.baseDao.save(product);
            if(StringUtils.isNotEmpty(product.getId())){
                for(ProductDetail pd: list){
                    pd.setProductId(product.getId());
                    pd.setDeleted(0);
                    pd.setVersion(0);
                    pd.setCreateDateTime(new Date());
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
    public Result updatedata(List<ProductDetail> list, String productName, String id,String headImgId) {
        Product product = this.baseDao.get(Product.class,id);
        product.setProductName(productName);
        product.setHeadImgId(headImgId);
        product.setUpdateDateTime(new Date());
        //查询sku信息 如果 sku中 有存在 该类型的   颜色 质量等信息 的 sku在用 不允许 修改
//        String skuHql = "select sd.* from SkuDetail as sd,Sku as s where sd.skuId = s.id and s.deleted=0 and sd.deleted = 0 and sd.detailType !='PRICE' and  s.productId = '"+id+"'";
//        List<SkuDetail> skuDetailList = this.baseDao.find(skuHql,SkuDetail.class);
//
//        //查询商品原有的  颜色质量等信息
//        String oldProductHql = "from ProductDetail where deleted=0 and detailType!='BANNERIMG' " +
//                "and  detailType!='DETAILIMG' and detailType!='TYPEIMG' and productId='"+id+"'";
//        List<ProductDetail> oldDetailsList = this.baseDao.find(oldProductHql);

        this.baseDao.update(product);
        String hql = "update ProductDetail pd set pd.deleted=1 where pd.productId='"+id+"'";
        this.baseDao.executeHql(hql);
        for(ProductDetail pd: list){
            pd.setProductId(product.getId());
            pd.setCreateDateTime(new Date());
            pd.setUpdateDateTime(new Date());
            pd.setVersion(0);
            pd.setDeleted(0);
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
        String hql = "from ProductDetail where deleted=0 and productId='"+productId+"'";
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
        String hql = "from Product where deleted = 0 and productStatus = 1  order by createDateTime desc";
        return this.baseDao.find(hql);
    }


    /**
     * 小程序 获取商品列表
     * @return
     */
    @Override
    public List<Product> findList() {
        String hql = "from Product where deleted = 0 and productStatus = 1  order by createDateTime desc";
        List<Product> productList = this.baseDao.find(hql);
        if(productList!=null&&productList.size()>0){
            List<String> productIds = new ArrayList<>(productList.size());
            for(Product p :productList){
                productIds.add(p.getId());
            }
            List<Sku> skuList  = getSkuMinPriceByProductIds(productIds);
            for(Product p :productList){
                for(Sku s :skuList) {
                    if(p.getId().equals(s.getProductId())){
                        if(p.getMixPrice()==null||p.getMixPrice().equals(0)){
                            p.setMixPrice(s.getMixPrice());
                        }else{
                            if(p.getMixPrice().compareTo(s.getMixPrice())== 1){
                                p.setMixPrice(s.getMixPrice());
                            }
                        }
                    }
                }
            }
            //判断没有sku的商品
            for(int i=0;i< productList.size();i++){
                if(productList.get(i).getMixPrice()==null||productList.get(i).getMixPrice().equals(0)){
                    productList.remove(i);
                    if(i== productList.size()-1){
                        break;
                    }else{
                        i=i-1;
                    }
                }
            }
            return productList;
        }
        return null;
    }

    /**
     *  获取商品列表 的展示图片list
     * @return
     */
    @Override
    public List<ProductDetail> getImagesListByProductIds(List<String> productIds){
        Map<String,Object> params = new HashMap<>(2);
        String hql  = " select p.id as id,p.productId as productId,p.detailId as detailId " +
                " from ProductDetail as p" +
                " where p.deleted=0 and  p.detailType = 'BANNERIMG' and p.detailSeq =1  and p.productId in (:productIds)";
        params.put("productIds",productIds);
        return this.baseDao.find(hql,params,ProductDetail.class);
    }

    /**
     * 通过商品ids  获取sku最小价格list
     * @param productIds
     * @return
     */
    @Override
    public List<Sku> getSkuMinPriceByProductIds(List<String> productIds){
        //获取所有的sku
        Map<String,Object> params = new HashMap<>(2);
        String hql  = " select s.id as id,s.productId as productId " +
                " from Sku as s" +
                " where s.deleted=0 and s.skuStatus = 1 and s.productId in (:productIds)";
        params.put("productIds",productIds);
        List<Sku> list = this.baseDao.find(hql,params,Sku.class);
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
                    " where sd.deleted=0 and  sd.detailType='PRICE' and sd.skuId in (:skuIds)";
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
        return list;
    }

	@Override
	public Map<String, List<ProductDetail>> findProductDetailByProductIdAndType(Set<String> productIds, String type) {
		Map<String, Object> params = new HashMap<>();
		String hql = "select detailType as detailType,detailVal as detailVal,productId as productId,detailId as detailId,id as id from ProductDetail where deleted=0 and productId in(:productId) and detailType =:type";
		params.put("productId", productIds);
		params.put("type", type);
		List<ProductDetail> productDetails = this.find(hql, params,ProductDetail.class);
		
		return productDetails.stream().collect(Collectors.groupingBy(ProductDetail::getProductId));
	}

    /**
     *  修改商品上下架
     * @param id
     * @return
     * @autor cp
     */
    @Override
    public boolean updateStauts(String id) {
        Product product = this.baseDao.get(Product.class,id);
        if(product!=null){
            if(product.getProductStatus()==1){
                product.setProductStatus(2);
            }else{
                product.setProductStatus(1);
            }
            product.setUpdateDateTime(new Date());
            this.baseDao.update(product);
            return true;
        }
        return false;
    }
}
