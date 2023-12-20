package com.noelwon.mongo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.noelwon.model.chat.ChatMessageDto;
import com.noelwon.model.userDto.User;
import com.noelwon.mongoDb.ChattIng;
import com.noelwon.mongoDb.ChattRepository;
import com.noelwon.mongoDb.MsgDto;
import com.noelwon.service.UserService;
import com.noelwon.service.totalService.TotalRepositoryService;

@Service
public class MongoDbService {

	private final MongoTemplate mongoTemplate;
	private final ChattRepository chattRepository;
	private final UserService userService;
	private final TotalRepositoryService totalRepositoryService;

	@Autowired
	public MongoDbService(MongoTemplate mongoTemplate, ChattRepository chattRepository, UserService userService,
			TotalRepositoryService totalRepositoryService) {
		super();
		this.mongoTemplate = mongoTemplate;
		this.chattRepository = chattRepository;
		this.userService = userService;
		this.totalRepositoryService = totalRepositoryService;
	}

	Logger logger = LoggerFactory.getLogger("mongo.MongoDbService");

	private static Query QueryCreate(String key, String value) throws Exception { // Query문 생성

		Map<String, String> map = new HashMap<>();
		Criteria criteria = new Criteria(key);
		criteria.is(value);

		Query query = new Query(criteria);

		return query;
	}

	public void insert(Map<String, String> map) throws Exception { // 몽고db 저장

		mongoTemplate.insert(map, "userPlan");

	}

	public List<Map<String, String>> findAll() throws Exception { // 몽고db 전체 가져오기
		List<Map> mapList = mongoTemplate.findAll(Map.class, "userPlan");
		List<Map<String, String>> mapList2 = new ArrayList<>();
		for (int i = 0; i < mapList.size(); i++) {
			mapList.get(i).put("_id", mapList.get(i).get("_id").toString());
			Map<String, String> map = new LinkedHashMap<String, String>(mapList.get(i));
			mapList2.add(map);
		}

		return mapList2;
	}

	public List<Map<String, String>> findKeyValue(String key, String value) throws Exception { // 몽고db key value로 여러개
																								// 가져오기
		List<Map> mapList = mongoTemplate.find(QueryCreate("email", value), Map.class, "userPlan");
		List<Map<String, String>> mapList2 = new ArrayList<>();
		for (int i = 0; i < mapList.size(); i++) {
			mapList.get(i).put("_id", mapList.get(i).get("_id").toString());
			Map<String, String> map = new LinkedHashMap<String, String>(mapList.get(i));
			mapList2.add(map);
		}
		return mapList2;
	}

	public Map<String, String> planFindById(String value) throws Exception { // id로 플랜 찾기
		Map<String, String> map = new LinkedHashMap<>();
		Map<String, Object> tmpMap = mongoTemplate.findById(value, Map.class, "userPlan");

		tmpMap.forEach((strKey, objValue) -> {
			map.put(strKey, objValue.toString());
		});

		return map;
	}

	public Map<String, String> findAndModify(String value, Map<String, String> map) throws Exception { // 몽고 db 수정
		Query query = QueryCreate("_id", value);

		Update update = new Update();

		map.forEach((strKey, strValue) -> {
			update.set(strKey, strValue);
		});

		return mongoTemplate.findAndModify(query, update, Map.class, "userPlan");
	}

	public void remove(String value) throws Exception { // 몽고 db 삭제
		logger.info("삭제 함수 실행");
		Query query = QueryCreate("_id", value);
		mongoTemplate.remove(query, "userPlan");
	}

	public void edit(String value, Map<String, String> map) throws Exception { // 몽고 db 수정
		Query query = QueryCreate("_id", value);
		Update update = new Update();

		for (String str : map.keySet()) {
			update.set(str, map.get(str));
		}

		mongoTemplate.findAndModify(query, update, Map.class, "userPlan");
	}

//	=====================채팅방 관련======================

	public List<ChattIng> getChat() throws Exception { // 채팅 가져오기
		logger.info("getChat() 실행");
		List<ChattIng> mapList = mongoTemplate.findAll(ChattIng.class, "Chat");

		return mapList;
	}

	public boolean roomCheckUserList(List<Integer> userList, List<Integer> newList) { // 유저 리스트가 겹치는지 확인
		logger.info("roomCheckUserList 실행");
		int size = userList.size();
		logger.info("newList.toString()===>" + newList.toString());
		userList.addAll(newList);
		logger.info("userList.toString()===>" + userList.toString());
		Set<Integer> set = new HashSet<Integer>(userList);
		logger.info("set.toString()===>" + set.toString());
		return (size == set.size());
	}

	public String roomUserAdd(String id, String email) throws Exception { // 채팅방 유저 추가하기

		ChattIng chattIng = chattRepository.findById(id).get();
		List<Integer> list = chattIng.getUserList();

		if (roomCheckUserList(list, Arrays.asList(totalRepositoryService.findByType(email).getId()))) {
			logger.info("이미 채팅방에 존재하는 유저입니다.");
			return "이미 채팅방에 존재하는 유저입니다.";
		} else {
			chattIng.setUserList(list);
			chattRepository.save(chattIng);
			logger.info("채팅방에 추가했습니다.");
			return "채팅방에 추가했습니다.";
		}

	}

	public String outChat(String id, String email) throws Exception { // 채팅방 나가기
		ChattIng chattIng = chattRepository.findById(id).get();
		List<Integer> list = chattIng.getUserList();
		logger.info("list.toString()1====>" + list.toString());
		list.remove(Integer.valueOf(totalRepositoryService.findByType(email).getId()));
		logger.info("list.toString()2====>" + list.toString());
		chattIng.setUserList(list);
		if (list.size() < 1) {
			roomRemove(id);
			logger.info("if문 실행");
		} else {
			chattRepository.save(chattIng);
			logger.info("else문 실행");
		}

		return "채팅방을 나갔습니다.";

	}

	public void roomRemove(String id) throws Exception { // 방 삭제
		logger.info("삭제 함수 실행");

		ChattIng chattIng = chattRepository.findById(id).get();
		chattRepository.delete(chattIng);
	}

	public ChattIng CreateChatRoom(List<Integer> userList, String roomName) { // 방 생성(유저리스트,방 이름)
		logger.info("CreateChatRoom 실행");
		ArrayList<MsgDto> dtoList = new ArrayList<>();
		ChattIng chattIng = new ChattIng();
		chattIng.setRoomName(roomName);
		chattIng.setUserList(userList);
		chattIng.setChatList(dtoList);

		return chattRepository.save(chattIng);
	}

	public ChattIng getRoomOne(String _id) { // 채팅방 하나 찾기(id)
//		Criteria criteria = new Criteria("_id");
//		criteria.is(_id);
//		Query query = new Query(criteria);
//		ChattIng chattIng = mongoTemplate.findOne(query, ChattIng.class, "Chat");

		return chattRepository.findById(_id).get();
	}

	public ChattIng getRoomTOUserList(List<Integer> userList) { // 채팅방 하나 찾기(유저리스트)
		Criteria criteria = new Criteria("userList");
		criteria.is(userList);
		Query query = new Query(criteria);
		ChattIng chattIng = mongoTemplate.findOne(query, ChattIng.class, "Chat");
		return chattIng;
	}

	public List<ChattIng> getRoomToUserId(Integer userId) { // 유저id로 가입된 채팅방 전체 조회
		List<ChattIng> tmp = mongoTemplate.findAll(ChattIng.class, "Chat");
		List<ChattIng> chatUserList = new ArrayList<>();
		for (ChattIng dto : tmp) {
			if (dto.getUserList().contains(userId)) {
				chatUserList.add(dto);
				logger.info("asdasdasdasdasd");
			}
		}
		return chatUserList;
	}

	public void msgSave(ChatMessageDto chatMessageDto, String _id) throws Exception { // DB에 채팅내용 저장
		Criteria criteria = new Criteria("_id");
		criteria.is(_id);
		Query query = new Query(criteria);

		logger.info("chatMessageDto.toString()==>" + chatMessageDto.toString());

		ArrayList<MsgDto> dtoList = new ArrayList<>();

		ChattIng chattIng = getRoomOne(_id);
		List<Integer> readUsers = chattIng.getUserList();
		logger.info("chattIng.toString()===>" + chattIng.toString());
		if (chattIng.getChatList() != null) {
			dtoList = chattIng.getChatList();
		}

		MsgDto msgDto = new MsgDto();
		msgDto.setMsg(chatMessageDto.getMessa());
		msgDto.setUserId(chatMessageDto.getUserId().intValue());
		msgDto.setDate(LocalDateTime.now().toString());
		msgDto.setUserName(chatMessageDto.getUserName());
		readUsers.remove(Integer.valueOf(chatMessageDto.getUserId().intValue()));
		msgDto.setUnReadUsers(readUsers);
		logger.info("chatMessageDto.getUserId().intValue()==>"+chatMessageDto.getUserId().intValue());
		
		msgDto.setUserImg(totalRepositoryService.findById(chatMessageDto.getUserId().intValue(),
				Arrays.asList(totalRepositoryService.getUserRepository()), User.class).getPicture());

		dtoList.add(msgDto);

		Update update = new Update();
		update.set("chatList", dtoList);

		mongoTemplate.findAndModify(query, update, ChattIng.class, "Chat");

	}

	public void msgUpdate(ChattIng chattIng) throws Exception { // 업데이트
		Criteria criteria = new Criteria("_id");
		criteria.is(chattIng.get_id());
		Query query = new Query(criteria);

		mongoTemplate.findAndReplace(query, chattIng);

	}

}
