package com.gmail.volodymyrdotsenko.cms.be.services.users;

import java.util.Collection;
import java.util.Optional;

import com.gmail.volodymyrdotsenko.cms.be.domain.users.User;

public interface UserService {
	Optional<User> getUserById(long id);

	Optional<User> getUserByEmail(String email);

	Collection<User> getAllUsers();

	// User create(UserCreateForm form);
}