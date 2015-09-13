package com.gmail.volodymyrdotsenko.cms.be.domain.users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<UserRole, Long> {
}