package com.laodeng.crm.workbench.service.Impl;

import com.laodeng.crm.utils.SqlSessionUtil;
import com.laodeng.crm.workbench.dao.CustomerDao;
import com.laodeng.crm.workbench.domain.Customer;
import com.laodeng.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);


    @Override
    public List<String> getCustomerName(String name) {

        List<String> sList = customerDao.getCustomerName(name);

        return sList;

    }
}
