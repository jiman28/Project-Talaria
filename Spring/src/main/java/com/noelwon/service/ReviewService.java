package com.noelwon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.noelwon.mongoDb.ChattIng;

@Service
public class ReviewService {

	@Autowired
	private NotificationService notificationService;

	@Transactional
	public Long create(String id, String msg, ChattIng ChattIng) {
		notificationService.send(id, msg, ChattIng);

		return 15L;
	}
}