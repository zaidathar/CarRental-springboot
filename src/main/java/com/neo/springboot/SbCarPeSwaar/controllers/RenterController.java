package com.neo.springboot.SbCarPeSwaar.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.neo.springboot.SbCarPeSwaar.dto.CarDto;
import com.neo.springboot.SbCarPeSwaar.models.Car;
import com.neo.springboot.SbCarPeSwaar.serviceImp.CarsServiceImp;

@Controller
@RequestMapping("/dealer")
public class RenterController {
	
	
	
	@Autowired
	private CarsServiceImp carsService;
	
	@GetMapping("/rentedCars")
	public String getRentedCars(Model model) {

		return findPaginatedRentedCars(1, "id", "asc", model);
	}
	
	@GetMapping("/rentedCars/{pageNo}")
	public String findPaginatedRentedCars(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		
		
		int pageSize = 3;
		Page<Car> page = carsService.findAllRentedCars(pageNo, pageSize, sortField, sortDir);
		List<Car> cars = page.getContent();
		
		addPaginationAttributes(model, pageNo, page, sortField, sortDir);
		addDealerRole(model);
		model.addAttribute("cars", cars);
		
		return "rentedCars";
	}
	
	@ModelAttribute("car")
    public CarDto carAdditionDto() {
        return new CarDto();
    }

	@GetMapping
	public String getHomePage(Model model) {
		model.addAttribute("authority", "ROLE_DEALER");
		return "home";
	}
	
	@GetMapping("/viewCars")
	public String getManageCars(Model model) {
		

		return findPaginatedManageCars(1, "id", "asc", model);
	}
	
	@GetMapping("/viewCars/{pageNo}")
	public String findPaginatedManageCars(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		
		
		int pageSize = 3;
		Page<Car> page = carsService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Car> cars = page.getContent();
		
		addPaginationAttributes(model, pageNo, page, sortField, sortDir);
		addDealerRole(model);
		model.addAttribute("cars", cars);
		
		return "viewCars";
	}
	
	@GetMapping("/addCar")
	public String getAddCar(Model model) {
		addDealerRole(model);
		
		return "addCar";
	}
	
	@PostMapping("/addCar")
	public String addNewCar(
			@ModelAttribute("car") CarDto carDto
	) {
		Car car = new Car();
		
		car.setModel(carDto.getModel());
		car.setBrand(carDto.getBrand());
		car.setRegistNum(carDto.getRegistNum());

		carsService.save(car);
		
		return "redirect:/dealer/viewCars";
	}
	
	@GetMapping("/editCar/{carID}")
	public String getEditCar(
			@PathVariable("carID") Long id,
			Model model
	) {
		Car myCar = carsService.findById(id);
		
		addDealerRole(model);
		model.addAttribute("carObj", myCar);
		
		return "editCar";
	}

	
	@PostMapping("/saveEditCar/{carID}")
	public String postAddCar(
			@ModelAttribute("car") CarDto carDto,
			@PathVariable("carID") Long id,
			Model model
	) {
		System.out.println("Saving Car Details");
		Car car = new Car();
		
		car.setId(id);
		car.setModel(carDto.getModel());
		car.setBrand(carDto.getBrand());
		car.setRegistNum(carDto.getRegistNum());
		
		
		carsService.updateCar(car);
		
		return "redirect:/dealer/viewCars"; 
	}
	
	@PostMapping("/deleteCar/{carID}")
	public String deleteCarById(@PathVariable("carID") Long id) {
		carsService.deleteCar(id);
		
		return "redirect:/dealer/viewCars";
	}
	
	private void addDealerRole(Model model) {
		model.addAttribute("authority", "ROLE_DEALER");
	}
	
	private void addPaginationAttributes(
			Model model, 
			int pageNo,
			Page<Car> page,
			String sortField,
			String sortDir
	) {
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
	}
	
}
