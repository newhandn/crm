package com.laodeng.crm.workbench.dao;

import com.laodeng.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {

    int save(Clue c);

    Clue detail(String id);

    List<Clue> getAllClueByLimit(Map<String, Object> map);

    int getTotal();

    Clue getById(String clueId);

    int delete(String clueId);

}
