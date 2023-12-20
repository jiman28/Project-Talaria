package com.noelwon.service.totalService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.noelwon.model.ccc.BoardEntity;
import com.noelwon.model.ccc.BoardEntityRepository;
import com.noelwon.model.ccc.CompanyEntity;
import com.noelwon.model.ccc.CompanyEntityRepository;
import com.noelwon.model.ccc.ReplyEntity;
import com.noelwon.model.ccc.ReplyRepository;
import com.noelwon.model.ccc.TradeEntity;
import com.noelwon.model.ccc.TradeEntityRepository;
import com.noelwon.model.spot.CityRepository;
import com.noelwon.model.spot.CountryRepository;
import com.noelwon.model.spot.EntityCityDto;
import com.noelwon.model.spot.EntityCountryDto;
import com.noelwon.model.spot.EntityInterestDto;
import com.noelwon.model.spot.EntitySearchDto;
import com.noelwon.model.spot.EntitySearchDtoRepository;
import com.noelwon.model.spot.InterestRepository;
import com.noelwon.model.userDto.User;
import com.noelwon.model.userDto.UserInterest;
import com.noelwon.model.userDto.UserInterestRepository;
import com.noelwon.model.userDto.UserRepository;


@Service
public class TotalRepositoryService {

	private final BoardEntityRepository boardEntityRepository;
	private final CompanyEntityRepository companyEntityRepository;
	private final TradeEntityRepository tradeEntityRepository;
//	private final  BoardEntity boardEntity;
//	private final  CompanyEntity companyEntity;
//	private final  TradeEntity TradeEntity;

	private final UserRepository userRepository;
	private final UserInterestRepository userInterestRepository;
//	private final User user;

	private final ReplyRepository replyRepository;
	private final CityRepository cityRepository;
	private final CountryRepository countryRepository;
	private final InterestRepository interestRepository;
	private final EntitySearchDtoRepository entitySearchDtoRepository;

	@Autowired
	public TotalRepositoryService(BoardEntityRepository boardEntityRepository,
			CompanyEntityRepository companyEntityRepository, TradeEntityRepository tradeEntityRepository,
			UserRepository userRepository, UserInterestRepository userInterestRepository,
			ReplyRepository replyRepository, CityRepository cityRepository, CountryRepository countryRepository,
			InterestRepository interestRepository, EntitySearchDtoRepository entitySearchDtoRepository) {
		super();
		this.boardEntityRepository = boardEntityRepository;
		this.companyEntityRepository = companyEntityRepository;
		this.tradeEntityRepository = tradeEntityRepository;
		this.userRepository = userRepository;
		this.userInterestRepository = userInterestRepository;
		this.replyRepository = replyRepository;
		this.cityRepository = cityRepository;
		this.countryRepository = countryRepository;
		this.interestRepository = interestRepository;
		this.entitySearchDtoRepository = entitySearchDtoRepository;
	}

//	boardEntity 관련
	public <T, K> T findById(K id, List<? extends JpaRepository> entityClass, Class<T> ClassType) {

		Object tmp = (Object) entityClass.get(0).findById(id).get();
		T type1 = (T) tmp;

		return type1;
	}

	public <T> List<T> findAll(List<? extends JpaRepository> entityClass, Class<T> ClassType) {

		List<Object> tmp = entityClass.get(0).findAll();
		List<T> type1 = (List<T>) tmp;

		return type1;
	}

	public void deleteAll(List<? extends JpaRepository> entityClass) {
		entityClass.get(0).deleteAll();
	}

//	==========================delete 모음집 ==========================
	public void delete(User tmp) {

		userRepository.delete(tmp);
	};

	public void delete(UserInterest tmp) {

		userInterestRepository.delete(tmp);
	};

	public void delete(BoardEntity tmp) {

		boardEntityRepository.delete(tmp);
	};

	public void delete(CompanyEntity tmp) {

		companyEntityRepository.delete(tmp);
	};

	public void delete(TradeEntity tmp) {

		tradeEntityRepository.delete(tmp);
	};

	public void delete(ReplyEntity tmp) {

		replyRepository.delete(tmp);
	};

	public void delete(EntityCityDto tmp) {

		cityRepository.delete(tmp);
	};

	public void delete(EntityCountryDto tmp) {

		countryRepository.delete(tmp);
	};

	public void delete(EntityInterestDto tmp) {

		interestRepository.delete(tmp);
	};

	public void delete(EntitySearchDto tmp) {

		entitySearchDtoRepository.delete(tmp);
	};

//	================================================================

//	==========================save 모음집 ==========================

	public void save(User tmp) {
		userRepository.save(tmp);
	}

	public void save(UserInterest tmp) {
		userInterestRepository.save(tmp);
	}

	public void save(BoardEntity tmp) {
		boardEntityRepository.save(tmp);
	}

	public void save(CompanyEntity tmp) {
		companyEntityRepository.save(tmp);
	}

	public void save(TradeEntity tmp) {
		tradeEntityRepository.save(tmp);
	}

	public void save(ReplyEntity tmp) {
		replyRepository.save(tmp);
	}

	public void save(EntityCityDto tmp) {
		cityRepository.save(tmp);
	}

	public void save(EntityCountryDto tmp) {
		countryRepository.save(tmp);
	}

	public void save(EntityInterestDto tmp) {
		interestRepository.save(tmp);
	}

	public void save(EntitySearchDto tmp) {
		entitySearchDtoRepository.save(tmp);
	}

//	====================================================

//	==========================특정 조건 검색 모음집 ==========================
	public User findByType(String email) {
		try {
			return this.userRepository.findByEmail(email).get();
		} catch (Exception e) {
			return new User();
		}
	}
	
	public UserInterest findByType(User user) {
			return this.userInterestRepository.findByuser(user).get();
	}

	
	public <T> List<T> findByType(User user, Class<T> ClassType, String tap) {

		if (tap.equals("리뷰")) {
			List<T> type1 = (List<T>) boardEntityRepository.findByuser(user);
			return type1;
		} else if (tap.equals("동행")) {
			List<T> type1 = (List<T>) companyEntityRepository.findByuser(user);
			return type1;
		} else if (tap.equals("거래")) {
			List<T> type1 = (List<T>) tradeEntityRepository.findByuser(user);
			return type1;
		} else {
			return null;
		}

	}

//	==========================특정 조건 삭제 모음집 ==========================
	public void deleteByType(String email) {

		this.userRepository.delete(findByType(email));
	}
	
//	==========================================================
	
	public BoardEntityRepository getBoardEntityRepository() {
		return boardEntityRepository;
	}

	public CompanyEntityRepository getCompanyEntityRepository() {
		return companyEntityRepository;
	}

	public TradeEntityRepository getTradeEntityRepository() {
		return tradeEntityRepository;
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public UserInterestRepository getUserInterestRepository() {
		return userInterestRepository;
	}

	public ReplyRepository getReplyRepository() {
		return replyRepository;
	}

	public CityRepository getCityRepository() {
		return cityRepository;
	}

	public CountryRepository getCountryRepository() {
		return countryRepository;
	}

	public InterestRepository getInterestRepository() {
		return interestRepository;
	}

	public EntitySearchDtoRepository getEntitySearchDtoRepository() {
		return entitySearchDtoRepository;
	};

}
