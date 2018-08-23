package com.cnpc.packmall.product.util;/**
 * @author cuipeng
 * @create 2018-08-22 11:46
 **/

import com.cnpc.packmall.product.entity.ProductDetail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author cuipeng
 * @create 2018-08-22 11:46
 **/
public class productUtil {

    /**
     * 排序type类型list
     * @param type
     * @param list
     * @return
     */
    public static List<ProductDetail> sortByType(String type, List<ProductDetail> list){
        List<ProductDetail> typeDetailList = new ArrayList<>();
        for(ProductDetail pd:list){
            if(type.equals(pd.getDetailType())){
                typeDetailList.add(pd);
            }
        }

        Collections.sort(typeDetailList, new Comparator<ProductDetail>(){
            @Override
            public int compare(ProductDetail o1, ProductDetail o2) {
                //按照CityModel的city_code字段进行降序排列
                if(o1.getDetailSeq() > o2.getDetailSeq()){
                    return 1;
                }
                if(o1.getDetailSeq().equals(o2.getDetailSeq())){
                    return 0;
                }
                return -1;
            }
        });
        return typeDetailList;
    }

}
