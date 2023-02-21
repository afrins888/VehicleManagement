package com.cts.vehiclemanagement.security;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.annotation.Transactional;

public class DomainUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

	private final static Logger logger = LoggerFactory.getLogger(DomainUsernamePasswordAuthenticationProvider.class);
	private TokenService tokenService;
	private ExternalServiceAuthenticator externalServiceAuthenticator;


//	
//	@Value("${otds.auth.url}")
//	private String otdsAuthUrl;

//	final HttpSession session = httpRequest.getSession(true);
	public DomainUsernamePasswordAuthenticationProvider(TokenService tokenService,
			ExternalServiceAuthenticator externalServiceAuthenticator) {
		logger.info("DomainUsernamePasswordAuthenticationProvider:STARTS");
		this.tokenService = tokenService;
		this.externalServiceAuthenticator = externalServiceAuthenticator;

		logger.info(tokenService + " ---------------- " + externalServiceAuthenticator);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		AuthenticationWithToken resultOfAuthentication = null;
		try {
			logger.info("DomainUsernamePasswordAuthenticationProvider:authenticate:STARTS");
			String username = (String) authentication.getPrincipal();
			String password = (String) authentication.getCredentials();
			String loginType = (String) authentication.getDetails();
			logger.info("username----->" + username);

			if (username == null || password == null) {
				logger.error("BadCredentialsException is thrown ");
				throw new BadCredentialsException("Invalid Domain User Credentials");
			}

			resultOfAuthentication = externalServiceAuthenticator.authenticate(username, password, loginType);
//			if (resultOfAuthentication != null) {
//				logger.info("token----->" + resultOfAuthentication.getToken());
//				tokenService.store(username, resultOfAuthentication);
//			}

		} catch (Exception e) {
			e.printStackTrace();
//			loggingErrorTable(e,"ERR_DURING_AUTHENTICATION_WITH_OTDS",otdsAuthUrl);

		}
		return resultOfAuthentication;

	}

//	private void loggingErrorTable(Exception e, String errorCode,String msg){
//		String hostname = "Unknown";
//		try {
//			InetAddress addr;
//			addr = InetAddress.getLocalHost();
//			hostname = addr.getHostName();
//		} catch (UnknownHostException e1) {
//			System.out.println("Hostname can not be resolved");
//		}
//
//		ErrorLog errorLog = new ErrorLog();
//
//		StringWriter error = new StringWriter();
//		e.printStackTrace(new PrintWriter(error));
//		errorLog.setErrorDescription(error.toString());
//		errorLog.setTimeStamp(new Date());
//		errorLog.setErrorCode(errorCode.toString());
//		errorLog.setErrorUrl(msg);
//		errorLog.setLegName(hostname);
//		errorLogRepository.save(errorLog);
//	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
}
