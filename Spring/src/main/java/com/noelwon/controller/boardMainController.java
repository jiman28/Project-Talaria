package com.noelwon.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.JsonObject;
import com.noelwon.Security.SessionUser;
import com.noelwon.model.ccc.BoardEntity;
import com.noelwon.model.ccc.BoardEntityRepository;
import com.noelwon.model.ccc.CompanyEntity;
import com.noelwon.model.ccc.CompanyEntityRepository;
import com.noelwon.model.ccc.ReplyEntity;
import com.noelwon.model.ccc.ReplyRepository;
import com.noelwon.model.ccc.TradeEntity;
import com.noelwon.model.ccc.TradeEntityRepository;
import com.noelwon.model.userDto.User;
import com.noelwon.service.BoardService;
import com.noelwon.service.UserService;
import com.noelwon.service.totalService.TotalRepositoryService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/board")
public class boardMainController implements BoardController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private BoardService boardService;
	BoardController boardController;
	@Autowired
	private BoardEntityRepository boardEntityRepository;
	@Autowired
	private CompanyEntityRepository companyEntityRepository;
	@Autowired
	private TradeEntityRepository tradeEntityRepository;
	ReplyRepository replyRepository;
	BoardEntity boardEntity;
	CompanyEntity CompanyEntity;
	TradeEntity TradeEntity;
	User user;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private TotalRepositoryService totalRepositoryService;

	Logger logger = LoggerFactory.getLogger("========================>");
	
	
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

	@RequestMapping("/list")
	@Override
	public String getArticleList(Model model, String kw, int page, String type, String tabtitle) throws Exception {
		model = checkPermissions(model);
		Page<BoardEntity> boardEntityList = this.boardService.getArticleList(kw, page, type);
		Page<CompanyEntity> companyEntityList = this.boardService.CArticleList(kw, page, type);
		Page<TradeEntity> tradeEntityList = this.boardService.TradeArticleList(kw, page, type);
		logger.info("tabtitle===>" + tabtitle);
		logger.info("getArticleList: =============>");
		model.addAttribute("boardEntityList", boardEntityList);
		model.addAttribute("companyEntityList", companyEntityList);
		model.addAttribute("tradeEntityList", tradeEntityList);
		model.addAttribute("kw", kw);
		model.addAttribute("type", type);
		model.addAttribute("page", page);
		model.addAttribute("tabtitle", tabtitle);
		logger.info("" + page);
		return "list";
	}
	
	

	

	@Override
	@Transactional
	@RequestMapping("/view")
	public String viewArticle(Model model, String tabtitle, String articleNo, HttpSession session, int page) throws Exception {
//		List<ReplyEntity> questions = this.boardService.ReplyList(tabtitle, articleNo);
//		model.addAttribute("comments", questions);
		model = checkPermissions(model);
		SessionUser userInfo = (SessionUser) session.getAttribute("user");
		User user = totalRepositoryService.findByType(userInfo.getEmail());
		model.addAttribute("userNo", user.getId());
		model.addAttribute("username",user.getName());
		model.addAttribute(user.getPicture());
		List<ReplyEntity> questions = this.boardService.ReplyList(tabtitle, articleNo);
		model.addAttribute("comments", questions);
		
		if (tabtitle.equals("리뷰모음")) {
			model.addAttribute("user", session.getId());
			BoardEntity question1 = this.boardService.getview(tabtitle, Integer.parseInt(articleNo),page);
			model.addAttribute("article", question1);
			if(question1.getUser().getId() == user.getId()) {
				model.addAttribute("articleNoBool", true);
			}else {
				model.addAttribute("articleNoBool", false);
			}
			
			
			

		} else if (tabtitle.equals("동행자구인")) {
			model.addAttribute("user", session.getId());
			CompanyEntity question1 = this.boardService.getCompanyView(tabtitle, Integer.parseInt(articleNo),page);
			model.addAttribute("article", question1);
			if(question1.getUser().getId() == user.getId()) {
				model.addAttribute("articleNoBool", true);
			}else {
				model.addAttribute("articleNoBool", false);
			}

		} else if (tabtitle.equals("거래시스템")) {
			model.addAttribute("user", session.getId());
			TradeEntity question1 = this.boardService.getTradeView(tabtitle, Integer.parseInt(articleNo),page);
			model.addAttribute("article", question1);
			if(question1.getUser().getId() == user.getId()) {
				model.addAttribute("articleNoBool", true);
			}else {
				model.addAttribute("articleNoBool", false);
			}

		}

		model.addAttribute("tab", tabtitle);
		model.addAttribute("page", page);

		return "view";
	}

	@PostMapping("/pull-up")
	@Override
	public String pullUpArticle(String articleNo,String tabtitle) {
		if(tabtitle.equals("리뷰모음")) {
			BoardEntity boardEntity = this.boardEntityRepository.findById(Integer.parseInt(articleNo)).get();
			boardEntity.setWriteDate(LocalDateTime.now());
			boardEntityRepository.save(boardEntity);
		}else if (tabtitle.equals("동행자구인")) {
			CompanyEntity companyEntity = this.companyEntityRepository.findById(Integer.parseInt(articleNo)).get();
			companyEntity.setWriteDate(LocalDateTime.now());
			companyEntityRepository.save(companyEntity);
		}else if (tabtitle.equals("거래시스템")) {
			TradeEntity tradeEntity = this.tradeEntityRepository.findById(Integer.parseInt(articleNo)).get();
			tradeEntity.setWriteDate(LocalDateTime.now());
			tradeEntityRepository.save(tradeEntity);
		}
		
	    return "redirect:list";
	}
	
	
	@RequestMapping("/add")
	@Override
	public String writeArticle(String tabtitle, Model model) throws Exception {
		model = checkPermissions(model);
		model.addAttribute("tab", tabtitle);
		return "write";
	}

	@RequestMapping("/addArticle")
	@Override
	public String addArticle(String tabtitle, String title, String content) throws Exception {
		if (tabtitle.equals("리뷰모음")) {
			boardService.addArticle(tabtitle, title, content,session);
		} else if (tabtitle.equals("동행자구인")) {
			boardService.addCompanyArticle(tabtitle, title, content,session);
		} else if (tabtitle.equals("거래시스템")) {
			boardService.addTradeArticle(tabtitle, title, content,session);
		}

		return "redirect:list";
	}

	@Override
	@RequestMapping("/edit")
	public String editArticle(String tabtitle, String article_no, String title, String content,
			RedirectAttributes redirectAttr) throws Exception {
		if (tabtitle.equals("리뷰모음")) {
			boardService.editArticle(article_no, title, content);
		} else if (tabtitle.equals("동행자구인")) {
			boardService.editCompanyArticle(article_no, title, content);
		} else if (tabtitle.equals("거래시스템")) {
			boardService.editTradeArticle(article_no, title, content);
		}
		redirectAttr.addAttribute("no", article_no);
		redirectAttr.addAttribute("tabtitle", tabtitle);

		return "redirect:view";

	}

	@Override
	@RequestMapping("/remove")
	public String deleteArticle(String tabtitle, String articleNo) throws Exception {
		if (tabtitle.equals("리뷰모음")) {
			boardService.removeArticle(tabtitle, Integer.parseInt(articleNo));
		} else if (tabtitle.equals("동행자구인")) {
			boardService.removeCompanyArticle(tabtitle, Integer.parseInt(articleNo));
		} else if (tabtitle.equals("거래시스템")) {
			boardService.removeTradeArticle(tabtitle, Integer.parseInt(articleNo));
		}

		return "redirect:list";
	}

	@RequestMapping("/addreply")
	@Override
	public String addReply(String tabtitle, String article_no, String replycontent, HttpSession session,
			RedirectAttributes redirectAttr) throws Exception {
		if (tabtitle.equals("리뷰모음")) {
			boardService.ReplyAdd(tabtitle, article_no, replycontent, session);
			redirectAttr.addAttribute("no", article_no);
			redirectAttr.addAttribute("tabtitle", tabtitle);
		} else if (tabtitle.equals("동행자구인")) {
			boardService.ReplyCompanyAdd(tabtitle, article_no, replycontent, session);
			redirectAttr.addAttribute("no", article_no);
			redirectAttr.addAttribute("tabtitle", tabtitle);
		} else if (tabtitle.equals("거래시스템")) {
			boardService.ReplyTradeAdd(tabtitle, article_no, replycontent, session);
			redirectAttr.addAttribute("no", article_no);
			redirectAttr.addAttribute("tabtitle", tabtitle);
		}
		return "redirect:view";
	}

	@RequestMapping("/removereply")
	@Override
	public String deleteReply(String tabtitle, String replyNo, String article_no, HttpSession session,RedirectAttributes redirectAttr)
			throws Exception {
		if (tabtitle.equals("리뷰모음")) {
			redirectAttr.addAttribute("user", session.getId());
			boardService.deleteComment(tabtitle,article_no,Integer.parseInt(replyNo));
			redirectAttr.addAttribute("no", article_no);
			redirectAttr.addAttribute("tabtitle", tabtitle);
		} else if (tabtitle.equals("동행자구인")) {
			redirectAttr.addAttribute("user", session.getId());
			boardService.deleteCompanyComment(tabtitle,article_no,Integer.parseInt(replyNo));
			redirectAttr.addAttribute("no", article_no);
			redirectAttr.addAttribute("tabtitle", tabtitle);
		} else if (tabtitle.equals("거래시스템")) {
			redirectAttr.addAttribute("user", session.getId());
			boardService.deleteTradeComment(tabtitle,article_no,Integer.parseInt(replyNo));
			redirectAttr.addAttribute("no", article_no);
			redirectAttr.addAttribute("tabtitle", tabtitle);
		}
		return "redirect:view";
	}
	
	@RequestMapping(value="/uploadSummernoteImageFile", produces = "application/json")
	@ResponseBody
	public JsonObject uploadSummernoteImageFile(MultipartFile multipartFile) {
//		String tabtitle,
		logger.info("uploadSummernoteImageFile 시작");
		
		
		JsonObject jsonObject = new JsonObject();
		
		String fileRoot = "/home/ubuntu/trevel/summernote_image/";	//저장될 외부 파일 경로
		String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자
		String savedFileName = UUID.randomUUID() + extension;	//저장될 파일 명
		File targetFile = new File(fileRoot + savedFileName);	
		
		try {
			
			logger.info("uploadSummernoteImageFile try  시작");
			
			
			InputStream fileStream = multipartFile.getInputStream();
			FileUtils.copyInputStreamToFile(fileStream, targetFile);	//파일 저장
			jsonObject.addProperty("url", "/summernoteImage/"+savedFileName); // url 과 파일이름으로 경로 생성
			jsonObject.addProperty("responseCode", "success"); // responseCode success  반환
				
		} catch (IOException e) {
			
			logger.info("uploadSummernoteImageFile catch  시작");
			
			
			FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
			jsonObject.addProperty("responseCode", "error");
			e.printStackTrace();
		}
		
		
		logger.info("uploadSummernoteImageFile return 전");
		
		logger.info(jsonObject.toString());
		
		return jsonObject;
	}

}
