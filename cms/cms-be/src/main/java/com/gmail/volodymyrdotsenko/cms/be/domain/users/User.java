package com.gmail.volodymyrdotsenko.cms.be.domain.users;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.*;

import com.gmail.volodymyrdotsenko.cms.be.domain.BaseEntity;

@Entity
@Table(name = "usr_users")
public class User extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@NotBlank
	@Size(min = 5, max = 50)
	@Column(name = "user_name", nullable = false, unique = true, length = 50)
	private String userName;

	@NotBlank
	@Email
	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@NotBlank
	@Column(name = "password", nullable = false)
	private String password;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date expired;

	private Boolean locked;

	/**
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_visit")
	private Date lastVisit;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "usr_users_roles", joinColumns = @JoinColumn(name = "ref_user_id") , inverseJoinColumns = @JoinColumn(name = "ref_role_id") )
	private Set<UserRole> roles = new HashSet<>();

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getExpired() {
		return expired;
	}

	public void setExpired(Date expired) {
		this.expired = expired;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLock(Boolean locked) {
		this.locked = locked;
	}

	public Date getLastVisit() {
		return lastVisit;
	}

	public void setLastVisit(Date lastVisit) {
		this.lastVisit = lastVisit;
	}
}