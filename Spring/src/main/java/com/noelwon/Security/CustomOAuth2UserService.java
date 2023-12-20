package com.noelwon.Security;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.noelwon.model.userDto.User;
import com.noelwon.model.userDto.UserInterest;
import com.noelwon.model.userDto.UserInterestRepository;
import com.noelwon.model.userDto.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private HttpSession httpSession;

	@Autowired
	private UserInterestRepository userInterestRepository;

	Logger logger = LoggerFactory.getLogger("CustomOAuth2UserService=================>");

	@Override
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {

		logger.info("실행실행실행실행실행실행실행실행실행실행실행실행1");

		OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);

		// 현재 진행중인 서비스를 구분하기 위해 문자열로 받음.
		// oAuth2UserRequest.getClientRegistration().getRegistrationId()에 값이 들어있다.
		// {registrationId='naver'} 이런식으로
		String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();

		// OAuth2 로그인 시 키 값이 된다. 구글은 키 값이 "sub"이고, 네이버는 "response"이고, 카카오는 "id"이다. 각각
		// 다르므로 이렇게 따로 변수로 받아서 넣어줘야함.
		String userNameAttributeName = oAuth2UserRequest.getClientRegistration().getProviderDetails()
				.getUserInfoEndpoint().getUserNameAttributeName();

		// OAuth2 로그인을 통해 가져온 OAuth2User의 attribute를 담아주는 of 메소드.
		OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
				oAuth2User.getAttributes());

		User user = saveOrUpdate(attributes);
		
		httpSession.setAttribute("user", new SessionUser(user));

		System.out.println(attributes.getAttributes());
		return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
				attributes.getAttributes(), attributes.getNameAttributeKey());
	}

	// 혹시 이미 저장된 정보라면, update 처리
	private User saveOrUpdate(OAuthAttributes attributes) {
		logger.info("실행실행실행실행실행실행실행실행실행실행실행실행2");

		if (!userRepository.findByEmail(attributes.getEmail()).isPresent()) {
			logger.info("회원이 아님 회원가입을 해야함");
			// 이하 회원가입 로직 및 관심사 선택 페이지로 이동
			User user = new User();
//					userRepository.findByEmail(attributes.getEmail())
//					.map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
//					.orElse(attributes.toEntity());
			user.setEmail(attributes.getEmail());
			user.setName(attributes.getName());
			user.setPicture(attributes.getPicture());
			
			user.setType("google");
			user = userRepository.save(user);
			UserInterest n1 = new UserInterest();
			n1.setUser(user);
			n1.setReliability(0);
			userInterestRepository.save(n1);

			return user;

		} else {
			logger.info("회원임");
			User user = userRepository.findByEmail(attributes.getEmail()).get();
					

			return userRepository.save(user);
		}

	}
}
