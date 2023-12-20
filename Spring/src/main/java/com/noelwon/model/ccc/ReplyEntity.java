package com.noelwon.model.ccc;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import com.noelwon.model.userDto.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
@Component
public class ReplyEntity {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer replyNo;
		
//		@Column
//		private int articleNo;
		
		@Column(columnDefinition = "TEXT")
		private String replycontent;

		@Column(length = 30)
		private String writeId;
		

		
		
		@CreationTimestamp
		@Column(name = "reply_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
		private LocalDateTime writeDate;
		
		@ManyToOne
		private User user;
		
		
		@ManyToOne
		private BoardEntity boardEntity;
		
		@ManyToOne
		private CompanyEntity companyEntity;
		
		@ManyToOne
		private TradeEntity tradeEntity;


		public Integer getReplyNo() {
			return replyNo;
		}


		public TradeEntity getTradeEntity() {
			return tradeEntity;
		}


		public void setTradeEntity(TradeEntity tradeEntity) {
			this.tradeEntity = tradeEntity;
		}


		public void setReplyNo(Integer replyNo) {
			this.replyNo = replyNo;
		}


		public String getReplycontent() {
			return replycontent;
		}


		public void setReplycontent(String replycontent) {
			this.replycontent = replycontent;
		}


		public String getWriteId() {
			return writeId;
		}


		public void setWriteId(String writeId) {
			this.writeId = writeId;
		}


		public CompanyEntity getCompanyEntity() {
			return companyEntity;
		}


		public void setCompanyEntity(CompanyEntity companyEntity) {
			this.companyEntity = companyEntity;
		}


		public LocalDateTime getWriteDate() {
			return writeDate;
		}


		public void setWriteDate(LocalDateTime writeDate) {
			this.writeDate = writeDate;
		}


		public BoardEntity getBoardEntity() {
			return boardEntity;
		}


		public void setBoardEntity(BoardEntity boardEntity) {
			this.boardEntity = boardEntity;
		}


		public User getUser() {
			return user;
		}


		public void setUser(User user) {
			this.user = user;
		}
		
		
		
		
}
