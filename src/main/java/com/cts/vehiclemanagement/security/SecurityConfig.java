package com.cts.vehiclemanagement.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.cts.vehiclemanagement.config.SimpleCORSFilter;

@Configuration
@EnableWebSecurity
@ComponentScan("com.org.sanus")
@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final static Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
	  private static final String API_PATH = "/login"; 
	  public static final String AUTHENTICATE_URL = API_PATH + "/authenticate"; 
	  public static final String STUFF_URL = API_PATH + "/stuff"; // Spring Boot Actuator services public
	  static final String AUTOCONFIG_ENDPOINT = "/autoconfig"; 
	  public static final String BEANS_ENDPOINT = "/beans"; 
	  public static final String CONFIGPROPS_ENDPOINT = "/configprops"; 
	  public static final String ENV_ENDPOINT = "/env"; 
	  public static final String MAPPINGS_ENDPOINT = "/mappings";
	  public static final String METRICS_ENDPOINT = "/metrics"; 
	  public static final String SHUTDOWN_ENDPOINT = "/shutdown";
	 

	@Autowired
	private TokenService tokenService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	private static final String[] AUTH_WHITELIST = {
	        "/authenticate",
	        "/swagger-resources/**",
	        "/swagger-ui/**",
	        "/v3/api-docs",
	        "/webjars/**"
	};

	public TokenService getTokenService() {
		return tokenService;
	}

	public void setTokenService(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		logger.info("SecurityConfig:configure:STARTS");
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable()// .httpBasic()
				// .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).invalidSessionUrl("/logout").sessionAuthenticationErrorUrl("/login").
				// .and()
				// .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests()
				// .antMatchers(actuatorEndpoints()).hasRole("ADMIN")
				.antMatchers("/login/authenticate").permitAll()
				.antMatchers("/**/resources/**").permitAll().antMatchers("/api/login/getUser").permitAll().antMatchers("/")
				.permitAll().antMatchers("/403").permitAll()
				// need to check
				.antMatchers("/logout").permitAll()
				.antMatchers(AUTH_WHITELIST).permitAll()

				// dashboard urls
				.antMatchers("/rest/authorbooks/getallauthorbooks").hasAnyRole("ADMIN","USER","CREATOR","EDITOR")
//				.antMatchers("/api/Vehicle/login/signup").hasAnyRole("ADMIN","USER","CREATOR","EDITOR")
				.antMatchers("/user/getAllUsers").hasAnyRole("ADMIN","USER")
				.anyRequest().authenticated().and().anonymous().disable().exceptionHandling()
				.authenticationEntryPoint(new AuthenticationEntryPoint() {
					@Override
					public void commence(HttpServletRequest request, HttpServletResponse response,
							AuthenticationException authException) throws IOException, ServletException {
						logger.info("user is unauthorised...");
						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						response.setContentType("application/json");
						response.sendError(401, "Invalid Token");
					}
				}).accessDeniedPage("/403").accessDeniedHandler(myAccessDeniedHandler());

		http.addFilterBefore(new SimpleCORSFilter(authenticationManager()), BasicAuthenticationFilter.class)
				.addFilterBefore(new AuthenticationFilter(authenticationManager(), tokenService, jwtTokenUtil),
						BasicAuthenticationFilter.class)
//				.addFilterBefore(new ManagementEndpointAuthenticationFilter(authenticationManager()),
//						BasicAuthenticationFilter.class)
		;

	}

	@Bean
	public AccessDeniedHandler myAccessDeniedHandler() {
		// TODO Auto-generated method stub
		logger.info("Inside AccessDeniedHandler block..");
		return new MyAccessDeniedHandler();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/index.html").antMatchers("/main.*.js").antMatchers("/scripts.*.js")
				.antMatchers("/polyfills.*.js").antMatchers("/runtime.*.js").antMatchers("/styles.*.css")
				.antMatchers("/assets/**").antMatchers("/").antMatchers("/logout/")
				.antMatchers("/api/login/signup").antMatchers(AUTH_WHITELIST);

	}

	private String[] actuatorEndpoints() {
		return new String[] { AUTOCONFIG_ENDPOINT, BEANS_ENDPOINT,
				CONFIGPROPS_ENDPOINT, ENV_ENDPOINT,
				MAPPINGS_ENDPOINT, METRICS_ENDPOINT,
				SHUTDOWN_ENDPOINT };
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(domainUsernamePasswordAuthenticationProvider())
				.authenticationProvider(backendAdminUsernamePasswordAuthenticationProvider())
				.authenticationProvider(tokenAuthenticationProvider());
	}

	/*
	 * @Bean public TokenService tokenService() { return new TokenService(); }
	 */

	@Bean
	public ExternalServiceAuthenticator someExternalServiceAuthenticator() {
		return new SomeExternalServiceAuthenticator();
	}

	@Bean
	public AuthenticationProvider domainUsernamePasswordAuthenticationProvider() {
		return new DomainUsernamePasswordAuthenticationProvider(tokenService, someExternalServiceAuthenticator());
	}

	@Bean
	public AuthenticationProvider backendAdminUsernamePasswordAuthenticationProvider() {
		return new BackendAdminUsernamePasswordAuthenticationProvider();
	}

	@Bean
	public AuthenticationProvider tokenAuthenticationProvider() {
		return new TokenAuthenticationProvider(tokenService);

	}

}
