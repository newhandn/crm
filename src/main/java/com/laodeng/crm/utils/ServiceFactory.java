package com.laodeng.crm.utils;

public class ServiceFactory {
    public static Object getService(Object service){
        return new TransactionInvocationHandler(service).getProxy();
    }
}
