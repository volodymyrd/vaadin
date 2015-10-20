package com.gmail.volodymyrdotsenko.cms.be.domain.users;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PERSISTENT_LOGINS")
public class Login implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SERIES", length = 64)
	private String series;

	@NotNull
	@Column(name = "USERNAME", length = 64)
	private String username;

	@NotNull
	@Column(name = "TOKEN", length = 64)
	private String token;

	@NotNull
	@Column(name = "LAST_USED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUsed;

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getLastUsed() {
		return lastUsed;
	}

	public void setLastUsed(Date lastUsed) {
		this.lastUsed = lastUsed;
	}
}