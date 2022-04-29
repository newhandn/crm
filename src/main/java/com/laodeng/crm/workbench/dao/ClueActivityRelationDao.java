package com.laodeng.crm.workbench.dao;

import com.laodeng.crm.workbench.domain.ClueActivityRelation;

import java.util.List;
import java.util.Map;

public interface ClueActivityRelationDao {

    int unbund(String id);

    int bund(Map<String, String> map);

    List<ClueActivityRelation> getListByClueId(String clueId);

    int delete(ClueActivityRelation clueActivityRelation);

}
