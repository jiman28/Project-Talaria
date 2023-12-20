package com.noelwon.mongoDb;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Chat")
public class ChattIng {


	@Id
    private String _id;
	
	private String roomName;
	private List<Integer> userList;
	private ArrayList<MsgDto> chatList = new ArrayList<MsgDto>();
	private int newMsg;

	public ChattIng(ArrayList<MsgDto> dto) {
		this.chatList = dto;
	}	
	
	public ChattIng() {
		this.chatList = null;
	}

	public ChattIng(String _id,String roomName, List<Integer> userList, ArrayList<MsgDto> chatList) {
		this._id = _id;
		this.roomName = roomName;
		this.userList = userList;
		this.chatList = chatList;
	}

	


	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(userList);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChattIng other = (ChattIng) obj;
		return Objects.equals(userList, other.userList);
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public List<Integer> getUserList() {
		return userList;
	}

	public void setUserList(List<Integer> userList) {
		this.userList = userList;
	}

	public ArrayList<MsgDto> getChatList() {
		return chatList;
	}

	public void setChatList(ArrayList<MsgDto> chatList) {
		this.chatList = chatList;
	}

	public int getNewMsg() {
		return newMsg;
	}

	public void setNewMsg(int newMsg) {
		this.newMsg = newMsg;
	}

	@Override
	public String toString() {
		return "ChattIng [_id=" + _id + ", roomName=" + roomName + ", userList=" + userList + ", chatList=" + chatList.toString()
				+ ", newMsg=" + newMsg + "]";
	}

	
	
}
