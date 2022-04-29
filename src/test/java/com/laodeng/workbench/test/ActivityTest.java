package com.laodeng.workbench.test;

import com.laodeng.crm.utils.ServiceFactory;
import com.laodeng.crm.utils.UUIDUtil;
import com.laodeng.crm.workbench.domain.Activity;
import com.laodeng.crm.workbench.service.ActivityService;
import com.laodeng.crm.workbench.service.Impl.ActivityServiceImpl;
import org.junit.Assert;
import org.junit.Test;

/*
    Junit：
        单元测试
        是未来实际项目开发中，用来代替主main方法


 */
public class ActivityTest {

    @Test
    public void testSave(){

        Activity a = new Activity();
        a.setId(UUIDUtil.getUUID());
        a.setName("宣传推广会");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.saveOne(a);

        Assert.assertEquals(flag, true);

    }

    /*@Test
    public void testUpdate(){

        String str = null;
        str.length();

        System.out.println(234);

    }*/

}
