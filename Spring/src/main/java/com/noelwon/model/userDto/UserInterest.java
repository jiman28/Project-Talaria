package com.noelwon.model.userDto;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
@Component
@Entity
@Table(name = "entity_user_interest_dto")
public class UserInterest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	private User user;

	@Column(nullable = true)
	private Integer reliability;

	@Column(nullable = true)
	private Integer history;

	@Column(nullable = true)
	private Integer sights;

	@Column(nullable = true)
	private Integer culture;

	@Column(nullable = true)
	private Integer food;

	@Column(nullable = true)
	private Integer nature;

	@Column(nullable = true)
	private Integer religion;

	public UserInterest() {
	};

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getReliability() {
		return reliability;
	}

	public void setReliability(Integer reliability) {
		this.reliability = reliability;
	}

	public Integer getHistory() {
		return history;
	}

	public void setHistory(Integer history) {
		this.history = history;
	}

	public Integer getSights() {
		return sights;
	}

	public void setSights(Integer sights) {
		this.sights = sights;
	}

	public Integer getCulture() {
		return culture;
	}

	public void setCulture(Integer culture) {
		this.culture = culture;
	}

	public Integer getFood() {
		return food;
	}

	public void setFood(Integer food) {
		this.food = food;
	}

	public Integer getNature() {
		return nature;
	}

	public void setNature(Integer nature) {
		this.nature = nature;
	}

	public Integer getReligion() {
		return religion;
	}

	public void setReligion(Integer religion) {
		this.religion = religion;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "UserInterest [id=" + id + ", user=" + user + ", reliability=" + reliability + ", history=" + history
				+ ", sights=" + sights + ", culture=" + culture + ", food=" + food + ", nature=" + nature
				+ ", religion=" + religion + "]";
	}
	
	
	
	

}
