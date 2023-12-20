package com.noelwon.model.spot;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Component
@Entity
public class EntityInterestDto {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer InterestId;
	
	@Column(length = 100)
	private String InterestType;
	
	@Column(length = 500)
	private String interestImg;
	
//	@OneToMany(mappedBy = "EntityInterestDto", cascade = CascadeType.REMOVE)
//	private List<EntityTourAttractionDto> entityInterestDtoList;
	

	public Integer getInterestId() {
		return InterestId;
	}

	public void setInterestId(Integer interestId) {
		InterestId = interestId;
	}

//	public List<EntityTourAttractionDto> getEntityInterestDtoList() {
//		return entityInterestDtoList;
//	}

//	public void setEntityInterestDtoList(List<EntityTourAttractionDto> entityInterestDtoList) {
//		this.entityInterestDtoList = entityInterestDtoList;
//	}

	public String getInterestType() {
		return InterestType;
	}

	public void setInterestType(String interestType) {
		InterestType = interestType;
	}

	public String getInterestImg() {
		return interestImg;
	}

	public void setInterestImg(String interestImg) {
		this.interestImg = interestImg;
	}
	
	
}
