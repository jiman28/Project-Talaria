package com.noelwon.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import jakarta.servlet.http.HttpSession;

@Controller
public interface Webcontroller {

	String goEnterPage(Model model, Principal principal) throws Exception;

	public String addInfo(Model model, @RequestParam(value = "userInterest") String preference, HttpSession session)
			throws Exception;

	public String mypage(Model model, HttpSession session, @RequestParam(value = "page", defaultValue = "0") int page)
			throws Exception;

	public String youpage(Model model, @RequestParam(value = "youEmail") String email,
			@RequestParam(value = "page", defaultValue = "0") int page) throws Exception;

	String goSelectPage(Model model, @RequestParam(value = "selectedCountry") String countryName,
			@RequestParam(value = "date") String date) throws Exception;

	public String basket(Model model, @RequestParam(value = "placeId", required = false) String placeId,
			@RequestParam(value = "date") String date,
			@RequestParam(value = "foundNames", required = false) String finds) throws Exception;

	String popup(Model model) throws Exception;

	Map<String, String> find(@RequestPart(value = "uploadName") String searchName,
			@RequestPart(value = "radioTxt") String inOut, @RequestPart(value = "cityForplace") String cityId)
			throws Exception;

	public String addPlan(@RequestParam(value = "planName", required = false) String planName,
			@RequestParam(value = "days") String date, @RequestParam(value = "placeId") String plans,
			@RequestParam(value = "num") String num, HttpSession session) throws Exception;

	public String remove(@RequestParam(value = "_id") String id) throws Exception;

	public String planEdit(@RequestParam(value = "_id") String id, Model model) throws Exception;

	public String login() throws Exception;
	
	public String PlanAditDb(@RequestParam(value = "planName", required = false) String planName,
			@RequestParam(value = "days") String date, @RequestParam(value = "placeId") String plans,
			@RequestParam(value = "num") String num,@RequestParam(value = "planId") String id)
			throws Exception;
}
