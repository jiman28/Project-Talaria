package com.noelwon.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.noelwon.model.ccc.BoardEntity;
import com.noelwon.model.ccc.BoardEntityRepository;
import com.noelwon.model.ccc.CompanyEntity;
import com.noelwon.model.ccc.CompanyEntityRepository;
import com.noelwon.model.ccc.ReplyEntity;
import com.noelwon.model.ccc.ReplyRepository;
import com.noelwon.model.ccc.TradeEntity;
import com.noelwon.model.ccc.TradeEntityRepository;
import com.noelwon.model.spot.AttractionRepository;
import com.noelwon.model.spot.CityRepository;
import com.noelwon.model.spot.CountryRepository;
import com.noelwon.model.spot.EntityCityDto;
import com.noelwon.model.spot.EntityCountryDto;
import com.noelwon.model.spot.EntityInterestDto;
import com.noelwon.model.spot.EntitySearchDto;
import com.noelwon.model.spot.EntitySearchDtoRepository;
import com.noelwon.model.spot.EntityTourAttractionDto;
import com.noelwon.model.spot.InterestRepository;
import com.noelwon.model.userDto.User;
import com.noelwon.model.userDto.UserInterest;
import com.noelwon.model.userDto.UserInterestRepository;
import com.noelwon.model.userDto.UserRepository;
import com.noelwon.service.totalService.TotalRepositoryService;

import jakarta.transaction.Transactional;

@Service
public class MobileService {

	@Autowired
	private BoardEntity boardEntity;
	@Autowired
	private CompanyEntity companyEntity;
	@Autowired
	private TradeEntity tradeEntity;
	@Autowired
	private UserService userService;
	@Autowired
	private ReplyEntity replyEntity;
	@Autowired
	private User user;
	@Autowired
	private  TotalRepositoryService totalRepositoryService;

	Logger logger = LoggerFactory.getLogger("com.pamela.controller.boardMainController");

	private final CompanyEntityRepository companyEntityRepository;
	private final TradeEntityRepository tradeEntityRepository;
	private final ReplyRepository replyRepository;
	private final CountryService countryService;
	private final CountryRepository countryRepository;
	private final CityRepository cityRepository;
	private final InterestRepository interestRepository;
	private final AttractionRepository attractionRepository;
	private final UserRepository userRepository;
	private final UserInterestRepository userInterestRepository;
	private final EntitySearchDtoRepository entitySearchDtoRepository;
	private final BoardEntityRepository boardEntityRepository;
	private final WeatherByGPSApplication weatherByGPSApplication;

	@Autowired
	public MobileService(CompanyEntityRepository companyEntityRepository, TradeEntityRepository tradeEntityRepository,
			ReplyRepository replyRepository, CountryService countryService, CountryRepository countryRepository,
			CityRepository cityRepository, InterestRepository interestRepository,
			AttractionRepository attractionRepository, UserRepository userRepository,
			UserInterestRepository userInterestRepository, EntitySearchDtoRepository entitySearchDtoRepository,
			BoardEntityRepository boardEntityRepository, WeatherByGPSApplication weatherByGPSApplication) {
		super();
		this.companyEntityRepository = companyEntityRepository;
		this.tradeEntityRepository = tradeEntityRepository;
		this.replyRepository = replyRepository;
		this.countryService = countryService;
		this.countryRepository = countryRepository;
		this.cityRepository = cityRepository;
		this.interestRepository = interestRepository;
		this.attractionRepository = attractionRepository;
		this.userRepository = userRepository;
		this.userInterestRepository = userInterestRepository;
		this.entitySearchDtoRepository = entitySearchDtoRepository;
		this.boardEntityRepository = boardEntityRepository;
		this.weatherByGPSApplication = weatherByGPSApplication;
	}

	// 나라 정보 전체 가져오기
	public List<EntityCountryDto> viewCountry() {
		return this.countryRepository.findAll();
	}

	// 관심사 전체 가져오기
	public List<EntityInterestDto> viewinterest() {
		return this.interestRepository.findAll();
	}

	// 도시 정보 전체 가져오기
	public List<EntityCityDto> viewCity() {
		return this.cityRepository.findAll();
	}

	public List<EntitySearchDto> getSearch() {
		return this.entitySearchDtoRepository.findAll();
	}

	// 관광지 전체 가져오기
	public List<EntityTourAttractionDto> viewPlace() {
		return this.attractionRepository.findAll();
	}

	public List<BoardEntity> getBoard() {

		return this.boardEntityRepository.findAll();
	}

	public List<CompanyEntity> getCompanyEntity() {

		return this.companyEntityRepository.findAll();
	}

	public List<TradeEntity> getTrade() {

		return this.tradeEntityRepository.findAll();
	}

	public List<ReplyEntity> getReply() {

		return this.replyRepository.findAll();
	}

	public List<User> userAll() {
		return this.userRepository.findAll();
	}

	public List<UserInterest> userInfoAll() {
		return this.userInterestRepository.findAll();
	}

	public Map<String, String[]> weather(String date, String lan, String lat) {
		List<String> strDate = this.countryService.getDate(date);
		Map<String, String[]> map = weatherByGPSApplication.getWeather(lan, lat);

		return map;
	}

	public void sendPlaceEdit(String name, String inOut) {
		EntitySearchDto dto = this.entitySearchDtoRepository.findById(name).get();
		dto.setInOut(Integer.parseInt(inOut));
		this.entitySearchDtoRepository.save(dto);

	}

	// view Counter = view올려줌 ==============================
	@Transactional
	public void detailMobile(int article_no, String tabtitle) {
		if (tabtitle.equals("리뷰모음")) {
			boardEntityRepository.updateViews(article_no);
		} else if (tabtitle.equals("동행자구인")) {
			companyEntityRepository.updateViews(article_no);
		} else if (tabtitle.equals("거래시스템")) {
			tradeEntityRepository.updateViews(article_no);
		}
	}

	// 댓글 추가 ==============================
	public void ReplyAddMobile(String tabtitle, String article_no, String replycontent, String email) {
		User user = totalRepositoryService.findByType(email);
		BoardEntity q = new BoardEntity();
		q.setArticleNo(Integer.parseInt(article_no));
		logger.info("================>" + article_no);
		ReplyEntity r = new ReplyEntity();
		r.setBoardEntity(q);
		r.setReplycontent(replycontent);
		r.setWriteId(user.getName()); // 유저id 받아서 넣어줘야함
		r.setUser(user);
		this.replyRepository.save(r);
	}

	public void ReplyCompanyAddMobile(String tabtitle, String article_no, String replycontent, String email) {
		User user = totalRepositoryService.findByType(email);
		CompanyEntity q = new CompanyEntity();
		q.setArticleNo(Integer.parseInt(article_no));
		logger.info("================>" + article_no);
		ReplyEntity r = new ReplyEntity();
		r.setCompanyEntity(q);
		r.setReplycontent(replycontent);
		r.setWriteId(user.getName()); // 유저id 받아서 넣어줘야함
		r.setUser(user);
		this.replyRepository.save(r);

	}

	public void ReplyTradeAddMobile(String tabtitle, String article_no, String replycontent, String email) {
		User user = totalRepositoryService.findByType(email);
		TradeEntity q = new TradeEntity();
		q.setArticleNo(Integer.parseInt(article_no));
		logger.info("================>" + article_no);
		ReplyEntity r = new ReplyEntity();
		r.setTradeEntity(q);
		r.setReplycontent(replycontent);
		r.setWriteId(user.getName()); // 유저id 받아서 넣어줘야함
		r.setUser(user);
		this.replyRepository.save(r);
	}

	// 댓글 삭제 ==============================
	public void deleteCommentMobile(String tabtitle, String article_no, int replyNo) {
		Optional<ReplyEntity> oq = this.replyRepository.findById(replyNo);
		ReplyEntity q = oq.get();
		this.replyRepository.delete(q);
	}

	public void deleteCompanyCommentMobile(String tabtitle, String article_no, int replyNo) {
		Optional<ReplyEntity> oq = this.replyRepository.findById(replyNo);
		ReplyEntity q = oq.get();
		this.replyRepository.delete(q);
	}

	public void deleteTradeCommentMobile(String tabtitle, String article_no, int replyNo) {
		Optional<ReplyEntity> oq = this.replyRepository.findById(replyNo);
		ReplyEntity q = oq.get();
		this.replyRepository.delete(q);
	}

	// 게시글 삭제 ==============================
	public void removeArticleMobile(String tabtitle, int articleNo) {
		Optional<BoardEntity> question = this.boardEntityRepository.findById(articleNo);
		this.boardEntityRepository.delete(question.get());
	}

	public void removeCompanyArticleMobile(String tabtitle, int articleNo) {
		Optional<CompanyEntity> question = this.companyEntityRepository.findById(articleNo);
		this.companyEntityRepository.delete(question.get());
	}

	public void removeTradeArticleMobile(String tabtitle, int articleNo) {
		Optional<TradeEntity> question = this.tradeEntityRepository.findById(articleNo);
		this.tradeEntityRepository.delete(question.get());
	}

	// 게시글 추가 ==============================
	public void addBoardArticleMobile(String tabtitle, String title, String content, String email) {
		BoardEntity be = new BoardEntity();
		User user = totalRepositoryService.findByType(email);
		be.setTitle(title);
		be.setContent(content);
		be.setWriteId(user.getName()); // 유저id 받아서 넣어줘야함
		be.setUser(user);
		be.setWriteDate(LocalDateTime.now());
		be.setViews(0);
		this.boardEntityRepository.save(be);
	}

	public void addCompanyArticleMobile(String tabtitle, String title, String content, String email) {
		CompanyEntity be = new CompanyEntity();
		User user = totalRepositoryService.findByType(email);
		be.setTitle(title);
		be.setContent(content);
		be.setWriteId(user.getName()); // 유저id 받아서 넣어줘야함
		be.setUser(user);
		be.setWriteDate(LocalDateTime.now());
		be.setViews(0);
		this.companyEntityRepository.save(be);
	}

	public void addTradeArticleMobile(String tabtitle, String title, String content, String email) {
		TradeEntity be = new TradeEntity();
		User user = totalRepositoryService.findByType(email);
		be.setTitle(title);
		be.setContent(content);
		be.setWriteId(user.getName()); // 유저id 받아서 넣어줘야함
		be.setUser(user);
		be.setWriteDate(LocalDateTime.now());
		be.setViews(0);
		this.tradeEntityRepository.save(be);
	}

	// 유저 회원가입
	public String signIn(String email, String name, String password) {
		String code = "d";
		User user = totalRepositoryService.findByType(email);
		String userEmail = user.getEmail();
		if (userEmail != null && userEmail.equals(email)) {
			code = "b";
		} else {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			user.setEmail(email);
			user.setName(name);
			user.setType("mobile");
			user.setRole("ROLE_USER");
			user.setPicture(
					"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR82ZL5TC9-Q-wVfoEdIBFIMVbZiQf0jqD2VFi-Gy4Q5g&s");
			user.setPassword(passwordEncoder.encode(password));
			userRepository.save(user);
			code = "a";
		}
		return code;
	}

	// 유저 회원 가입 후 유저 선호도 설정
	public boolean addPreference(JSONObject sendInterest) {

		String email = (String) sendInterest.get("email");
		String sights = (String) sendInterest.get("sights");
		String nature = (String) sendInterest.get("nature");
		String culture = (String) sendInterest.get("culture");
		String history = (String) sendInterest.get("history");
		String food = (String) sendInterest.get("food");
		String religion = (String) sendInterest.get("religion");

		try {
			User user = totalRepositoryService.findByType(email);
			UserInterest userInterest = new UserInterest();
			userInterest.setUser(user);
			userInterest.setHistory(Integer.valueOf(history));
			userInterest.setSights(Integer.valueOf(sights));
			userInterest.setCulture(Integer.valueOf(culture));
			userInterest.setFood(Integer.valueOf(food));
			userInterest.setNature(Integer.valueOf(nature));
			userInterest.setReligion(Integer.valueOf(religion));
			userInterest.setReliability(0);
			userInterestRepository.save(userInterest);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// BoardListMobile
	public JSONObject BoardListMobile(String kw, int page, String type, String email) throws Exception {

		boolean bo = email.equals("");

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("created_at"));
		Pageable pageable = PageRequest.of(page, 30, Sort.by(sorts));
		Page tmp = this.boardEntityRepository.findAllBytitleKeyword(kw, pageable);

		if (!bo) {
			// 리스트가 descending, 즉 최신순으로 안나오는 문제 해결해야함
			User userOne = totalRepositoryService.findByType(email);
			List<BoardEntity> boardEntityList = this.boardEntityRepository.findByuser(userOne);
			int start = (int) pageable.getOffset();
			int end = Math.min((start + pageable.getPageSize()), boardEntityList.size());
			tmp = new PageImpl<>(boardEntityList.subList(start, end), pageable, boardEntityList.size());
		}

		if (type.equals("content")) {
			tmp = this.boardEntityRepository.findAllBytitleKeyword(kw, pageable);
		} else if (type.equals("writeuser")) { // 유저 아이디로 바꿔주고 비교해서 유저 아이디와 일치하는 것을 검색
			tmp = this.boardEntityRepository.findAllBytitleKeyword(kw, pageable);
		} else if (type.equals("title")) {
			tmp = this.boardEntityRepository.findAllBytitleKeyword(kw, pageable);
		}

		JSONObject jSONObject = new JSONObject();
		jSONObject.put("pages", tmp.getTotalPages());

		JSONArray jArray = objectMapper.convertValue(tmp.toList(), JSONArray.class);
		jSONObject.put("list", jArray);

		Map<String, Integer> map = new LinkedHashMap<>();
		JSONArray replyCounter = new JSONArray();
		for (int i = 0; i < tmp.toList().size(); i++) {
			logger.info(1 + "");
			Object getReOb = jArray.get(i);
			BoardEntity b = objectMapper.convertValue(getReOb, BoardEntity.class);
			logger.info(getReOb.toString());
			JSONObject rOb = new JSONObject();
			int aNo = b.getArticleNo();
			logger.info(2 + "");
			rOb.put("articleNo", aNo);
			rOb.put("replyCount", this.boardEntityRepository.findById(aNo).get().getBoardList().size());

			logger.info("map.toString()===>" + map.toString());
			replyCounter.add(rOb);
		}
		jSONObject.put("replyCount", replyCounter);

		return jSONObject;
	}

	// CompanyListMobile
	public JSONObject CompanyListMobile(String kw, int page, String type, String email) throws Exception {

		boolean bo = email.equals("");

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("created_at"));
		Pageable pageable = PageRequest.of(page, 30, Sort.by(sorts));
		Page tmp = this.companyEntityRepository.findAllBytitleKeyword(kw, pageable);

		if (!bo) {
			// 리스트가 descending, 즉 최신순으로 안나오는 문제 해결해야함
			User userOne = totalRepositoryService.findByType(email);
			List<CompanyEntity> companyEntityList = this.companyEntityRepository.findByuser(userOne);
			int start = (int) pageable.getOffset();
			int end = Math.min((start + pageable.getPageSize()), companyEntityList.size());
			tmp = new PageImpl<>(companyEntityList.subList(start, end), pageable, companyEntityList.size());
		}

		if (type.equals("content")) {
			tmp = this.companyEntityRepository.findAllBytitleKeyword(kw, pageable);
		} else if (type.equals("writeuser")) { // 유저 아이디로 바꿔주고 비교해서 유저 아이디와 일치하는 것을 검색
			tmp = this.companyEntityRepository.findAllBytitleKeyword(kw, pageable);
		} else if (type.equals("title")) {
			tmp = this.companyEntityRepository.findAllBytitleKeyword(kw, pageable);
		}

		JSONObject jSONObject = new JSONObject();
		jSONObject.put("pages", tmp.getTotalPages());

		JSONArray jArray = objectMapper.convertValue(tmp.toList(), JSONArray.class);
		jSONObject.put("list", jArray);

		Map<String, Integer> map = new LinkedHashMap<>();
		JSONArray replyCounter = new JSONArray();
		for (int i = 0; i < tmp.toList().size(); i++) {
			logger.info(1 + "");
			Object getReOb = jArray.get(i);
			BoardEntity b = objectMapper.convertValue(getReOb, BoardEntity.class);
			logger.info(getReOb.toString());
			JSONObject rOb = new JSONObject();
			int aNo = b.getArticleNo();
			logger.info(2 + "");
			rOb.put("articleNo", aNo);
			rOb.put("replyCount", this.companyEntityRepository.findById(aNo).get().getBoardList().size());

			logger.info("map.toString()===>" + map.toString());
			replyCounter.add(rOb);
		}
		jSONObject.put("replyCount", replyCounter);

		return jSONObject;
	}

	// TradeListMobile
	public JSONObject TradeListMobile(String kw, int page, String type, String email) throws Exception {

		boolean bo = email.equals("");

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("created_at"));
		Pageable pageable = PageRequest.of(page, 30, Sort.by(sorts));
		Page tmp = this.tradeEntityRepository.findAllBytitleKeyword(kw, pageable);

		if (!bo) {
			// 리스트가 descending, 즉 최신순으로 안나오는 문제 해결해야함
			User userOne = totalRepositoryService.findByType(email);
			List<TradeEntity> tradeEntityList = this.tradeEntityRepository.findByuser(userOne);
			int start = (int) pageable.getOffset();
			int end = Math.min((start + pageable.getPageSize()), tradeEntityList.size());
			tmp = new PageImpl<>(tradeEntityList.subList(start, end), pageable, tradeEntityList.size());
		}

		if (type.equals("content")) {
			tmp = this.tradeEntityRepository.findAllBytitleKeyword(kw, pageable);
		} else if (type.equals("writeuser")) { // 유저 아이디로 바꿔주고 비교해서 유저 아이디와 일치하는 것을 검색
			tmp = this.tradeEntityRepository.findAllBytitleKeyword(kw, pageable);
		} else if (type.equals("title")) {
			tmp = this.tradeEntityRepository.findAllBytitleKeyword(kw, pageable);
		}

		JSONObject jSONObject = new JSONObject();
		jSONObject.put("pages", tmp.getTotalPages());

		JSONArray jArray = objectMapper.convertValue(tmp.toList(), JSONArray.class);
		jSONObject.put("list", jArray);

		Map<String, Integer> map = new LinkedHashMap<>();
		JSONArray replyCounter = new JSONArray();
		for (int i = 0; i < tmp.toList().size(); i++) {
			logger.info(1 + "");
			Object getReOb = jArray.get(i);
			BoardEntity b = objectMapper.convertValue(getReOb, BoardEntity.class);
			logger.info(getReOb.toString());
			JSONObject rOb = new JSONObject();
			int aNo = b.getArticleNo();
			logger.info(2 + "");
			rOb.put("articleNo", aNo);
			rOb.put("replyCount", this.tradeEntityRepository.findById(aNo).get().getBoardList().size());

			logger.info("map.toString()===>" + map.toString());
			replyCounter.add(rOb);
		}
		jSONObject.put("replyCount", replyCounter);

		return jSONObject;
	}

	// ReplyListMobile
	public List<ReplyEntity> ReplyListMobile(String tabtitle, String articleNo) throws Exception {

		if (tabtitle.equals("리뷰모음")) {
			BoardEntity entity = new BoardEntity();
			entity.setArticleNo(Integer.parseInt(articleNo));
			List<ReplyEntity> question = this.replyRepository.findByboardEntity(entity);

			return question;
		} else if (tabtitle.equals("동행자구인")) {
			CompanyEntity entity = new CompanyEntity();
			entity.setArticleNo(Integer.parseInt(articleNo));
			List<ReplyEntity> question = this.replyRepository.findBycompanyEntity(entity);

			return question;
		} else if (tabtitle.equals("거래시스템")) {
			TradeEntity entity = new TradeEntity();
			entity.setArticleNo(Integer.parseInt(articleNo));
			List<ReplyEntity> question = this.replyRepository.findByTradeEntity(entity);

			return question;
		}
		return null;
	}

	// ==================== Working ====================

	// UserInterestListMobile
	public Optional<UserInterest> InterestListMobile(String email) throws Exception {
		User userOne = totalRepositoryService.findByType(email);
		return userInterestRepository.findByuser(userOne);
	}

}