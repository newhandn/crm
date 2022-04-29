package com.laodeng.crm.settings.service;

import com.laodeng.crm.settings.domain.DicType;
import com.laodeng.crm.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

public interface DicService {

    Map<String, List<DicValue>> getAll();

}
