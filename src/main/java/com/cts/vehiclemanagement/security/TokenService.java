package com.cts.vehiclemanagement.security;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.vehiclemanagement.domain.Users;
import com.cts.vehiclemanagement.repository.UserRepositoryIfc;
import com.cts.vehiclemanagement.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);
    public static final String SPRING_SECURITY_ROLE_PREFIX = "ROLE_";
	@Value("${jwt.expiration}")
	private long EXPIRATIONTIME;

	@Value("${jwt.secret}")
	private String SECRET;
	
	@Value("${jwt.tokenprefix}")
	private String TOKEN_PREFIX;
	
	
  
    
    @Autowired
	private UserRepositoryIfc userRepository;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
	@Autowired
	private UserService loginServiceIfc;
    
//    private Cache restApiAuthTokenCache = CacheManager.getInstance().getCache("restApiAuthTokenCache");
//    CacheConfiguration config = restApiAuthTokenCache.getCacheConfiguration();
//    public static final int HALF_AN_HOUR_IN_MILLISECONDS =  10 * 1000;

   /* @Scheduled(fixedRate = HALF_AN_HOUR_IN_MILLISECONDS)
    public void evictExpiredTokens() {
        logger.info("Evicting expired tokens");
        restApiAuthTokenCache.evictExpiredElements();
    }*/

    public String generateNewToken(String username,String userType)throws Exception {
    	//return UUID.randomUUID().toString();
    	logger.info("User Type......"+userType);
    	    	
    	try {
        	
    		 return TOKEN_PREFIX+" "+Jwts.builder()
		        .setSubject(username)
		        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
		        .signWith(SignatureAlgorithm.HS512, SECRET)
		        .compact();

             	
    	} catch (Exception e) {
			throw new Exception("Exception occured during token creations");
		}

    }

//    @Transactional
//    public void store(String token, Authentication authentication) {
//    	
//    	logger.info("authentication User Name..."+authentication.getName());
//    	
//    	Element element = new Element(token, authentication);
//    	//element.setTimeToIdle(15);
//    	//element.setTimeToLive(60);
//        restApiAuthTokenCache.put(element);
//       
//        logger.info("Token got stored in both DB and Cache");
//    }
    
    
//    public void store(String token, String id) {
//        restApiAuthTokenCache.put(new Element(token, id));
//    }

    public boolean contains(String token) {
    	boolean isContains = false;

    	isContains = true;
    	logger.info("Token is present in Cache..");
        	
        
        return isContains;
    }
    
    @Transactional
    public Authentication retrieve (String username){
    	Authentication authentication = null;
//    	Element ele = restApiAuthTokenCache.get(username);
    	logger.info("token",username);
    	Users user = new Users();
		
		user = userRepository.findByUsername(username);
		
    	authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), null,
			    AuthorityUtils.createAuthorityList(SPRING_SECURITY_ROLE_PREFIX + user.getRole().getRoleName()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
    	
    	return authentication;
    }
    
//    @Transactional
//    public boolean removeToken(String username){
//    	if(username != null){
//    		restApiAuthTokenCache.remove(username);
//    		
//       	 	logger.info("Deleted the token from DB and Cache...");
//    	}
//	     return true;
//    }
    
}
