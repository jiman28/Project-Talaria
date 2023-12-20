package com.noelwon.mongoDb;

import java.util.List;

import com.noelwon.model.userDto.User;

public class MsgDto {


	private int userId;
	
	private String userImg;
	
	private String userName;

	private String msg;

	private String date;
	
	private List<Integer> unReadUsers;
	
	public MsgDto() {};
	

	public MsgDto(int userId, String userName, String msg, String date, List<Integer> unReadUsers) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.msg = msg;
		this.date = date;
		this.unReadUsers = unReadUsers;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Integer> getUnReadUsers() {
		return unReadUsers;
	}

	public void setUnReadUsers(List<Integer> unReadUsers) {
		this.unReadUsers = unReadUsers;
	}

	public String getUserImg() {
		return userImg;
	}


	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}


	@Override
	public String toString() {
		return "MsgDto [userId=" + userId + ", userName=" + userName + ", msg=" + msg + ", date=" + date
				+ ", unReadUsers=" + unReadUsers + "]";
	}
	
	
	

}
