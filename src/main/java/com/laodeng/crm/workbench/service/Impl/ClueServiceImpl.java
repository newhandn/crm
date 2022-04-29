package com.laodeng.crm.workbench.service.Impl;

import com.laodeng.crm.utils.DateTimeUtil;
import com.laodeng.crm.utils.SqlSessionUtil;
import com.laodeng.crm.utils.UUIDUtil;
import com.laodeng.crm.workbench.dao.*;
import com.laodeng.crm.workbench.domain.*;
import com.laodeng.crm.workbench.service.ClueService;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {

    // 线索相关的表
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao  clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    // 客户相关的表
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    // 联系人相关的表
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    // 交易相关的表
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public boolean convert(String clueId, Tran t, String createBy) {

        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();

        boolean flag = true;

        // (1) 通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue c = clueDao.getById(clueId);

        // (2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司名称精准匹配，判断该客户是否存在）
        String company = c.getCompany();
        Customer cus = customerDao.getCustomerByName(company);
        // 如果cus为空，说明以前没有这个客户，需要新建一个
        if(cus == null){

            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setAddress(c.getAddress());
            cus.setWebsite(c.getWebsite());
            cus.setPhone(c.getPhone());
            cus.setOwner(c.getOwner());
            cus.setNext_contact_time(c.getNextContactTime());
            cus.setName(company);
            cus.setDescription(c.getDescription());
            cus.setCreate_time(createTime);
            cus.setCreate_by(createBy);
            cus.setContact_summary(c.getContactSummary());
            // 添加客户
            int count = customerDao.save(cus);
            if(count != 1){
                flag = false;
            }
        }

        // -----------------------------------
        // 经过第二步处理后，客户的信息我们已经拥有了，将来在处理其它表的时候，如果要使用到客户的id
        // 直接使用cus.getId() 就可以了
        // -----------------------------------

        // (3) 通过线索对象提取联系人信息，保存联系人
        Contacts con = new Contacts();
        con.setId(UUIDUtil.getUUID());
        con.setSource(c.getSource());
        con.setOwner(c.getOwner());
        con.setNext_contact_time(c.getNextContactTime());
        con.setMphone(c.getMphone());
        con.setJob(c.getJob());
        con.setFullname(c.getFullname());
        con.setEmail(c.getEmail());
        con.setDescription(c.getDescription());
        con.setCustomer_id(cus.getId());
        con.setCreate_time(createTime);
        con.setContact_summary(c.getContactSummary());
        con.setAppellation(c.getAppellation());
        con.setAddress(c.getAddress());

        // 添加联系人
        int count2 = contactsDao.save(con);
        if(count2 != 1){
            flag = false;
        }

        // -----------------------------------
        // 经过第三步处理后，联系人的信息我们已经拥有了，将来在处理其它表的时候，如果要使用到联系人的id
        // 直接使用con.getId() 就可以了
        // -----------------------------------

        // (4)将线索的备注转换为客户的备注和联系人的备注
        // 查询出与该线索关联的备注信息列表
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);
        // 取出每一条线索的备注
        for(ClueRemark clueRemark : clueRemarkList){

            // 取出备注信息（主要转换到客户备注和联系人备注的就是这个备注信息）
            String noteContent = clueRemark.getNote_content();

            // 创建客户备注对象，添加客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreate_by(createBy);
            customerRemark.setCreate_time(createTime);
            customerRemark.setCustomer_id(cus.getId());
            customerRemark.setEdit_flag("0");
            customerRemark.setNote_content(noteContent);
            int count3 = customerRemarkDao.save(customerRemark);
            if(count3 != 1){
                flag = false;
            }

            // 创建联系人备注对象，添加联系人
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreate_by(createBy);
            contactsRemark.setCreate_time(createTime);
            contactsRemark.setContacts_id(con.getId());
            contactsRemark.setEdit_flag("0");
            contactsRemark.setNote_content(noteContent);
            int count4 = contactsRemarkDao.save(contactsRemark);
            if(count4 != 1){
                flag = false;
            }

        }

        // (5) ”线索和市场活动“的关系转换到”联系人和市场活动“的关系
        // 查询出与该条线索关联的市场活动，查询关联市场活动的关联关系列表
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        // 遍历出每一条市场活动关联的关联关系记录
        Iterator<ClueActivityRelation> it = clueActivityRelationList.iterator();
        while(it.hasNext()){

            // 从每一条遍历出来的记录中，取出关联的市场活动id
            String activityId = it.next().getActivityId();
            System.out.println(activityId);

            // 创建联系人与市场活动的关联关系对象，让第三步生成的联系人与市场活动做关联
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivity_id(activityId);
            contactsActivityRelation.setContacts_id(con.getId());
            // 添加联系人与市场活动的关联关系
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if(count5 != 1){
                flag = false;
            }

        }

        // (6) 如果有创建交易需求，创建一条交易
        if(t != null){

            /*
                t对象在controller里面已经封装好的信息如下：
                    id、money、name、expectedDate、stage、activityId、createBy、createTime

                接下来可以通过第一步生成的c对象，取出一些信息，继续完善对t对象的封装
             */

            t.setSource(c.getSource());
            t.setOwner(c.getOwner());
            t.setNext_contact_time(c.getNextContactTime());
            t.setDescription(c.getDescription());
            t.setCustomer_id(cus.getId());
            t.setContact_summary(c.getContactSummary());
            t.setContacts_id(con.getId());
            // 添加交易
            int count6 = tranDao.save(t);
            if(count6 != 1){
                flag = false;
            }

            // (7) 如果创建了交易，则创建一条该交易下的交易历史
            TranHistory th = new TranHistory();
            th.setId(UUIDUtil.getUUID());
            th.setCreate_by(createBy);
            th.setCreate_time(createTime);
            th.setExpected_date(t.getExpected_date());
            th.setMoney(t.getMoney());
            th.setStage(t.getStage());
            th.setTran_id(t.getId());
            // 添加交易历史
            int count7 = tranHistoryDao.save(th);
            if(count7 != 1){
                flag = false;
            }

        }

        // (8) 删除线索备注
        for(ClueRemark clueRemark : clueRemarkList){

            int count8 = clueRemarkDao.delete(clueRemark);
            if(count8 != 1){
                flag = false;
            }

        }

        // (9) 删除线索和市场活动的关系
        for(ClueActivityRelation clueActivityRelation : clueActivityRelationList){

            int count9 = clueActivityRelationDao.delete(clueActivityRelation);
            if(count9 != 1){
                flag = false;
            }

        }

        // (9) 删除线索
        int count10 = clueDao.delete(clueId);
        if(count10 != 1){
            flag = false;
        }

        return flag;

    }

    @Override
    public boolean bund(String cid, String[] aids) {

        // 定义标识
        boolean flag = true;
        int count = 0;

        for(String aid : aids){

            // 生成随机数
            String UUID = UUIDUtil.getUUID();

            // 取得每一个aid和cid关联
            // 创建集合存储数据
            Map<String, String> map = new HashMap<>();
            map.put("id", UUID);
            map.put("clueId", cid);
            map.put("activityId", aid);

            // 调用dao层方法
            count += clueActivityRelationDao.bund(map);

        }

        if(count != aids.length){
            flag = false;
        }

        return flag;

    }

    @Override
    public Clue detail(String id) {

        Clue c = clueDao.detail(id);

        return c;
    }

    @Override
    public boolean unbund(String id) {

        // 作标识，根据flag判断是否解除关联成功
        boolean flag = true;

        int count = clueActivityRelationDao.unbund(id);

        if(count != 1){
            flag = false;
        }

        return flag;

    }

    @Override
    public Map<String, Object> getAllClueByLimit(Map<String, Object> map) {

        // 获取分页查询的数据
        List<Clue> cList = clueDao.getAllClueByLimit(map);

        // 获取总记录条数
        int total = clueDao.getTotal();

        // 将两个数据封装在map中
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("total", total);
        infoMap.put("dataList", cList);

        return infoMap;
    }

    @Override
    public boolean save(Clue c) {

        boolean flag = true;

        int count = clueDao.save(c);

        if(count != 1){
            flag = false;
        }

        return flag;

    }
}
