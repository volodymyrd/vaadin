package com.gmail.volodymyrdotsenko.cms.be.domain.media;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.*;

import com.gmail.volodymyrdotsenko.cms.be.domain.BaseEntity;
import com.gmail.volodymyrdotsenko.cms.be.domain.local.Language;
import com.gmail.volodymyrdotsenko.cms.be.domain.users.User;

@Entity
@Table(name = "MEDIA_FOLDERS")
public class Folder extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "REF_FOLDER_ID")
	private Folder parent;

	@ManyToOne
	@JoinColumn(name = "REF_USER_ID")
	private User user;

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Folder> children = new HashSet<>();

	@ElementCollection
	@CollectionTable(name = "MEDIA_FOLDERS_LOCAL", joinColumns = {
			@javax.persistence.JoinColumn(name = "REF_FOLDER_ID", referencedColumnName = "ID") })
	@MapKeyJoinColumn(name = "CODE")
	private Map<Language, FolderLocal> local = new HashMap<>();

	public Folder getParent() {
		return parent;
	}

	public void setParent(Folder parent) {
		this.parent = parent;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Folder> getChildren() {
		return children;
	}

	public void setChildren(Set<Folder> children) {
		this.children = children;
	}

	public Map<Language, FolderLocal> getLocal() {
		return local;
	}

	public void setLocal(Map<Language, FolderLocal> local) {
		this.local = local;
	}
}