
package com.gmail.volodymyrdotsenko.cms.be.domain.media;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gmail.volodymyrdotsenko.cms.be.domain.local.Language;

/**
 * Empty JpaRepository is enough for a simple crud.
 */
public interface AudioSubtitleRepository extends JpaRepository<AudioSubtitle, AudioSubtitlePk> {
	@Query("SELECT s from AudioSubtitle s WHERE s.id.lang = ?1 AND s.id.item = ?2")
	List<AudioSubtitle> findByLangAndByItem(Language lang, AudioItem item);

	@Query("SELECT s from AudioSubtitle s WHERE s.id.item = ?1")
	List<AudioSubtitle> findByItem(AudioItem item);

	@Query("SELECT s from AudioSubtitle s WHERE s.id.item = ?1 ORDER BY s.id.orderNum")
	List<AudioSubtitle> findByItemOderByNum(AudioItem item);
	
	@Query("SELECT s from AudioSubtitle s WHERE s.id.item.id = ?1 ORDER BY s.id.orderNum")
	List<AudioSubtitle> findByItemOderByNum(Long itemId);
}