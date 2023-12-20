package com.noelwon.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.noelwon.Security.SessionUser;
import com.noelwon.model.ccc.BoardAll;
import com.noelwon.model.spot.EntityCityDto;
import com.noelwon.model.spot.EntityCountryDto;
import com.noelwon.model.spot.EntityInterestDto;
import com.noelwon.model.spot.EntityTourAttractionDto;
import com.noelwon.model.spot.SpotDto;
import com.noelwon.model.userDto.User;
import com.noelwon.mongo.MongoDbService;
import com.noelwon.service.BoardService;
import com.noelwon.service.CountryService;
import com.noelwon.service.ReviewService;
import com.noelwon.service.TransferService;
import com.noelwon.service.UserCreateForm;
import com.noelwon.service.UserService;
import com.noelwon.service.WeatherByGPSApplication;
import com.noelwon.service.totalService.TotalRepositoryService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import net.minidev.json.JSONObject;

@Controller
@RequestMapping("/project")
public class Main implements Webcontroller {
	Logger logger = LoggerFactory.getLogger("controller.MainController");
	private int tmpI = 0;
	
	private final CountryService countryService;
	private final TransferService transferService;
	private final UserService userService;
	private final BoardService boardService;
	private final ReviewService reviewService;
	private final MongoDbService mongoDbService;
	private final TotalRepositoryService totalRepositoryService;
	private final WeatherByGPSApplication weatherByGPSApplication;
	
	private HttpSession session;
	

	@Autowired
	public Main(CountryService countryService, TransferService transferService, UserService userService,
			BoardService boardService, ReviewService reviewService,
			MongoDbService mongoDbService, TotalRepositoryService totalRepositoryService, HttpSession session,
			WeatherByGPSApplication weatherByGPSApplication) {
		super();
		this.countryService = countryService;
		this.transferService = transferService;
		this.userService = userService;
		this.boardService = boardService;
		this.reviewService = reviewService;
		this.mongoDbService = mongoDbService;
		this.totalRepositoryService = totalRepositoryService;
		this.session = session;
		this.weatherByGPSApplication = weatherByGPSApplication;
	}

	private Model checkPermissions(Model model) {
		SessionUser user = (SessionUser) session.getAttribute("user");
		if (user != null) {
			User userOne = totalRepositoryService.findByType(user.getEmail());
			model.addAttribute("loginUser", userOne.getId());
		} else {
			model.addAttribute("loginUser", "0");
		}
		return model;
	}



	//	메인 페이지 접속
	@RequestMapping({ "/home", "/" })
	@Override
	public String goEnterPage(Model model, Principal principal) throws Exception {
		model = checkPermissions(model);

		// 데이터베이스의 나라list를 countryList에 담는다. viewCountry()는 나라 정보 전체를 가져온다.
		List<EntityCountryDto> countryList = this.countryService.viewCountry();
		// countryList를 html에서 사용할 때 totalCountry로 전달
		model.addAttribute("totalCountry", countryList);
		// 사용하지 않는 것같음
		List<EntityInterestDto> interestList = this.countryService.viewinterest();

		model.addAttribute("interestList", interestList);

//		logger.info("principal=====>"+principal.getName());
		return userService.reportCheck(principal, session);
	}

	@RequestMapping("/addInfo")
	@Override
	public String addInfo(Model model, String preference, HttpSession session) throws Exception {
		this.userService.addPreference(preference, session);
		return "redirect:home";
	}

	
	
	
	
	public List<Map<String, String>> listMapSetImg(List<Map<String, String>> list,
			Map<String, Map<String, List<SpotDto>>> map) {
		
		for (int i = 0; i < list.size();i++) {
			String img = null;
			
			for (List<SpotDto> dto : map.get(list.get(i).get("_id")).values()) {
				img = dto.get(0).getImg();
				break;
			}
			
			
			
			list.get(i).put("img", img);
		}
		
		return list;
	}
	
	
	
	

	@RequestMapping("/mypage")
	@Override
	public String mypage(Model model, HttpSession session, int page) throws Exception {
		SessionUser user = (SessionUser) session.getAttribute("user");
		User userOne = totalRepositoryService.findByType(user.getEmail());

		model = checkPermissions(model);

		Page<BoardAll> boardList = boardService.findByUserId(userOne, page);
		logger.info("mypage===>" + userOne.getEmail());
		List<Map<String, String>> listMap = mongoDbService.findKeyValue("email", user.getEmail()); // 플랜 정보
		Map<String, Map<String, List<SpotDto>>> mapSpot = new LinkedHashMap<>();

		for (Map<String, String> dbMap : listMap) {
			for (String id : dbMap.keySet()) {
				Map<String, List<SpotDto>> tmp = new LinkedHashMap<>();
				List<String> date = this.countryService.getDate(dbMap.get("startDay") + " to " + dbMap.get("endDay"));
				for (String day : date) {
					logger.info("day===>" +day);
					logger.info("dbMap.toString()===>" +dbMap.toString());
					logger.info("dbMap.get(day)===>" + dbMap.get(day));
					tmp.put(day, countryService.listTOList(dbMap.get(day)));
				}
				mapSpot.put(dbMap.get("_id"), tmp);
			}
		}

		try {
			SpotDto tmp = mapSpot.values().iterator().next().values().iterator().next().get(0);
			Map<String, String[]> map2 = weatherByGPSApplication.getWeather(tmp.getLan(), tmp.getLat());
			model.addAttribute("weatherList", map2);
		} catch (Exception e) {
			Map<String, String[]> map2 = weatherByGPSApplication.getWeather("0", "0");
			model.addAttribute("weatherList", map2);
		}

		listMap = listMapSetImg(listMap, mapSpot);
		
		model.addAttribute("authority", true);
		model.addAttribute("boardSize", boardList.getTotalElements()); // 유저 게시글 정보
		model.addAttribute("boardList", boardList); // 유저 게시글 정보
		model.addAttribute("userOne", userOne); // 유저 정보
		model.addAttribute("userInfo", this.userService.getUserInfo(session, null)); // 유저 선호도 정보(그래프 용)
		model.addAttribute("userPlan", listMap); // 유저 플랜 정보
		model.addAttribute("datePlan", mapSpot); // 유저 플랜 날짜별 정보
		model.addAttribute("aiCode", false);

		return "mypage";
	}

	@RequestMapping("/youpage")
	@Override
	public String youpage(Model model, String email, int page) throws Exception {
		logger.info("email===>" + email);
		User userOne = totalRepositoryService.findByType(email);

		model = checkPermissions(model);

		Page<BoardAll> boardList = boardService.findByUserId(userOne, page);
		logger.info("mypage===>" + userOne.getEmail());
		List<Map<String, String>> listMap = mongoDbService.findKeyValue("email", userOne.getEmail()); // 플랜 정보
		Map<String, Map<String, List<SpotDto>>> mapSpot = new LinkedHashMap<>();

		for (Map<String, String> dbMap : listMap) {
			for (String id : dbMap.keySet()) {
				Map<String, List<SpotDto>> tmp = new LinkedHashMap<>();
				List<String> date = this.countryService.getDate(dbMap.get("startDay") + " to " + dbMap.get("endDay"));
				for (String day : date) {
					tmp.put(day, countryService.listTOList(dbMap.get(day)));
				}
				mapSpot.put(dbMap.get("_id"), tmp);
			}
		}

		try {
			SpotDto tmp = mapSpot.values().iterator().next().values().iterator().next().get(0);
			Map<String, String[]> map2 = weatherByGPSApplication.getWeather(tmp.getLan(), tmp.getLat());
			model.addAttribute("weatherList", map2);
		} catch (Exception e) {
			Map<String, String[]> map2 = weatherByGPSApplication.getWeather("0", "0");
			model.addAttribute("weatherList", map2);
		}

		listMap = listMapSetImg(listMap, mapSpot);
		
		model.addAttribute("authority", false);
		model.addAttribute("boardSize", boardList.getTotalElements()); // 유저 게시글 정보
		model.addAttribute("boardList", boardList); // 유저 게시글 정보
		model.addAttribute("userOne", userOne); // 유저 정보
		model.addAttribute("userInfo", this.userService.getUserInfo(null, userOne)); // 유저 선호도 정보(그래프 용)
		model.addAttribute("userPlan", listMap); // 유저 플랜 정보
		model.addAttribute("datePlan", mapSpot); // 유저 플랜 날짜별 정보
		model.addAttribute("aiCode", false);

		return "mypage";
	}

	@RequestMapping({ "/checkPass" })
	@ResponseBody
	public Map<String, Boolean> checkPass(@RequestBody JSONObject pass) {
		logger.info(pass.getAsString("pass"));
		Map<String, Boolean> map = new HashMap<>();
		
		map.put("key", this.userService.passwordCheck(session, pass.getAsString("pass")));
		
		
		return map;
	}
	
	@RequestMapping({ "/deactivate" })
	public String deactivate(@RequestParam(value = "email")String email) {
		totalRepositoryService.delete(totalRepositoryService.findByType(email));
		
		return "redirect:/user/logout";
	}
	
	@RequestMapping({ "/selectpage" })
	@Override
	public String goSelectPage(Model model, String countryName, String date) throws Exception {

		model = checkPermissions(model);

//		일정탭에서 나라셀렉트를 담기 위한것
		List<EntityCountryDto> countryList = this.countryService.viewCountry();
		model.addAttribute("totalCountry", countryList);
//		나라별 도시 표시하는 것 우선 데이터 베이스 도시를 다 가져오고 html에서 cityfromcountry 로 필터를 해준다.
		List<EntityCityDto> cityList = this.countryService.viewCity();
		model.addAttribute("cityList", cityList);

//		도시별 관광지 표시하는 것
		List<EntityTourAttractionDto> placeList = this.countryService.viewPlace();
		model.addAttribute("placeList", placeList);

//		선택된 나라 이름 표시
		EntityCountryDto countryInfo = this.countryService.CountryOne(countryName);
		model.addAttribute("checkCountry", countryInfo);

//		관심사 카테고리 표시
		List<EntityInterestDto> interestList = this.countryService.viewinterest();
		model.addAttribute("interestList", interestList);

//		달력 날짜 표시		
		logger.info("===========>" + date);
		model.addAttribute("date", date);

		return "selectpage";

	}

	@RequestMapping({ "/basket" })
	@Override
	public String basket(Model model, String placeId, String date, String finds) throws Exception {
		model = checkPermissions(model);
		List<String> strDate = this.countryService.getDate(date);

		Map<String, List<SpotDto>> list2 = this.countryService.selectPlace(date, placeId, finds);
		Map<String, String[]> map2 = weatherByGPSApplication.getWeather(list2.get(strDate.get(0)).get(0).getLan(),
				list2.get(strDate.get(0)).get(0).getLat());

		Map<String, List<SpotDto>> filterInOut = this.countryService.inOutPlace(date, placeId, finds, map2);

		model.addAttribute("basketList", list2);
		model.addAttribute("date", date);
		model.addAttribute("dateList", strDate);
		model.addAttribute("weatherList", map2);
		model.addAttribute("filter", filterInOut);

		// 관광지 추가 폼
		List<EntityCountryDto> countryList = this.countryService.viewCountry();
		model.addAttribute("totalCountry", countryList);
		List<EntityCityDto> cityList = this.countryService.viewCity();
		model.addAttribute("cityList", cityList);

		return "basket";
	}

	@Override
	@RequestMapping({ "/addPlan" })
	public String addPlan(String planName, String date, String plans, String num, HttpSession session)
			throws Exception {

		logger.info(num);

		Map<String, String> findAllMap = this.userService.userPlan(planName, date, plans, num, session);

		mongoDbService.insert(findAllMap);

		List<Map<String, String>> map = mongoDbService.findAll();

		return "redirect:home";
	}

	@RequestMapping({ "/popup" })
	public String popup(Model model) throws Exception {

		List<EntityCountryDto> countryList = this.countryService.viewCountry();
		model.addAttribute("totalCountry", countryList);

		List<EntityCityDto> cityList = this.countryService.viewCity();
		model.addAttribute("cityList", cityList);

		return "popup";
	}

	@PostMapping("/find")
	@ResponseBody
	@Override
	public Map<String, String> find(String searchName, String inOut, String cityId) throws Exception {
		Map<String, String> map = transferService.dbTODjango(searchName, inOut, cityId);

		return map;
	}

	@RequestMapping("/model")
	public String model(@RequestPart(value = "userId") String userId, Model model) throws Exception {
		logger.info("model 실행 userId값==>" + userId);
		List<Integer> list = transferService.modelTODjango(Integer.parseInt(userId));

		List<User> userList = new ArrayList<>();

		for (Integer i : list) {
			if (Integer.parseInt(userId) != i) {
				userList.add(totalRepositoryService.findById(i, Arrays.asList(totalRepositoryService.getUserRepository()),User.class));
				logger.info("동일인 추천 제외" + (Integer.parseInt(userId) != i));
			}

		}

		model.addAttribute("userList", userList);
		model.addAttribute("aiCode", true);
		model.addAttribute("userOne", totalRepositoryService.findById(Integer.parseInt(userId), Arrays.asList(totalRepositoryService.getUserRepository()),User.class));

		return "mypage :: #featuredUser";
	}

	@Override
	@RequestMapping({ "/planRemove" })
	public String remove(String id) throws Exception {
		logger.info("======>" + id);
		mongoDbService.remove(id);

		return "redirect:mypage";
	}

	@RequestMapping({ "/planEdit" })
	public String planEdit(String id, Model model) throws Exception {
		logger.info("======>" + id);

		Map<String, String> map = mongoDbService.planFindById(id);
		Map<String, List<SpotDto>> planMap = new LinkedHashMap<>();
		List<String> strarr = Arrays.asList("_id", "planName", "email", "startDay", "endDay");
		List<EntityCityDto> cityList = this.countryService.viewCity();
		List<EntityCountryDto> countryList = this.countryService.viewCountry();
		map.forEach((strKey, strValue) -> {
			if (!strarr.contains(strKey)) {
				logger.info("strValue===>" + strValue);
				planMap.put(strKey, countryService.listTOList(strValue));
			}
		});

		List<String> strDate = this.countryService.getDate(map.get("startDay") + " to " + map.get("endDay"));

		Map<String, String[]> map2 = weatherByGPSApplication.getWeather(planMap.get(strDate.get(0)).get(0).getLan(),
				planMap.get(strDate.get(0)).get(0).getLat());

		model = checkPermissions(model);
		model.addAttribute("totalCountry", countryList);
		model.addAttribute("cityList", cityList);
		model.addAttribute("planName", map.get("planName"));
		model.addAttribute("planMap", planMap);
		model.addAttribute("weatherList", map2);
		model.addAttribute("planId", id);

		return "planAdit";
	}

	@RequestMapping("/changeDays")
	public String changeDays(@RequestPart(value = "date") String date, @RequestPart(value = "id") String id,
			Model model) throws Exception {
		this.tmpI = 0;
		Map<String, String> map = mongoDbService.planFindById(id);
		Map<String, List<SpotDto>> planMap = new LinkedHashMap<>();
		List<String> strarr = Arrays.asList("_id", "planName", "email", "startDay", "endDay");
		List<String> strDate = this.countryService.getDate(date);
		logger.info("strDate.toString()===>" + strDate.toString());
		logger.info("map.toString()===>" + map.toString());
		
		map.forEach((strKey, strValue) -> {
			if (!strarr.contains(strKey)) {
				logger.info("strValue===>" + strValue);
				planMap.put(strDate.get(tmpI), countryService.listTOList(strValue));
				tmpI++;
			}
		});
		
		for (; tmpI < strDate.size(); tmpI++) {
			planMap.put(strDate.get(tmpI), new ArrayList<SpotDto>());
		}

		Map<String, String[]> map2 = weatherByGPSApplication.getWeather(planMap.get(strDate.get(0)).get(0).getLan(),
				planMap.get(strDate.get(0)).get(0).getLat());
		model.addAttribute("weatherList", map2);
		model.addAttribute("planMap", planMap);
		return "planAdit :: .asdf";
	}

	@Override
	@RequestMapping({ "/PlanAditDb" })
	public String PlanAditDb(String planName, String date, String plans, String num, String id) throws Exception {

		logger.info(num);

		Map<String, String> planMap = mongoDbService.planFindById(id);
		SessionUser userInfo = (SessionUser) session.getAttribute("user");
		if (planMap.get("email").equals(userInfo.getEmail())) {
			Map<String, String> findAllMap = this.userService.userPlan(planName, date, plans, num, session);
			mongoDbService.findAndModify(id, findAllMap);
		} else {
			Map<String, String> findAllMap = this.userService.userPlan(planName, date, plans, num, session);
			mongoDbService.insert(findAllMap);
		}
		return "redirect:mypage";
	}

	// =============로그인 관련

	@Override
	@GetMapping("/formLogin")
	public String login() {
		return "login_form";
	}

	@GetMapping("/signup")
	public String signup(UserCreateForm userCreateForm) {
		return "signup_form";
	}

	@PostMapping("/signup")
	public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "signup_form";
		}

		if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
			return "signup_form";
		}

		userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword1());

		return "redirect:home";
	}

}
