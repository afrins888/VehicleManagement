package com.cts.vehiclemanagement.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;



  public class TokenAuthenticationProvider implements AuthenticationProvider {
		
	private final static Logger logger = LoggerFactory.getLogger(TokenAuthenticationProvider.class);
	  TokenService tokenService;  //need to be otds
	  @Autowired
	  private JwtTokenUtil jwtTokenUtil;
	  
	  //private UserDetailsService userDetailsService;
      
      String authToken =  null;
      String decToken = null;
      
	  public TokenAuthenticationProvider(TokenService tokenService2) {
	    	this.tokenService = tokenService2;
	 }

		@Override
		public synchronized Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
			logger.info("TokenAuthenticationProvider:authenticate:STARTS");
	    	
	        String token = (String) authentication.getPrincipal();
	        
	        logger.info("token----->"+token);
	        if (token == null || token.isEmpty() ) {
	        	logger.error("Invalid token");
	            throw new BadCredentialsException("Invalid token");
	        }
	        
	        if (token != null && token.startsWith("Bearer ")) {
				authToken = token.substring(7);
				try {
					decToken = jwtTokenUtil.getUsernameFromToken(authToken);
					logger.info("decToken"+decToken);
					logger.info("ExpirationDate is"+jwtTokenUtil.getExpirationDateFromToken(authToken));
					boolean tokenStatus = tokenService.contains(decToken);
					System.out.println("before validation");
					if (jwtTokenUtil.validateToken(authToken) && (tokenStatus)) {
						System.out.println("inside validation");
						return tokenService.retrieve(decToken);
					} else {
//						tokenService.removeToken(token);
						logger.error("Invalid token or token expired");
					}
				}catch(MalformedJwtException | UnsupportedJwtException | SignatureException | IllegalArgumentException e){
					logger.error("Invalid token or token expired", e);
				}catch (ExpiredJwtException e) {
//					tokenService.removeToken(e.getClaims().getSubject());
					logger.error("Invalid token or token expired", e);
				}
	        }else {
//	        	tokenService.removeToken(token);
	        	logger.error("Invalid token or token expired");
	        }
			return null;
	    }

	    @Override
	    public boolean supports(Class<?> authentication) {
	        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
	    }
	    
	}