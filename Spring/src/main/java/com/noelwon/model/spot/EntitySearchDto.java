package com.noelwon.model.spot;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Component
@Entity
@Table(name = "entity_Searc_dto")
public class EntitySearchDto {

	@Id
	private String name;

	@Column(length = 100)
	private String Address;

	@Column(length = 100, nullable = true)
	private String globalCode;

	@Column(length = 100, nullable = true)
	private String compoundCode;

	@Column(length = 100)
	private String lat;

	@Column(length = 100)
	private String lng;

	@Column(length = 5000, nullable = true)
	private String img;
	
	@Column(length = 100)
	private int inOut;
	
	@Column(length = 100)
	private int cityId;
	
	

	public int getInOut() {
		return inOut;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public void setInOut(int inOut) {
		this.inOut = inOut;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getGlobalCode() {
		return globalCode;
	}

	public void setGlobalCode(String globalCode) {
		this.globalCode = globalCode;
	}

	public String getCompoundCode() {
		return compoundCode;
	}

	public void setCompoundCode(String compoundCode) {
		this.compoundCode = compoundCode;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}


	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

}
