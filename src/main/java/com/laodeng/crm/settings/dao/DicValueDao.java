package com.laodeng.crm.settings.dao;

import com.laodeng.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {

    List<DicValue> getListByCode(String code);

}
