package com.gmail.volodymyrdotsenko.cms.be.domain.local;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "LANG_LANGUAGES")
public class Language implements Serializable {

	private static final long serialVersionUID = 1L;

	public Language() {
	}

	public Language(String code, String name, String shortName) {
		this.code = code;
		this.name = name;
		this.shortName = shortName;
	}

	@Id
	@Column(name = "CODE", length = 10)
	private String code;

	@NotNull
	@Column(name = "NAME", length = 100)
	private String name;

	@Column(name = "SHORT_NAME", length = 5)
	private String shortName;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		Language other = (Language) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
}