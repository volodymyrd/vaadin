
package com.gmail.volodymyrdotsenko.cms.be.domain.media;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Empty JpaRepository is enough for a simple crud.
 */
public interface FolderRepository extends JpaRepository<Folder, Long> {
	
	  @Query("SELECT f FROM Folder f WHERE f.id = (SELECT MIN(f1.id) FROM Folder f1)")
	  Folder findRoot();
}