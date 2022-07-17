package com.neo.springboot.SbCarPeSwaar.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import com.neo.springboot.SbCarPeSwaar.models.Car;

public interface CarsRepo extends JpaRepository<Car, Long> {
	@Query("SELECT c.rented FROM Car c WHERE c.id = ?1")
	char findIfCarRented(Long id);

	@Query(value = "select * from cars c where c.brand like %:keyword% or c.model like %:keyword% or c.regist_num like %:keyword% "  ,nativeQuery = true)
	List<Car> findByKeyword(@Param("keyword") String keyword);
	
	@Query("SELECT c FROM Car c WHERE c.rented = 'N'")
	Page<Car> findFreeCarsPaginated(Pageable pageable);
	
	Page<Car> findByRentedBy(String mail, Pageable pageable);

	@Query("SELECT c FROM Car c WHERE c.rented = 'Y'")
	Page<Car> findAllRentedCar(Pageable pageable);
}
