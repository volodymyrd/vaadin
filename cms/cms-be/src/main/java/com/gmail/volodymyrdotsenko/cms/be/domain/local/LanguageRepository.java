
package com.gmail.volodymyrdotsenko.cms.be.domain.local;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Empty JpaRepository is enough for a simple crud.
 */
public interface LanguageRepository extends JpaRepository<Language, Long> {
}