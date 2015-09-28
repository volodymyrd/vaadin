package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.administration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import com.gmail.volodymyrdotsenko.cms.be.domain.users.User;
import com.gmail.volodymyrdotsenko.cms.be.domain.users.UserRepository;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.MainUI;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.Sections;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * View that is available to administrators only.
 *
 */
@Secured("ROLE_ADMIN")
@SpringView(name = "users")
@SideBarItem(sectionId = Sections.ADMINISTRATION, captionCode = "application.section.item.users")
@FontAwesomeIcon(FontAwesome.USERS)
public class UsersView extends TabSheet implements View, CloseHandler {

	private static final long serialVersionUID = 1L;

	private MainUI mainUI;

	private UserRepository repo;

	private I18N i18n;

	@Autowired
	public UsersView(UserRepository repo, MainUI mainUI, I18N i18n) {
		this.repo = repo;
		this.mainUI = mainUI;
		this.i18n = i18n;

		setSizeFull();
		addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		setCloseHandler(this);

		addTab(buildUserList());
	}

	public void openUserForm(User user) {
		UserForm form = new UserForm(user, repo, this, i18n);

		form.setSizeFull();
		form.setCaption("User: " + user.getUserName());

		addTab(form).setClosable(true);
		setSelectedTab(getComponentCount() - 1);
	}

	private Component buildUserList() {
		final UsersList list = new UsersList(repo, mainUI, this, i18n);
		list.setSizeFull();
		list.setCaption(i18n.get("admin.users.tab.users"));

		// Button btn = new Button("test");
		// btn.addClickListener(e ->{
		// addTab(new Button("test")).setClosable(true);
		// setSelectedTab(getComponentCount() - 1);
		// });
		// vert.addComponent(btn);

		// VerticalLayout titleAndDrafts = new VerticalLayout();
		// titleAndDrafts.setSizeUndefined();
		// titleAndDrafts.setSpacing(true);
		// titleAndDrafts.addStyleName("drafts");
		// allDrafts.addComponent(titleAndDrafts);
		// allDrafts.setComponentAlignment(titleAndDrafts,
		// Alignment.MIDDLE_CENTER);
		//
		// Label draftsTitle = new Label("Drafts");
		// draftsTitle.addStyleName(ValoTheme.LABEL_H1);
		// draftsTitle.setSizeUndefined();
		// titleAndDrafts.addComponent(draftsTitle);
		// titleAndDrafts.setComponentAlignment(draftsTitle,
		// Alignment.TOP_CENTER);
		//
		// titleAndDrafts.addComponent(buildDraftsList());

		return list;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		System.out.println("ViewChangeEvent");
	}

	@Override
	public void onTabClose(TabSheet tabsheet, Component tabContent) {
		removeComponent(tabContent);
	}
}