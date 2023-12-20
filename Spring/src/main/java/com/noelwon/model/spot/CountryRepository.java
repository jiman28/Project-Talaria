package com.noelwon.model.spot;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<EntityCountryDto, Integer>{
	Optional<EntityCountryDto> findBycountryName (String countryName);
	
}
