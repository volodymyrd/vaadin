package com.gmail.volodymyrdotsenko.cms.be.domain.media;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Embeddable
public class MediaItemLocal implements Serializable {

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