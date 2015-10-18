package com.gmail.volodymyrdotsenko.cms.be.domain.media;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.gmail.volodymyrdotsenko.cms.be.domain.BaseEntity;

@MappedSuperclass
public abstract class MediaItem extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "REF_FOLDER_ID")
	protected Folder folder;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private MediaItemContent content;

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