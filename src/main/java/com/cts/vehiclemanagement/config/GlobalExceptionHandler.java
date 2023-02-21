package com.cts.vehiclemanagement.config;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	@ExceptionHandler(Exception.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public @ResponseBody ExceptionJSONInfo handleException(HttpServletRequest request, Exception ex) {
		logger.info("GlobalExceptionHandler::handleException():STARTS");
		logger.info("GlobalExceptionHandler::handleException():ex:");
		ex.printStackTrace();
		ExceptionJSONInfo response = new ExceptionJSONInfo();
		response.setUrl(request.getRequestURL().toString());
		response.setMessage(ex.getMessage());
//		response.setErrorCode(ex.getErrorCode());
		logger.error(ex.getMessage(),ex);
		logger.info("GlobalExceptionHandler::handleException():ENDS");
		return response;
	}


}