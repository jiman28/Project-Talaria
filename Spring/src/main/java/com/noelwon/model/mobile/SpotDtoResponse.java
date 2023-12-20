package com.noelwon.model.mobile;

import java.util.List;

import com.noelwon.model.spot.SpotDto;

public class SpotDtoResponse {
	private String date;
	private List<SpotDto> list;
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<SpotDto> getList() {
		return list;
	}
	public void setList(List<SpotDto> list) {
		this.list = list;
	}
	
	
	
}
