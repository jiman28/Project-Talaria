package com.noelwon.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noelwon.model.userDto.UserInterest;

public class RequestUtil {
	// url과 파일이름을 매개변수로
	
	Logger logger = LoggerFactory.getLogger("controller.RequestUtil");
		public static JsonNode restRequest(String requestUrl, String name) { 
			//보낼 파라메터 셋팅(file name)
			
			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("search", name);
			String[] search_info = new String[6];
			
		     
			//헤더셋팅
			HttpHeaders headers = new HttpHeaders();
			headers.add("accept", "text/plain;charset=UTF-8");
		       
			//파라메터와 헤더 합치기
			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
			//RestTemplate 객체 만들고 초기화
			RestTemplate restTemplate = new RestTemplate();
			
			
			
			// 상대에게 결과를 요청하고 받아오는 것 = exchange이다. String.class 는 String타입의 결과를 받을 것이다. 8번과정
			ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.POST, entity, String.class);
			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root;
			JsonNode target = null;
			try {
				// response.getBody() json파일 내용을 가져온다.
				root = mapper.readTree(response.getBody());
				return root;
//				search_info[0] = root.path("name").asText();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		        
			return null;
			
		}
		
		
		
		public static JsonNode aiModel(String requestUrl, List<UserInterest> userList, UserInterest user) { 
			//보낼 파라메터 셋팅(file name)
			Logger logger = LoggerFactory.getLogger("controller.RequestUtil.aiModel");
			List<UserInterest> userInfoList = new ArrayList<>();
			userInfoList.add(user);
			
			MultiValueMap<String, List<UserInterest>> params = new LinkedMultiValueMap<>();
			logger.info(user.toString());
			
			params.add("userList", userList);
			params.add("user", userInfoList);
		     
			//헤더셋팅
			HttpHeaders headers = new HttpHeaders();
			headers.add("accept", "text/plain;charset=UTF-8");
		        
			//파라메터와 헤더 합치기
			HttpEntity<MultiValueMap<String, List<UserInterest> >> entity = new HttpEntity<>(params, headers);
			//RestTemplate 객체 만들고 초기화
			RestTemplate restTemplate = new RestTemplate();
			
			
			
			// 상대에게 결과를 요청하고 받아오는 것 = exchange이다. String.class 는 String타입의 결과를 받을 것이다. 8번과정
			ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.POST, entity, String.class);
			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root;
			JsonNode target = null;;
			try {
				// response.getBody() json파일 내용을 가져온다.
				root = mapper.readTree(response.getBody());
				logger.info("root.toString()====>"+root.toString());
				return root;
//				search_info[0] = root.path("name").asText();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		        
			return null;
			
		}
		
		
	
}
