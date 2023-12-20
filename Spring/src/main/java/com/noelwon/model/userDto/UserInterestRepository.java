package com.noelwon.model.userDto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInterestRepository extends JpaRepository<UserInterest, Integer> {
	Optional<UserInterest> findByuser(User user);
}