package com.cts.vehiclemanagement.security;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class SomeExternalServiceAuthenticator implements ExternalServiceAuthenticator {
	
	Logger logger = LoggerFactory.getLogger(SomeExternalServiceAuthenticator.class);
	
	//public static final String SPRING_SECURITY_ROLE_PREFIX="ROLE_" ;

	
	@Autowired
	com.cts.vehiclemanagement.service.UserService userService;
	
	@Autowired
	TokenService tokenService;
	
    @Override
    public AuthenticatedExternalWebService authenticate(String username, String password, String loginType) throws Exception {
    	logger.info("SomeExternalServiceAuthenticator:AuthenticatedExternalWebService:STARTS");
    	AuthenticatedExternalWebService authenticatedExternalWebService = null;
    	logger.info("username------>"+username);
		try {
			com.cts.vehiclemanagement.domain.Users user = userService.getUser(username, password);
			authenticatedExternalWebService = new AuthenticatedExternalWebService(user.getUsername(), null,
			AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_"+user.getRole().getRoleName()));
			authenticatedExternalWebService.setExternalWebService(userService);
			
			authenticatedExternalWebService.setToken(tokenService.generateNewToken(user.getUsername(),"OTDS"));
			// END of JWT authentication
			authenticatedExternalWebService.setAuthenticated(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
        // Do all authentication mechanisms required by external web service protocol and validated response.
        // Throw descendant of Spring AuthenticationException in case of unsucessful authentication. For example BadCredentialsException

        // ...
        // ...

        return authenticatedExternalWebService;
    }
}

