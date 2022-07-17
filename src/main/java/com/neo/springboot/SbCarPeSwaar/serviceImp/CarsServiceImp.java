package com.neo.springboot.SbCarPeSwaar.serviceImp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.neo.springboot.SbCarPeSwaar.exception.ResourceNotFoundException;
import com.neo.springboot.SbCarPeSwaar.models.Car;
import com.neo.springboot.SbCarPeSwaar.repo.CarsRepo;
import com.neo.springboot.SbCarPeSwaar.services.CarsService;

@Service
public class CarsServiceImp implements CarsService {

	@Autowired
	CarsRepo carsRepo;

	@Override
	public Car save(Car car) {
		return carsRepo.save(car);
	}
	
	
	@Override
	public List<Car> findByKeyword(String keyword) {
		return this.carsRepo.findByKeyword(keyword);
	}

	@Override
	public char findIfCarRented(Long id) {
		return carsRepo.findIfCarRented(id);
	}

	@Override
	public List<Car> getAllCars() {
		return carsRepo.findAll();
	}

	@Override
	public Car findById(Long id) {
		Car car = carsRepo.findById(id).orElseThrow(() -> 
							new ResourceNotFoundException("Car", "CarID", id));
		return car;
	}

	@Override
	public Car updateCar(Car car) {
		Car previousCar = carsRepo.findById(car.getId()).orElseThrow(() -> 
							new ResourceNotFoundException("Car", "CarID", car.getId()));
		
		previousCar.setRented(car.getRented());
		previousCar.setRentedBy(car.getRentedBy());
		
		previousCar.setRegistNum(car.getRegistNum());
		previousCar.setModel(car.getModel());
		previousCar.setBrand(car.getBrand());
		
		return carsRepo.save(previousCar);
	}

	@Override
	public void deleteCar(Long id) {
		carsRepo.findById(id).orElseThrow(() -> 
							new ResourceNotFoundException("Car", "CarID", id));
		
		carsRepo.deleteById(id);
	}

	@Override
	public void rentCarByEmail(Long carId, String custMail) {
		
		Car car = carsRepo.findById(carId).orElseThrow(
					() -> new ResourceNotFoundException("Car", "CarID", carId)
		);
		
		car.setRented('Y');
		car.setRentedBy(custMail);
		
		carsRepo.save(car);
		
	}



	@Override
	public void returnCar(Long id) {
		Car car = carsRepo.findById(id).orElseThrow(
					() -> new ResourceNotFoundException("Car", "CarID", id)
				);
		
		car.setRented('N');
		car.setRentedBy(null);
		
		carsRepo.save(car);
	}

	@Override
	public Page<Car> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return carsRepo.findAll(pageable);
	}

	@Override
	public Page<Car> findPaginatedFreeCars(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return carsRepo.findFreeCarsPaginated(pageable);
	}
	
	@Override
	public Page<Car> findRentedCarsByMail(
			int pageNo, int pageSize, String sortField, String sortDirection,
			String mail
	) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return carsRepo.findByRentedBy(mail, pageable);
	}
	
	public Page<Car> findAllRentedCars(
			int pageNo, int pageSize, String sortField, String sortDirection
	) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		System.out.println("FindAllRentedCar"+carsRepo.findAll(pageable));
		return carsRepo.findAllRentedCar(pageable);
	}
	
}
