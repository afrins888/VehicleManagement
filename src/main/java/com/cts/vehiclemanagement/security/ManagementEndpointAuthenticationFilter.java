package com.cts.vehiclemanagement.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;



public class ManagementEndpointAuthenticationFilter extends GenericFilterBean {

    private final static Logger logger = LoggerFactory.getLogger(ManagementEndpointAuthenticationFilter.class);
    private AuthenticationManager authenticationManager;
    private Set<String> managementEndpoints;
    
    // Spring Boot Actuator services
    public static final String AUTOCONFIG_ENDPOINT = "/autoconfig";
    public static final String BEANS_ENDPOINT = "/beans";
    public static final String CONFIGPROPS_ENDPOINT = "/configprops";
    public static final String ENV_ENDPOINT = "/env";
    public static final String MAPPINGS_ENDPOINT = "/mappings";
    public static final String METRICS_ENDPOINT = "/metrics";
    public static final String SHUTDOWN_ENDPOINT = "/shutdown";

    public ManagementEndpointAuthenticationFilter(AuthenticationManager authenticationManager) {
    	logger.info("ManagementEndpointAuthenticationFilter:STARTS");
        this.authenticationManager = authenticationManager;
        prepareManagementEndpointsSet();
    }

    private void prepareManagementEndpointsSet() {
        managementEndpoints = new HashSet<>();
        managementEndpoints.add(AUTOCONFIG_ENDPOINT);
        managementEndpoints.add(BEANS_ENDPOINT);
        managementEndpoints.add(CONFIGPROPS_ENDPOINT);
        managementEndpoints.add(ENV_ENDPOINT);
        managementEndpoints.add(MAPPINGS_ENDPOINT);
        managementEndpoints.add(METRICS_ENDPOINT);
        managementEndpoints.add(SHUTDOWN_ENDPOINT);
        // managementEndpoints.add("/");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	
    	logger.info("ManagementEndpointAuthenticationFilter:doFilter:STARTS");
        HttpServletRequest httpRequest = asHttp(request);
        HttpServletResponse httpResponse = asHttp(response);
//
        String username = httpRequest.getHeader(com.cts.vehiclemanagement.enums.HeaderEnum.USER_NAME_TOKEN.getCodeDesc());
        String password = httpRequest.getHeader(com.cts.vehiclemanagement.enums.HeaderEnum.PWD_NAME_TOKEN.getCodeDesc());
        String token = httpRequest.getHeader(com.cts.vehiclemanagement.enums.HeaderEnum.TOKEN.getCodeDesc());
        
        String resourcePath = new UrlPathHelper().getPathWithinApplication(httpRequest);
        
        logger.info("resourcePath-->"+resourcePath);
        try {
        	logger.info("username--->"+username);
        	
        	
            if (postToManagementEndpoints(resourcePath)) {
                logger.info("Trying to authenticate user {} for management endpoint by X-Auth-Username method", username);
                	processManagementEndpointUsernamePasswordAuthentication(username, password);
            }
            	chain.doFilter(request, response);
        } catch (AuthenticationException authenticationException) {
            logger.error("AuthenticationException is thrown");
            SecurityContextHolder.clearContext();
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
        }
    }

    private HttpServletRequest asHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    private HttpServletResponse asHttp(ServletResponse response) {
        return (HttpServletResponse) response;
    }

    private boolean postToManagementEndpoints(String resourcePath) {
        return managementEndpoints.contains(resourcePath);
    }

    private void processManagementEndpointUsernamePasswordAuthentication(String username, String password) throws IOException {
        Authentication resultOfAuthentication = tryToAuthenticateWithUsernameAndPassword(username, password);
        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
    }

    private Authentication tryToAuthenticateWithUsernameAndPassword(String username, String password) {
        BackendAdminUsernamePasswordAuthenticationToken requestAuthentication = new BackendAdminUsernamePasswordAuthenticationToken(username, password);
        return tryToAuthenticate(requestAuthentication);
    }

    private Authentication tryToAuthenticate(Authentication requestAuthentication) {
        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate Backend Admin for provided credentials");
        }
        logger.debug("Backend Admin successfully authenticated");
        return responseAuthentication;
    }
}

