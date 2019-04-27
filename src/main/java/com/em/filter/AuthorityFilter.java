package com.em.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebFilter(filterName = "authorityFilter", urlPatterns = "/*")
//@Order(2)
public class AuthorityFilter implements Filter {
	
	protected final Logger logger = LoggerFactory.getLogger(AuthorityFilter.class);

//	@Autowired
//	RedisUtil redisUtil;

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			HttpServletResponse response = (HttpServletResponse) servletResponse;
		logger.info("AuthorityFilter");
		String servletPath = request.getServletPath();
		String requestURI = request.getRequestURI();

		logger.info(servletPath);// /user/name
		logger.info(requestURI);// /bd-demo/user/name



		filterChain.doFilter(request,response);
	}

	@Override
	public void destroy() {
		
	}

}
