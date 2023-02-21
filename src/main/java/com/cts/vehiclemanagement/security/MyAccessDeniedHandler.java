package com.cts.vehiclemanagement.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;



public class MyAccessDeniedHandler implements AccessDeniedHandler {

	private final static Logger logger = LoggerFactory.getLogger(MyAccessDeniedHandler.class);
	private String errorPage;

	public MyAccessDeniedHandler() {
	}

	public MyAccessDeniedHandler(String errorPage) {
		this.errorPage = errorPage;
	}

	public String getErrorPage() {
		return errorPage;
	}

	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException)
                throws IOException, ServletException {
		logger.info("access denied..................");
        
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.sendError(403, "Access Denied");

	}
}
