package com.noelwon.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noelwon.model.ccc.BoardEntity;
import com.noelwon.model.ccc.CompanyEntity;
import com.noelwon.model.ccc.ReplyEntity;
import com.noelwon.model.ccc.TradeEntity;
import com.noelwon.model.mobile.CallBoardDto;
import com.noelwon.model.mobile.PlansData;
import com.noelwon.model.mobile.SpotDtoResponse;
import com.noelwon.model.spot.EntityCityDto;
import com.noelwon.model.spot.EntityCountryDto;
import com.noelwon.model.spot.EntityInterestDto;
import com.noelwon.model.spot.EntitySearchDto;
import com.noelwon.model.spot.EntityTourAttractionDto;
import com.noelwon.model.spot.SpotDto;
import com.noelwon.model.userDto.User;
import com.noelwon.model.userDto.UserInterest;
import com.noelwon.mongo.MongoDbService;
import com.noelwon.service.BoardService;
import com.noelwon.service.CountryService;
import com.noelwon.service.DtoTomap;
import com.noelwon.service.MobileService;
import com.noelwon.service.TransferService;
import com.noelwon.service.UserService;
import com.noelwon.service.WeatherByGPSApplication;
import com.noelwon.service.totalService.TotalRepositoryService;

@RestController
@RequestMapping({ "/m" })
public class MobileController {
	Logger logger = LoggerFactory.getLogger("controller.MainController");

	private final MobileService mobileService;
	private final TransferService transferService;
	private final DtoTomap dtoTomap;
	private final CountryService countryService;
	private final WeatherByGPSApplication weatherByGPSApplication;
	private final UserService userService;
	private final MongoDbService mongoDbService;
	private final BoardService boardService;

	@Autowired
	private TotalRepositoryService totalRepositoryService;

	@Autowired
	public MobileController(MobileService mobileService, TransferService transferService, DtoTomap dtoTomap,
			CountryService countryService, WeatherByGPSApplication weatherByGPSApplication, UserService userService,
			MongoDbService mongoDbService, BoardService boardService) {
		super();

		this.mobileService = mobileService;
		this.transferService = transferService;
		this.dtoTomap = dtoTomap;
		this.countryService = countryService;
		this.weatherByGPSApplication = weatherByGPSApplication;
		this.userService = userService;
		this.mongoDbService = mongoDbService;
		this.boardService = boardService;
	}

	@RequestMapping({ "/list" })
	public JSONArray getlist() {
		JSONArray jArray = new JSONArray();
		Map<String, String[]> map = new HashMap<>();
		String[] arr = { "country", "city", "tourattraction", "interest", "board", "company", "trade", "reply",
				"search", "user", "userinfo", "planlist" };
		map.put("info", arr);

		JSONObject sObject = new JSONObject(map);
		jArray.add(sObject);

		return jArray;
	}

	@RequestMapping({ "/country" })
	public JSONArray goEnterPage() {
		List<EntityCountryDto> countryList = this.mobileService.viewCountry();
		JSONArray jArray = dtoTomap.goEnterPage(countryList);
		return jArray;
	}

	@RequestMapping({ "/city" })
	public JSONArray selectpage() {
		List<EntityCityDto> EntityCityList = this.mobileService.viewCity();
		JSONArray jArray = dtoTomap.selectpage(EntityCityList);
		return jArray;
	}

	@RequestMapping({ "/tourattraction" })
	public JSONArray TourAttraction() {
		List<EntityTourAttractionDto> EntityCityList = this.mobileService.viewPlace();
		JSONArray jArray = dtoTomap.TourAttraction(EntityCityList);
		return jArray;
	}

	@RequestMapping({ "/interest" })
	public JSONArray Interest() {
		List<EntityInterestDto> entityInterestDtoList = this.mobileService.viewinterest();
		JSONArray jArray = dtoTomap.Interest(entityInterestDtoList);
		return jArray;
	}

	@RequestMapping({ "/board" })
	public JSONArray board() {

		List<BoardEntity> entityInterestDtoList = this.mobileService.getBoard();
		JSONArray jArray = dtoTomap.board(entityInterestDtoList);

		return jArray;
	}

	@RequestMapping({ "/company" })
	public JSONArray company() {
		List<CompanyEntity> entityInterestDtoList = this.mobileService.getCompanyEntity();
		JSONArray jArray = dtoTomap.company(entityInterestDtoList);

		return jArray;
	}

	@RequestMapping({ "/trade" })
	public JSONArray trade() {
		List<TradeEntity> entityInterestDtoList = this.mobileService.getTrade();
		JSONArray jArray = dtoTomap.trade(entityInterestDtoList);

		return jArray;
	}

	@RequestMapping({ "/reply" })
	public JSONArray reply() {
		List<ReplyEntity> entityInterestDtoList = this.mobileService.getReply();
		JSONArray jArray = dtoTomap.reply(entityInterestDtoList);

		return jArray;
	}

	@RequestMapping({ "/search" })
	public JSONArray searc() {
		List<EntitySearchDto> entityInterestDtoList = this.mobileService.getSearch();
		JSONArray jArray = dtoTomap.searc(entityInterestDtoList);

		return jArray;
	}

//	// 민감한 개인정보는 밑의 로그인에서 직접 받아옴 = 주석으로 막아둠 = 지우는 것은 금지
//	@RequestMapping({ "/user" })
//	public JSONArray user() {
//		List<User> entityInterestDtoList = this.mobileService.userAll();
//		JSONArray jArray = dtoTomap.user(entityInterestDtoList);
//
//		return jArray;
//	}

	@RequestMapping({ "/userinfo" })
	public JSONArray userInfo() {

		List<UserInterest> entityInterestDtoList = this.mobileService.userInfoAll();
		JSONArray jArray = dtoTomap.userInfo(entityInterestDtoList);

		return jArray;
	}
	// ======================================================================

	// Django 로 관광지 이름 보내기
	@PostMapping(value = "sendplacename")
	@ResponseBody
	public String sendPlaceName(String placeName, String cityId, String stateInOut) throws Exception {
		logger.info("========================" + placeName);
		logger.info("========================" + cityId);
		logger.info("========================" + stateInOut);
		Map<String, String> map = transferService.dbTODjango(placeName, stateInOut, cityId);
		if (map != null) {
			return placeName;
		} else {
			return null;
		}
	}

	// 실내외 설정
	@PostMapping(value = "sendinout")
	@ResponseBody
	public String sendInOut(String placeName, String stateInOut) throws Exception {
		mobileService.sendPlaceEdit(placeName, stateInOut);

		return "실내외 잘 받았당께";
	}

	// 최근 5일 날씨 데이터
	@PostMapping(value = "sendDate")
	@ResponseBody
	public JSONArray sendDate(@RequestBody JSONObject selectedDates) {
		// selectedDates에서 데이터 추출
		String startDate = (String) selectedDates.get("startDate");
		String endDate = (String) selectedDates.get("endDate");
		String lat = (String) selectedDates.get("lat");
		String lng = (String) selectedDates.get("lng");

		String fullDate = startDate + " to " + endDate;

		Map<String, String[]> map = mobileService.weather(fullDate, lng, lat);

		JSONArray jArray = new JSONArray();

		for (String str : map.keySet()) {
			JSONObject response = new JSONObject();
			response.put("day", str);
			response.put("inOut", map.get(str)[0]);
			response.put("icon", map.get(str)[1]);
			jArray.add(response);
		}

		return jArray;
	}

	// 도시에 따라 뿌려줌
	@PostMapping(value = "sendAttrCity")
	@ResponseBody
	public JSONArray sendAttrCity(@RequestBody JSONObject getAttrWeath) {
		String placeId = (String) getAttrWeath.get("placeId");
		String finds = (String) getAttrWeath.get("finds");
		if (finds.equals("")) {
			finds = null;
		}
		if (placeId.equals("")) {
			placeId = null;
		}
		String date = (String) getAttrWeath.get("selectedDate");

		logger.info("zzzzzzzzzzzzz받는거 22sendAttrCity222sendAttrCitysendAttrCity222 placeId : " + placeId);
		logger.info("zzzzzzzzzzzzz받는거 22sendAttrCity222sendAttrCitysendAttrCity222 finds : " + finds);
		logger.info("zzzzzzzzzzzzz받는거 22sendAttrCity222sendAttrCitysendAttrCity222 date : " + date);

		List<String> strDate = this.countryService.getDate(date);

		Map<String, List<SpotDto>> list = this.countryService.selectPlace(date, placeId, finds);

		logger.info("처리하는거 sendAttrCity111 의 selectPlace 확인 : " + list.toString());

		JSONArray jArray = new JSONArray();

		for (String str : list.keySet()) {
			JSONObject response = new JSONObject();
			response.put("date", str);

			// SpotDto 리스트를 JSON 배열로 변환
			JSONArray spotArray = new JSONArray();
			List<SpotDto> spotList = list.get(str);
			for (SpotDto spot : spotList) {
				JSONObject spotJson = new JSONObject();
				spotJson.put("pk", spot.getPk());
				spotJson.put("name", spot.getName());
				spotJson.put("img", spot.getImg());
				spotJson.put("lan", spot.getLan());
				spotJson.put("lat", spot.getLat());
				spotJson.put("inOut", spot.getInOut());
				spotJson.put("cityId", spot.getCityId());
				spotArray.add(spotJson);
			}

			response.put("list", spotArray);
			jArray.add(response);
		}
		logger.info("보내는거22sendAttrCity222sendAttrCitysendAttrCity222 jArray : " + jArray.toString());

		return jArray;

	}

	// 날씨에 따라 뿌려줌
	@PostMapping(value = "sendAttrWeather")
	@ResponseBody
	public JSONArray sendAttrWeather(@RequestBody JSONObject getAttrWeath) {

		String placeId = (String) getAttrWeath.get("placeId");
		String finds = (String) getAttrWeath.get("finds");
		if (finds.equals("")) {
			finds = null;
		}
		if (placeId.equals("")) {
			placeId = null;
		}
		String date = (String) getAttrWeath.get("selectedDate");

		logger.info("zzzzzzzzzzzzz보내는거placeId : " + placeId);
		logger.info("zzzzzzzzzzzzz보내는거finds : " + finds);
		logger.info("zzzzzzzzzzzzz보내는거date : " + date);

		List<String> strDate = this.countryService.getDate(date);

		Map<String, List<SpotDto>> list = this.countryService.selectPlace(date, placeId, finds);
		Map<String, String[]> map = weatherByGPSApplication.getWeather(list.get(strDate.get(0)).get(0).getLan(),
				list.get(strDate.get(0)).get(0).getLat());

		logger.info("처리하는거 sendAttrWeather222 의 selectPlace 확인 : " + list.toString());

		Map<String, List<SpotDto>> filterInOut = this.countryService.inOutPlace(date, placeId, finds, map);

		logger.info("보내는거 filterInOut : " + filterInOut.toString());

		JSONArray jArray = new JSONArray();

		for (String str : filterInOut.keySet()) {
			JSONObject response = new JSONObject();
			response.put("date", str);

			// SpotDto 리스트를 JSON 배열로 변환
			JSONArray spotArray = new JSONArray();
			List<SpotDto> spotList = filterInOut.get(str);
			for (SpotDto spot : spotList) {
				JSONObject spotJson = new JSONObject();
				spotJson.put("pk", spot.getPk());
				spotJson.put("name", spot.getName());
				spotJson.put("img", spot.getImg());
				spotJson.put("lan", spot.getLan());
				spotJson.put("lat", spot.getLat());
				spotJson.put("inOut", spot.getInOut());
				spotJson.put("cityId", spot.getCityId());
				spotArray.add(spotJson);
			}

			response.put("list", spotArray);
			jArray.add(response);
		}
		logger.info("보내는거22222222222222222222 jArray : " + jArray.toString());

		return jArray;
	}

	// Android Login
	@PostMapping(value = "androidlogin")
	@ResponseBody
	public JSONObject getAndroidLogin(@RequestBody JSONObject user) {
		String email = (String) user.get("email");
		String password = (String) user.get("password");

		User tmpUser = new User();

		tmpUser = totalRepositoryService.findByType(email);
		logger.info("받은거22222222222222222222 jArray : " + email);
		logger.info("받은거22222222222222222222 jArray : " + password);
		logger.info("확인 jArray : " + tmpUser);

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		logger.info("tmpUser.getPassword()==>" + tmpUser.getPassword());
		logger.info("tmpUser.getPassword().getClass()==>" + tmpUser.getPassword().getClass());
		logger.info("비밀 번호 확인" + passwordEncoder.matches(password, tmpUser.getPassword()));

		if (passwordEncoder.matches(password, tmpUser.getPassword())) {
			JSONObject response = new JSONObject();
			response.put("id", Integer.toString(tmpUser.getId()));
			response.put("email", tmpUser.getEmail());
			response.put("name", tmpUser.getName());
			response.put("picture", tmpUser.getPicture());
			logger.info("비밀 번호 확인" + response.toJSONString());
			return response;
		} else {
			return null;
		}
	}

	// Android SignIn
	@PostMapping(value = "androidsignin")
	@ResponseBody
	public String getAndroidSignIn(String email, String name, String password) throws Exception {
		// 회원가입 성공 = "a" // 중복된 이메일 = "b // 회원가입 실패 = "d"
		return mobileService.signIn(email, name, password);
	}

	// Android 선호도 설정
	@PostMapping(value = "/androidfirstinterest")
	@ResponseBody
	public Boolean saveFirstInterestMobile(@RequestBody JSONObject sendInterest) {
		return mobileService.addPreference(sendInterest);
	}

	// 게시글의 view 조회수 올려줌
	@PostMapping(value = "sendviewcount")
	@ResponseBody
	public String setViewMobile(String tabtitle, String articleNo) throws Exception {
		mobileService.detailMobile(Integer.parseInt(articleNo), tabtitle);
		return "조회수 잘 받았당께";
	}

	// 댓글 추가
	@PostMapping(value = "/sendreply")
	@ResponseBody
	public Boolean addReplyMobile(@RequestBody JSONObject sendComment) throws Exception {

		String tabtitle = (String) sendComment.get("tabTitle");
		String article_no = (String) sendComment.get("articleNo");
		String replycontent = (String) sendComment.get("replyContent");
		String email = (String) sendComment.get("email");

		if (tabtitle.equals("리뷰모음")) {
			mobileService.ReplyAddMobile(tabtitle, article_no, replycontent, email);
		} else if (tabtitle.equals("동행자구인")) {
			mobileService.ReplyCompanyAddMobile(tabtitle, article_no, replycontent, email);
		} else if (tabtitle.equals("거래시스템")) {
			mobileService.ReplyTradeAddMobile(tabtitle, article_no, replycontent, email);
		}

		return true;
	}

	// 댓글 삭제
	@PostMapping(value = "/removereply")
	@ResponseBody
	public Boolean deleteReplyMobile(@RequestBody JSONObject removeComment) throws Exception {

		String tabtitle = (String) removeComment.get("tabTitle");
		String article_no = (String) removeComment.get("articleNo");
		String replyNo = (String) removeComment.get("replyNo");

		if (tabtitle.equals("리뷰모음")) {
			mobileService.deleteCommentMobile(tabtitle, article_no, Integer.parseInt(replyNo));
		} else if (tabtitle.equals("동행자구인")) {
			mobileService.deleteCompanyCommentMobile(tabtitle, article_no, Integer.parseInt(replyNo));
		} else if (tabtitle.equals("거래시스템")) {
			mobileService.deleteTradeCommentMobile(tabtitle, article_no, Integer.parseInt(replyNo));
		}
		return true;
	}

	// 게시글 작성
	@PostMapping(value = "/sendarticle")
	@ResponseBody
	public Boolean addArticleMobile(@RequestBody JSONObject sendArticle) throws Exception {
		String tabtitle = (String) sendArticle.get("tabTitle");
		String title = (String) sendArticle.get("title");
		String content = (String) sendArticle.get("content");
		String email = (String) sendArticle.get("email");

		if (tabtitle.equals("리뷰모음")) {
			mobileService.addBoardArticleMobile(tabtitle, title, content, email);
		} else if (tabtitle.equals("동행자구인")) {
			mobileService.addCompanyArticleMobile(tabtitle, title, content, email);
		} else if (tabtitle.equals("거래시스템")) {
			mobileService.addTradeArticleMobile(tabtitle, title, content, email);
		}
		return true;
	}

	// 게시글 삭제
	@PostMapping(value = "/removearticle")
	@ResponseBody
	public Boolean deleteArticleMobile(@RequestBody JSONObject removeArticle) throws Exception {
		String tabtitle = (String) removeArticle.get("tabTitle");
		String articleNo = (String) removeArticle.get("articleNo");

		if (tabtitle.equals("리뷰모음")) {
			mobileService.removeArticleMobile(tabtitle, Integer.parseInt(articleNo));
		} else if (tabtitle.equals("동행자구인")) {
			mobileService.removeCompanyArticleMobile(tabtitle, Integer.parseInt(articleNo));
		} else if (tabtitle.equals("거래시스템")) {
			mobileService.removeTradeArticleMobile(tabtitle, Integer.parseInt(articleNo));
		}
		return true;
	}

	// 계획 저장
	@PostMapping(value = "/saveplan")
	@ResponseBody
	public Boolean savePlanMobile(@RequestBody JSONObject plansData) {

		logger.info("JSONObject 확인" + plansData.toJSONString());
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			PlansData tmp = objectMapper.readValue(plansData.toJSONString(), PlansData.class);
			Map<String, String> map = new LinkedHashMap<>();
			map.put("planName", tmp.getPlanName());
			map.put("email", tmp.getEmail());
			map.put("startDay", tmp.getStartDay());
			map.put("endDay", tmp.getEndDay());
			for (SpotDtoResponse t : tmp.getPlans()) {
				String strTmp = "";
				for (SpotDto d : t.getList()) {
					strTmp = strTmp + d.getPk() + ",";
				}
				strTmp = strTmp.replaceFirst(".$", "");
				map.put(t.getDate(), strTmp);
			}
			mongoDbService.insert(map);
			return true;
		} catch (Exception e) {
			logger.info(e.getMessage());
			return false;
		}

	}

	// 계획 삭제
	@PostMapping(value = "/deleteplan")
	@ResponseBody
	public String deletePlanMobile(String planId) throws Exception {
		logger.info("deletePlanMobile 확인" + planId);
		try {
			mongoDbService.remove(planId);
			return "good";
		} catch (Exception e) {
			logger.info("deletePlanMobile error 확인" + e.getMessage());
			return null;
		}
	}

	// 유저 plansData 전체
	@RequestMapping({ "/planlist" })
	public JSONArray planlist() throws Exception {

		List<Map<String, String>> list = mongoDbService.findAll();
		List<PlansData> tmpList = new ArrayList<>();

		for (Map<String, String> map : list) {
			PlansData tmp = new PlansData();
			tmp.setId(map.get("_id"));
			tmp.setPlanName(map.get("planName"));
			tmp.setEmail(map.get("email"));
			tmp.setStartDay(map.get("startDay"));
			tmp.setEndDay(map.get("endDay"));

			List<SpotDtoResponse> plans = mapToPlansData(map);
			tmp.setPlans(plans);

			tmpList.add(tmp);

		}

		ObjectMapper objectMapper = new ObjectMapper();

		String josn = objectMapper.writeValueAsString(tmpList);

		JSONArray jArr = objectMapper.readValue(josn, JSONArray.class);

		return jArr;
	}

	// 유저 plansData 에 이어짐
	public List<SpotDtoResponse> mapToPlansData(Map<String, String> map) {
		List<SpotDtoResponse> plans = new ArrayList<>();
		List<String> strarr = Arrays.asList("_id", "planName", "email", "startDay", "endDay");
		for (String key : map.keySet()) {
			if (!strarr.contains(key)) {
				SpotDtoResponse dto = new SpotDtoResponse();
				dto.setDate(key);
//				dto.setList(countryService.listTOList(map.get(key)));
				// Check if the list is not present or empty
				if (map.get(key) == null || map.get(key).isEmpty()) {
					dto.setList(Collections.emptyList());
				} else {
					dto.setList(countryService.listTOList(map.get(key)));
				}
				plans.add(dto);
			}
		}
		return plans;
	}

	// 성향 같은 유저
	@PostMapping(value = "likeme")
	@ResponseBody
	public JSONArray checkLikeMe(@RequestBody JSONObject checkOtherUser) throws Exception {
		String userId = (String) checkOtherUser.get("id");
		String email = (String) checkOtherUser.get("email");
		String name = (String) checkOtherUser.get("name");
		String picture = (String) checkOtherUser.get("picture");

		logger.info("zzzzzzzzzzzzz보내는거 id : " + userId);
		logger.info("zzzzzzzzzzzzz보내는거 email : " + email);
		logger.info("zzzzzzzzzzzzz보내는거 name : " + name);
		logger.info("zzzzzzzzzzzzz보내는거 picture : " + picture);

		JSONArray jArray = new JSONArray();

		List<Integer> list = transferService.modelTODjango(Integer.parseInt(userId));

		List<User> userList = new ArrayList<>();

		for (Integer i : list) {
			if (Integer.parseInt(userId) != i) {
				userList.add(totalRepositoryService.findById(i,
						Arrays.asList(totalRepositoryService.getUserRepository()), User.class));
				logger.info("동일인 추천 제외" + (Integer.parseInt(userId) != i));
			}
		}

		for (User user : userList) {
			JSONObject response = new JSONObject();
			response.put("id", user.getId());
			response.put("email", user.getEmail());
			response.put("name", user.getName());
			response.put("picture", user.getPicture());
			jArray.add(response);
		}

		return jArray;
	}

	// Android Mypage for Other User Info
	@PostMapping(value = "seepersonalpage")
	@ResponseBody
	public JSONObject getOtherUserInfo(@RequestBody JSONObject seeUser) {
		String id = (String) seeUser.get("id");

		User tmpUser = new User();

		tmpUser = totalRepositoryService.findById(Integer.parseInt(id),
				Arrays.asList(totalRepositoryService.getUserRepository()), User.class);

		if (tmpUser != null) {
			JSONObject response = new JSONObject();
			response.put("id", Integer.toString(tmpUser.getId()));
			response.put("email", tmpUser.getEmail());
			response.put("name", tmpUser.getName());
			response.put("picture", tmpUser.getPicture());
			return response;
		} else {
			return null;
		}
	}

	// Android All BoardLists Call
	@PostMapping(value = "callboardlist")
	@ResponseBody
	public JSONObject getBoardListMobile(@RequestBody CallBoardDto callBoard) throws Exception {
		String kw = callBoard.getKw();
		int page = callBoard.getPage();
		String type = callBoard.getType();
		String email = callBoard.getEmail();

		try {
			JSONObject jSONObject = mobileService.BoardListMobile(kw, page, type, email);
			logger.info("getArticleListMobile Json 확인" + jSONObject.toJSONString());
			return jSONObject;
		} catch (Exception e) {
			logger.info("getArticleListMobile error 확인" + e.getMessage());
			return null;
		}
	}

	// Android All BoardLists Call
	@PostMapping(value = "callcompanylist")
	@ResponseBody
	public JSONObject getCompanyListMobile(@RequestBody CallBoardDto callBoard) throws Exception {
		String kw = callBoard.getKw();
		int page = callBoard.getPage();
		String type = callBoard.getType();
		String email = callBoard.getEmail();

		try {
			JSONObject jSONObject = mobileService.CompanyListMobile(kw, page, type, email);
			logger.info("getArticleListMobile Json 확인" + jSONObject.toJSONString());
			return jSONObject;
		} catch (Exception e) {
			logger.info("getArticleListMobile error 확인" + e.getMessage());
			return null;
		}
	}

	// Android All BoardLists Call
	@PostMapping(value = "calltradelist")
	@ResponseBody
	public JSONObject getTradeListMobile(@RequestBody CallBoardDto callBoard) throws Exception {
		String kw = callBoard.getKw();
		int page = callBoard.getPage();
		String type = callBoard.getType();
		String email = callBoard.getEmail();

		try {
			JSONObject jSONObject = mobileService.TradeListMobile(kw, page, type, email);
			logger.info("getArticleListMobile Json 확인" + jSONObject.toJSONString());
			return jSONObject;
		} catch (Exception e) {
			logger.info("getArticleListMobile error 확인" + e.getMessage());
			return null;
		}
	}

	// Android certain ReplyLists Call
	@PostMapping(value = "callreplylist")
	@ResponseBody
	public JSONArray getReplyListMobile(@RequestBody JSONObject callReply) throws Exception {
		String tabtitle = (String) callReply.get("tabtitle");
		String articleNo = (String) callReply.get("articleNo");

		logger.info("값 확인tabtitle" + tabtitle);
		logger.info("값 확인articleNo" + articleNo);

		try {
			List<ReplyEntity> entityInterestDtoList = this.mobileService.ReplyListMobile(tabtitle, articleNo);
			JSONArray jArray = dtoTomap.reply(entityInterestDtoList);
			return jArray;
		} catch (Exception e) {
			logger.info("getArticleListMobile error 확인" + e.getMessage());
			return null;
		}
	}

	// Android certain ReplyLists Call
	@PostMapping(value = "callplanlist")
	@ResponseBody
	public JSONArray callMyPlanList(@RequestBody JSONObject checkOtherUser) throws Exception {
		String email = (String) checkOtherUser.get("email");

		List<Map<String, String>> list = mongoDbService.findKeyValue("email", email);
		List<PlansData> tmpList = new ArrayList<>();

		for (Map<String, String> map : list) {
			PlansData tmp = new PlansData();
			tmp.setId(map.get("_id"));
			tmp.setPlanName(map.get("planName"));
			tmp.setEmail(map.get("email"));
			tmp.setStartDay(map.get("startDay"));
			tmp.setEndDay(map.get("endDay"));

			List<SpotDtoResponse> plans = mapToPlansData(map);
			tmp.setPlans(plans);

			tmpList.add(tmp);

		}

		ObjectMapper objectMapper = new ObjectMapper();

		String josn = objectMapper.writeValueAsString(tmpList);

		JSONArray jArr = objectMapper.readValue(josn, JSONArray.class);

		return jArr;
	}

	// Android User Interest List Call
	@PostMapping(value = "callmyinterest")
	@ResponseBody
	public JSONObject getInterestListMobile(@RequestBody JSONObject checkOtherUser) throws Exception {
		String email = (String) checkOtherUser.get("email");
		logger.info("getInterestListMobile 모바일 값 확인" + email);
		try {
			Optional<UserInterest> entityInterestDto = this.mobileService.InterestListMobile(email);
			Map<String, Integer> map = new HashMap<>();
			map.put("id", entityInterestDto.get().getId());
			map.put("user", entityInterestDto.get().getUser().getId());
			map.put("reliability", entityInterestDto.get().getReliability());
			map.put("history", entityInterestDto.get().getHistory());
			map.put("sights", entityInterestDto.get().getSights());
			map.put("culture", entityInterestDto.get().getCulture());
			map.put("food", entityInterestDto.get().getFood());
			map.put("nature", entityInterestDto.get().getNature());
			map.put("religion", entityInterestDto.get().getReligion());
			JSONObject sObject = new JSONObject(map);
			return sObject;
		} catch (Exception e) {
			logger.info("getInterestListMobile error 확인" + e.getMessage());
			return null;
		}
	}

	// ==================== Working ====================

	// Android User edit
	@PostMapping(value = "calledituser")
	@ResponseBody
	public Boolean editUserInfoMobile(@RequestBody JSONObject checkUser) throws Exception {
		String email = (String) checkUser.get("email");
		String name = (String) checkUser.get("name");
		String password = (String) checkUser.get("password");
		logger.info("editUserInfoMobile 모바일 email 값 확인" + email);
		logger.info("editUserInfoMobile 모바일 name 값 확인" + name);
		logger.info("editUserInfoMobile 모바일 password 값 확인" + password);

		User tmpUser = new User();
		tmpUser = totalRepositoryService.findByType(email);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		try {
			return true;
		} catch (Exception e) {
			logger.info("getInterestListMobile error 확인" + e.getMessage());
			return false;
		}

	}

	// Android User withdrawal
	@PostMapping(value = "callwithdrawaluser")
	@ResponseBody
	public Boolean withdrawUserMobile(@RequestBody JSONObject checkUser) throws Exception {
		String email = (String) checkUser.get("email");
		String name = (String) checkUser.get("name");
		String password = (String) checkUser.get("password");
		logger.info("withdrawUserMobile 모바일 email 값 확인" + email);
		logger.info("withdrawUserMobile 모바일 name 값 확인" + name);
		logger.info("withdrawUserMobile 모바일 password 값 확인" + password);

		User tmpUser = new User();
		tmpUser = totalRepositoryService.findByType(email);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		try {
			if (passwordEncoder.matches(password, tmpUser.getPassword())) {
				totalRepositoryService.delete(totalRepositoryService.findByType(email));
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.info("getInterestListMobile error 확인" + e.getMessage());
			return false;
		}
	}
}