package com.gmail.volodymyrdotsenko.cms.fe.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gmail.volodymyrdotsenko.cms.be.domain.users.User;
import com.gmail.volodymyrdotsenko.cms.be.services.users.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserService userService;

	@Autowired
	public UserDetailsServiceImpl(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userService.getUserByEmail(email).orElseThrow(
				() -> new UsernameNotFoundException(String.format("User with email=%s was not found", email)));

		return new CurrentUser(user);
	}

	private class CurrentUser extends org.springframework.security.core.userdetails.User {
		private static final long serialVersionUID = 1L;

		private User user;

		public CurrentUser(User user) {
			super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList("USER"));
			this.user = user;
		}

		public User getUser() {
			return user;
		}

		public Long getId() {
			return user.getId();
		}
	}
}