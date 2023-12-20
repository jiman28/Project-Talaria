package com.noelwon.model.spot;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Component
@Entity
public class EntityCityDto {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cityId;
	
	@Column(length = 100)
	private String cityName;
	


	@ManyToOne
	@JoinColumn(name="countryId")
	private EntityCountryDto entityCountryDto;
	
//	@OneToMany(mappedBy = "entityCityDto", cascade = CascadeType.REMOVE)
//	private List<EntityTourAttractionDto> entityCityDtoList;
	
	
	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public EntityCountryDto getEntityCountryDto() {
		return entityCountryDto;
	}

	public void setEntityCountryDto(EntityCountryDto entityCountryDto) {
		this.entityCountryDto = entityCountryDto;
	}

}
