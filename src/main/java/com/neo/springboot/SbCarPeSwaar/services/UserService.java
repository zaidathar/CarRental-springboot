package com.neo.springboot.SbCarPeSwaar.services;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.neo.springboot.SbCarPeSwaar.dto.UserRUDto;
import com.neo.springboot.SbCarPeSwaar.models.User;

public interface UserService extends UserDetailsService {
	User save(UserRUDto registrationDto);

	Page<User> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}
