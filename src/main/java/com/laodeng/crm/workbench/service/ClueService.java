package com.laodeng.crm.workbench.service;

import com.laodeng.crm.workbench.domain.Clue;
import com.laodeng.crm.workbench.domain.Tran;

import java.util.Map;

public interface ClueService {

    boolean save(Clue c);

    Clue detail(String id);

    Map<String, Object> getAllClueByLimit(Map<String, Object> map);

    boolean unbund(String id);

    boolean bund(String cid, String[] aids);

    boolean convert(String clueId, Tran t, String createBy);

}
