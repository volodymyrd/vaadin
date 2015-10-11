package com.gmail.volodymyrdotsenko.cms.be.domain.media;

import java.io.Serializable;

import javax.persistence.*;

//@Entity
//@Table(name = "")
@Embeddable
public class TextItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Lob
	@Column(name = "text")
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}