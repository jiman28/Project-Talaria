package com.noelwon.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.noelwon.model.ccc.BoardEntity;
import com.noelwon.model.ccc.CompanyEntity;
import com.noelwon.model.ccc.ReplyEntity;
import com.noelwon.model.ccc.TradeEntity;
import com.noelwon.model.spot.EntityCityDto;
import com.noelwon.model.spot.EntityCountryDto;
import com.noelwon.model.spot.EntityInterestDto;
import com.noelwon.model.spot.EntitySearchDto;
import com.noelwon.model.spot.EntityTourAttractionDto;
import com.noelwon.model.userDto.User;
import com.noelwon.model.userDto.UserInterest;

@Service
public class DtoTomap {
	private final MobileService MobileService;
	private final TransferService transferService;

	@Autowired
	public DtoTomap(MobileService MobileService, TransferService TransferService) {
		this.MobileService = MobileService;
		this.transferService = TransferService;

	}

	public JSONArray goEnterPage(List<EntityCountryDto> countryList) {
		JSONArray jArray = new JSONArray();
		Map<String, String> map = new HashMap<>();

		for (int i = 0; i < countryList.size(); i++) {
			map.put("countryId", ((EntityCountryDto) countryList.get(i)).getCountryId().toString());
			map.put("countryName", ((EntityCountryDto) countryList.get(i)).getCountryName());
			map.put("countryInfo", ((EntityCountryDto) countryList.get(i)).getCountryInfo());
			map.put("timeD", ((EntityCountryDto) countryList.get(i)).getTimeD());
			map.put("languageC", ((EntityCountryDto) countryList.get(i)).getLanguageC());
			map.put("currency", ((EntityCountryDto) countryList.get(i)).getCurrency());
			map.put("imageC", ((EntityCountryDto) countryList.get(i)).getImageC());
			JSONObject sObject = new JSONObject(map);
			jArray.add(sObject);
		}

		return jArray;
	}

	public JSONArray selectpage(List<EntityCityDto> EntityCityList) {

		JSONArray jArray = new JSONArray();
		Map<String, String> map = new HashMap<>();

		for (int i = 0; i < EntityCityList.size(); i++) {
			map.put("cityId", Integer.toString(EntityCityList.get(i).getCityId()));
			map.put("cityName", EntityCityList.get(i).getCityName());
			map.put("countryId", Integer.toString(EntityCityList.get(i).getEntityCountryDto().getCountryId()));
			JSONObject sObject = new JSONObject(map);
			jArray.add(sObject);
		}
		return jArray;
	}

	public JSONArray TourAttraction(List<EntityTourAttractionDto> EntityTourAttractionDtoList) {
		JSONArray jArray = new JSONArray();
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < EntityTourAttractionDtoList.size(); i++) {
			map.put("placeId", Integer.toString(EntityTourAttractionDtoList.get(i).getPlaceId()));
			map.put("imageP", EntityTourAttractionDtoList.get(i).getImageP());
			map.put("inOut", Integer.toString(EntityTourAttractionDtoList.get(i).getInOut()));
			map.put("placeAddress", EntityTourAttractionDtoList.get(i).getPlaceAddress());
			map.put("placeName", EntityTourAttractionDtoList.get(i).getPlaceName());
			map.put("placeType", EntityTourAttractionDtoList.get(i).getPlaceType());
			map.put("price", EntityTourAttractionDtoList.get(i).getPrice());
			map.put("cityId", Integer.toString(EntityTourAttractionDtoList.get(i).getEntityCityDto().getCityId()));
			map.put("interestId",
					Integer.toString(EntityTourAttractionDtoList.get(i).getEntityInterestDto().getInterestId()));
			map.put("lan", Float.toString(EntityTourAttractionDtoList.get(i).getLan()));
			map.put("lat", Float.toString(EntityTourAttractionDtoList.get(i).getLat()));
			JSONObject sObject = new JSONObject(map);

			jArray.add(sObject);
		}
		return jArray;
	}

	public JSONArray Interest(List<EntityInterestDto> entityInterestDtoList) {
		JSONArray jArray = new JSONArray();
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < entityInterestDtoList.size(); i++) {
			map.put("interestId", Integer.toString(entityInterestDtoList.get(i).getInterestId()));
			map.put("interestType", entityInterestDtoList.get(i).getInterestType());
			map.put("interestImg", entityInterestDtoList.get(i).getInterestImg());
			JSONObject sObject = new JSONObject(map);
			jArray.add(sObject);
		}

		return jArray;
	}

	public JSONArray board(List<BoardEntity> entityInterestDtoList) {
		JSONArray jArray = new JSONArray();
		Map<String, String> map = new HashMap<>();

		for (int i = 0; i < entityInterestDtoList.size(); i++) {
			map.put("articleNo", Integer.toString(entityInterestDtoList.get(i).getArticleNo()));
			map.put("content", entityInterestDtoList.get(i).getContent());
			map.put("title", entityInterestDtoList.get(i).getTitle());
			map.put("writeId", entityInterestDtoList.get(i).getWriteId());
			map.put("views", Integer.toString(entityInterestDtoList.get(i).getViews()));
			map.put("writeDate", entityInterestDtoList.get(i).getWriteDate().toString());
			map.put("userId", Integer.toString(entityInterestDtoList.get(i).getUser().getId()));
			JSONObject sObject = new JSONObject(map);
			jArray.add(sObject);
		}
		return jArray;
	}

	public JSONArray company(List<CompanyEntity> entityInterestDtoList) {

		Map<String, String> map = new HashMap<>();
		JSONArray jArray = new JSONArray();
		for (int i = 0; i < entityInterestDtoList.size(); i++) {
			map.put("articleNo", Integer.toString(entityInterestDtoList.get(i).getArticleNo()));
			map.put("content", entityInterestDtoList.get(i).getContent());
			map.put("title", entityInterestDtoList.get(i).getTitle());
			map.put("writeId", entityInterestDtoList.get(i).getWriteId());
			map.put("views", Integer.toString(entityInterestDtoList.get(i).getViews()));
			map.put("writeDate", entityInterestDtoList.get(i).getWriteDate().toString());
			map.put("userId", Integer.toString(entityInterestDtoList.get(i).getUser().getId()));
			JSONObject sObject = new JSONObject(map);
			jArray.add(sObject);
		}
		return jArray;
	}

	public JSONArray trade(List<TradeEntity> entityInterestDtoList) {
		JSONArray jArray = new JSONArray();
		Map<String, String> map = new HashMap<>();

		for (int i = 0; i < entityInterestDtoList.size(); i++) {
			map.put("articleNo", Integer.toString(entityInterestDtoList.get(i).getArticleNo()));
			map.put("content", entityInterestDtoList.get(i).getContent());
			map.put("title", entityInterestDtoList.get(i).getTitle());
			map.put("writeId", entityInterestDtoList.get(i).getWriteId());
			map.put("views", Integer.toString(entityInterestDtoList.get(i).getViews()));
			map.put("writeDate", entityInterestDtoList.get(i).getWriteDate().toString());
			map.put("userId", Integer.toString(entityInterestDtoList.get(i).getUser().getId()));
			JSONObject sObject = new JSONObject(map);
			jArray.add(sObject);
		}
		return jArray;
	}
	
	public JSONArray reply(List<ReplyEntity> entityInterestDtoList) {
		JSONArray jArray = new JSONArray();
		Map<String, String> map = new HashMap<>();

		for (int i = 0; i < entityInterestDtoList.size(); i++) {
			
			BoardEntity boardEntity = entityInterestDtoList.get(i).getBoardEntity();
			CompanyEntity companyEntity = entityInterestDtoList.get(i).getCompanyEntity();
			TradeEntity tradeEntity = entityInterestDtoList.get(i).getTradeEntity();

			map.put("replyNo", Integer.toString(entityInterestDtoList.get(i).getReplyNo()));
			map.put("replyContent", entityInterestDtoList.get(i).getReplycontent());
			map.put("writeDate", entityInterestDtoList.get(i).getWriteDate().toString());
			map.put("writeId", entityInterestDtoList.get(i).getWriteId());
			map.put("userId", Integer.toString(entityInterestDtoList.get(i).getUser().getId()));
	
			if (boardEntity != null) {
				map.put("boardEntity", Integer.toString(entityInterestDtoList.get(i).getBoardEntity().getArticleNo()));
				map.put("companyEntity", "0");
				map.put("tradeEntity", "0");
				
			} else if (companyEntity != null) {
				map.put("boardEntity", "0");
				map.put("companyEntity", Integer.toString(entityInterestDtoList.get(i).getCompanyEntity().getArticleNo()));
				map.put("tradeEntity", "0");
			} else {
				map.put("boardEntity", "0");
				map.put("companyEntity", "0");
				map.put("tradeEntity", Integer.toString(entityInterestDtoList.get(i).getTradeEntity().getArticleNo()));
			}
			JSONObject sObject = new JSONObject(map);
			jArray.add(sObject);
		}
		return jArray;
	}

	public JSONArray searc(List<EntitySearchDto> entityInterestDtoList) {
		JSONArray jArray = new JSONArray();
		Map<String, String> map = new HashMap<>();

		for (int i = 0; i < entityInterestDtoList.size(); i++) {

			map.put("name", entityInterestDtoList.get(i).getName());
			map.put("Address", entityInterestDtoList.get(i).getAddress());
			map.put("globalCode", entityInterestDtoList.get(i).getGlobalCode());
			map.put("compoundCode", entityInterestDtoList.get(i).getCompoundCode());
			map.put("lat", entityInterestDtoList.get(i).getLat());
			map.put("lng", entityInterestDtoList.get(i).getLng());
			map.put("img", entityInterestDtoList.get(i).getImg());
			map.put("inOut", entityInterestDtoList.get(i).getImg());
			map.put("cityId", entityInterestDtoList.get(i).getImg());

			JSONObject sObject = new JSONObject(map);
			jArray.add(sObject);
		}
		return jArray;
	}

	public JSONArray user(List<User> entityInterestDtoList) {
		JSONArray jArray = new JSONArray();
		Map<String, String> map = new HashMap<>();

		for (int i = 0; i < entityInterestDtoList.size(); i++) {

			map.put("id", Integer.toString(entityInterestDtoList.get(i).getId()));
			map.put("name", entityInterestDtoList.get(i).getName());
			map.put("email", entityInterestDtoList.get(i).getEmail());
			map.put("picture", entityInterestDtoList.get(i).getPicture());
			map.put("role", entityInterestDtoList.get(i).getRole());

			JSONObject sObject = new JSONObject(map);
			jArray.add(sObject);
		}
		return jArray;
	}

	public JSONArray userInfo(List<UserInterest> entityInterestDtoList) {
		JSONArray jArray = new JSONArray();
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < entityInterestDtoList.size(); i++) {
		
			map.put("id", Integer.toString(entityInterestDtoList.get(i).getId()));
			map.put("user", Integer.toString(entityInterestDtoList.get(i).getUser().getId()));
			map.put("reliability", Integer.toString(entityInterestDtoList.get(i).getReliability()));
			map.put("history", Integer.toString(entityInterestDtoList.get(i).getHistory()));
			map.put("sights", Integer.toString(entityInterestDtoList.get(i).getSights()));
			map.put("culture", Integer.toString(entityInterestDtoList.get(i).getCulture()));
			map.put("food", Integer.toString(entityInterestDtoList.get(i).getFood()));
			map.put("nature", Integer.toString(entityInterestDtoList.get(i).getNature()));
			map.put("religion", Integer.toString(entityInterestDtoList.get(i).getReligion()));

			JSONObject sObject = new JSONObject(map);
			jArray.add(sObject);
		}
		return jArray;
	}

}