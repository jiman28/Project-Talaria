package com.noelwon.model.chat;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class EmitterRepository {
	// 모든 Emitters를 저장하는 ConcurrentHashMap
	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
	private final Map<String, Notification> notificationMap = new ConcurrentHashMap<>();
	 private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
	
	Logger logger = LoggerFactory.getLogger("chat.EmitterRepository");
	/**
	 * 주어진 아이디와 이미터를 저장
	 *
	 * @param id      - 사용자 아이디.
	 * @param emitter - 이벤트 Emitter.
	 */
	public SseEmitter save(String id, SseEmitter emitter) {
		logger.info("save emitters.size():===>"+emitters.size());
		emitters.put(id, emitter);
		return emitters.get(id);
	}

	/**
	 * 주어진 아이디의 Emitter를 제거
	 *
	 * @param id - 사용자 아이디.
	 */
	public void deleteById(String id) {
		logger.info("deleteById emitters.size():===>"+emitters.size());
		emitters.remove(id);
	}

	/**
	 * 주어진 아이디의 Emitter를 가져옴.
	 *
	 * @param id - 사용자 아이디.
	 * @return SseEmitter - 이벤트 Emitter.
	 */
	public SseEmitter get(String id) {
		logger.info("get emitters.size():===>"+emitters.size());
		return emitters.get(id);
	}

	public Map<String, SseEmitter> findAllStartWithById(String id) {
		logger.info("findAllStartWithById emitters.size():===>"+emitters.size());
		Map<String, SseEmitter> map = new LinkedHashMap<>();
		if(emitters.containsKey(id)) {
			map.put(id, emitters.get(id));
		}else {
			emitters.put(id, new SseEmitter(DEFAULT_TIMEOUT));
			map.put(id, emitters.get(id));
		}
		

		return map;
	}

	public void saveEventCache(String key, Notification notification) {
		logger.info("saveEventCache emitters.size():===>"+emitters.size());
		
		logger.info("saveEventCache 실행 ID:===>"+key);
		notificationMap.put(key, notification);
	}
	
	public Map<String, SseEmitter> findAllEventCacheStartWithId(String id) {
		Map<String, SseEmitter> map = new LinkedHashMap<>();
		map.put(id, emitters.get(id));
		
		return map;
	}
}