package com.noelwon.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.noelwon.model.spot.EntitySearchDto;
import com.noelwon.model.spot.EntitySearchDtoRepository;
import com.noelwon.model.userDto.UserInterest;
import com.noelwon.service.totalService.TotalRepositoryService;

@Service
public class TransferService {

	Logger logger = LoggerFactory.getLogger("service.TransferService");

	private final EntitySearchDtoRepository entitySearchDtoRepository;
	private final UserService userService;
	private final TotalRepositoryService totalRepositoryService;
	private final CountryService countryService;

	@Autowired
	public TransferService(EntitySearchDtoRepository entitySearchDtoRepository, UserService userService,
			TotalRepositoryService totalRepositoryService, CountryService countryService) {
		super();
		this.entitySearchDtoRepository = entitySearchDtoRepository;
		this.userService = userService;
		this.totalRepositoryService = totalRepositoryService;
		this.countryService = countryService;
	}

	public JsonNode webToLocal(String findName) throws IOException {

		JsonNode check;

		check = RequestUtil.restRequest("http://localhost:8000/place/popup/", findName);
		return check;
	}

	public JsonNode webToModel(UserInterest user) throws IOException {

		JsonNode check;
		List<UserInterest> userList = totalRepositoryService
				.findAll(Arrays.asList(totalRepositoryService.getUserInterestRepository()), UserInterest.class);

		check = RequestUtil.aiModel("http://localhost:8000/place/model/", userList, user);

		return check;
	}

	public Map<String, String> dbToMap(String keyword) throws IOException {
		Map<String, String> map = new HashMap<>();
		EntitySearchDto dto = entitySearchDtoRepository.findById(keyword).get();
		map.put("name", dto.getName());
		map.put("global_code", dto.getGlobalCode());
		map.put("compound_code", dto.getCompoundCode());
		map.put("address", dto.getAddress());
		map.put("lat", dto.getLat());
		map.put("lng", dto.getLng());
		map.put("image", dto.getImg());

		return map;
	}

	public EntitySearchDto spotSave(JsonNode fromDjango, String inOut, String cityId) throws IOException {
		EntitySearchDto entitySearchDto = new EntitySearchDto();
		entitySearchDto.setName(fromDjango.get("name").asText());
		entitySearchDto.setGlobalCode(fromDjango.get("global_code").asText());
		entitySearchDto.setCompoundCode(fromDjango.get("compound_code").asText());
		entitySearchDto.setAddress(fromDjango.get("address").asText());
		entitySearchDto.setLat(fromDjango.get("lat").asText());
		entitySearchDto.setLng(fromDjango.get("lng").asText());
		entitySearchDto.setImg(fromDjango.get("image").asText());

		entitySearchDto.setInOut(Integer.valueOf(inOut));
		entitySearchDto.setCityId(Integer.valueOf(cityId));

		return entitySearchDtoRepository.save(entitySearchDto);

	}

	public Map<String, String> dbTODjango(String keyword, String inOut, String cityId) throws IOException {

		if (!entitySearchDtoRepository.findById(keyword).isPresent()) {
			logger.info("DB에 없음 장고를 이용해서 서칭");
			JsonNode fromDjango = webToLocal(keyword);
			spotSave(fromDjango, inOut, cityId);
			Map<String, String> map = dbToMap(keyword);

			return map;
		} else {
			logger.info("DB에 있음 DB값 바로 가져옴");
			Map<String, String> map = dbToMap(keyword);

			return map;
		}

	}

	public List<Integer> modelTODjango(int keyword) throws IOException {

		UserInterest user = totalRepositoryService.findById(keyword,
				Arrays.asList(totalRepositoryService.getUserInterestRepository()), UserInterest.class);

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode fromDjango = webToModel(user);

		logger.info("fromDjango.get(userList)===>" + fromDjango.get("userList"));
		logger.info("fromDjango.get(userList).getClass()===>" + fromDjango.get("userList").getClass());
		List<Integer> list = new ArrayList<>();

		JsonNode aaa = fromDjango.get("userList");

		ArrayNode lis = (ArrayNode) aaa;

		for (int c = 0; c < lis.size(); c++) {
			IntNode t = (IntNode) lis.get(c);
			list.add(t.intValue());
		}

		return list;
	}

}
