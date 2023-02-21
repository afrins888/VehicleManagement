package com.cts.vehiclemanagement.enums;

public enum HeaderEnum {
	ACCESS_CONTROL_ALLOW_ORIGIN("Access-Control-Allow-Origin"),
	ACCESS_CONTROL_ALLOW_CREDENTIALS("Access-Control-Allow-Credentials"),
	ACCESS_CONTROL_ALLOW_METHODS("Access-Control-Allow-Methods"),
	ACCESS_CONTROL_MAX_AGE("Access-Control-Max-Age"),
	ACCESS_CONTROL_ALLOW_HEADERS("Access-Control-Allow-Headers"),
	ACCESS_CONTROL_EXPOSE_HEADERS("Access-Control-Expose-Headers"),
	ACCESS_CONTROL_ALLOW_ORIGIN_VALUES("*"),
	ACCESS_CONTROL_ALLOW_CREDENTIALS_VALUES("true"),
	ACCESS_CONTROL_ALLOW_METHODS_VALUES("POST, GET, OPTIONS, DELETE, PUT"),
	ACCESS_CONTROL_MAX_AGE_VALUES("3600"),
	ACCESS_CONTROL_ALLOW_HEADERS_VALUES("Content-Type,enctype, authorization, Accept,cache-control, X-Requested-With, remember-me,X-Auth-Username,X-Auth-Password,X-Auth-Token,IM_UserID, im_userid,im_login,im_buid,im_country,x-auth-tgotoken,Origin,HTTP_IM_UserID,X-Auth-Tgo-UserId,responsetype,X-Auth-LoginType"),
	ACCESS_CONTROL_EXPOSE_HEADERS_VALUES("X-Auth-Token,x-auth-tgotoken,IM_UserID,HTTP_IM_UserID,X-Auth-Tgo-UserId,responsetype,X-Auth-LoginType"),
	
	TOKEN_SESSION_KEY("token"),
	USER_SESSION_KEY("user"),
	SKYNET("skynet"),
	USER_NAME_TOKEN("X-Auth-Username"),
	PWD_NAME_TOKEN("X-Auth-Password"),
	TOKEN("Authorization"),

	
	im_user_id("im_userid"),
	IM_USER_ID("IM_UserID"),
	IM_BUILD("im_buid"),
	IM_COMMUNITY_ID("im_communityid"),
	IM_COUNTRY("im_country"),
	IM_FIRSTNAME("im_firstname"),
	IM_LASTNAME("im_lastname"),
	IM_MIDDLEINITIALS("im_middleinitials"),
	IM_PREFERRED_LANGUAGE("im_preferredlanguage"),
	IM_SERVICE_INST_ID("im_serviceinstid"),
	IM_TIME_ZONE("im_timezone"),
	SM_USER_IMPERSONATORNAME("sm_userimpersonatorname"),
	IM_LOGIN("im_login"),
	HTTP_IM_USER_ID("HTTP_IM_UserID"),
	LOGIN_TYPE("X-Auth-LoginType")
	;


	private String codeDesc;

	HeaderEnum(String desc) {
		this.codeDesc = desc;
	}

	public String getCodeDesc() {
		return codeDesc;
	}

	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}
}
