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

        System.out.println("?????????????????????????????????????????????");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        /*
            ????????????????????????
                total
                dataList

                ??????map??????????????????????????????

         */
        Map<String, Object> map = ts.getCharts();

        PrintJson.printJsonObj(response, map);

    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("???????????????????????????");

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

        System.out.println("????????????Id???????????????????????????");

        String tranId = request.getParameter("tranId");

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());

        List<TranHistory> thList = tranService.getHistoryListByTranId(tranId);

        // ???????????????????????????????????????
        Map<String, String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");

        // ???????????????????????????
        for(TranHistory t : thList){

            // ???????????????????????????????????????????????????
            String stage = t.getStage();
            String possibility = pMap.get(stage);
            t.setPossibility(possibility);

        }

        PrintJson.printJsonObj(response, thList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("????????????????????????");

        String id = request.getParameter("id");

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Tran t = tranService.detail(id);

        // ???????????????
        /*
            ??????
            ??????????????????????????????????????? pMap
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

        System.out.println("???????????????????????????");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expected_date = request.getParameter("expected_date");
        String customerName = request.getParameter("customerName"); // ????????????????????????????????????????????????id
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

            // ?????????????????????????????????????????????
            // request.getRequestDispatcher("/workbench/transaction/index.jsp").forward(request, response);

            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");

        }

    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("??????????????????????????????????????????????????????????????????");

        // ????????????
        String name = request.getParameter("name");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> sList = cs.getCustomerName(name);

        // ?????????json
        PrintJson.printJsonObj(response, sList);

    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("??????????????????????????????????????????");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        request.setAttribute("uList", uList);

        // ?????????????????????
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);

    }
}
