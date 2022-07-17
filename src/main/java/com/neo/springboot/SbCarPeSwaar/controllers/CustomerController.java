package com.neo.springboot.SbCarPeSwaar.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.neo.springboot.SbCarPeSwaar.models.Car;
import com.neo.springboot.SbCarPeSwaar.serviceImp.CarsServiceImp;

@Controller
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	private CarsServiceImp carsServ;

	@GetMapping
	public String getHomePage(Model model) {
		addCustomerRole(model);
		return "home";
	}
	
	@GetMapping("/availableCars")
	public String getAvailableCars(Model model) {
		
		return findPaginatedAvailableCars(1, "id", "asc", model);
	}
	
	@GetMapping("/rentedCars/{pageNo}")
	public String findPaginatedRentedCars(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		
		
		int pageSize = 3;
		Page<Car> page = carsServ.findRentedCarsByMail(pageNo, pageSize, sortField, sortDir, getUserName());
		List<Car> cars = page.getContent();
		
		addPaginationAttributes(model, pageNo, page, sortField, sortDir);
		addCustomerRole(model);
		model.addAttribute("cars", cars);
		
		return "rentedCars";
	}
	
	@GetMapping("/availableCars/{pageNo}")
	public String findPaginatedAvailableCars(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		
		
		int pageSize = 3;
		Page<Car> page = carsServ.findPaginatedFreeCars(pageNo, pageSize, sortField, sortDir);
		List<Car> cars = page.getContent();
		
		addPaginationAttributes(model, pageNo, page, sortField, sortDir);
		addCustomerRole(model);
		model.addAttribute("cars", cars);
		
		return "availableCars";
	}


	@GetMapping("/rentedCars")
	public String getRentedCars(Model model) {

		return findPaginatedRentedCars(1, "id", "asc", model);
	}
	
	
	
	@PostMapping("/rentCar/{carId}")
	public String postRentCar(@PathVariable("carId") Long id) {
		
		String custMail = getUserName();
		
		carsServ.rentCarByEmail(id, custMail);
		
		return "redirect:/customer/availableCars";
	}
	
	@PostMapping("/returnCar/{carId}")
	public String postReturnCar(@PathVariable("carId") Long id) {
		
		carsServ.returnCar(id);
		
		return "redirect:/customer/rentedCars";
	}
	
	private void addCustomerRole(Model model) {
		model.addAttribute("authority", "ROLE_CUSTOMER");
	}
	
	private String getUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		return auth.getName();
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
