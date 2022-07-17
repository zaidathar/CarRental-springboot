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
import com.neo.springboot.SbCarPeSwaar.dto.UserRUDto;
import com.neo.springboot.SbCarPeSwaar.exception.ResourceNotFoundException;
import com.neo.springboot.SbCarPeSwaar.models.Car;
import com.neo.springboot.SbCarPeSwaar.models.User;
import com.neo.springboot.SbCarPeSwaar.repo.UserRepo;
import com.neo.springboot.SbCarPeSwaar.serviceImp.CarsServiceImp;
import com.neo.springboot.SbCarPeSwaar.serviceImp.UserServiceImpl;
import com.neo.springboot.SbCarPeSwaar.services.CarsService;
import com.neo.springboot.SbCarPeSwaar.util.MyUtility;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private CarsServiceImp carsService;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	UserServiceImpl userServ;
	
	@ModelAttribute("user")
    public UserRUDto userRegistrationDto() {
        return new UserRUDto();
    }
	

	@ModelAttribute("car")
    public CarDto carAdditionDto() {
        return new CarDto();
    }
	
	@GetMapping
	public String getHomePage(Model model) {
		addAdminRole(model);
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
		
		addPaginationAttributes2(model, pageNo, page, sortField, sortDir);
		addAdminRole(model);
		model.addAttribute("cars", cars);
		
		return "viewCars";
	}
	//All the routes handling dealers
	@GetMapping("/editCar/{carID}")
	public String getEditCar(
			@PathVariable("carID") Long id,
			Model model
	) {
		Car prevCar = carsService.findById(id);
		
		addAdminRole(model);
		model.addAttribute("carObj", prevCar);
		
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
		
		return "redirect:/admin/viewCars"; 
	}
	@GetMapping("/viewDealers")
	public String getManageDealers(Model model) {
		return findPaginatedManageDealers(1, "id", "asc", model);
	}

	@GetMapping("/editDealer/{dealerID}")
	public String getEditDealerPage(
			Model model, 
			@PathVariable("dealerID") Long id
	) {
		System.out.println("Controller in admin");
		addAdminRole(model);
		
		User dealer = userRepo.getById(id);
		model.addAttribute("dealerObj", dealer);
		
		return "editDealer";
	}
	
	@PostMapping("/saveEditDealer/{dealerID}")
	public String saveEditedDealer(
			@ModelAttribute("user") UserRUDto updationDto,
			@PathVariable("dealerID") Long id
	) {
		User existingUser = userRepo.getById(id);
		
		if (existingUser == null)
			throw new ResourceNotFoundException("User", "UserID", id);
		
		existingUser.setEmail(updationDto.getEmail());
		existingUser.setFirstName(updationDto.getFirstName());
		existingUser.setLastName(updationDto.getLastName());
		
		userRepo.save(existingUser);
		
		return "redirect:/admin/viewDealers";
	}
	@PostMapping("/deleteCar/{carID}")
	public String deleteCarById(@PathVariable("carID") Long id) {
		carsService.deleteCar(id);
		
		return "redirect:/admin/viewCars";
	}
	@PostMapping("/removeDealers/{id}")
	public String removeDealer(@PathVariable("id") Long id) {
		
		if (id == null)
			throw new RuntimeException("User ID should NOT be null");
		
		userRepo.findById(id).orElseThrow(() -> 
		new ResourceNotFoundException("User", "UserID", id));
		
		userRepo.deleteById(id);
		
		return "redirect:/admin/viewDealers";
	}
	
	// All the routes handling normal customers
	
	
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
		Page<Car> page = CarsService.findAllRentedCars(pageNo, pageSize, sortField, sortDir);
		List<Car> cars = page.getContent();
		
		addPaginationAttributes2(model, pageNo, page, sortField, sortDir);
		addAdminRole(model);
		model.addAttribute("cars", cars);
		
		return "rentedCars";
	}
	

	@PostMapping("/removeCustomer/{id}")
	public String removeCustomer(@PathVariable("id") Long id) {
		
		if (id == null)
			throw new RuntimeException("User ID should NOT be null");
		
		userRepo.findById(id).orElseThrow(() -> 
		new ResourceNotFoundException("User", "UserID", id));
		
		userRepo.deleteById(id);
		
		return "redirect:/admin/manageCustomers";
	}
	
	@GetMapping("/viewDealers/{pageNo}")
	public String findPaginatedManageDealers(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		
		
		int pageSize = 3;
		
		Page<User> page = userServ.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<User> users = page.getContent();
		
		users = MyUtility.getUserListByRole("ROLE_DEALER", users);
		
		addPaginationAttributes(model, pageNo, page, sortField, sortDir);
		addAdminRole(model);
		model.addAttribute("dealersList", users);
		
		return "manageDealers";
	}
	
	
	
	private void addAdminRole(Model model) {
		model.addAttribute("authority", "ROLE_ADMIN");
	}
	
	private void addPaginationAttributes(
			Model model, 
			int pageNo,
			Page<User> page,
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
	
	private void addPaginationAttributes2(Model model, int pageNo, Page<Car> page, String sortField, String sortDir) {
		// TODO Auto-generated method stub
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
	}
	@GetMapping("/manageCustomers")
	public String getManageCustomers(Model model) {
		

		return findPaginatedManageCustomers(1, "id", "asc", model);
	}
	@GetMapping("/manageCustomers/{pageNo}")
	public String findPaginatedManageCustomers(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		
		
		int pageSize = 3;
		Page<User> page = userServ.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<User> users = page.getContent();
		users = MyUtility.getUserListByRole("ROLE_CUSTOMER", users);
		
		addPaginationAttributes(model, pageNo, page, sortField, sortDir);
		addAdminRole(model);
		model.addAttribute("customersList", users);
		
		return "manageCustomers";
	}
	
}
