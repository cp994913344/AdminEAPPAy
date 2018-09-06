package com.cnpc.packmall.invoice.service.impl;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.packmall.center.entity.ShippingAddress;
import com.cnpc.packmall.invoice.dto.InvoiceDTO;
import com.cnpc.packmall.invoice.entity.InvoiceDedicated;
import com.cnpc.packmall.order.entity.Order;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.packmall.invoice.service.InvoiceDedicatedService;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.*;

/**
* 发票管理（增专）服务实现
* @author WY
* 2018-08-16 14:37:56由代码生成器自动生成
*/
@Service("invoiceDedicatedService")
public class InvoiceDedicatedServiceImpl extends BaseServiceImpl implements InvoiceDedicatedService {

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
        InvoiceDedicated invoiceDedicated = this.baseDao.get(InvoiceDedicated.class,invoiceId);
        if(invoiceDedicated!=null){
            invoiceDedicated.setInvoiceMark(invoiceMark);
            invoiceDedicated.setInvoiceNo(invoiceNo);
            invoiceDedicated.setCourierNo(courierNo);
            invoiceDedicated.setInvoiceStatus(2);
            this.baseDao.saveOrUpdate(invoiceDedicated);
            return true;
        }
        return false;
    }

    @Override
    public Result insertData(InvoiceDedicated invoiceDedicated,String orderIds) {
        Calendar calendar = Calendar.getInstance();
        String invoiceCode =String.valueOf(calendar.get(Calendar.YEAR)).substring(2,4);
        invoiceCode = "SL"+invoiceCode+(calendar.get(Calendar.MONTH)+1)+calendar.get(Calendar.DATE);
        String hql = "from InvoiceDedicated where invoiceCode like  '"+ invoiceCode+"%' order by invoiceCode desc";
        List<InvoiceDedicated> lastInvoiceClientList = this.baseDao.find(hql,1,1);
        if(lastInvoiceClientList!=null&&lastInvoiceClientList.size()>0){
            InvoiceDedicated lastInvoice = lastInvoiceClientList.get(0);
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
        invoiceDedicated.setInvoiceCode(invoiceCode);
        invoiceDedicated.setInvoiceStatus(1);
        invoiceDedicated.setDeleted(0);
        invoiceDedicated.setCreateDateTime(new Date());
        invoiceDedicated.setVersion(0);
        List<String> orderIdList = new ArrayList<>();
        String[] orderIdsArray = orderIds.split(",");
        if(orderIdsArray!=null&&orderIdsArray.length>0) {
            //判断订单是否已经开过票了
            orderIdList =  Arrays.asList(orderIdsArray);
            Map<String, Object> orderParams = new HashMap<>(2);
            orderParams.put("orderIdList", orderIdList);
            String orderHql = "select o from  Order as o where o.id in (:orderIdList)";
            List<Order> orderList = this.baseDao.find(orderHql, orderParams);
            for(Order o:orderList){
                if((o.getWhetherId()==null||StringUtils.isEmpty(o.getWhetherId()))&&(o.getWhetherState()==null||o.getWhetherState().equals("0"))){
                    continue;
                }else {
                    return new Result(false,o.getCode()+"：该订单已开票");
               }
            }
            BigDecimal price = new BigDecimal(0);
            if(orderList!=null&&orderList.size()>0){
                for (Order order:orderList){
                    price = price.add(order.getTotalPrice());
                };
                if(price.compareTo(new BigDecimal(0))>0&&(price.compareTo(invoiceDedicated.getInvoicePrice())==0)){
                    this.baseDao.save(invoiceDedicated);
                    for (Order order:orderList){
                        order.setWhetherId(invoiceDedicated.getId());
                        order.setWhetherState("1");
                        order.setUpdateDateTime(new Date());
                    };
                    invoiceDedicated.setPayStatus("1");
                    invoiceDedicated.setInvoicePrice(price);
                    this.baseDao.update(invoiceDedicated);
                    this.batchUpdate(orderList);
                    Map<String,Object> result = new HashMap<>(4);
                    result.put("invoiceId",invoiceDedicated.getId());
                    result.put("invoiceType","1");
                    return new Result(true,result);
                }else{
                    return new Result(false,"保存错误：发票金额错误");
                }
            }
        }
        //修改订单状态
        return new Result(false);
    }

    /**
     * 根据openid 查询客户的发票
     * @param openId
     * @return
     */
    @Override
    public Result findByOpenId(String openId) {
        if(StringUtils.isEmpty(openId)){
            return new Result(false);
        }
        String hql = "SELECT dedicated.id AS id,dedicated.invoiceStatus AS invoiceStatus,dedicated.invoicePrice as invoicePrice," +
                " '1' AS type,dedicated.createDateTime AS createDateTime,dedicated.updateDateTime AS updateDateTime " +
                " FROM InvoiceDedicated AS dedicated WHERE dedicated.openId = '"+openId+"'";
        String hql2  = " SELECT normal.id AS id,normal.invoiceStatus AS invoiceStatus,'2' AS type,normal.invoicePrice as invoicePrice," +
                " normal.createDateTime AS createDateTime,normal.updateDateTime AS updateDateTime" +
                " FROM InvoiceNormal AS normal WHERE normal.openId = '"+openId+"'";
        List<InvoiceDTO> list = this.baseDao.find(hql,InvoiceDTO.class);
        List<InvoiceDTO> list2 = this.baseDao.find(hql2,InvoiceDTO.class);
        list.addAll(list2);
        if(list!=null&&list.size()>0) {
            list.sort(new Comparator<InvoiceDTO>() {
                @Override
                public int compare(InvoiceDTO o1, InvoiceDTO o2) {
                    if(o1.getCreateDateTime().compareTo(o2.getCreateDateTime())==1){ return -1;
                    }else if(o1.getCreateDateTime().compareTo(o2.getCreateDateTime())==-1){ return 1;
                    }else{ return 0; }
                }
            });
        }
        return new Result(true,list);
    }

    /**
     * 通过id 查询详情
     * @param id
     * @return
     */
    @Override
    public Result getById(String id) {
        if(StringUtils.isEmpty(id)){
            return new Result(false);
        }
        Map<String, Object> result = new HashMap<>(4);
        InvoiceDedicated invoiceDedicated = this.baseDao.get(InvoiceDedicated.class, id);
        result.put("invoiceDedicated", invoiceDedicated);
        return new Result(true,result);
    }
}
