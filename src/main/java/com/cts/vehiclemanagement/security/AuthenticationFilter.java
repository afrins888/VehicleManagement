package com.cts.vehiclemanagement.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;



public class AuthenticationFilter extends GenericFilterBean {

	private final static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

	/*
	 * public static final String TOKEN_SESSION_KEY = "token"; public static final
	 * String USER_SESSION_KEY = "user"; public static final String SKYNET =
	 * "skynet"; private static final String OTDS_USER_NAME_TOKEN =
	 * "X-Auth-Username"; private static final String OTDS_PWD_NAME_TOKEN =
	 * "X-Auth-Password"; private static final String TOKEN = "X-Auth-Token";
	 * private static final String TGO_TEMP_TOKEN = "X-Auth-Tgo-UserId"; private
	 * static final String TGO_USER_ID = "IM_Userid";
	 */

	private AuthenticationManager authenticationManager;
	private TokenService tokenService;
	private JwtTokenUtil jwtTokenUtil;


	public AuthenticationFilter(AuthenticationManager authenticationManager, TokenService tokenService,
			 JwtTokenUtil jwtTokenUtil) {
		this.authenticationManager = authenticationManager;
		this.tokenService = tokenService;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		logger.info("AuthenticationFilter:doFilter::STARTS");
		String username = null;
		String password = null;
		String loginType = null;
		String token = null;
		Authentication authentication = null;
		HttpServletRequest httpRequest = null;
		HttpServletResponse httpResponse = null;
		try {
			httpRequest = asHttp(request);
			httpResponse = asHttp(response);

			logger.info("httpRequest==" + httpRequest);

			String resourcePath = new UrlPathHelper().getPathWithinApplication(httpRequest);

		
			token = httpRequest.getHeader(com.cts.vehiclemanagement.enums.HeaderEnum.TOKEN.getCodeDesc());

			logger.info("token----->" + token);

		

			// from TGO for first time
			

				if (token == null) {

					username = httpRequest.getHeader(com.cts.vehiclemanagement.enums.HeaderEnum.USER_NAME_TOKEN.getCodeDesc());
					password = httpRequest.getHeader(com.cts.vehiclemanagement.enums.HeaderEnum.PWD_NAME_TOKEN.getCodeDesc());
					
					logger.info("username------>" + username);

					if (postToAuthenticate(httpRequest, resourcePath)) {
//
						authentication = processUsernamePasswordAuthentication(httpResponse, username, password, loginType);

						token = (String) authentication.getDetails() != null ? (String) authentication.getDetails()
								: (String) authentication.getPrincipal();
						if (token != null) {
							final HttpSession session = httpRequest.getSession(true);

							session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
									SecurityContextHolder.getContext());
						}
						logger.info("token------>" + token);

						httpResponse.setStatus(HttpServletResponse.SC_OK);
						httpResponse.setHeader(com.cts.vehiclemanagement.enums.HeaderEnum.TOKEN.getCodeDesc(), token);

						return;
//
					}

				} else {

					processTokenAuthentication(token);
					String refreshedToken = "Bearer " + jwtTokenUtil.refreshToken(token);
					httpResponse.setStatus(HttpServletResponse.SC_OK);
					httpResponse.setHeader(com.cts.vehiclemanagement.enums.HeaderEnum.TOKEN.getCodeDesc(), refreshedToken);
				}
//				if (!(StringUtils.isNotBlank(tgoServiceMap) && username != null)) {
//					logger.info("Inside addSessionContextToLogging block");
//					addSessionContextToLogging();
//				}
				chain.doFilter(request, response);

			}


		catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
			SecurityContextHolder.clearContext();
			logger.error("Internal authentication service exception", internalAuthenticationServiceException);
			httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (AuthenticationException authenticationException) {
			logger.error("AuthenticationException exception.. in authentication filter");
			SecurityContextHolder.clearContext();
			httpResponse.setStatus((HttpServletResponse.SC_UNAUTHORIZED));
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());

		} catch (Exception e) {
			httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} finally {
			MDC.remove(com.cts.vehiclemanagement.enums.HeaderEnum.TOKEN_SESSION_KEY.getCodeDesc());
			MDC.remove(com.cts.vehiclemanagement.enums.HeaderEnum.USER_SESSION_KEY.getCodeDesc());
		}
	}

	private void setAuthenticatonToContext(PreAuthenticatedAuthenticationToken requestAuthentication) {
		SecurityContextHolder.getContext().setAuthentication(requestAuthentication);
	}
   private void addSessionContextToLogging() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String tokenValue = "EMPTY";
		if (authentication != null && authentication.getDetails() != null) {
//			MessageDigestPasswordEncoder encoder = new MessageDigestPasswordEncoder(ApplicationConstants.SHA);
			
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//			tokenValue = encoder.encodePassword(authentication.getDetails().toString(),
//					ApplicationConstants.NOT_SO_RANDOM_SALT);
			tokenValue = encoder.encode(authentication.getDetails().toString());
			logger.info("tokenValue---->" + tokenValue);
		}
		MDC.put(com.cts.vehiclemanagement.enums.HeaderEnum.TOKEN_SESSION_KEY.getCodeDesc(), tokenValue);

		String userValue = "EMPTY";
		if (authentication != null && authentication.getPrincipal().toString() != null) {
			userValue = authentication.getPrincipal().toString();
			logger.info("userValue---->" + userValue);
		}
		MDC.put(com.cts.vehiclemanagement.enums.HeaderEnum.USER_SESSION_KEY.getCodeDesc(), userValue);
	}

	private HttpServletRequest asHttp(ServletRequest request) {
		return (HttpServletRequest) request;
	}

	private HttpServletResponse asHttp(ServletResponse response) {
		return (HttpServletResponse) response;
	}

	private boolean postToAuthenticate(HttpServletRequest httpRequest, String resourcePath) {
		logger.info("---postToAuthenticate----");
//		String AUTHENTICATE_URL = ApplicationConstants.LOGIN_AUTH;
		return "/api/login".equalsIgnoreCase(resourcePath)
				&& (httpRequest.getMethod().equals("POST"));
	}

	private Authentication processUsernamePasswordAuthentication(HttpServletResponse httpResponse, String username,
			String password, String loginType) throws IOException {
		logger.info("Inside processUsernamePasswordAuthentication block..");
		Authentication resultOfAuthentication = tryToAuthenticateWithUsernameAndPassword(username, password, loginType);
		SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
		return resultOfAuthentication;
	}

	private Authentication tryToAuthenticateWithUsernameAndPassword(String username, String password, String loginType) {
		logger.info("Authenticate with with user name and password. Only for login this should execute.");
		UsernamePasswordAuthenticationToken requestAuthentication = new UsernamePasswordAuthenticationToken(username,
				password);
		requestAuthentication.setDetails(loginType);
		return tryToAuthenticate(requestAuthentication);
	}

	private Authentication processTokenAuthentication(String token) {
		Authentication resultOfAuthentication = tryToAuthenticateWithToken(token);
		SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
		return resultOfAuthentication;
	}

	private Authentication tryToAuthenticateWithToken(String token) {
		logger.info("Authenticate with token. For every request this line will execute if token is available");
		PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(token,
				null);
		return tryToAuthenticate(requestAuthentication);
	}

	private Authentication tryToAuthenticate(Authentication requestAuthentication) {
		Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
		if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
			throw new InternalAuthenticationServiceException(
					"Unable to authenticate Domain User for provided credentials");
		}
		return responseAuthentication;
	}
}
