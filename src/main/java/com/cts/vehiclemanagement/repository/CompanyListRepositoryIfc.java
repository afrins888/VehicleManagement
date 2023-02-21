package com.cts.vehiclemanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cts.vehiclemanagement.domain.CompanyList;

public interface CompanyListRepositoryIfc extends CrudRepository<CompanyList,Long> {
	@Query (value= "SELECT ct.company_id,ct.company_name,ct.address,ct.contact_no,ct.created_date ,ct.updated_on,u1.UserName AS created_by, u2.UserName AS updated_by FROM company_table ct INNER JOIN users u1 ON ct.created_by = u1.user_id LEFT JOIN users u2 ON ct.updated_by = u2.user_id" ,nativeQuery= true )
List<CompanyList> findAllCompanyList();

}
