package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.administration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import com.gmail.volodymyrdotsenko.cms.be.domain.PersonRepository;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.MainUI;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.Sections;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CustomComponent;

/**
 * View that is available to administrators only.
 *
 */
@Secured("ROLE_ADMIN")
@SpringView(name = "users")
@SideBarItem(sectionId = Sections.ADMINISTRATION, captionCode = "application.section.item.users")
@FontAwesomeIcon(FontAwesome.USERS)
public class UsersView extends CustomComponent implements View {

	private static final long serialVersionUID = 1L;

	private MainUI mainUI;

	private PersonRepository repo;

	@Autowired
	public UsersView(PersonRepository repo, MainUI mainUI) {
		this.repo = repo;
		this.mainUI = mainUI;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
}