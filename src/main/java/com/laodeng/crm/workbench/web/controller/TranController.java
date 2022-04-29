package com.laodeng.crm.workbench.web.controller;

import com.laodeng.crm.settings.domain.User;
import com.laodeng.crm.settings.service.Impl.UserServiceImpl;
import com.laodeng.crm.settings.service.UserService;
import com.laodeng.crm.utils.DateTimeUtil;
import com.laodeng.crm.utils.PrintJson;
import com.laodeng.crm.utils.ServiceFactory;
import com.laodeng.crm.utils.UUIDUtil;
import com.laodeng.crm.workbench.domain.Tran;
import com.laodeng.crm.workbench.domain.TranHistory;
import com.laodeng.crm.workbench.service.CustomerService;
import com.laodeng.crm.workbench.service.Impl.CustomerServiceImpl;
import com.laodeng.crm.workbench.service.Impl.TranServiceImpl;
import com.laodeng.crm.workbench.service.TranService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet({"/workbench/transaction/add.do", "/workbench/transaction/getCustomerName.do",
            "/workbench/transaction/save.do", "/workbench/transaction/detail.do",
            "/workbench/transaction/getHistoryListByTranId.do", "/workbench/transaction/changeStage.do",
            "/workbench/transaction/getCharts.do"})
public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();

        if("/workbench/transaction/add.do".equals(servletPath)){

            add(request, response);

        }else if("/workbench/transaction/getCustomerName.do".equals(servletPath)){

            getCustomerName(request, response);

        }else if("/workbench/transaction/save.do".equals(servletPath)){

            save(request, response);

        }else if("/workbench/transaction/detail.do".equals(servletPath)){

            detail(request, response);

        }else if("/workbench/transaction/getHistoryListByTranId.do".equals(servletPath)){

            getHistoryListByTranId(request, response);

        }else if("/workbench/transaction/changeStage.do".equals(servletPath)){

            changeStage(request, response);

        }else if("/workbench/transaction/getCharts.do".equals(servletPath)){

            getCharts(request, response);

        }

    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得交易阶段数量统计图表的数据");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        /*
            业务层为我们返回
                total
                dataList

                通过map打包以上两项进行返回

         */
        Map<String, Object> map = ts.getCharts();

        PrintJson.printJsonObj(response, map);

    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行改变阶段的操作");

        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expected_date = request.getParameter("expected_date");
        String edit_by = ((User) request.getSession().getAttribute("user")).getName();
        String edit_time = DateTimeUtil.getSysTime();

        Tran t = new Tran();
        t.setId(id);
        t.setStage(stage);
        t.setMoney(money);
        t.setExpected_date(expected_date);
        t.setEdit_by(edit_by);
        t.setEdit_time(edit_time);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.changeStage(t);

        Map<String, String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        t.setPossibility(pMap.get(stage));

        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("t", t);

        PrintJson.printJsonObj(response, map);
    }

    private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据交易Id取得相应的历史列表");

        String tranId = request.getParameter("tranId");

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());

        List<TranHistory> thList = tranService.getHistoryListByTranId(tranId);

        // 阶段和可能性之间的对应关系
        Map<String, String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");

        // 将交易历史列表遍历
        for(TranHistory t : thList){

            // 根据每一条交易历史，取出每一个阶段
            String stage = t.getStage();
            String possibility = pMap.get(stage);
            t.setPossibility(possibility);

        }

        PrintJson.printJsonObj(response, thList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("跳转到详细信息页");

        String id = request.getParameter("id");

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Tran t = tranService.detail(id);

        // 处理可能性
        /*
            阶段
            阶段和可能性之间的对应关系 pMap
         */

        String stage = t.getStage();
        ServletContext application1 = this.getServletContext();
        ServletContext application2 = request.getServletContext();
        ServletContext application3 = this.getServletConfig().getServletContext();

        Map<String, String> pMap = (Map<String, String>) application1.getAttribute("pMap");

        String possibility = pMap.get(stage);

        /*
            t

            vo
                private Tran t;
                private String possibility;
         */

        t.setPossibility(possibility);

        request.setAttribute("t", t);

        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("执行添加交易的操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expected_date = request.getParameter("expected_date");
        String customerName = request.getParameter("customerName"); // 此处我们暂时只有客户名称，还没有id
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activity_id = request.getParameter("activityId");
        String contacts_id = request.getParameter("contactsId");
        String create_by = ((User) request.getSession().getAttribute("user")).getName();
        String create_time = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contact_summary = request.getParameter("contactSummary");
        String next_contact_time = request.getParameter("nextContactTime");

        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpected_date(expected_date);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivity_id(activity_id);
        t.setContacts_id(contacts_id);
        t.setCreate_by(create_by);
        t.setCreate_time(create_time);
        t.setDescription(description);
        t.setContact_summary(contact_summary);
        t.setNext_contact_time(next_contact_time);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.save(t, customerName);

        if(flag){

            // 如果添加交易成功，跳转到列表页
            // request.getRequestDispatcher("/workbench/transaction/index.jsp").forward(request, response);

            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");

        }

    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得客户名称列表（按照客户名称进行模糊查询）");

        // 获取参数
        String name = request.getParameter("name");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> sList = cs.getCustomerName(name);

        // 转换为json
        PrintJson.printJsonObj(response, sList);

    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到跳转到交易添加页的操作");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        request.setAttribute("uList", uList);

        // 转发，一次请求
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);

    }
}
