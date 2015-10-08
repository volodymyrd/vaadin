
package com.gmail.volodymyrdotsenko.cms.be.domain.media;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Empty JpaRepository is enough for a simple crud.
 */
public interface AudioItemRepository extends JpaRepository<AudioItem, Long> {
	
	public List<AudioItem> findByFolder(Folder f);
}