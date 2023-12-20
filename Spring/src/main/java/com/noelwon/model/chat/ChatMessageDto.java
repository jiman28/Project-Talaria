package com.noelwon.model.chat;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageDto {
//	 메시지 타입 : 입장, 채팅
//	public enum MessageType {
//		ENTER, TALK
//	}

	private String messageType; // 메시지 타입
	private String roomId; // 방 번호
	private String userName; // 유저 이름
	private String picture; // 유저 사진
	private String roomName;
	private Long userId; // 채팅을 보낸 사람
	private String messa; // 메세지

	public String getMessageType() {
		return messageType;
	}

	public ChatMessageDto(String messageType, String roomId, String userName, String picture, String roomName,
			Long userId, String messa) {
		super();
		this.messageType = messageType;
		this.roomId = roomId;
		this.userName = userName;
		this.picture = picture;
		this.roomName = roomName;
		this.userId = userId;
		this.messa = messa;
	}

	@Override
	public String toString() {
		return "ChatMessageDto [messageType=" + messageType + ", roomId=" + roomId + ", userName=" + userName
				+ ", roomName=" + roomName + ", userId=" + userId + ", messa=" + messa + "]";
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getMessa() {
		return messa;
	}

	public void setMessa(String messa) {
		this.messa = messa;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public static ChatMessageDto.BuildMeBuilder builder() {
		return new ChatMessageDto.BuildMeBuilder();
	}

	public static class BuildMeBuilder {
		private String messageType; // 메시지 타입
		private String roomId; // 방 번호
		private Long userId; // 채팅을 보낸 사람
		private String picture; // 유저 사진
		private String messa;
		private String roomName;
		private String userName; // 유저 이름

		BuildMeBuilder() {
		}

		public ChatMessageDto.BuildMeBuilder messageType(String messageType) {
			this.messageType = messageType;
			return this;
		}

		public ChatMessageDto.BuildMeBuilder RoomId(String roomId) {
			this.roomId = roomId;
			return this;
		}

		public ChatMessageDto.BuildMeBuilder userId(Long userId) {
			this.userId = userId;
			return this;
		}

		public ChatMessageDto.BuildMeBuilder messa(String messa) {
			this.messa = messa;
			return this;
		}

		public ChatMessageDto.BuildMeBuilder roomName(String roomName) {
			this.roomName = roomName;
			return this;
		}

		public ChatMessageDto.BuildMeBuilder userName(String userName) {
			this.userName = userName;
			return this;
		}
		
		public ChatMessageDto.BuildMeBuilder picture(String picture) {
			this.picture = picture;
			return this;
		}

		public ChatMessageDto build() {
			return new ChatMessageDto(this.messageType, this.roomId, this.userName,this.picture ,this.roomName, this.userId,
					this.messa);
		}

	}

	public ChatMessageDto() {
		super();
	}

}