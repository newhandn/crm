package com.laodeng.crm.workbench.service;

import com.laodeng.crm.settings.domain.User;
import com.laodeng.crm.vo.PaginationVO;
import com.laodeng.crm.workbench.domain.Activity;
import com.laodeng.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;


public interface ActivityService {

    List<User> selectAllUser();

    boolean saveOne(Activity a);

    PaginationVO<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity a);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemark ar);

    boolean updateRemark(ActivityRemark ar);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);


}
