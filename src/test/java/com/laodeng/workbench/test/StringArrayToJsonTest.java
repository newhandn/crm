package com.laodeng.workbench.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class StringArrayToJsonTest {

    @Test
    public void test() throws JsonProcessingException {

        String[] array = {"1", "2", "3"};

        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(array);

        System.out.println(json);

    }

}
