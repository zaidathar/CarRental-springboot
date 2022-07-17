package com.neo.springboot.SbCarPeSwaar.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neo.springboot.SbCarPeSwaar.models.User;

public interface UserRepo extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}
