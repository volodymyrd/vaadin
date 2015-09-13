package com.gmail.volodymyrdotsenko.cms.be.domain.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findOneByEmail(String email);

	@Query("SELECT u FROM User u INNER JOIN FETCH u.roles WHERE u.email=?1")
	Optional<User> findOneByEmailWithRoles(String email);
}