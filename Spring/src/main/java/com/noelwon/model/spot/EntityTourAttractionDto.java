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
public class EntityTourAttractionDto {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer placeId;
	
	@ManyToOne
	@JoinColumn(name="cityId")
	private EntityCityDto entityCityDto;
	
	@ManyToOne
	@JoinColumn(name="InterestId")
	private EntityInterestDto entityInterestDto;
	
	@Column(length = 100)
	private String placeName;
	
	@Column(length = 100)
	private String placeType;
	
	@Column(length = 100)
	private String price;
	
	@Column(length = 6000)
	private String imageP;
	
	@Column(length = 100)
	private int inOut;
	
	@Column(length = 5000)
	private String placeAddress;
	
	
	
	@Column(length = 100)
	private Float lat;
	
	@Column(length = 100)
	private Float lan;
	
	


	public Float getLat() {
		return lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	public Float getLan() {
		return lan;
	}

	public void setLan(Float lan) {
		this.lan = lan;
	}

	public String getPlaceAddress() {
		return placeAddress;
	}

	public void setPlaceAddress(String placeAddress) {
		this.placeAddress = placeAddress;
	}

	public Integer getPlaceId() {
		return placeId;
	}

	public void setPlaceId(Integer placeId) {
		this.placeId = placeId;
	}

	public EntityCityDto getEntityCityDto() {
		return entityCityDto;
	}

	public void setEntityCityDto(EntityCityDto entityCityDto) {
		this.entityCityDto = entityCityDto;
	}

	public EntityInterestDto getEntityInterestDto() {
		return entityInterestDto;
	}

	public void setEntityInterestDto(EntityInterestDto entityInterestDto) {
		this.entityInterestDto = entityInterestDto;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getPlaceType() {
		return placeType;
	}

	public void setPlaceType(String placeType) {
		this.placeType = placeType;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getImageP() {
		return imageP;
	}

	public void setImageP(String imageP) {
		this.imageP = imageP;
	}

	public int getInOut() {
		return inOut;
	}

	public void setInOut(int inOut) {
		this.inOut = inOut;
	}
	
	
	
}
