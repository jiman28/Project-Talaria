package com.noelwon.model.spot;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<EntityCityDto, Integer>{
	Optional<EntityCityDto> findBycityId (int cityId);
}
