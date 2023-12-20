package com.noelwon.model.userDto;

import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.noelwon.model.ccc.BoardEntity;
import com.noelwon.model.ccc.CompanyEntity;
import com.noelwon.model.ccc.ReplyEntity;
import com.noelwon.model.ccc.TradeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Component
@Entity
@Table(name = "entity_user_dto")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String picture= "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR82ZL5TC9-Q-wVfoEdIBFIMVbZiQf0jqD2VFi-Gy4Q5g&s";
	
	@Column(nullable = false)
	private String role = "ROLE_USER";

	@Column(nullable = false)
	private String type;

	@Column(nullable = true)
	private String password;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
	private UserInterest userInterest;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<BoardEntity> boardEntityList;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<CompanyEntity> companyEntityList;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<TradeEntity> tradeEntityList;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<ReplyEntity> replyEntityList;

	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserInterest getUserInterest() {
		return userInterest;
	}

	public void setUserInterest(UserInterest userInterest) {
		this.userInterest = userInterest;
	}

	public List<BoardEntity> getBoardEntityList() {
		return boardEntityList;
	}

	public void setBoardEntityList(List<BoardEntity> boardEntityList) {
		this.boardEntityList = boardEntityList;
	}

	public List<CompanyEntity> getCompanyEntityList() {
		return companyEntityList;
	}

	public void setCompanyEntityList(List<CompanyEntity> companyEntityList) {
		this.companyEntityList = companyEntityList;
	}

	public List<TradeEntity> getTradeEntityList() {
		return tradeEntityList;
	}

	public void setTradeEntityList(List<TradeEntity> tradeEntityList) {
		this.tradeEntityList = tradeEntityList;
	}

	public List<ReplyEntity> getReplyEntityList() {
		return replyEntityList;
	}

	public void setReplyEntityList(List<ReplyEntity> replyEntityList) {
		this.replyEntityList = replyEntityList;
	}

	public User(Integer id, String name, String email, String picture, String role, String type, String password,
			UserInterest userInterest, List<BoardEntity> boardEntityList, List<CompanyEntity> companyEntityList,
			List<TradeEntity> tradeEntityList, List<ReplyEntity> replyEntityList) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.picture = picture;
		this.role = role;
		this.type = type;
		this.password = password;
		this.userInterest = userInterest;
		this.boardEntityList = boardEntityList;
		this.companyEntityList = companyEntityList;
		this.tradeEntityList = tradeEntityList;
		this.replyEntityList = replyEntityList;
	}
	
	
	
	
	public User(String name, String email, String picture) {
		super();
		this.name = name;
		this.email = email;
		this.picture = picture;
	}

	public User() {
		super();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", picture=" + picture + ", role=" + role
				+ ", type=" + type + ", password=" + password + "]";
	}
	


	
	
}