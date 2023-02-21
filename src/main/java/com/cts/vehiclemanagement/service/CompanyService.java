package com.cts.vehiclemanagement.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.vehiclemanagement.Dto.CompanyDto;
import com.cts.vehiclemanagement.domain.Company;
import com.cts.vehiclemanagement.domain.CompanyList;
import com.cts.vehiclemanagement.domain.Users;
import com.cts.vehiclemanagement.repository.CompanyListRepositoryIfc;
import com.cts.vehiclemanagement.repository.CompanyRepositoryIfc;
import com.cts.vehiclemanagement.repository.UserRepositoryIfc;
@Service
public class CompanyService {
	@Autowired
	private UserRepositoryIfc userRepository;
	@Autowired
	private CompanyRepositoryIfc companyRepository;
	@Autowired
	private CompanyListRepositoryIfc companyListRepository;
	@Autowired 
	private UserService userService;

	public List<Company> getAllCompany() {

		return companyRepository.findAll() ;
	}

	public String addNewCompany(CompanyDto companyDto) throws Exception {
		Users loggedUser = userService.getCurrentUserWithRole();
		Company company = new Company();
		company.setCompanyid(companyDto.getCompanyid());
		company.setCompanyname(companyDto.getCompanyname());
		company.setAddress(companyDto.getAddress());
		company.setContactno(companyDto.getContactno());
		if (company.getCompanyid() == null) {
			company.setCreatedBy(loggedUser.getUserid());
			company.setCreatedOn(new Date());
		}else {
			Company savedCompany= companyRepository.findBycompanyid(company.getCompanyid());
			company.setCreatedBy(savedCompany.getCreatedBy());
			company.setUpdatedBy(savedCompany.getUpdatedBy());
			company.setCreatedOn(savedCompany.getCreatedOn());
			company.setUpdatedOn(savedCompany.getUpdatedOn());
		}

		companyRepository.save(company);
		return "success";

	}

	/*public List<CompanyDto> getAll() {
		List<Company> company = companyRepository.findAll();
		List<CompanyDto> companyDtos= new ArrayList<>();
		for (Company companys: company ) {
			CompanyDto companyDto = new CompanyDto();
			companyDto.setCompanyid(companys.getCompanyid());
			companyDto.setCompanyname(companys.getCompanyname());
			companyDto.setAddress(companys.getAddress());
			companyDto.setContactno(companys.getContactno());
			companyDto.setCreatedby(getUserNameById(companys.getCreatedBy()));
			companyDto.setUpdatedby(getUserNameById(companys.getUpdatedBy()));
			companyDto.setCreatedon(convertDateToString(companys.getCreatedOn()));
			//companyDto.setUpdatedon(getUserNameById(companys.getUpdatedOn()));
			companyDto.setUpdatedon(convertDateToString(companys.getUpdatedOn()));

			String updatedOn = (getUsersById(companys.getUpdatedOn()) != null)?
					getUsersById(companys.getUpdatedOn()).getUsername():"";
			companyDto.setUpdatedOn(updatedOn);

}		

		return companyDtos;
	}*/
	
	
	public List<CompanyDto> getAll() {
		List<CompanyList> companys = companyListRepository.findAllCompanyList();
		List<CompanyDto> companyDtos= new ArrayList<>();
		for (CompanyList company: companys ) {
			CompanyDto companyDto = new CompanyDto();
			companyDto.setCompanyid(company.getCompanyid());
			companyDto.setCompanyname(company.getCompanyname());
			companyDto.setAddress(company.getAddress());
			companyDto.setContactno(company.getContactno());
			companyDto.setCreatedby(company.getCreatedBy());
			companyDto.setUpdatedby(company.getUpdatedBy());
			companyDto.setCreatedon(company.getCreatedOn());
			companyDto.setCreatedon(company.getCreatedOn());
			companyDto.setUpdatedon(company.getUpdatedOn());
			
		}
		return companyDtos;
	}
	private String getUserNameById (Long id) {
		Users user= userRepository.findByuserid(id);
		if (user== null) {
			return "";
		}else 
			return user.getUsername();

	}

	private Users getUsersById(Long id) {
		Users user = userRepository.findByuserid(id);
		return user;
	}

	private String convertDateToString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");  
		String strDate = formatter.format(date);  
		return strDate;

	}

}
