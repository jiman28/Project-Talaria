package com.noelwon.model.ccc;

import java.time.LocalDateTime;
import java.util.List;

import com.noelwon.model.userDto.User;

public class BoardAll {

	private Integer articleNo;
	private String content;
	private String title;
	private User user;
	private LocalDateTime writeDate;
	private int views;
	private String type;

	public Integer getArticleNo() {
		return articleNo;
	}

	public void setArticleNo(Integer articleNo) {
		this.articleNo = articleNo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(LocalDateTime writeDate) {
		this.writeDate = writeDate;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BoardAll(Integer articleNo, String content, String title, User user, LocalDateTime writeDate, int views,
			String type) {
		super();
		this.articleNo = articleNo;
		this.content = content;
		this.title = title;
		this.user = user;
		this.writeDate = writeDate;
		this.views = views;
		this.type = type;
	}

	public BoardAll() {
		super();
	}

	@Override
	public String toString() {
		return "BoardAll [articleNo=" + articleNo + ", content=" + content + ", title=" + title + ", user=" + user
				+ ", writeDate=" + writeDate + ", views=" + views + ", type=" + type + "]";
	}

}
