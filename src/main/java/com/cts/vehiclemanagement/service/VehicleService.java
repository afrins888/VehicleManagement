package com.cts.vehiclemanagement.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.vehiclemanagement.Dto.VehicleDto;
import com.cts.vehiclemanagement.domain.Users;
import com.cts.vehiclemanagement.domain.Vehicle;
import com.cts.vehiclemanagement.repository.VehicleRepositoryIfc;
@Service
public class VehicleService {
@Autowired
private UserService userService;
@Autowired 
private VehicleRepositoryIfc vehicleRepository;
	public List<Vehicle> getAll() {
		
		return vehicleRepository.findAll() ;
	}
	public String addNewVehicle(VehicleDto vehicleDto) throws Exception {
		Users loggedUser = userService.getCurrentUserWithRole();
		Vehicle vehicle = new Vehicle();
		vehicle.setVehicleid(vehicleDto.getVehicle_id());
		vehicle.setVehiclename(vehicleDto.getVehiclename());
		vehicle.setEnginecc(vehicleDto.getEnginecc());
		if (vehicle.getVehicleid()== null) {
			vehicle.setCreatedBy(loggedUser.getUserid());
			vehicle.setCreatedOn(new Date());
		}else {
		Vehicle savedVehicle= vehicleRepository.findByvehicleid(vehicle.getVehicleid());
		vehicle.setCreatedBy(savedVehicle.getCreatedBy());
		vehicle.setUpdatedBy(savedVehicle.getUpdatedBy());
		vehicle.setCreatedOn(savedVehicle.getCreatedOn());
		vehicle.setUpdatedOn(savedVehicle.getUpdatedOn());
		}
		vehicleRepository.save(vehicle);
		return "success";
	}

}
