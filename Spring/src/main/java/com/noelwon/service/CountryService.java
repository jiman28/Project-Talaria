package com.noelwon.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.noelwon.model.spot.SpotDto;
import com.noelwon.model.userDto.User;
import com.noelwon.model.userDto.UserInterestRepository;
import com.noelwon.model.userDto.UserRepository;

@Service
public class CountryService {

	@Autowired
	EntityCountryDto entityCountryDto;
	@Autowired
	EntityCityDto entityCityDto;
	@Autowired
	EntityInterestDto entityInterestDto;
	@Autowired
	EntityTourAttractionDto entityTourAttractionDto;

	@Autowired
	User user;

	Logger logger = LoggerFactory.getLogger("service.CountryService");

	private final CountryRepository countryRepository;
	private final CityRepository cityRepository;
	private final InterestRepository interestRepository;
	private final AttractionRepository attractionRepository;
	private final UserRepository userRepository;
	private final UserInterestRepository userInterestRepository;
	private final EntitySearchDtoRepository entitySearchDtoRepository;

	@Autowired
	public CountryService(CountryRepository countryRepository, CityRepository cityRepository,
			InterestRepository interestRepository, AttractionRepository attractionRepository,
			UserRepository userRepository, UserInterestRepository userInterestRepository,
			EntitySearchDtoRepository entitySearchDtoRepository) {
		this.countryRepository = countryRepository;
		this.cityRepository = cityRepository;
		this.interestRepository = interestRepository;
		this.attractionRepository = attractionRepository;
		this.userRepository = userRepository;
		this.userInterestRepository = userInterestRepository;
		this.entitySearchDtoRepository = entitySearchDtoRepository;
	}

	// 나라 정보 전체 가져오기
	public List<EntityCountryDto> viewCountry() {
		return this.countryRepository.findAll();
	}

	// 관심사 전체 가져오기
	public List<EntityInterestDto> viewinterest() {
		return this.interestRepository.findAll();
	}

	// 나라 정보 하나 가져오기
	public EntityCountryDto CountryOne(String countryName) {
		return this.countryRepository.findBycountryName(countryName).get();
	}

	// 도시 정보 하나 가져오기
	public EntityCityDto CityOne(int cityId) {
		return this.cityRepository.findBycityId(cityId).get();
	}

	// 도시 정보 전체 가져오기
	public List<EntityCityDto> viewCity() {
		return this.cityRepository.findAll();
	}

	// 관광지 전체 가져오기
	public List<EntityTourAttractionDto> viewPlace() {
		return this.attractionRepository.findAll();
	}

	public EntityCountryDto selectCountry(int countryId) {
		Optional<EntityCountryDto> list = this.countryRepository.findById(countryId);
		return list.get();
	}

	public List<EntitySearchDto> getSearch() {
		return this.entitySearchDtoRepository.findAll();
	}

	public SpotDto dtoToSpotDto(EntityTourAttractionDto dataDto, EntitySearchDto findDto) {

		if (dataDto != null) {
			return new SpotDto(dataDto.getPlaceId().toString(), dataDto.getPlaceName(), dataDto.getImageP(),
					dataDto.getLan().toString(), dataDto.getLat().toString(), dataDto.getInOut(),
					dataDto.getEntityCityDto().getCityId());
		} else {
			return new SpotDto(findDto.getName().toString(), findDto.getName(), findDto.getImg(),
					findDto.getLng().toString(), findDto.getLat().toString(), findDto.getInOut(), findDto.getCityId());
		}

	}

	public List<SpotDto> StringToList(String placeId, String finds) {

		List<SpotDto> list = new ArrayList<>();
		if (placeId != null) {
			String[] arr = placeId.split(",");
			for (int i = 0; i < arr.length; i++) {
				EntityTourAttractionDto dto = this.attractionRepository.findById(Integer.parseInt(arr[i])).get();
				list.add(dtoToSpotDto(dto, null));
			}
		}
		if (finds != null) {
			String[] arr = finds.split(",");
			for (int i = 0; i < arr.length; i++) {
				EntitySearchDto dto = this.entitySearchDtoRepository.findById(arr[i]).get();
				list.add(dtoToSpotDto(null, dto));
			}
		}

		return list;
	}
	
	public Map<String, List<Integer>> listToSet(Map<String, List<SpotDto>> map){
		Map<String, List<Integer>> mapSet = new LinkedHashMap<String, List<Integer>>();
		
		for(String str : map.keySet()) {
			Set<Integer> set = new HashSet<Integer>();
			
			for(int i = 0; i < map.get(str).size(); i++) {
				set.add(map.get(str).get(i).getCityId());
			}
			
			mapSet.put(str, new ArrayList<Integer>(set));
		}
		
		
		return mapSet;
	}

	public Map<String, List<SpotDto>> selectPlace(String date, String placeId, String finds) {
		List<SpotDto> tmp;
		List<String> strList = getDate(date);
		Map<String, List<SpotDto>> map = new LinkedHashMap<>();
		
		
		
		List<SpotDto> list = new ArrayList<>();
		List<SpotDto> dayList = new ArrayList<>();
		if (placeId != null) {
			String[] arr = placeId.split(",");
			for (int i = 0; i < arr.length; i++) {
				EntityTourAttractionDto dto = this.attractionRepository.findById(Integer.parseInt(arr[i])).get();
				list.add(dtoToSpotDto(dto, null));
				dayList.add(dtoToSpotDto(dto, null));
			}
		}
		if (finds != null) {
			String[] arr = finds.split(",");
			for (int i = 0; i < arr.length; i++) {
				EntitySearchDto dto = this.entitySearchDtoRepository.findById(arr[i]).get();
				list.add(dtoToSpotDto(null, dto));
				dayList.add(dtoToSpotDto(null, dto));
			}
		}
		// 여기까지 우선 SpotDto를 이용해서 관광지를 다 담아줌
		int count = dayList.size() / strList.size();
		if (count < 1) {
			count = 1;
		} else {
			count = dayList.size() / strList.size();
		}

		for (String str : strList) {
			tmp = new ArrayList<>();
			for (int i = 0; i < count;) {
				if (tmp.size() < count && list.size() != 0) {
					tmp.add(list.get(0));
					list.remove(0);
					i = 0;
				} else {
					i++;
				}
			}
			map.put(str, tmp);
		}
		
		Map<String, List<Integer>> mapSet = listToSet(map);
		
		for (String str : map.keySet()) {
			try {
				while (map.get(str).get(map.get(str).size() - 1).getCityId() == list.get(0).getCityId()|| list.size()!=0) {
					map.get(str).add(list.get(0));
					list.remove(0);
				}
			} catch (IndexOutOfBoundsException e) {
				break;
			}
		}
		return map;

	}
//   2023.10.26 5일 이상 날씨 오류 수정한 것.
	public Map<String, List<SpotDto>> inOutPlace(String date, String placeId, String finds,
			Map<String, String[]> weather) {

		List<SpotDto> Door = new ArrayList<>();
		List<SpotDto> dayDoor = new ArrayList<>();
		List<SpotDto> tmp;
		List<String> strList = getDate(date);
		Map<String, List<SpotDto>> map = new LinkedHashMap<>();
		// 데이터 베이스 관광지
		if (placeId != null) {
			String[] arr = placeId.split(",");
			for (int i = 0; i < arr.length; i++) {
				EntityTourAttractionDto dto = this.attractionRepository.findById(Integer.parseInt(arr[i])).get();

				Door.add(new SpotDto(dto.getPlaceId().toString(), dto.getPlaceName(), dto.getImageP(),
						dto.getLan().toString(), dto.getLat().toString(), dto.getInOut(),
						dto.getEntityCityDto().getCityId()));
				dayDoor.add(new SpotDto(dto.getPlaceId().toString(), dto.getPlaceName(), dto.getImageP(),
						dto.getLan().toString(), dto.getLat().toString(), dto.getInOut(),
						dto.getEntityCityDto().getCityId()));
			}
		}
		// 사용자 검색 관광지
		if (finds != null) {
			String[] arr = finds.split(",");
			for (int i = 0; i < arr.length; i++) {
				EntitySearchDto dto = this.entitySearchDtoRepository.findById(arr[i]).get();
				Door.add(new SpotDto(dto.getName().toString(), dto.getName(), dto.getImg(), dto.getLng().toString(),
						dto.getLat().toString(), dto.getInOut(), dto.getCityId()));
				dayDoor.add(new SpotDto(dto.getName().toString(), dto.getName(), dto.getImg(), dto.getLng().toString(),
						dto.getLat().toString(), dto.getInOut(), dto.getCityId()));
			}
		}
		logger.info("초기 Door.size():     " + Door.size());
		int count = dayDoor.size() / strList.size();

		if (count < 1) {
			count = 1;
		} else {
			count = dayDoor.size() / strList.size();
		}

		// 날씨 랜덤 생성
		for (String str : strList) {
			tmp = new ArrayList<>();
			String icon="";
			for (int i = 0; i < Door.size();) {
				if(weather.get(str) != null){
					if (Door.get(i).getInOut() != Integer.valueOf(weather.get(str)[0]) && tmp.size() < count
							&& Door.size() != 0) {
						tmp.add(Door.get(i));
						Door.remove(i);
						i = 0;
					} else {
						i++;
					}
				}else {
					Random rn = new Random();
					int t = rn.nextInt(2);
					
					if(t == 0) {
						icon = "https://openweathermap.org/img/wn/01d.png";
					}else {
						icon = "https://openweathermap.org/img/wn/09d.png";
					}
					
					weather.put(str,new String[] {String.valueOf(t),icon});
				}
				
			}
			map.put(str, tmp);
		}
//		2023.10.26 수정한 부분 끝

		logger.info("Door.size():   " + Door.size());
		for (int i = 0; i < Door.size(); i++) {
			logger.info("남은 이름:    " + Door.get(i).getName());
		}

		for (String str : strList) {
			try {
//				2023.10.25 날씨 필터 적용 안되는 부분 수정
//				원인 : Integer.valueOf(weather.get(str)[0])!=Door.get(0).getInOut() &&
				while ( (map.get(str).get(map.get(str).size() - 1).getCityId() == Door.get(0).getCityId()
						|| map.get(str).get(map.get(str).size() - 1).getInOut() != Door.get(0).getInOut())
						&& Door.size() != 0 ) {
					map.get(str).add(Door.get(0));
					Door.remove(0);
				}
			} catch (IndexOutOfBoundsException e) {
				while (Door.size() != 0) {
					map.get(str).add(Door.get(0));
					Door.remove(0);
				}
				
			}
		}
		logger.info("최종 Door.size():   " + Door.size());
		for (int i = 0; i < Door.size(); i++) {
			logger.info("남은 이름:    " + Door.get(i).getName());
		}
		return map;

	}

	public List<String> getDate(String date) {

		String[] sarr = date.split(" to ");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		LocalDate startDate = LocalDate.parse(sarr[0], formatter);
		LocalDate endDate = LocalDate.parse(sarr[1], formatter).plusDays(1);

		List<LocalDate> dateList = startDate.datesUntil(endDate).collect(Collectors.toList());
		List<String> strDate = new ArrayList<>();

		for (LocalDate dateOne : dateList) {
			strDate.add(dateOne.toString());
		}

		return strDate;
	}

	public List<SpotDto> listTOList(String plan) {

		List<SpotDto> list = new ArrayList<>();

		logger.info(plan);

		String[] sarr = plan.split(",");

		logger.info(sarr.toString());

		for (String str : sarr) {
			try {
				int i = Integer.parseInt(str);
				EntityTourAttractionDto dto = this.attractionRepository.findById(i).get();
				list.add(new SpotDto(dto.getPlaceId().toString(), dto.getPlaceName(), dto.getImageP(),
						dto.getLan().toString(), dto.getLat().toString(), dto.getInOut(),
						dto.getEntityCityDto().getCityId()));
			} catch (NumberFormatException e) {
				EntitySearchDto dto = this.entitySearchDtoRepository.findById(str).get();
				list.add(new SpotDto(dto.getName().toString(), dto.getName(), dto.getImg(), dto.getLng().toString(),
						dto.getLat().toString(), dto.getInOut(), dto.getCityId()));

			}

		}

		return list;
	}
	
	
	public EntityTourAttractionDto spotDtoToDto(String pk) {
		try {
		EntityTourAttractionDto dto = attractionRepository.findById(Integer.parseInt(pk)).get();
		return dto;
		}catch (Exception e) {
			return null;
		}
		
	}

}
