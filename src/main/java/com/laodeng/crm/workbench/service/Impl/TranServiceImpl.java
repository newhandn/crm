package com.laodeng.crm.workbench.service.Impl;

import com.laodeng.crm.utils.DateTimeUtil;
import com.laodeng.crm.utils.SqlSessionUtil;
import com.laodeng.crm.utils.UUIDUtil;
import com.laodeng.crm.workbench.dao.CustomerDao;
import com.laodeng.crm.workbench.dao.TranDao;
import com.laodeng.crm.workbench.dao.TranHistoryDao;
import com.laodeng.crm.workbench.domain.Customer;
import com.laodeng.crm.workbench.domain.Tran;
import com.laodeng.crm.workbench.domain.TranHistory;
import com.laodeng.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public boolean save(Tran t, String customerName) {

        /*
            交易添加业务：

                在做添加之前，参数t里面少了一项信息，就是客户的主键 customerId

                先处理客户相关的需求

                （1）判断customerName，根据客户名称在客户表进行精确查询
                        如果有这个客户，则取出这个客户的id，封装到t对象中
                        如果没有这个客户，则在这个客户表新建一个信息，然后将新建的客户的id取出，封装到t对象中

                （2）经过以上操作后，t对象的信息就全了，需要执行添加交易的操作

                （3）添加交易完毕后，需要创建一条交易历史


         */

        boolean flag = true;

        Customer customer = customerDao.getCustomerByName(customerName);

        if(customer == null){

            // 创建客户
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setCreate_by(t.getCreate_by());
            customer.setCreate_time(DateTimeUtil.getSysTime());
            customer.setContact_summary(t.getContact_summary());
            customer.setNext_contact_time(t.getNext_contact_time());
            customer.setOwner(t.getOwner());
            // 添加客户
            int count1 = customerDao.save(customer);
            if(count1 != 1){
                flag = false;
            }

        }

        // 通过以上对于客户的处理，不论是查询出来已有的客户，还是以前没有我们新增的客户，总之客户已经有了
        // 将客户的id封装到t对象中
        t.setCustomer_id(customer.getId());

        // 添加交易
        int count2 = tranDao.save(t);
        if(count2 != 1){
            flag = false;
        }

        // 添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTran_id(t.getId());
        th.setCreate_by(t.getCreate_by());
        th.setMoney(t.getMoney());
        th.setExpected_date(t.getExpected_date());
        th.setCreate_time(DateTimeUtil.getSysTime());
        th.setStage(t.getStage());
        int count3 = tranHistoryDao.save(th);
        if(count3 != 1){
            flag = false;
        }

        return flag;

    }

    @Override
    public Tran detail(String id) {

        Tran t = tranDao.detail(id);

        return t;

    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {

        List<TranHistory> thList = tranHistoryDao.getHistoryListByTranId(tranId);

        return thList;
    }

    @Override
    public Map<String, Object> getCharts() {

        // 取得total
        int total = tranDao.getTotal();

        // 取得dataList
        List<Map<String, Object>> dataList = tranDao.getCharts();

        // 将total和dataList保存到map中
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("dataList", dataList);

        // 返回map
        return map;

    }

    @Override
    public boolean changeStage(Tran t) {

        boolean flag = true;

        // 改变交易阶段
        int count1 = tranDao.changeStage(t);
        if(count1 != 1){

            flag = false;

        }

        // 交易阶段改变后，生成一条交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreate_by(t.getEdit_by());
        th.setCreate_time(DateTimeUtil.getSysTime());
        th.setMoney(t.getMoney());
        th.setExpected_date(t.getExpected_date());
        th.setTran_id(t.getId());
        th.setStage(t.getStage());
        // 添加交易历史
        int count2 = tranHistoryDao.save(th);
        if(count2 != 1){

            flag = false;

        }


        return flag;

    }
}
