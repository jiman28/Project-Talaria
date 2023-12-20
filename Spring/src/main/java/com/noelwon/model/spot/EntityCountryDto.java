package com.noelwon.model.spot;

import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Component
@Entity
public class EntityCountryDto {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer countryId;
	
	@Column(length = 100)
	private String countryName;
	
	@Column(length = 5000)
	private String countryInfo;
	
	@Column(length = 100)
	private String timeD;
	
	@Column(length = 100)
	private String languageC;
	
	@Column(length = 100)
	private String currency;
	
	@Column(length = 5000)
	private String imageC;
	
	
	@OneToMany(mappedBy = "entityCountryDto", cascade = CascadeType.REMOVE)
	private List<EntityCityDto> entityCountryDtoList;
	
	
	
	

	public List<EntityCityDto> getEntityCountryDtoList() {
		return entityCountryDtoList;
	}

	public void setEntityCountryDtoList(List<EntityCityDto> entityCountryDtoList) {
		this.entityCountryDtoList = entityCountryDtoList;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCountryInfo() {
		return countryInfo;
	}

	public void setCountryInfo(String countryInfo) {
		this.countryInfo = countryInfo;
	}

	public String getTimeD() {
		return timeD;
	}

	public void setTimeD(String timeD) {
		this.timeD = timeD;
	}

	public String getLanguageC() {
		return languageC;
	}

	public void setLanguageC(String languageC) {
		this.languageC = languageC;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getImageC() {
		return imageC;
	}

	public void setImageC(String imageC) {
		this.imageC = imageC;
	}

	
	
}
