package com.cts.vehiclemanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cts.vehiclemanagement.Dto.UserDTO;
import com.cts.vehiclemanagement.application.ApplicationConstants;
import com.cts.vehiclemanagement.domain.Users;
import com.cts.vehiclemanagement.repository.UserRepositoryIfc;
@Service
public class UserService {
	@Autowired
	private UserRepositoryIfc userRepository;

	Logger LOG = LoggerFactory.getLogger(UserService.class);

	public String signup(Users user) {

		userRepository.save(user);
		return "sucessfully signed up";

	}

	public Users getUser(String username, String password)throws Exception {

		Users saveduser = userRepository.findByUsername(username);

		if(saveduser == null) {
			throw new Exception("User doesnot exist");
		}
		else if(!saveduser.getPassword().equals(password)) {
			throw new Exception("Username password doesn't match");
		}
		else {
			return saveduser;
		} 
	}
	public Users getUserByUserName(String userName) {
		Users user = userRepository.findByUsername(userName);
		return user;
	}

	public Users getCurrentUserWithRole() throws Exception {
		LOG.info("inside getCurrentUserWithRole");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users user = null;
		if(authentication != null) {
			try {
				String userName = authentication.getPrincipal().toString();
				user = getUserByUserName(userName);


			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}	

		}else {
			throw new Exception("Error while fetching userdetails");
		}
		return user;
	}

	/*public List<UserDTO> getAll() {
	List<Users> users= (List<Users>) userRepository.findAll();

	List<UserDTO> userDTOs= new ArrayList<>();
	for (Users user:users ) {
		UserDTO userDTO = new UserDTO();

		userDTO.setUserid(user.getUserid());
		userDTO.setUsername(user.getUsername());
		userDTO.setFirstname(user.getFirstname());
		userDTO.setLastname(user.getLastname());
		userDTO.setDateofbirth(user.getDateodbirth());
		userDTOs.add(userDTO);
		}
	return userDTOs;

}*/

	public List<Users> getAllUsers () throws Exception {
		Users loggedUser = getCurrentUserWithRole();
		if(loggedUser.getRole().getRoleName().equals(ApplicationConstants.ADMIN_ROLE)) {
			return userRepository.findAll();
		}else {
			return userRepository.findByStatus(true);



		}
	}

//	public UserDTO getUserStatus() {
//		List<Users> users= (List<Users>) userRepository.findAllByuserid();//findByStatus(true)
//
//		UserDTO userDTO = new UserDTO();
//		for (Users user:users ) {
//
//
//			userDTO.setUserid(user.getUserid());
//			userDTO.setStatus(user.isStatus());
//
//		}
//		return userDTO;  
//	}



	/*List<UserDTO> userDTOs= new ArrayList<>();
	for (Users user:users ) {
		UserDTO userDTO = new UserDTO();

		userDTO.setUserid(user.getUserid());

}*/



	/*public List<Users> getAllUsers() {

Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
if (authentication != null && authentication.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
    return userRepository.findAll();
} else {
    return userRepository.findByStatus(false);
}
}
}*/


}



