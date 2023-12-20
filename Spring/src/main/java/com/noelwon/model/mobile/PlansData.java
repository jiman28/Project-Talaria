package com.noelwon.model.mobile;

import java.util.List;

public class PlansData {
	private String id;
	private String planName;
	private String email;
	private String startDay;
	private String endDay;
	private List<SpotDtoResponse> plans;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStartDay() {
		return startDay;
	}
	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}
	public String getEndDay() {
		return endDay;
	}
	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}
	public List<SpotDtoResponse> getPlans() {
		return plans;
	}
	public void setPlans(List<SpotDtoResponse> plans) {
		this.plans = plans;
	}
	
	
	
}
