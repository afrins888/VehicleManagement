package com.cts.vehiclemanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cts.vehiclemanagement.Dto.VehicleDto;
import com.cts.vehiclemanagement.domain.Vehicle;
import com.cts.vehiclemanagement.service.VehicleService;

@RestController
public class VehicleController {
@Autowired

private VehicleService vehicleService;

@GetMapping (path="/rest/vehicle/getAll")
public List<Vehicle> getAll(){
List<Vehicle> vehicle = vehicleService.getAll();
return vehicle;
}

@PostMapping (path="/rest/vehicle/addNewVehicle")
public String addNewVehicle(@RequestBody VehicleDto vehicleDto) throws Exception {
	return vehicleService.addNewVehicle(vehicleDto);
}
}