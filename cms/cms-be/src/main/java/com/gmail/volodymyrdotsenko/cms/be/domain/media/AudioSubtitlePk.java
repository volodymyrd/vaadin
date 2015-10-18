package com.gmail.volodymyrdotsenko.cms.be.domain.media;

import java.io.Serializable;

import javax.persistence.*;

import com.gmail.volodymyrdotsenko.cms.be.domain.local.Language;

@Embeddable
public class AudioSubtitlePk implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "LANG_CODE")
	private Language lang;

	@ManyToOne
	@JoinColumn(name = "REF_ITEM_ID")
	private AudioItem item;

	@Column(name = "ORDER_NUM")
	private Integer orderNum;

	public Language getLang() {
		return lang;
	}

	public AudioSubtitlePk() {
	}

	public AudioSubtitlePk(Language lang, AudioItem item, Integer orderNum) {
		this.lang = lang;
		this.item = item;
		this.orderNum = orderNum;
	}

	public void setLang(Language lang) {
		this.lang = lang;
	}

	public AudioItem getItem() {
		return item;
	}

	public void setItem(AudioItem item) {
		this.item = item;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		result = prime * result + ((lang == null) ? 0 : lang.hashCode());
		result = prime * result + ((orderNum == null) ? 0 : orderNum.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AudioSubtitlePk other = (AudioSubtitlePk) obj;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		if (lang == null) {
			if (other.lang != null)
				return false;
		} else if (!lang.equals(other.lang))
			return false;
		if (orderNum == null) {
			if (other.orderNum != null)
				return false;
		} else if (!orderNum.equals(other.orderNum))
			return false;
		return true;
	}
}