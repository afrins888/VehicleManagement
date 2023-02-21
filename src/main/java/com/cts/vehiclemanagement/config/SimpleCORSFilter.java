package com.cts.vehiclemanagement.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;


public class SimpleCORSFilter implements Filter {

	private final Logger LOGGER = LoggerFactory.getLogger(SimpleCORSFilter.class);

	//static final String ORIGIN = "Origin";
	private AuthenticationManager authenticationManager;

	public SimpleCORSFilter(AuthenticationManager authenticationManager) {
		LOGGER.info("Inside SimpleCORSFilter block");
		this.authenticationManager = authenticationManager;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
		
		LOGGER.info("Inside doFilter block..");
		LOGGER.info("origin : "+request.getHeader("Origin"));
			response.setHeader(com.cts.vehiclemanagement.enums.HeaderEnum.ACCESS_CONTROL_ALLOW_ORIGIN.getCodeDesc(), request.getHeader("Origin"));			
			response.setHeader(com.cts.vehiclemanagement.enums.HeaderEnum.ACCESS_CONTROL_ALLOW_CREDENTIALS.getCodeDesc(),com.cts.vehiclemanagement.enums.HeaderEnum.ACCESS_CONTROL_ALLOW_CREDENTIALS_VALUES.getCodeDesc());
			response.setHeader(com.cts.vehiclemanagement.enums.HeaderEnum.ACCESS_CONTROL_ALLOW_METHODS.getCodeDesc(), com.cts.vehiclemanagement.enums.HeaderEnum.ACCESS_CONTROL_ALLOW_METHODS_VALUES.getCodeDesc());
			response.setHeader(com.cts.vehiclemanagement.enums.HeaderEnum.ACCESS_CONTROL_MAX_AGE.getCodeDesc(),com.cts.vehiclemanagement.enums.HeaderEnum.ACCESS_CONTROL_MAX_AGE_VALUES.getCodeDesc());
			response.setHeader(com.cts.vehiclemanagement.enums.HeaderEnum.ACCESS_CONTROL_ALLOW_HEADERS.getCodeDesc(),com.cts.vehiclemanagement.enums.HeaderEnum.ACCESS_CONTROL_ALLOW_HEADERS_VALUES.getCodeDesc());
			response.setHeader(com.cts.vehiclemanagement.enums.HeaderEnum.ACCESS_CONTROL_EXPOSE_HEADERS.getCodeDesc(),com.cts.vehiclemanagement.enums.HeaderEnum.ACCESS_CONTROL_EXPOSE_HEADERS_VALUES.getCodeDesc());

		
		if (request.getMethod().equals("OPTIONS")) {
			try {
				LOGGER.info("Inside option method..");
				response.getWriter().print("OK");
				response.getWriter().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
	}
	

}