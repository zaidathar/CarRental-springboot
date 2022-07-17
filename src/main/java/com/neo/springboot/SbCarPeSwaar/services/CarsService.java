package com.neo.springboot.SbCarPeSwaar.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.neo.springboot.SbCarPeSwaar.models.Car;

public interface CarsService {
	Car save(Car car);
	
	void rentCarByEmail(Long carId, String custMail);
	
	char findIfCarRented(Long id);
	
	Page<Car> findPaginatedFreeCars(int pageNo, int pageSize, String sortField, String sortDirection);
	
//	List<Car> findRentedCarsByMail(String mail);
	
	List<Car> getAllCars();
	
	Page<Car> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
	
	Car findById(Long id);
	
	Car updateCar(Car car);
	
	void returnCar(Long id);
	
	void deleteCar(Long id);

	Page<Car> findRentedCarsByMail(int pageNo, int pageSize, String sortField, String sortDirection, String mail);
	static Page<Car> findAllRentedCars(int pageNo,int pageSize,String sortField,String sortDirection) {
		// TODO Auto-generated method stub
		return null;
	}

	List<Car> findByKeyword(String keyword);
	
}
