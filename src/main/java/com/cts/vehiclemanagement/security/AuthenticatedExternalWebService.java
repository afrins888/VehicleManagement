package com.cts.vehiclemanagement.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;


public class AuthenticatedExternalWebService extends AuthenticationWithToken {


	@Autowired
	private com.cts.vehiclemanagement.service.UserService externalWebService;
    

	private String latestToken;

    public String getLatestToken() {
		return latestToken;
	}

	public void setLatestToken(String latestToken) {
		this.latestToken = latestToken;
	}

	public com.cts.vehiclemanagement.service.UserService getExternalWebService() {
		return externalWebService;
	}

	public void setExternalWebService(com.cts.vehiclemanagement.service.UserService externalWebService) {
		this.externalWebService = externalWebService;
	}

	public AuthenticatedExternalWebService(Object aPrincipal, Object aCredentials, Collection<? extends GrantedAuthority> anAuthorities) {
        super(aPrincipal, aCredentials, anAuthorities);
    }
}
