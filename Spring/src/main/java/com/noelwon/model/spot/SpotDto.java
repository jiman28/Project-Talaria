package com.noelwon.model.spot;

public class SpotDto {

	private String pk;

	private String name;
	
	private String img;
	
	private String lan;
	
	private String lat;
	
	private int inOut;
	
	private int cityId;
	
	
	public SpotDto() {}

	public SpotDto(String pk, String name, String img, String lan, String lat, int inOut, int cityId) {
		this.pk = pk;
		this.name = name;
		this.img = img;
		this.lan = lan;
		this.lat = lat;
		this.inOut = inOut;
		this.cityId = cityId;
	}

	public String getPk() {
		return pk;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getLan() {
		return lan;
	}

	public void setLan(String lan) {
		this.lan = lan;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public int getInOut() {
		return inOut;
	}

	public void setInOut(int inOut) {
		this.inOut = inOut;
	}
	
	
	
	
}
