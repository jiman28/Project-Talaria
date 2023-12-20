package com.noelwon.Security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity // 해당 애노테이션을 붙인 필터(현재 클래스)를 스프링 필터체인에 등록.
public class SecurityConfig {
	Logger logger = LoggerFactory.getLogger("SecurityConfig=================>");
	// 커스텀한 OAuth2UserService DI.
	@Autowired
	private CustomOAuth2UserService customOAuth2UserService;

//	// encoder를 빈으로 등록.
//	@Bean
//	public BCryptPasswordEncoder bCryptPasswordEncoder() {
//		return new BCryptPasswordEncoder();
//	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		logger.info("실행");
		http
		.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
//				.requestMatchers("/project/selectpage").hasRole("USER")
				.requestMatchers("/goolge/login").hasRole("USER")
				.requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
		.csrf((csrf) -> csrf
                .ignoringRequestMatchers(new AntPathRequestMatcher("/**")))
				.oauth2Login((oauth2Login) -> oauth2Login
						.userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig.userService(this.customOAuth2UserService))
						.defaultSuccessUrl("/project/home", true))
				.formLogin((formLogin) -> formLogin
		                .loginPage("/project/formLogin")
		                .defaultSuccessUrl("/project/home"))
				.logout((logout) -> logout
	                    .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
	                    .logoutSuccessUrl("/project/home")
	                    .invalidateHttpSession(true));	// 로그아웃시 생성된 사용자 세션도 삭제하도록 처리

		return http.build();

	}
	@Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}