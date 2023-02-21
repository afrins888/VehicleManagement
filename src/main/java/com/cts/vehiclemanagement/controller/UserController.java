package com.cts.vehiclemanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cts.vehiclemanagement.Dto.UserDTO;
import com.cts.vehiclemanagement.domain.Users;
import com.cts.vehiclemanagement.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	@PostMapping(path="/api/login/signup")
	public  String  signup (@RequestBody Users  user) {
		return userService.signup(user) ;
		
	}
	@GetMapping(path="user/getAllUsers")
	public  List<Users> getAllUsers() throws Exception {
	return userService.getAllUsers();
	}
	
//	@GetMapping(path="user/getStatus")
//	public UserDTO getUsersStatus(){
//		UserDTO userDTO = userService.getUserStatus();
//		return userDTO;
//	}
}
