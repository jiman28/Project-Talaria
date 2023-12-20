package com.noelwon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpSession;

@Controller
public interface BoardController {
	public String getArticleList(Model model, 
			@RequestParam(value = "kw", defaultValue ="") String kw,
			@RequestParam(value = "page",defaultValue = "0")int page,
			@RequestParam(value = "types",defaultValue = "")String type,
			@RequestParam(value = "tab_title",defaultValue = "리뷰모음")String tabtitle
				) throws Exception;
	
	
	public String writeArticle(@RequestParam(value = "tabtitle") String tabtitle, Model model)throws Exception;
	
	public String addArticle(
			@RequestParam(value = "tabtitle") String tabtitle,
			@RequestParam(value = "i_title") String title,
			@RequestParam(value = "i_content") String content
			)throws Exception;
	
	public String viewArticle(Model model,
			@RequestParam(value = "tabtitle")String tabtitle,
			@RequestParam(value = "no") String articleNo,
			HttpSession session,
			@RequestParam(value = "page",defaultValue = "0")int page)throws Exception;
			

	
	public String editArticle(
			@RequestParam(value = "tabtitle")String tabtitle,
			@RequestParam(value = "articleNo") String articleNo,
			@RequestParam(value = "title") String title, 
			@RequestParam(value = "i_content") String content,
			RedirectAttributes redirectAttr)throws Exception;

	
	public String pullUpArticle(@RequestParam(value = "articleNo")String articleNo,
								@RequestParam(value = "tabtitle") String tabtitle) throws Exception;
	
	public String deleteArticle(
			@RequestParam(value = "tabtitle") String tabtitle,
			@RequestParam(value = "article_no") String articleNo)throws Exception;
	
	public String addReply(
			@RequestParam(value = "tabtitle") String tabtitle,
			@RequestParam(value = "article_no") String articleNo, 
			@RequestParam(value = "reply_text") String replycontent,
			HttpSession session,
			RedirectAttributes redirectAttr) throws Exception;
	
	public String deleteReply(
			@RequestParam(value = "tabtitle") String tabtitle,
			@RequestParam(value = "reply_id") String replyNo,
			@RequestParam(value = "article_no") String articleNo,
			HttpSession session,
			RedirectAttributes redirectAttr) throws Exception;
	
	
	
	public JsonObject uploadSummernoteImageFile(
			@RequestParam(value = "file") MultipartFile multipartFile) throws Exception;
	
}
