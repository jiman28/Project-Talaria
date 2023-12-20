package com.noelwon.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.noelwon.Security.SessionUser;
import com.noelwon.model.chat.ChatMessageDto;
import com.noelwon.model.userDto.User;
import com.noelwon.mongo.MongoDbService;
import com.noelwon.mongoDb.ChattIng;
import com.noelwon.mongoDb.ChattRepository;
import com.noelwon.mongoDb.MsgDto;
import com.noelwon.service.NotificationService;
import com.noelwon.service.UserService;
import com.noelwon.service.totalService.TotalRepositoryService;

import jakarta.servlet.http.HttpSession;

@Controller
public class StompChatController {

	Logger log = LoggerFactory.getLogger("controller.StompChatController");

	@Autowired
	private HttpSession session;

	private final SimpMessagingTemplate template; // 특정 Broker로 메세지를 전달
	private final ChatMessageDto chatMessageDto;
	private final ChattRepository chattRepository;
	private final MongoDbService mongoDbService;
	private final UserService userService;

	@Autowired
	private TotalRepositoryService totalRepositoryService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	public StompChatController(SimpMessagingTemplate template, ChatMessageDto chatMessageDto,
			com.noelwon.mongoDb.ChattRepository chattRepository, MongoDbService mongoDbService,
			UserService userService) {
		super();
		this.template = template;
		this.chatMessageDto = chatMessageDto;
		this.chattRepository = chattRepository;
		this.mongoDbService = mongoDbService;
		this.userService = userService;
	}

	// Client 가 SEND 할 수 있는 경로
	// stompConfig 에서 설정한 applicationDestinationPrefixes 와 @MessageMapping 경로가 병합됨
	// "/pub/chat/enter"
	@MessageMapping(value = "/chat/enter")
	public void enter(ChatMessageDto message) throws Exception { // 채팅방 들어갈때 실행(띄워져 있는 알림을 지운다)
		log.info("enter===>" + message.toString());
		ChattIng chattIng = mongoDbService.getRoomOne(message.getRoomId());
		log.info("chattIng===>" + chattIng.toString());
		for (int i = 0; i < chattIng.getChatList().size(); i++) {
			chattIng.getChatList().get(i).getUnReadUsers().remove(Integer.valueOf(message.getUserId().intValue()));

		}

		mongoDbService.msgUpdate(chattIng);
	}

	@MessageMapping(value = "/chat/message")
	public void message(ChatMessageDto message) throws Exception { // 유저가 msg 보낼때 받는곳
		template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);

		log.info("message====>" + message.toString());

//		message.getUserId()
		User user = totalRepositoryService.findById(message.getUserId().intValue(),
				Arrays.asList(totalRepositoryService.getUserRepository()), User.class);

		message.setUserName(user.getName());

//      DB에 채팅내용 저장
		ChattIng ChattIng = mongoDbService.getRoomOne(message.getRoomId());
		mongoDbService.msgSave(message, message.getRoomId());

		for (int i : ChattIng.getUserList()) { // 다른 유저에게 푸쉬 알람 보내기
			log.info("for i====>" + i);
			if (i != message.getUserId().intValue()) {
				log.info("if i====>" + i);
				notificationService.send(i + "", message.getMessa(), ChattIng);
			}
		}

	}

//    @MessageMapping 을 통해 WebSocket 으로 들어오는 메세지 발행을 처리한다.
//    Client 에서는 prefix 를 붙여 "/pub/chat/enter"로 발행 요청을 하면
//    Controller 가 해당 메세지를 받아 처리하는데,
//    메세지가 발행되면 "/sub/chat/room/[roomId]"로 메세지가 전송되는 것을 볼 수 있다.
//    Client 에서는 해당 주소를 SUBSCRIBE 하고 있다가 메세지가 전달되면 화면에 출력한다.
//    이때 /sub/chat/room/[roomId]는 채팅방을 구분하는 값이다.
//    기존의 핸들러 ChatHandler 의 역할을 대신 해주므로 핸들러는 없어도 된다.

	@RequestMapping("/roomRemove")
	public String roomRemove(@RequestParam(value = "roomId") String id) throws Exception {
		mongoDbService.roomRemove(id);

		return "redirect:myMentoring";
	}

	// 나의 멘토링 조회
	@GetMapping("/myMentoring")
	public String myMentoring(Model model) {

		SessionUser userSession = (SessionUser) session.getAttribute("user");

		User user = totalRepositoryService.findByType(userSession.getEmail());

		log.info("myMentoring 실행");
		log.info("session 실행===>" + userSession.getEmail());
		List<ChattIng> myChatList = mongoDbService.getRoomToUserId(user.getId());

		for (ChattIng chat : myChatList) {
			int i = 0;
			for (MsgDto m : chat.getChatList()) {
				if (m.getUnReadUsers().contains(user.getId())) {
					i++;
				}
			}
			chat.setNewMsg(i);
		}

		log.info("myChatList.size()" + myChatList.size());
		model.addAttribute("rooms", myChatList);
		model.addAttribute("loginUser", user);
		model.addAttribute("ffff", 0);

		return "myMentoring";
	}

	// 채팅방 개설
	@PostMapping(value = "/room")
	public String create(@RequestParam(value = "userA") String userA, @RequestParam(value = "roomName") String roomName,
			HttpSession session, Model model) {
		log.info("create 실행");
		log.info("session.getId()" + session.getId());
		SessionUser userSession = (SessionUser) session.getAttribute("user");

		User user = totalRepositoryService.findByType(userSession.getEmail());

		List<Integer> userList = new ArrayList<>();

		userList.add(totalRepositoryService.findByType(userA).getId());
		userList.add(user.getId());

		mongoDbService.CreateChatRoom(userList, roomName);
		return "redirect:/myMentoring";
	}

	// 채팅방 개설
	@PostMapping(value = "/createChatBoard")
	public String createChatBoard(@RequestParam(value = "youEmail") String userA) {
		SessionUser userSession = (SessionUser) session.getAttribute("user");
		User user = totalRepositoryService.findByType(userSession.getEmail());
		User addUser = totalRepositoryService.findByType(userA);

		List<Integer> userList = new ArrayList<>();
		userList.add(addUser.getId());
		userList.add(user.getId());

		String roomName = user.getName() + "님, " + addUser.getName() + "님";

		mongoDbService.CreateChatRoom(userList, roomName);

		return "redirect:/myMentoring";
	}

	// 채팅방 유저 추가
	@PostMapping(value = "/userAdd")
	public String userAdd(@RequestParam(value = "userAddRoomId") String roomid,
			@RequestParam(value = "userAddText") String userEmail) throws Exception {
		log.info("roomid" + roomid);
		log.info("userEmail" + userEmail);
		mongoDbService.roomUserAdd(roomid, userEmail);

		return "redirect:/myMentoring";
	}

	// 채팅방 나가기
	@PostMapping(value = "/outChat")
	public String outChat(@RequestParam(value = "userAddRoomId") String roomid,
			@RequestParam(value = "userAddText") String userEmail) throws Exception {
		log.info("roomid" + roomid);
		log.info("userEmail" + userEmail);
		mongoDbService.outChat(roomid, userEmail);

		return "redirect:/myMentoring";
	}

	// 채팅방 조회
	@PostMapping("/roomOne")
	public String getRoom(@RequestParam(value = "roomId") String roomId, Model model, HttpSession session) {
		log.info("getRoom 실행");
		ChattIng chattIng = mongoDbService.getRoomOne(roomId);

		SessionUser userSession = (SessionUser) session.getAttribute("user");
		User user = totalRepositoryService.findByType(userSession.getEmail());

//		PostChat postChat = new PostChat(user,user.getName(),chattIng,chattIng.getChatList());

		model.addAttribute("ffff", 1);
		model.addAttribute("user", user);
		model.addAttribute("loginNick", user.getName());
		model.addAttribute("room", chattIng);
		model.addAttribute("ChatList", chattIng.getChatList());

		return "myMentoring :: .modal";
	}

	// 채팅방 조회
	@GetMapping("/room")
	@ResponseBody
	public Map<String, Object> getRooms(@RequestParam(value = "roomId") String roomId, Model model,
			HttpSession session) {
		log.info("getRooms 실행");
		ChattIng chattIng = mongoDbService.getRoomOne(roomId);

		SessionUser userSession = (SessionUser) session.getAttribute("user");
		User user = totalRepositoryService.findByType(userSession.getEmail());

		Map<String, Object> map = new HashMap<>();

		map.put("user", user);
		map.put("loginNick", user.getName());
		map.put("room", chattIng);
		map.put("ChatList", chattIng.getChatList());

		return map;
	}

}
