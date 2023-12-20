package com.noelwon.service;

import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.noelwon.model.chat.EmitterRepository;
import com.noelwon.model.chat.Notification;
import com.noelwon.model.userDto.User;
import com.noelwon.mongo.MongoDbService;
import com.noelwon.mongoDb.ChattIng;
import com.noelwon.service.totalService.TotalRepositoryService;

import net.minidev.json.JSONObject;

@Service
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private TotalRepositoryService totalRepositoryService;
    
    @Autowired
    private MongoDbService mongoDbService;
    
    Logger logger = LoggerFactory.getLogger("service.NotificationService");

    @Autowired
    public NotificationService(EmitterRepository emitterRepository) {
        this.emitterRepository = emitterRepository;
    }

    public SseEmitter subscribe(Long userId, String lastEventId) {  // 사용자 sse 연결 메소드
        // 1
//        String id = userId + "_" + System.currentTimeMillis();
    	String id = userId + "";
        // 2
        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

	// 3
        // 503 에러를 방지하기 위한 더미 이벤트 전송
        JSONObject jo = new JSONObject();
        jo.put("msg", "EventStream Created. [userId=" + userId + "] 접속 완료");
        jo.put("newMsg", false);
        jo.put("title", "새 메세지입니다");
        sendToClient(emitter, id, jo);

	// 4
//         클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        
        if (!lastEventId.isEmpty()) {
            Map<String, SseEmitter> events = emitterRepository.findAllEventCacheStartWithId(String.valueOf(userId));
            logger.info("if문 실행 events.size()====>"+events.size());
            events.entrySet().stream()
                  .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                  .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }

        return emitter;
    }
    
    
    
    
    public void send(String userId, String content, ChattIng ChattIng) {
        Notification notification = createNotification(userId, content, ChattIng);
        String id = userId;
        
        // 로그인 한 유저의 SseEmitter 모두 가져오기
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(id);
        logger.info("sseEmitters====>"+sseEmitters.toString());
        sseEmitters.forEach(
                (key, emitter) -> {
                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                    emitterRepository.saveEventCache(key, notification);
                    // 데이터 전송
                    sendToClient(emitter, key, notification);
                }
        );
    }

    

    // 3
    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event().id(id).name("sse").data(data));
        } catch (Exception exception) {
            emitterRepository.deleteById(id);
            throw new RuntimeException("연결 오류!");
        }
    }
    
    private Notification createNotification(String userId, String content, ChattIng ChattIng) {
    	Notification notification = new Notification();
    	User user = totalRepositoryService.findById(Integer.parseInt(userId), Arrays.asList(totalRepositoryService.getUserRepository()),User.class);
    	notification.setMsg(content);
    	notification.setTitle(ChattIng.getRoomName());
    	notification.setSentUser(Integer.parseInt(userId));
    	notification.setUrl("/myMentoring");
    	notification.setNewMsg(true);
    	notification.setSentUserName(user.getName());
    	notification.setRoomId(ChattIng.get_id());
//    	notification.setUnReadMsgSize();
    	
    	return notification;
    }
}