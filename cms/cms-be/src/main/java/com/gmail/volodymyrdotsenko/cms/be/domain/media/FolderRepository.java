
package com.gmail.volodymyrdotsenko.cms.be.domain.media;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Empty JpaRepository is enough for a simple crud.
 */
public interface FolderRepository extends JpaRepository<Folder, Long> {
}