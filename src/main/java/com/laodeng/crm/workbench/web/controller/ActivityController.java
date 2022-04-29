package com.laodeng.crm.workbench.web.controller;

import com.laodeng.crm.settings.domain.User;
import com.laodeng.crm.utils.DateTimeUtil;
import com.laodeng.crm.utils.PrintJson;
import com.laodeng.crm.utils.ServiceFactory;
import com.laodeng.crm.utils.UUIDUtil;
import com.laodeng.crm.vo.PaginationVO;
import com.laodeng.crm.workbench.domain.Activity;
import com.laodeng.crm.workbench.domain.ActivityRemark;
import com.laodeng.crm.workbench.service.ActivityService;
import com.laodeng.crm.workbench.service.Impl.ActivityServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet({"/workbench/activity/pageList.do", "/workbench/activity/getUserList.do", "/workbench/activity/save.do",
        "/workbench/activity/delete.do", "/workbench/activity/getUserListAndActivity.do",
        "/workbench/activity/update.do",
        "/workbench/activity/detail.do",
        "/workbench/activity/getRemarkListByAid.do",
        "/workbench/activity/deleteRemark.do",
        "/workbench/activity/saveRemark.do",
        "/workbench/activity/updateRemark.do"})
public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("进入市场活动控制器");

        // 获取前端访问的路径名
        String servletPath = request.getServletPath();

        if("/workbench/activity/pageList.do".equals(servletPath)){

            // 具体市场活动具体
            pageList(request, response);

        }else if("/workbench/activity/getUserList.do".equals(servletPath)){

            getUserList(request, response);

        }else if("/workbench/activity/save.do".equals(servletPath)){

            save(request, response);

        }else if("/workbench/activity/delete.do".equals(servletPath)){

            delete(request, response);

        }else if("/workbench/activity/getUserListAndActivity.do".equals(servletPath)){

            getUserListAndActivity(request, response);

        }else if("/workbench/activity/update.do".equals(servletPath)){

            update(request, response);

        }else if("/workbench/activity/detail.do".equals(servletPath)){

            detail(request, response);

        }else if("/workbench/activity/getRemarkListByAid.do".equals(servletPath)){

            getRemarkListByAid(request, response);

        }else if("/workbench/activity/deleteRemark.do".equals(servletPath)){

            deleteRemark(request, response);

        }else if("/workbench/activity/saveRemark.do".equals(servletPath)){

            saveRemark(request, response);

        }else if("/workbench/activity/updateRemark.do".equals(servletPath)){

            updateRemark(request, response);

        }

    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行修改备注的操作");

        // 获取数据
        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        String editFlag = "1";

        // 创建pojo，存储数据
        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditBy(editBy);
        ar.setEditTime(editTime);
        ar.setEditFlag(editFlag);

        // 获取代理对象
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        // 执行方法
        boolean flag = as.updateRemark(ar);
        if(flag){
            // 创建map集合，封装数据
            Map<String, Object> map = new HashMap<>();
            map.put("success", flag);
            map.put("ar", ar);

            // 将map格式化为json类型数据
            PrintJson.printJsonObj(response, map);
        }else{
            PrintJson.printJsonFlag(response, flag);
        }
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行添加备注的操作");

        String noteContent = request.getParameter("noteContent");
        String activityId = request.getParameter("activityId");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setActivityId(activityId);
        ar.setCreateBy(createBy);
        ar.setCreateTime(createTime);
        ar.setEditFlag(editFlag);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.saveRemark(ar);

        // 将flag和ActivityRemark对象存入map中，在转为json格式数据
        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("ar", ar);
        PrintJson.printJsonObj(response, map);

    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("删除备注操作");

        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.deleteRemark(id);

        PrintJson.printJsonFlag(response, flag);
    }

    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据市场活动id，获取备注信息列表");

        String activityId = request.getParameter("activityId");

        // 获取业务层的代理对象
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<ActivityRemark> arList = as.getRemarkListByAid(activityId);

        PrintJson.printJsonObj(response, arList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到跳转到详细信息页的操作");

        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity a = as.detail(id);

        request.setAttribute("a", a);

        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request, response);

    }

    private void update(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场活动修改操作");

        // 添加操作
        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        // 修改时间，当前系统事件
        String editTime = DateTimeUtil.getSysTime();
        // 修改人：当前登录的用户
        String editBy = ((User) request.getSession(false).getAttribute("user")).getName();

        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setEditTime(editTime);
        a.setEditBy(editBy);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        // 执行添加方法
        boolean flag = activityService.update(a);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询用户信息列表和根据市场活动id查询单条记录的操作");

        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /*
            总结：
                controller调用service的方法，返回值应该是什么？
                你得想一想前端要什么，就要从service层取什么

            前端需要的，管业务层去要
            uList
            a

            以上两项信息，复用率不高，我们选择使用map打包这两项信息即可
            map
         */
        Map<String, Object> map = as.getUserListAndActivity(id);

        PrintJson.printJsonObj(response, map);

    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场活动的删除操作");

        String ids[] = request.getParameterValues("id");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = activityService.delete(ids);

        PrintJson.printJsonFlag(response, flag);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        // 添加操作
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        // 创建时间，当前系统事件
        String createTime = DateTimeUtil.getSysTime();
        // 创建人：当前登录的用户
        String createBy = ((User) request.getSession(false).getAttribute("user")).getName();

        System.out.println(owner);
        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setCreateTime(createTime);
        a.setCreateBy(createBy);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        // 执行添加方法
        boolean flag = activityService.saveOne(a);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        // 调用查询方法
        List<User> allUser = activityService.selectAllUser();

        // System.out.println(allUser);

        // 将其转为json类型，并传递到ajax
        PrintJson.printJsonObj(response, allUser);

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        // 接收数据
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        // 每页展示的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        // 计算出掠过的记录数
        int skipCount = (pageNo-1) * pageSize;

        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("pageSize", pageSize);
        map.put("skipCount", skipCount);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());


        // 通过代理调方法
        /*
            前端要的是：市场活动信息列表
                        查询的总条数

                        业务层拿到了以上两项信息之后，如何作返回呢？
                            map
                                map.put("dataList", dataList);
                                map.put("total", total)
                                PrintJson map --> json

                            vo
                            PagenationVO<T>
                                private int total;
                                private List<?> dataList;

                            PaginationVO<Activity> vo = new PaginationVO<>();
                            vo.setTotal(total);
                            vo.setDataList(dataList);
                            PrintJson vo -- json
                            {"total" : 100, "dataList" : [{市场活动1}, {市场活动2}...]}

                            将来分页查询，每个模块都有，所以我们选择使用一个通用的vo，操作起来比较方便

         */
        PaginationVO<Activity> vo = activityService.pageList(map);


        // 转为json类型，输出到嵌套ajax
        PrintJson.printJsonObj(response, vo);

    }

}
