package com.neo.springboot.SbCarPeSwaar.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.neo.springboot.SbCarPeSwaar.models.User;

public class MyUtility {
	
	public static List<User> getUserListByRole(String role, List<User> users) {
		List<User> filteredUsers = new ArrayList<>();
		
		for(User user: users) {
			Set<String> roles = user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet());
			if (roles.contains(role))
				filteredUsers.add(user);
		}
		
		return filteredUsers;
	}
	
}
