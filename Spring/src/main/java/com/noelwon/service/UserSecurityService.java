package com.noelwon.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.noelwon.Security.SessionUser;
//import com.noelwon.model.userDto.User;
import com.noelwon.model.userDto.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class UserSecurityService implements UserDetailsService {

	private final UserRepository userRepository;

	@Autowired
	private HttpSession httpSession;

	public UserSecurityService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<com.noelwon.model.userDto.User> _siteUser = this.userRepository.findByEmail(username);
		if (_siteUser.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
		}
		com.noelwon.model.userDto.User siteUser = _siteUser.get();
		List<GrantedAuthority> authorities = new ArrayList<>();

		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		httpSession.setAttribute("user", new SessionUser(siteUser));

		return new User(siteUser.getName(), siteUser.getPassword(), authorities);
	}
}