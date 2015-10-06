package com.gmail.volodymyrdotsenko.cms.be.domain.users;

import java.util.Set;

import javax.persistence.*;

import org.hibernate.validator.constraints.NotBlank;

import com.gmail.volodymyrdotsenko.cms.be.domain.BaseEntity;

@Entity
@Table(name = "USR_ROLE")
public class UserRole extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@NotBlank
	@Column(name = "NAME", nullable = false, length = 100)
	private String name;

	private String description;

	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	private Set<User> users;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
}