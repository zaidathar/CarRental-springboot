package com.neo.springboot.SbCarPeSwaar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CarDto {

	Long id;
	
	String brand,model,registNum;
	
	char rented = 'N';
	String rentedBy;
	
}
