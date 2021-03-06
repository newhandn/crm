package com.laodeng.crm.workbench.web.controller;

import com.laodeng.crm.settings.domain.User;
import com.laodeng.crm.settings.service.Impl.UserServiceImpl;
import com.laodeng.crm.settings.service.UserService;
import com.laodeng.crm.utils.DateTimeUtil;
import com.laodeng.crm.utils.PrintJson;
import com.laodeng.crm.utils.ServiceFactory;
import com.laodeng.crm.utils.UUIDUtil;
import com.laodeng.crm.workbench.domain.Activity;
import com.laodeng.crm.workbench.domain.Clue;
import com.laodeng.crm.workbench.domain.Tran;
import com.laodeng.crm.workbench.service.ActivityService;
import com.laodeng.crm.workbench.service.ClueService;
import com.laodeng.crm.workbench.service.Impl.ActivityServiceImpl;
import com.laodeng.crm.workbench.service.Impl.ClueServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet({"/workbench/clue/getUserList.do", "/workbench/clue/save.do", "/workbench/clue/detail.do",
            "/workbench/clue/getAllClueByLimit.do", "/workbench/clue/getActivityListByClueId.do",
            "/workbench/clue/unbund.do",
            "/workbench/clue/getActivityListByNameAndNotByClueId.do",
            "/workbench/clue/bund.do",
            "/workbench/clue/getActivityListByName.do",
            "/workbench/clue/convert.do"})
public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("进入线索控制器");

        // 获取前端访问的路径名
        String servletPath = request.getServletPath();

        if("/workbench/clue/getUserList.do".equals(servletPath)){

            // 具体市场活动具体
            getUserList(request, response);

        }else if("/workbench/clue/save.do".equals(servletPath)) {

            save(request, response);

        }else if("/workbench/clue/detail.do".equals(servletPath)){

            detail(request, response);

        }else if("/workbench/clue/getAllClueByLimit.do".equals(servletPath)){

            getAllClueByLimit(request, response);

        }else if("/workbench/clue/getActivityListByClueId.do".equals(servletPath)){

            getActivityListByClueId(request, response);

        }else if("/workbench/clue/unbund.do".equals(servletPath)){

            unbund(request, response);

        }else if("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(servletPath)){

            getActivityListByNameAndNotByClueId(request, response);

        }else if("/workbench/clue/bund.do".equals(servletPath)){

            bund(request, response);

        }else if("/workbench/clue/getActivityListByName.do".equals(servletPath)){

            getActivityListByName(request, response);

        }else if("/workbench/clue/convert.do".equals(servletPath)){

            convert(request, response);

        }
    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("执行线索转换的操作");

        String clueId = request.getParameter("clueId");

        // 接收是否需要创建交易的标记
        String flag = request.getParameter("flag");

        Tran t = null;

        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        // 如果需要创建交易
        if(flag != null){

            t = new Tran();
            // 接收交易表单的参数
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();

            t.setId(id);
            t.setMoney(money);
            t.setName(name);
            t.setExpected_date(expectedDate);
            t.setStage(stage);
            t.setActivity_id(activityId);
            t.setCreate_time(createTime);
            t.setCreate_by(createBy);

        }

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        /*
            为业务层传递的参数：
                1、必须传递的参数clueId，有了这个clueId之后我们才知道要转换哪条记录
                2、必须传递的参数t，因为在线索转换的过程中，有可能会临时创建一笔交易（业务层接收的t也有可能是一个空）
         */
        boolean flag1 = cs.convert(clueId, t, createBy);

        if(flag1){

            // 重定向
            response.sendRedirect(request.getContextPath() + "/workbench/clue/index.jsp");

        }

    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询市场活动列表（根据名称模糊查）");

        String aname = request.getParameter("aname");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = as.getActivityListByName(aname);

        PrintJson.printJsonObj(response, aList);

    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行关联市场活动的操作");

        String cid = request.getParameter("cid");
        String aids[] = request.getParameterValues("aid");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.bund(cid, aids);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getActivityListByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询市场活动列表（根据名称模糊查 + 排除掉已经关联指定线索的列表）");

        String aname = request.getParameter("aname");
        String clueId = request.getParameter("clueId");

        // 创建map存储数据
        Map<String, String> map = new HashMap<>();

        map.put("aname", aname);
        map.put("clueId", clueId);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = as.getActivityListByNameAndNotByClueId(map);

        PrintJson.printJsonObj(response, aList);

    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行解除关联操作");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.unbund(id);

        // 将数据格式化为json类型，传递给前台
        PrintJson.printJsonFlag(response, flag);

    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据线索id查询关联的市场活动列表");

        String clueId = request.getParameter("clueId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = as.getActivityListByClueId(clueId);

        PrintJson.printJsonObj(response, aList);

    }

    private void getAllClueByLimit(HttpServletRequest request, HttpServletResponse response) {

        // 获取数据
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");
        String fullname = request.getParameter("fullname");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String phone = request.getParameter("phone");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");

        // 获取需要跳过的条数
        int pageNo = Integer.valueOf(pageNoStr);

        int pageSize = Integer.valueOf(pageSizeStr);

        int skipCount = (pageNo-1) * pageSize;

        // 将数据存入map中，传递个业务层
        Map<String, Object> map = new HashMap<>();
        map.put("fullname",fullname);
        map.put("owner",owner);
        map.put("company",company);
        map.put("phone",phone);
        map.put("mphone",mphone);
        map.put("state",state);
        map.put("source",source);
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);


        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        // 执行查询方法
        Map<String, Object> uMap = cs.getAllClueByLimit(map);

        PrintJson.printJsonObj(response, uMap);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("跳转到线索的详细信息页");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Clue c = cs.detail(id);

        request.setAttribute("c", c);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行线索添加操作");

        String id= UUIDUtil.getUUID();
        String fullname= request.getParameter("fullname");
        String appellation= request.getParameter("appellation");
        String owner= request.getParameter("owner");
        String company= request.getParameter("company");
        String job= request.getParameter("job");
        String email= request.getParameter("email");
        String phone= request.getParameter("phone");
        String website= request.getParameter("website");
        String mphone= request.getParameter("mphone");
        String state= request.getParameter("state");
        String source= request.getParameter("source");
        String createBy= ((User) request.getSession().getAttribute("user")).getName();
        String createTime= DateTimeUtil.getSysTime();
        String description= request.getParameter("description");
        String contactSummary= request.getParameter("contactSummary");
        String nextContactTime= request.getParameter("nextContactTime");
        String address= request.getParameter("address");

        Clue c = new Clue();
        c.setId(id);
        c.setFullname(fullname);
        c.setAppellation(appellation);
        c.setOwner(owner);
        c.setCompany(company);
        c.setJob(job);
        c.setEmail(email);
        c.setPhone(phone);
        c.setWebsite(website);
        c.setMphone(mphone);
        c.setState(state);
        c.setSource(source);
        c.setCreateBy(createBy);
        c.setCreateTime(createTime);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setAddress(address);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.save(c);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        // 获取代理对象
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        // 执行方法
        List<User> uList = us.getUserList();

        // 将uList转换为json格式数据
        PrintJson.printJsonObj(response, uList);

    }

}
