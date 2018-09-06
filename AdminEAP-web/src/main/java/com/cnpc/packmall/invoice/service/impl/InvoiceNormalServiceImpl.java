package com.cnpc.packmall.invoice.service.impl;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.packmall.invoice.entity.InvoiceDedicated;
import com.cnpc.packmall.invoice.entity.InvoiceNormal;
import com.cnpc.packmall.order.entity.Order;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.packmall.invoice.service.InvoiceNormalService;

import java.math.BigDecimal;
import java.util.*;

/**
* 发票管理（增普）服务实现
* @author WY
* 2018-08-16 14:37:42由代码生成器自动生成
*/
@Service("invoicenormalService")
public class InvoiceNormalServiceImpl extends BaseServiceImpl implements InvoiceNormalService {

    /**
     *  保存状态变更数据
     * @param invoiceId
     * @param invoiceMark
     * @param invoiceNo
     * @param courierNo
     * @return
     */
    @Override
    public boolean saveInvoiceStatusChangeData(String invoiceId, String invoiceMark, String invoiceNo, String courierNo) {
        InvoiceNormal invoiceNormal = this.baseDao.get(InvoiceNormal.class,invoiceId);
        if(invoiceNormal!=null){
            invoiceNormal.setInvoiceMark(invoiceMark);
            invoiceNormal.setInvoiceNo(invoiceNo);
            invoiceNormal.setCourierNo(courierNo);
            invoiceNormal.setInvoiceStatus(2);
            this.baseDao.saveOrUpdate(invoiceNormal);
            return true;
        }
        return false;
    }

    /**
     * 保存
     * @param invoiceNormal
     * @param orderIds
     * @return
     */
    @Override
    public Result insertData(InvoiceNormal invoiceNormal,String orderIds) {
        Calendar calendar = Calendar.getInstance();
        String invoiceCode =String.valueOf(calendar.get(Calendar.YEAR)).substring(2,4);
        invoiceCode = "RL"+invoiceCode+(calendar.get(Calendar.MONTH)+1)+calendar.get(Calendar.DATE);
        String hql = "from InvoiceNormal where invoiceCode like  '"+ invoiceCode+"%' order by invoiceCode desc";
        List<InvoiceNormal> lastInvoiceClientList = this.baseDao.find(hql,1,1);
        if(lastInvoiceClientList!=null&&lastInvoiceClientList.size()>0){
            InvoiceNormal lastInvoice = lastInvoiceClientList.get(0);
            String lastCode = lastInvoice.getInvoiceCode();
            Integer lastN = Integer.parseInt(lastCode.replace(invoiceCode,"0"))+1;
            try{
                if(lastN>0&&lastN<10){
                    invoiceCode=invoiceCode+"000"+lastN;
                }else if(lastN<100&&lastN>=10){
                    invoiceCode=invoiceCode+"00"+lastN;
                }else if(lastN<1000&&lastN>=100){
                    invoiceCode=invoiceCode+"0"+lastN;
                }else if(lastN>=1000){
                    invoiceCode=invoiceCode+lastN;
                }
            }catch (ClassCastException e){
                return new Result(false);
            }catch (NumberFormatException exception){
                return new Result(false);
            }
        }else{
            invoiceCode = invoiceCode+"0001";
        }
        if(StringUtils.isEmpty(invoiceCode)){
            return new Result(false);
        }
        invoiceNormal.setInvoiceCode(invoiceCode);
        invoiceNormal.setInvoiceStatus(1);
        invoiceNormal.setDeleted(0);
        invoiceNormal.setCreateDateTime(new Date());
        invoiceNormal.setVersion(0);
        List<String> orderIdList = new ArrayList<>();
        String[] orderIdsArray = orderIds.split(",");
        if(orderIdsArray!=null&&orderIdsArray.length>0) {
            orderIdList =  Arrays.asList(orderIdsArray);
            Map<String, Object> orderParams = new HashMap<>(4);
            orderParams.put("orderIdList", orderIdList);
            String orderHql = "select o from Order as o where o.id in (:orderIdList)";
            List<Order> orderList = this.baseDao.find(orderHql, orderParams);
            for(Order o:orderList){
                if(StringUtils.isNotEmpty(o.getWhetherId())||o.getWhetherId().trim().length()>0||o.getWeekend().equals("1")){
                    return new Result(false,o.getCode()+"：该订单已开票");
                }
            }

            BigDecimal price = new BigDecimal(0);
            if(orderList!=null&&orderList.size()>0){
                 for (Order order:orderList){
                     price = price.add(order.getTotalPrice());
                  }
                if(price.compareTo(new BigDecimal(0))>0&&(price.compareTo(invoiceNormal.getInvoicePrice())==0)){
                    this.baseDao.save(invoiceNormal);
                    for (Order order:orderList){
                        order.setWhetherId(invoiceNormal.getId());
                        order.setWhetherState("1");
                        order.setUpdateDateTime(new Date());
                    }
                    invoiceNormal.setInvoicePrice(price);
                    invoiceNormal.setPayStatus("1");
                    this.baseDao.update(invoiceNormal);
                    this.batchUpdate(orderList);
                    Map<String,Object> result = new HashMap<>(4);
                    result.put("invoiceId",invoiceNormal.getId());
                    result.put("invoiceType","2");
                    return new Result(true,result);
                }
            }
        }
        //修改订单状态
        return new Result(false);
    }
}
