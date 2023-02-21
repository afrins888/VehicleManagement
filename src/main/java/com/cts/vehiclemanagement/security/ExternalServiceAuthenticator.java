package com.cts.vehiclemanagement.security;

public interface ExternalServiceAuthenticator    
{
	public AuthenticationWithToken authenticate(String username, String password, String loginType) throws Exception;
}

