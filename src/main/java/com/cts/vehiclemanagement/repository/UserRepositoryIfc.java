package com.cts.vehiclemanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cts.vehiclemanagement.domain.Users;

public interface UserRepositoryIfc extends CrudRepository<Users, Long> {

	Users findByUsername(String username);

	Users findByuserid(Long id);
	List<Users> findAll();
	
	
	@Query(value="select * from users u where status = ?1",nativeQuery=true)
	List<Users> findByStatus(Boolean status);

	
	List<Users> findAllByuserid(Long userid);

}
