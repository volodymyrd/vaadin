package com.gmail.volodymyrdotsenko.cms.be.domain.media;

import java.io.Serializable;

import javax.persistence.*;

import com.gmail.volodymyrdotsenko.cms.be.domain.local.Language;

@Entity
@Table(name = "MEDIA_AUDIO_SUBTITLES")
public class AudioSubtitle implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AudioSubtitlePk id;

	public AudioSubtitle() {
	}

	public AudioSubtitle(Language lang, AudioItem item, Integer orderNum) {
		this.id = new AudioSubtitlePk(lang, item, orderNum);
	}

	@Column(name = "TEXT", length = 255)
	private String text;

	@Column(name = "START", length = 15)
	private String start;

	@Column(name = "END", length = 15)
	private String end;

	public AudioSubtitlePk getId() {
		return id;
	}

	public void setId(AudioSubtitlePk id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
}