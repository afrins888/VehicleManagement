package com.cts.vehiclemanagement.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.cts.vehiclemanagement.Dto.CompanyDto;
import com.cts.vehiclemanagement.domain.Company;
import com.cts.vehiclemanagement.helper.Excelgenerator;
import com.cts.vehiclemanagement.service.CompanyService;

@RestController
public class CompanyController {
@Autowired
private CompanyService companyService;

@GetMapping (path="/rest/company/getAllCompany")
public List<Company> getAllCompany(){
	List<Company> company= companyService.getAllCompany();
	return company;
	}

@PostMapping (path="/rest/company/addNewCompany")
public String addNewCompany(@RequestBody CompanyDto companyDto ) throws Exception  {
	return companyService.addNewCompany(companyDto);
	
}
@GetMapping(path="/company/getAllDto")
public List<CompanyDto> getAll(){

	return companyService.getAll();
	
}


@GetMapping("/company/export-to-excel")
public void exportIntoExcelFile(HttpServletResponse response) throws IOException {
    response.setContentType("application/octet-stream");
    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    String currentDateTime = dateFormatter.format(new Date());

    String headerKey = "Content-Disposition";
    String headerValue = "attachment; filename=company" + currentDateTime + ".xlsx";
    response.setHeader(headerKey, headerValue);

    List < Company > listOfCompany = (List<Company>) companyService.getAllCompany();
    Excelgenerator generator = new Excelgenerator(listOfCompany);
    generator.generateExcelFile(response);

}
}
