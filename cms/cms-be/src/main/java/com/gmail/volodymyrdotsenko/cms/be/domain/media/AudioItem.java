package com.gmail.volodymyrdotsenko.cms.be.domain.media;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "MEDIA_ITEM_AUDIOS")
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class AudioItem extends MediaItem {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "NAME", length = 255)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}