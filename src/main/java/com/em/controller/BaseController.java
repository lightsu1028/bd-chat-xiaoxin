package com.em.controller;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Lu Baikang on 2017/10/26.
 */
public class BaseController {

    /**
     * 获取HttpServletRequest
     * @return
     */
    protected HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    /**
     *  获取HttpServletResponse
     * @return
     */
    protected HttpServletResponse getResponse(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getResponse();
    }

    /**
     * 根据CookieName查找对应值
     * @param cookieName
     * @return
     */
    protected String getCookieValue(String cookieName){
        String result = null;
        HttpServletRequest request = getRequest();
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies){
            if(cookieName.equals(cookie.getName())){
                result = cookie.getValue();
            }
        }
        return result;
    }
}
