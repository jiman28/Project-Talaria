package com.noelwon.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.noelwon.Security.SessionUser;
import com.noelwon.model.ccc.BoardAll;
import com.noelwon.model.ccc.BoardEntity;
import com.noelwon.model.ccc.BoardEntityRepository;
import com.noelwon.model.ccc.CompanyEntity;
import com.noelwon.model.ccc.CompanyEntityRepository;
import com.noelwon.model.ccc.ReplyEntity;
import com.noelwon.model.ccc.ReplyRepository;
import com.noelwon.model.ccc.TradeEntity;
import com.noelwon.model.ccc.TradeEntityRepository;
import com.noelwon.model.userDto.User;
import com.noelwon.service.totalService.TotalRepositoryService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
public class BoardService{


	private final UserService userService;
	private final TotalRepositoryService totalRepositoryService;
	private final BoardEntityRepository boardEntityRepository;
	private final CompanyEntityRepository companyEntityRepository;
	private final TradeEntityRepository tradeEntityRepository;
	
	@Autowired
	private ReplyRepository replyRepository;
	

	@Autowired
	public BoardService(UserService userService, TotalRepositoryService totalRepositoryService,
			BoardEntityRepository boardEntityRepository, CompanyEntityRepository companyEntityRepository,
			TradeEntityRepository tradeEntityRepository) {
		super();
		this.userService = userService;
		this.totalRepositoryService = totalRepositoryService;
		this.boardEntityRepository = boardEntityRepository;
		this.companyEntityRepository = companyEntityRepository;
		this.tradeEntityRepository = tradeEntityRepository;
	}

	Logger logger = LoggerFactory.getLogger("com.pamela.controller.boardMainController");



	public Page<BoardEntity> getArticleList(String kw, int page, String type) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("created_at"));
		Pageable pageable = PageRequest.of(page, 25, Sort.by(sorts));
		Page<BoardEntity> all = this.boardEntityRepository.findAllBytitleKeyword(kw, pageable);

		if (type.equals("content")) {
			all = this.boardEntityRepository.findAllBycontentKeyword(kw, pageable);
		} else if (type.equals("writeuser")) { // 유저 아이디로 바꿔주고 비교해서 유저 아이디와 일치하는 것을 검색
			all = this.boardEntityRepository.findAllBywriterKeyword(kw, pageable);
		} else if (type.equals("title")) {
			all = this.boardEntityRepository.findAllBytitleKeyword(kw, pageable);
		}
		
			

		return all;
	}
	

	public Page<CompanyEntity> CArticleList(String kw, int page, String type) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("created_at"));
		Pageable pageable = PageRequest.of(page, 25, Sort.by(sorts));
		Page<CompanyEntity> all = this.companyEntityRepository.findAllBytitleKeyword(kw, pageable);

		if (type.equals("content")) {
			all = this.companyEntityRepository.findAllBycontentKeyword(kw, pageable);
		} else if (type.equals("writeuser")) { // 유저 아이디로 바꿔주고 비교해서 유저 아이디와 일치하는 것을 검색
			all = this.companyEntityRepository.findAllBywriterKeyword(kw, pageable);
		} else if (type.equals("title")) {
			all = this.companyEntityRepository.findAllBytitleKeyword(kw, pageable);
		}

		return all;
	}

	
	public Page<TradeEntity> TradeArticleList(String kw, int page, String type) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("created_at"));
		Pageable pageable = PageRequest.of(page, 25, Sort.by(sorts));
		Page<TradeEntity> all = this.tradeEntityRepository.findAllBytitleKeyword(kw, pageable);

		if (type.equals("content")) {
			all = this.tradeEntityRepository.findAllBycontentKeyword(kw, pageable);
		} else if (type.equals("writeuser")) { // 유저 아이디로 바꿔주고 비교해서 유저 아이디와 일치하는 것을 검색
			all = this.tradeEntityRepository.findAllBywriterKeyword(kw, pageable);
		} else if (type.equals("title")) {
			all = this.tradeEntityRepository.findAllBytitleKeyword(kw, pageable);
		}

		return all;
	}
	
	public Page<BoardAll> findByUserId(User userOne,int page) throws Exception {
		User user = userOne;
		logger.info(user.toString());
		List<BoardEntity> boardEntityList = this.totalRepositoryService.findByType(user,BoardEntity.class,"리뷰");
		List<CompanyEntity> companyEntity = this.totalRepositoryService.findByType(user,CompanyEntity.class,"동행");
		List<TradeEntity> tradeEntityList = this.totalRepositoryService.findByType(user,TradeEntity.class,"거래");
		
		List<BoardAll> BoardAllList = new ArrayList<>();
		
		BoardAllList = dtoToBoardAll(boardEntityList ,BoardAllList,"리뷰모음");
		BoardAllList = dtoToBoardAll(companyEntity ,BoardAllList,"동행자구인");
		BoardAllList = dtoToBoardAll(tradeEntityList ,BoardAllList,"거래시스템");
		
		logger.info("BoardAllList.toString()=====>" + BoardAllList.toString());
		logger.info("BoardAllList.size()=====>" + BoardAllList.size());
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("created_at"));
		PageRequest pageRequest = PageRequest.of(page, 30, Sort.by(sorts));
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), BoardAllList.size());
		Page<BoardAll> BoardAllListPage = new PageImpl<>(BoardAllList.subList(start, end), pageRequest, BoardAllList.size());
		
		return BoardAllListPage;
	}
	
	private <T> List<BoardAll> dtoToBoardAll(List<T> list, List<BoardAll> BoardAllList,String tabtitle)  throws Exception {
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule()); // JavaTimeModule 등록
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		for (T tmp : list) {
			String json = objectMapper.writeValueAsString(tmp);
			logger.info("json=====>" + json);
			BoardAll BoardList = objectMapper.readValue(json, BoardAll.class);
			BoardList.setType(tabtitle);
			BoardAllList.add(BoardList);
		}
		
		
		return BoardAllList;
	}


	public void addArticle(String tabtitle, String title, String content, HttpSession session) {
		BoardEntity be = new BoardEntity();
		SessionUser userInfo = (SessionUser) session.getAttribute("user");
		User user = totalRepositoryService.findByType(userInfo.getEmail());

		be.setTitle(title);
		be.setContent(content);
		be.setWriteId(user.getName()); // 유저id 받아서 넣어줘야함
		be.setUser(user);
		be.setWriteDate(LocalDateTime.now());
		be.setViews(0);
		this.totalRepositoryService.save(be);
	}
	
	
	public void addCompanyArticle(String tabtitle, String title, String content, HttpSession session) {
		CompanyEntity be = new CompanyEntity();
		SessionUser userInfo = (SessionUser) session.getAttribute("user");
		User user = totalRepositoryService.findByType(userInfo.getEmail());

		be.setTitle(title);
		be.setContent(content);
		be.setWriteId(user.getName()); // 유저id 받아서 넣어줘야함
		be.setUser(user);
		be.setWriteDate(LocalDateTime.now());
		be.setViews(0);
		this.totalRepositoryService.save(be);
	}

	public void addTradeArticle(String tabtitle, String title, String content, HttpSession session) {
		TradeEntity be = new TradeEntity();
		SessionUser userInfo = (SessionUser) session.getAttribute("user");
		User user = totalRepositoryService.findByType(userInfo.getEmail());
		be.setTitle(title);
		be.setContent(content);
		be.setWriteId(user.getName()); // 유저id 받아서 넣어줘야함
		be.setUser(user);
		be.setWriteDate(LocalDateTime.now());
		be.setViews(0);
		this.totalRepositoryService.save(be);
	}

	@Transactional
	public void detail(int article_no, String tabtitle, int page) {
		if (tabtitle.equals("리뷰모음")) {
			boardEntityRepository.updateViews(article_no);
		} else if (tabtitle.equals("동행자구인")) {
			companyEntityRepository.updateViews(article_no);
		} else if (tabtitle.equals("거래시스템")) {
			tradeEntityRepository.updateViews(article_no);
		}

	}

	public BoardEntity getview(String tabtitle, Integer articleNo, int page) {
		detail(articleNo, tabtitle,page);
		Optional<BoardEntity> question = this.boardEntityRepository.findById(articleNo);
		return question.get();

	}

	public CompanyEntity getCompanyView(String tabtitle, Integer articleNo,int page ) {
		detail(articleNo, tabtitle,page);
		Optional<CompanyEntity> question = this.companyEntityRepository.findById(articleNo);
		return question.get();

	}

	public TradeEntity getTradeView(String tabtitle, Integer articleNo,int page) {
		detail(articleNo, tabtitle,page);
		Optional<TradeEntity> question = this.tradeEntityRepository.findById(articleNo);
		return question.get();

	}
	

	public void removeArticle(String tabtitle, int articleNo) {
		Optional<BoardEntity> question = this.boardEntityRepository.findById(articleNo);
		this.boardEntityRepository.delete(question.get());

	}

	public void removeCompanyArticle(String tabtitle, int articleNo) {
		Optional<CompanyEntity> question = this.companyEntityRepository.findById(articleNo);
		this.companyEntityRepository.delete(question.get());

	}

	public void removeTradeArticle(String tabtitle, int articleNo) {
		Optional<TradeEntity> question = this.tradeEntityRepository.findById(articleNo);
		this.tradeEntityRepository.delete(question.get());

	}

	public void editArticle(String article_no, String title, String content) {
		Optional<BoardEntity> question = this.boardEntityRepository.findById(Integer.parseInt(article_no));
		BoardEntity q = question.get();
		q.setTitle(title);
		q.setContent(content);
		this.boardEntityRepository.save(q);
	}

	public void editCompanyArticle(String article_no, String title, String content) {
		Optional<CompanyEntity> question = this.companyEntityRepository.findById(Integer.parseInt(article_no));
		CompanyEntity q = question.get();
		q.setTitle(title);
		q.setContent(content);
		this.companyEntityRepository.save(q);
	}

	public void editTradeArticle(String article_no, String title, String content) {
		Optional<TradeEntity> question = this.tradeEntityRepository.findById(Integer.parseInt(article_no));
		TradeEntity q = question.get();
		q.setTitle(title);
		q.setContent(content);
		this.tradeEntityRepository.save(q);
	}

	public List<ReplyEntity> ReplyList(String tabtitle, String article_no) {

		if (tabtitle.equals("리뷰모음")) {
			BoardEntity entity = new BoardEntity();
			entity.setArticleNo(Integer.parseInt(article_no));
			List<ReplyEntity> question = this.replyRepository.findByboardEntity(entity);
			return question;
		} else if (tabtitle.equals("동행자구인")) {
			CompanyEntity entity = new CompanyEntity();
			entity.setArticleNo(Integer.parseInt(article_no));
			List<ReplyEntity> question = this.replyRepository.findBycompanyEntity(entity);
			return question;
		} else if (tabtitle.equals("거래시스템")) {
			TradeEntity entity = new TradeEntity();
			entity.setArticleNo(Integer.parseInt(article_no));
			List<ReplyEntity> question = this.replyRepository.findByTradeEntity(entity);
			return question;
		}
		return null;
	}

	public void ReplyCompanyAdd(String tabtitle, String article_no, String replycontent, HttpSession session) {
		SessionUser userInfo = (SessionUser) session.getAttribute("user");
		User user = totalRepositoryService.findByType(userInfo.getEmail());
		CompanyEntity q = new CompanyEntity();
		q.setArticleNo(Integer.parseInt(article_no));
		logger.info("================>" + article_no);
		ReplyEntity r = new ReplyEntity();
		r.setCompanyEntity(q);
		r.setReplycontent(replycontent);
		r.setWriteId(user.getName()); // 유저id 받아서 넣어줘야함
		r.setUser(user);
		this.totalRepositoryService.save(r);

	}

	public void ReplyTradeAdd(String tabtitle, String article_no, String replycontent, HttpSession session) {
		SessionUser userInfo = (SessionUser) session.getAttribute("user");
		User user = totalRepositoryService.findByType(userInfo.getEmail());
		TradeEntity q = new TradeEntity();
		q.setArticleNo(Integer.parseInt(article_no));
		logger.info("================>" + article_no);
		ReplyEntity r = new ReplyEntity();
		r.setTradeEntity(q);
		r.setReplycontent(replycontent);
		r.setWriteId(user.getName()); // 유저id 받아서 넣어줘야함
		r.setUser(user);
		this.totalRepositoryService.save(r);

	}

	public void ReplyAdd(String tabtitle, String article_no, String replycontent, HttpSession session) {
		SessionUser userInfo = (SessionUser) session.getAttribute("user");
		User user = totalRepositoryService.findByType(userInfo.getEmail());
		BoardEntity q = new BoardEntity();
		q.setArticleNo(Integer.parseInt(article_no));
		logger.info("================>" + article_no);
		ReplyEntity r = new ReplyEntity();
		r.setBoardEntity(q);
		r.setReplycontent(replycontent);
		r.setWriteId(user.getName()); // 유저id 받아서 넣어줘야함
		r.setUser(user);
		this.totalRepositoryService.save(r);

	}

	public void deleteComment(String tabtitle, String article_no, int replyNo) {
		ReplyEntity q = this.totalRepositoryService.findById(replyNo, Arrays.asList(replyRepository),ReplyEntity.class);
		this.totalRepositoryService.delete(q);
	}

	public void deleteCompanyComment(String tabtitle, String article_no, int replyNo) {
		Optional<ReplyEntity> oq = this.replyRepository.findById(replyNo);
		ReplyEntity q = oq.get();
		this.replyRepository.delete(q);
	}

	public void deleteTradeComment(String tabtitle, String article_no, int replyNo) {
		Optional<ReplyEntity> oq = this.replyRepository.findById(replyNo);
		ReplyEntity q = oq.get();
		this.replyRepository.delete(q);
	}

}
