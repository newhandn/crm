package com.laodeng.crm.workbench.service.Impl;

import com.laodeng.crm.settings.dao.UserDao;
import com.laodeng.crm.settings.domain.User;
import com.laodeng.crm.vo.PaginationVO;
import com.laodeng.crm.workbench.dao.ActivityRemarkDao;
import com.laodeng.crm.workbench.domain.Activity;
import com.laodeng.crm.utils.SqlSessionUtil;
import com.laodeng.crm.workbench.dao.ActivityDao;
import com.laodeng.crm.workbench.domain.ActivityRemark;
import com.laodeng.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public List<User> selectAllUser() {

        return activityDao.selectAllUser();

    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {

        List<Activity> aList = activityDao.getActivityListByClueId(clueId);

        return aList;
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map) {

        List<Activity> aList = activityDao.getActivityListByNameAndNotByClueId(map);

        return aList;

    }

    @Override
    public List<Activity> getActivityListByName(String aname) {

        List<Activity> aList = activityDao.getActivityListByName(aname);

        return aList;

    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {

        boolean flag = true;

        int count = activityRemarkDao.updateRemark(ar);

        if(count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {

        boolean flag = true;

        int count = activityRemarkDao.saveRemark(ar);

        if(count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean deleteRemark(String id) {

        boolean flag = true;

        int count = activityRemarkDao.deleteRemark(id);

        if(count != 1){
            flag = false;
        }

        return flag;

    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {

        // 取uList
        List<User> uList = userDao.getUserList();

        // 取a
        Activity a = activityDao.getById(id);

        // 将uList和a打包到map中
        Map<String, Object> map = new HashMap<>();
        map.put("uList", uList);
        map.put("a", a);

        // 返回map即可
        return map;

    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {

        List<ActivityRemark> asList = activityRemarkDao.getRemarkListByAid(activityId);

        return asList;
    }

    @Override
    public boolean update(Activity a) {

        boolean flag = true;
        // 调用dao层方法
        int count = activityDao.update(a);
        if(count != 0){
            return flag;
        }else{
            return false;
        }

    }

    @Override
    public Activity detail(String id) {

        Activity a = activityDao.detail(id);

        return a;

    }

    @Override
    public boolean delete(String[] ids) {
        
        boolean flag = true;

        // 查询出需要删除的备注的数量
        int count1 = activityRemarkDao.getCountByAids(ids);

        // 删除备注，返回受到影响的条数（实际删除的数量）
        int count2 = activityRemarkDao.deleteByAids(ids);

        System.out.println(count2);
        if(count1 != count2){
            flag = false;
        }

        // 删除市场活动
        int count3 = activityDao.delete(ids);
        if(count3 != ids.length){
            flag = false;
        }

        return flag;
    }

    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map) {

        List<Activity> sList = activityDao.selectPageList(map);

        int count = activityDao.getTotalByCondition(map);

        // 将上述数据封装至PaginationVO中
        PaginationVO<Activity> p = new PaginationVO<>();
        p.setDataList(sList);
        p.setTotal(count);

        // 返回集合
        return p;

    }

    @Override
    public boolean saveOne(Activity a) {

        boolean flag = true;
        // 调用dao层方法
        int count = activityDao.saveOne(a);
        if(count != 0){
            return flag;
        }else{
            return false;
        }
    }
}
