package com.noelwon.service;

import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.noelwon.Security.SessionUser;
import com.noelwon.model.spot.EntityTourAttractionDto;
import com.noelwon.model.spot.SpotDto;
import com.noelwon.model.userDto.User;
import com.noelwon.model.userDto.UserInterest;
import com.noelwon.service.totalService.TotalRepositoryService;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService {

	Logger logger = LoggerFactory.getLogger("UserService=================>");

	private final PasswordEncoder passwordEncoder;
	private final CountryService countryService;
	private final UtilBox utilBox;
	private final TotalRepositoryService totalRepositoryService;

	@Autowired
	public UserService(PasswordEncoder passwordEncoder, CountryService countryService, UtilBox utilBox,
			TotalRepositoryService totalRepositoryService) {
		this.passwordEncoder = passwordEncoder;
		this.countryService = countryService;
		this.utilBox = utilBox;
		this.totalRepositoryService = totalRepositoryService;
	}

	public User create(String username, String email, String password) {
		User user = new User();
		user.setName(username);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		user.setType("form");

		UserInterest n1 = new UserInterest();
		n1.setUser(user);
		n1.setReliability(0);

		totalRepositoryService.save(user);
		totalRepositoryService.save(n1);
		return user;
	}

	public String reportCheck(Principal principal, HttpSession session) {
		new HashMap<>();
		if (principal != null) {
			logger.info("principal.toString()==>" + principal.toString());
			logger.info("session.toString()==>" + session.getId());

			try {
				SessionUser userInfo = (SessionUser) session.getAttribute("user");
				User user = totalRepositoryService.findByType(userInfo.getEmail());
				UserInterest userInterest = totalRepositoryService.findByType(user);
				if (userInterest.getFood() == null) {
					return "preference";
				}
			} catch (Exception e) {
				logger.info(e.getMessage());
				return "home";
			}

		}
		return "home";

	}

//	 지금 접속해있는 사람의 비밀번호와 새로 받은 패스워드 검증
	public boolean passwordCheck(HttpSession session, String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		SessionUser userInfo = (SessionUser) session.getAttribute("user");
		User user = totalRepositoryService.findByType(userInfo.getEmail());
		return passwordEncoder.matches(password, user.getPassword());
	}

//	선택한 유저 선호도 적용
	public void addPreference(String preference, HttpSession session) {
		String[] arr = preference.split(",");
		SessionUser userInfo = (SessionUser) session.getAttribute("user");
		User user = totalRepositoryService.findByType(userInfo.getEmail());

		UserInterest userInterest = totalRepositoryService.findByType(user);
		userInterest.setHistory(Integer.valueOf(arr[0]));
		userInterest.setSights(Integer.valueOf(arr[1]));
		userInterest.setCulture(Integer.valueOf(arr[2]));
		userInterest.setFood(Integer.valueOf(arr[3]));
		userInterest.setNature(Integer.valueOf(arr[4]));
		userInterest.setReligion(Integer.valueOf(arr[5]));
		totalRepositoryService.save(userInterest);
	}

	public String getUserInfo(HttpSession session, User user) {

		if (user == null) {
			SessionUser userInfo = (SessionUser) session.getAttribute("user");
			user = totalRepositoryService.findByType(userInfo.getEmail());
		}

		UserInterest userInterest = totalRepositoryService.findByType(user);
		String str = userInterest.getHistory().toString() + "," + userInterest.getSights() + ","
				+ userInterest.getCulture() + "," + userInterest.getFood() + "," + userInterest.getNature() + ","
				+ userInterest.getReligion();
		return str;
	}

	public Map<String, String> userPlan(String planName, String date, String plan, String num, HttpSession session) {

		Map<String, String> map = new LinkedHashMap<>();

		SessionUser user = (SessionUser) session.getAttribute("user");
		User userOne = totalRepositoryService.findByType(user.getEmail());

		UserInterest userInfo = totalRepositoryService.findByType(userOne);

		String[] dateArr = date.split(",");
		String[] plansArr = plan.split(",");
		String[] numArr = num.split(",");
		String plans = "";
		int sum = 0;

		logger.info(planName);
		logger.info(date);
		logger.info(plan);
		logger.info(num);

		Map<String, Integer> mapInterestId = new HashMap<>() {
			{
				put("역사", 0);
				put("명소", 1);
				put("문화", 2);
				put("음식", 3);
				put("자연", 4);
				put("종교", 5);
			}
		};

		List<SpotDto> list = countryService.listTOList(plan);
		int[] add = new int[6];
		int[] intarr = new int[6];

		intarr[0] = userInfo.getHistory();
		intarr[1] = userInfo.getSights();
		intarr[2] = userInfo.getCulture();
		intarr[3] = userInfo.getFood();
		intarr[4] = userInfo.getNature();
		intarr[5] = userInfo.getReligion();

//		list.get(0).getPk()
		for (SpotDto spot : list) {
//			spot.getPk()
			EntityTourAttractionDto dto = countryService.spotDtoToDto(spot.getPk());
			if (dto != null) {
				String type = dto.getEntityInterestDto().getInterestType();
				add[mapInterestId.get(type)] = add[mapInterestId.get(type)] + 1;
			}
		}

		logger.info("userInfo1====>" + userInfo.toString());

		int[] calArr = utilBox.cal(intarr, add);

		userInfo.setHistory(calArr[0]);
		userInfo.setSights(calArr[1]);
		userInfo.setCulture(calArr[2]);
		userInfo.setFood(calArr[3]);
		userInfo.setNature(calArr[4]);
		userInfo.setReligion(calArr[5]);

		logger.info("userInfo1====>" + userInfo.toString());

		totalRepositoryService.save(userInfo);

		map.put("planName", planName);
		map.put("email", user.getEmail());
		map.put("startDay", dateArr[0]);
		map.put("endDay", dateArr[dateArr.length - 1]);

		for (int j = 0; j < dateArr.length; j++) { //
			plans = "";
			if (Integer.parseInt(numArr[j]) != 0) {
				for (int i = 0; i < Integer.parseInt(numArr[j]); i++) {
					plans = plans + plansArr[sum] + ",";
					sum++;
				}
				plans = plans.substring(0, plans.length() - 1);
				map.put(dateArr[j], plans);
			}

		}
		return map;
	}

}
