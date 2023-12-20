package com.noelwon.model.chat;

import java.util.List;

import com.noelwon.model.userDto.User;
import com.noelwon.mongoDb.ChattIng;
import com.noelwon.mongoDb.MsgDto;

public class PostChat {
	private User user;
	private String loginNick;
	private ChattIng chattIng;
	private List<MsgDto> ChatList;

	public PostChat() {
	};

	public PostChat(User user, String loginNick, ChattIng chattIng, List<MsgDto> chatList) {
		super();
		this.user = user;
		this.loginNick = loginNick;
		this.chattIng = chattIng;
		ChatList = chatList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getLoginNick() {
		return loginNick;
	}

	public void setLoginNick(String loginNick) {
		this.loginNick = loginNick;
	}

	public ChattIng getChattIng() {
		return chattIng;
	}

	public void setChattIng(ChattIng chattIng) {
		this.chattIng = chattIng;
	}

	public List<MsgDto> getChatList() {
		return ChatList;
	}

	public void setChatList(List<MsgDto> chatList) {
		ChatList = chatList;
	}

}
