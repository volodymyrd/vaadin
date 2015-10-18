package com.gmail.volodymyrdotsenko.cms.be.domain.media;

import java.io.Serializable;
import java.util.Date;

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

	@Temporal(TemporalType.TIME)
	@Column(name = "START")
	private Date start;

	@Temporal(TemporalType.TIME)
	@Column(name = "END")
	private Date end;

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

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
}