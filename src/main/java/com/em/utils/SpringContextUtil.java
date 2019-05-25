package com.em.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author Baikang Lu
 * @date 2018/7/9
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    private static boolean isReloaded = false;

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }



    public static <T> T getBean(String name){
//        synchronized (SpringContextUtil.class){
//            while (!isReloaded){
//                try {
//                    SpringContextUtil.class.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            return (T)applicationContext.getBean(name);
//        }
        return (T)applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
//        synchronized (SpringContextUtil.class){
//            SpringContextUtil.applicationContext = applicationContext;
//            isReloaded =true;
//            SpringContextUtil.class.notifyAll();
//        }
    }
}
