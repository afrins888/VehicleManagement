package com.cts.vehiclemanagement.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.cts.vehiclemanagement.domain.Company;

public interface CompanyRepositoryIfc extends CrudRepository<Company, Long> {
List<Company> findAll();
Company findBycompanyid(Long companyid);

}
