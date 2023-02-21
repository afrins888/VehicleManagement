package com.cts.vehiclemanagement.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;


public class BackendAdminUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

	private final static Logger logger = LoggerFactory.getLogger(BackendAdminUsernamePasswordAuthenticationProvider.class);
//    public static final String INVALID_BACKEND_ADMIN_CREDENTIALS = "Invalid Backend Admin Credentials";

    @Value("${backend.admin.username}")
    private String backendAdminUsername;

    @Value("${backend.admin.password}")
    private String backendAdminPassword;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	logger.error("BackendAdminUsernamePasswordAuthenticationProvider:authenticate:STARTS");
        String username = (String)authentication.getPrincipal();
        String password = (String)authentication.getCredentials();

        logger.info("username----->"+username);
        
        if (credentialsMissing(username, password) || credentialsInvalid(username, password)) {
        	logger.error("BadCredentialsException is thrown ");
            throw new BadCredentialsException("Invalid Backend Admin Credentials");
        }

        return new UsernamePasswordAuthenticationToken(username, null,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN"));
    }

    private boolean credentialsMissing(String username, String password) {
    	logger.info("Inside credentialsMissing block");
        return username == null || password == null;
    }

    private boolean credentialsInvalid(String username, String password) {
    	logger.info("Inside credentialsInvalid block");
        return !isBackendAdmin(username) || !password.equals(backendAdminPassword);
    }

    private boolean isBackendAdmin(String username) {
        return backendAdminUsername.equals(username);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(BackendAdminUsernamePasswordAuthenticationToken.class);
    }
}

