package com.laodeng.crm.settings.service.Impl;

import com.laodeng.crm.settings.dao.DicTypeDao;
import com.laodeng.crm.settings.dao.DicValueDao;
import com.laodeng.crm.settings.domain.DicType;
import com.laodeng.crm.settings.domain.DicValue;
import com.laodeng.crm.settings.service.DicService;
import com.laodeng.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {

    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);

    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {

        // 创建map，用于储存数据
        Map<String, List<DicValue>> map = new HashMap<>();

        // 将字典类型列表取出
        List<DicType> dtList = dicTypeDao.getTypeList();

        // 将字典类型列表遍历
        for(DicType dt : dtList){

            // 取得每一种类型的字典类型编码
            String code = dt.getCode();

            // 根据每一个字典类型来取得字典值列表
            List<DicValue> dvList = dicValueDao.getListByCode(code);

            map.put(code, dvList);
        }

        return map;
    }
}
