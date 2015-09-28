package com.gmail.volodymyrdotsenko.cms.be.domain.users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findOneByEmail(String email);

	@Query("SELECT u FROM User u INNER JOIN FETCH u.roles WHERE u.email=?1")
	Optional<User> findOneByEmailWithRoles(String email);

	@Query("SELECT u FROM User u INNER JOIN FETCH u.roles WHERE u.userName=?1")
	Optional<User> findOneByUserNameWithRoles(String userName);

	/* A version to fetch List instead of Page to avoid extra count query. */
	List<User> findAllBy(Pageable pageable);
}