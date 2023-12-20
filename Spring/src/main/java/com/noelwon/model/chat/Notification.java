package com.noelwon.model.chat;

import java.util.List;

public class Notification {

	private int sentUser;

	private String sentUserName;

	private List<Integer> rReceivingUser;

	private String title;

	private String msg;

	private String url;

	private boolean newMsg;
	
	private String roomId;
	
	private	int unReadMsgSize;
	
	public int getUnReadMsgSize() {
		return unReadMsgSize;
	}

	public void setUnReadMsgSize(int unReadMsgSize) {
		this.unReadMsgSize = unReadMsgSize;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getSentUserName() {
		return sentUserName;
	}

	public void setSentUserName(String sentUserName) {
		this.sentUserName = sentUserName;
	}

	public boolean isNewMsg() {
		return newMsg;
	}

	public void setNewMsg(boolean newMsg) {
		this.newMsg = newMsg;
	}

	public int getSentUser() {
		return sentUser;
	}

	public void setSentUser(int sentUser) {
		this.sentUser = sentUser;
	}

	public List<Integer> getrReceivingUser() {
		return rReceivingUser;
	}

	public void setrReceivingUser(List<Integer> rReceivingUser) {
		this.rReceivingUser = rReceivingUser;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
