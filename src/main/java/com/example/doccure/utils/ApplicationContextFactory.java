package com.example.doccure.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class ApplicationContextFactory{
    private static ApplicationContext applicationContext = null;
    public static void setApplicationContext(ApplicationContext applicationContext1) throws BeansException {
        applicationContext = applicationContext1;
    }
    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }
}