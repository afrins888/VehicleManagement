package com.cts.vehiclemanagement.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cts.vehiclemanagement.domain.Vehicle;

public interface VehicleRepositoryIfc extends CrudRepository<Vehicle,Long>  {
List<Vehicle> findAll();
Vehicle findByvehicleid(Long vehicleid);
}
