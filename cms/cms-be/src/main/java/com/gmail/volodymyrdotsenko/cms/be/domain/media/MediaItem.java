package com.gmail.volodymyrdotsenko.cms.be.domain.media;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.gmail.volodymyrdotsenko.cms.be.domain.BaseEntity;
import com.gmail.volodymyrdotsenko.cms.be.domain.local.Language;

@MappedSuperclass
public abstract class MediaItem extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "REF_FOLDER_ID")
	protected Folder folder;

	@ElementCollection
	@CollectionTable(name = "MEDIA_ITEMS_LOCAL", joinColumns = {
			@javax.persistence.JoinColumn(name = "REF_ITEM_ID", referencedColumnName = "ID") })
	@MapKeyJoinColumn(name = "CODE")
	private Map<Language, MediaItemLocal> local = new HashMap<>();

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private MediaItemContent content;

	public Map<Language, MediaItemLocal> getLocal() {
		return local;
	}

	public void setLocal(Map<Language, MediaItemLocal> local) {
		this.local = local;
	}

	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	public MediaItemContent getContent() {
		return content;
	}

	public void setContent(MediaItemContent content) {
		this.content = content;
	}
}