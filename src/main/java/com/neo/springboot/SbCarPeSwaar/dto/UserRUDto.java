package com.neo.springboot.SbCarPeSwaar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserRUDto {
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private double mobile;
}
