package com.neo.springboot.SbCarPeSwaar.serviceImp;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.neo.springboot.SbCarPeSwaar.dto.UserRUDto;
import com.neo.springboot.SbCarPeSwaar.models.Role;
import com.neo.springboot.SbCarPeSwaar.models.User;
import com.neo.springboot.SbCarPeSwaar.repo.UserRepo;
import com.neo.springboot.SbCarPeSwaar.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepo userRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public User save(UserRUDto registrationDto) {
		
		String USER_MAIL = registrationDto.getEmail();
		String mailDomain = USER_MAIL.split("@")[1];
		
		String ROLE = "ROLE_CUSTOMER";
		
		if (USER_MAIL.equals("admin@admin.com"))
			ROLE = "ROLE_ADMIN";
		else if (mailDomain.equals("cps.com"))
			ROLE = "ROLE_DEALER";
		
		
		Set<Role> roles = new HashSet<>();
		roles.add(new Role(ROLE));
			
		User user = new User();
		
		user.setFirstName(registrationDto.getFirstName());
		user.setLastName(registrationDto.getLastName());
		user.setEmail(USER_MAIL);
		user.setPassword(passwordEncoder.encode(registrationDto.getPassword()).toString());
		user.setRoles(roles);
		
		return userRepo.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> user = userRepo.findByEmail(username);
		if (!user.isPresent()) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(),
				mapRolesToAuthorities(user.get().getRoles()));
	}

	private Set<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
	}

	@Override
	public Page<User> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return userRepo.findAll(pageable);
	}

}
