package com.gmail.volodymyrdotsenko.cms.fe.auth;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gmail.volodymyrdotsenko.cms.be.domain.users.User;
import com.gmail.volodymyrdotsenko.cms.be.domain.users.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepo;

	@Autowired
	public UserDetailsServiceImpl(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.findOneByEmailWithRoles(email).orElseThrow(
				() -> new UsernameNotFoundException(String.format("User with email=%s was not found", email)));

		return new CurrentUser(user);
	}

	private class CurrentUser extends org.springframework.security.core.userdetails.User {
		private static final long serialVersionUID = 1L;

		// private User user;

		public CurrentUser(User user) {
			super(user.getEmail(), user.getPassword(), user.getRoles().stream()
					.map(e -> new SimpleGrantedAuthority(e.getName())).collect(Collectors.toList()));

			// this.user = user;
		}
	}
}