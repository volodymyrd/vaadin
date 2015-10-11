package com.gmail.volodymyrdotsenko.cms.be.domain.media;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.gmail.volodymyrdotsenko.cms.be.domain.local.Language;

@Entity
@Table(name = "MEDIA_ITEM_VIDEOS")
public class VideoItem extends MediaItem {

	private static final long serialVersionUID = 1L;

	@NotBlank
	@Column(name = "NAME", length = 255)
	private String name;

	@Column(name = "LENGTH")
	private Long length;

	@ElementCollection
	@CollectionTable(name = "MEDIA_ITEM_TEXTS", joinColumns = {
			@javax.persistence.JoinColumn(name = "REF_ITEM_ID", referencedColumnName = "ID") })
	@MapKeyJoinColumn(name = "CODE")
	private Map<Language, TextItem> textItem = new HashMap<>();
}